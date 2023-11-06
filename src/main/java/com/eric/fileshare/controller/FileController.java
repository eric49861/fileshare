package com.eric.fileshare.controller;

import com.eric.fileshare.beans.File;
import com.eric.fileshare.service.IEmailService;
import com.eric.fileshare.service.IFileService;
import com.eric.fileshare.service.IUserService;
import com.eric.fileshare.service.IVisitorService;
import com.eric.fileshare.util.EncryptionUtil;
import com.eric.fileshare.util.Result;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Timestamp;

@RestController
public class FileController {

    private IFileService fileService;
    private IEmailService emailService;
    private IVisitorService visitorService;
    private IUserService userService;

    public FileController(){}

    @Autowired
    public FileController(IFileService fileService, IEmailService emailService, IVisitorService visitorService, IUserService userService) {
        this.fileService = fileService;
        this.emailService = emailService;
        this.visitorService = visitorService;
        this.userService = userService;
    }

    /*
    * 文件上传的功能，包含文件的内容，文件的过期时间
    * 上传完成时，通过邮件告知上传者文件的下载链接
    * */
    @PostMapping("/upload")
    public Result<String> upload(@RequestParam("file")MultipartFile file, HttpServletRequest request) throws IOException {
        if(file.getSize() > 50 * 1024 * 1024) {
            return Result.fail(HttpStatus.FORBIDDEN.value(), "文件大小超出限制");
        }
        if(userService.getBalance((String) request.getAttribute("email")) < file.getSize()) {
            return Result.fail(HttpStatus.BAD_REQUEST.value(), "剩余空间不足");
        }

        return Result.fail(HttpStatus.SERVICE_UNAVAILABLE.value(), "暂时不需要该功能, 请暂时使用游客模式上传");
    }

    /*
    * 游客上传文件的接口
    * 将文件保存到数据库，然后将文件的下载链接发送到email
    * */
    @PostMapping("/visitor/upload")
    public Result<String> visitorUpload(@RequestParam("file") MultipartFile file, @RequestParam("email") String email, @RequestParam("expireAt") Long expireAt, HttpServletRequest request) {
        if(file.getSize() > 50 * 1024 * 1024) {
            return Result.fail(HttpStatus.FORBIDDEN.value(), "文件大小超出限制");
        }
        if(visitorService.getBalance((String) request.getAttribute("ip")) < file.getSize()) {
            return Result.fail(HttpStatus.BAD_REQUEST.value(), "剩余空间不足");
        }

        File f = new File();
        try {
            // 获取文件的基本信息
            String fullName = file.getOriginalFilename();
            int index = fullName.lastIndexOf('.');
            String extension = fullName.substring(index + 1);
            String filename = fullName.substring(0, index);
            f.setFilename(filename);
            f.setUploaderIP((String) request.getAttribute("ip"));
            f.setExtension(extension);
            f.setUploadAt(new Timestamp(System.currentTimeMillis()));
            f.setExpireAt(new Timestamp(expireAt));
            f.setContent(file.getBytes());
            f.setHash(EncryptionUtil.md5(file.getBytes()));
            // 调用文件服务保存文件
            fileService.upload(f);
            emailService.sendLink("http://localhost:8080/fileshare/download/" + f.getHash(), email);
        } catch (IOException e) {
            e.printStackTrace();
            return Result.fail(HttpStatus.INTERNAL_SERVER_ERROR.value(), "文件上传失败");
        }catch (Exception e) {
            e.printStackTrace();
            return Result.fail(HttpStatus.INTERNAL_SERVER_ERROR.value(), "文件上传失败:" + e.getMessage());
        }
        return Result.success("上传成功");
    }

    /*
    * 文件下载功能，通过路径参数获取文件的唯一标识
    * */
    @GetMapping("/download/{fileHash}")
    public Result<String> download(@PathVariable("fileHash") String fileHash, HttpServletRequest request, HttpServletResponse response) {

        // 根据文件的hash查询该文件
        File file = fileService.download(fileHash);
        if(file == null) {
            return Result.fail(HttpStatus.BAD_REQUEST.value(), "文件分享不存在或者已过期");
        }else if(file.getExpireAt().before(new Timestamp(System.currentTimeMillis()))) {
            return Result.fail(HttpStatus.BAD_REQUEST.value(), "文件已过期");
        }
        try {
            response.reset();
            response.setCharacterEncoding("UTF-8");
            response.setContentType("multipart/form-data");
            response.setHeader("Content-Disposition",
                    "attachment;fileName="+ URLEncoder.encode(file.fullName(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            //todo:记录日志，查看错误
            e.printStackTrace();
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
