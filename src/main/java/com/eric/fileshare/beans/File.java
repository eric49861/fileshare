package com.eric.fileshare.beans;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

import java.sql.Timestamp;

@Data
public class File {
    // 行 id
    private Integer id;
    // 文件名称
    private String filename;
    // 文件后缀
    private String extension;
    // 文件大小
    private Long filesize;
    // 上传者的id(不是必须的)
    private Integer uploaderID;
    // 上传者的IP地址
    private String uploaderIP;
    // 文件的内容，以字节数组的形式存储在数据库中
    private byte[] content;
    // 该文件的过期时间
    private Timestamp expireAt;
    // 文件上传的时间戳
    private Timestamp uploadAt;
    // 文件的hash值
    private String hash;

    public String fullName() {
        return filename + "." + extension;
    }
}
