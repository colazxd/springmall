package com.mmall.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author colaz
 * @date 2019/5/3
 **/
public interface IFileService {

    String upload(MultipartFile file, String path);
}
