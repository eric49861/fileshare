package com.eric.fileshare.controller;

import com.eric.fileshare.beans.File;
import com.eric.fileshare.exceptions.upload.UploadException;
import com.eric.fileshare.service.IEmailService;
import com.eric.fileshare.service.IFileService;
import com.eric.fileshare.util.EncryptionUtil;
import com.eric.fileshare.util.Result;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import org.apache.commons.mail.EmailException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.concurrent.ExecutionException;

@RestController
public class FileController {

    private final Logger logger = Logger.getLogger(getClass());

    @NotNull
    private IFileService fileService;
    @NotNull
    private IEmailService emailService;

    private final Long MAX_SIZE = 50 * 1024 * 1024 * 1024L;
    private final String BASE_URL = System.getenv("base_url");

    public FileController(){}

    @Autowired
    public FileController(IFileService fileService, IEmailService emailService) {
        this.fileService = fileService;
        this.emailService = emailService;
    }

    /*
    * 上传文件的接口
    * 将文件保存到数据库，然后将文件的下载链接发送到email
    * todo: 优化异常的处理
    * */
    @PostMapping("/upload")
    public Result<String> upload(@RequestParam("file") MultipartFile multipartFile,
                                 @RequestParam("targetEmail")String targetEmail,
                                 @RequestParam("expireAt") Long expireAt,
                                 @RequestParam("code") String code,
                                 HttpServletRequest request) {
        // 检查邮箱的验证码是否正确
        try {
            emailService.checkCode(code, targetEmail);
        }catch (UploadException e) {
            return Result.fail(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }catch (ExecutionException e) {
            return Result.fail(HttpStatus.INTERNAL_SERVER_ERROR.value(), "文件上传失败，请稍后重试");
        }
        // 校验文件的大小并调用文件服务上传
        String ip = request.getRemoteAddr();
        try {
            checkFile(multipartFile);
            File f = getFile(multipartFile, ip, expireAt);
            fileService.upload(f);
            emailService.sendLink(BASE_URL + f.getHash(), targetEmail);
        }catch(UploadException e) {
            logger.debug("[IP = " + ip + "] 尝试上传大于50M的文件");
            return Result.fail(HttpStatus.FORBIDDEN.value(), e.getMessage());
        }catch (IOException | DataAccessException | EmailException e) {
            logger.debug("文件上传失败, 失败的原因: " + e.getMessage());
            return Result.fail(HttpStatus.INTERNAL_SERVER_ERROR.value(), "文件上传失败: " + e.getMessage());
        }
        return Result.success("上传成功");
    }

//    @PostMapping("/test/upload")
    public Result<String> testUpload(@RequestParam("file") MultipartFile multipartFile) {
        System.out.println(multipartFile.getOriginalFilename());
        return Result.success(String.valueOf(multipartFile.getSize()));
    }



    private File getFile(MultipartFile multipartFile, String ip, Long expireAt) throws IOException {
        File f = new File();
        // 获取文件的基本信息
        String fullName = multipartFile.getOriginalFilename();
        int index = fullName.lastIndexOf('.');
        String extension = fullName.substring(index + 1);
        String filename = fullName.substring(0, index);
        f.setFilename(filename);
        f.setUploaderIP(ip);
        f.setExtension(extension);
        f.setUploadAt(new Timestamp(System.currentTimeMillis()));
        f.setExpireAt(new Timestamp(System.currentTimeMillis() + expireAt * 60 * 60 * 1000));
        f.setContent(multipartFile.getBytes());
        f.setHash(EncryptionUtil.md5(multipartFile.getBytes()));
        f.setFilesize(multipartFile.getSize());
        return f;
    }

    private void checkFile(MultipartFile multipartFile) throws UploadException {
        if(multipartFile.getSize() > MAX_SIZE) {
            throw new UploadException("上传的文件大小超出限制");
        }
    }

    /*
    * 文件下载功能，通过路径参数获取文件的唯一标识
    * */
    @GetMapping("/download/{fileHash}")
    public Result<String> download(@PathVariable("fileHash") String fileHash, HttpServletRequest request, HttpServletResponse response) {

        // 根据文件的hash查询该文件
        File file = null;
        try {
            file = fileService.download(fileHash);
        }catch(DataAccessException e) {
            if(e.getClass() == EmptyResultDataAccessException.class) {
                // 没有找到该文件
                return Result.fail(HttpStatus.BAD_REQUEST.value(), "文件分享不存在或者已过期");
            }
        }

        if(file.getExpireAt().before(new Timestamp(System.currentTimeMillis()))) {
            try {
                fileService.deleteFileById(file.getId());
            }catch(DataAccessException e) {
                logger.debug("文件删除失败, 失败的原因: " + e.getMessage());
            }
            return Result.fail(HttpStatus.BAD_REQUEST.value(), "文件已过期");
        }
        try {
            response.reset();
            response.setCharacterEncoding("UTF-8");
            response.setContentType("multipart/form-data");
            response.setHeader("Content-Disposition",
                    "attachment;fileName="+ URLEncoder.encode(file.fullName(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            logger.error("文件下载失败, 失败的原因: " + e.getMessage());
            return Result.fail(HttpStatus.INTERNAL_SERVER_ERROR.value(), "未知错误，文件下载失败");
        }
        ServletOutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            outputStream.write(file.getContent());
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(outputStream != null) {
                try {
                    outputStream.close();
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return Result.success("获取文件成功");
    }
}
