package com.mmall.service.impl;

import com.google.common.collect.Lists;
import com.mmall.service.IFileService;
import com.mmall.util.FTPUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * @author colaz
 * @date 2019/5/3
 **/

@Service("iFileService")
public class FileServiceImpl implements IFileService {

    private Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    /**
     * 使用spring mvc的文件上传功能，上传文件到FTP服务器
     * @param file
     * @param path
     * @return 上传文件名
     */
    public String upload(MultipartFile file, String path) {
        String fileName = file.getOriginalFilename();
        String extensionName = fileName.substring(fileName.lastIndexOf(".") + 1);

        String uploadName = UUID.randomUUID().toString() + "." + extensionName;
        logger.info("开始上传文件:文件名{}，上传路径{}，新文件名{}",fileName, path, uploadName);

        File fileDir = new File(path);
        if (!fileDir.exists()) {
            fileDir.setWritable(true);
            fileDir.mkdirs();
        }

        File targetFile = new File(fileDir, uploadName);
        try {
            file.transferTo(targetFile);
            FTPUtil.upload(Lists.newArrayList(targetFile));

            //        上传完成后删除file
            targetFile.delete();
        } catch (IOException e) {
            logger.error("文件上传异常", e);
            return null;
        }
        return targetFile.getName();

    }
}
