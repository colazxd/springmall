package com.mmall.util;

import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/**
 * @author colaz
 * @date 2019/5/3
 **/
public class FTPUtil {

    private static final Logger logger = LoggerFactory.getLogger(FTPUtil.class);

    private static String ftpIp = PropertiesUtil.getProperty("ftp.server.ip");
    private static String ftpUser = PropertiesUtil.getProperty("ftp.user");
    private static String ftpPass = PropertiesUtil.getProperty("ftp.pass");

    private String ip;
    private int port;
    private String user;
    private String pwd;
    private FTPClient ftpClient;

    private FTPUtil(String ip, int port, String user, String pwd) {
        this.ip = ip;
        this.port = port;
        this.user = user;
        this.pwd = pwd;
    }

    public static boolean upload(List<File> files) throws IOException {
        FTPUtil ftpUtil = new FTPUtil(ftpIp, 21, ftpUser, ftpPass);
        logger.info("开始连接ftp服务器");
        boolean result = ftpUtil.upload("img", files);
        logger.info("结束上传，上传{}",result?"成功":"失败");
        return result;
    }

    private boolean upload(String remotePath, List<File> files) throws IOException {
        boolean isUploaded = false;
        FileInputStream fis = null;
        if (connectServer(this.ip, this.user, this.pwd)) {
            try {
                ftpClient.changeWorkingDirectory(remotePath);
                ftpClient.setBufferSize(1024);
                ftpClient.setControlEncoding("utf-8");
                ftpClient.setFileType(ftpClient.BINARY_FILE_TYPE);
                ftpClient.enterLocalPassiveMode();
                for (File file:files) {
                    fis = new FileInputStream(file);
                    ftpClient.storeFile(file.getName(), fis);
                }
                isUploaded = true;
            } catch (IOException e) {
                logger.error("上传文件异常",e);
            } finally {
                fis.close();
                ftpClient.disconnect();
            }
        }
        return isUploaded;
    }

    private boolean connectServer(String ip, String user, String pwd) {
        boolean isConnected = false;
        ftpClient = new FTPClient();
        try {
            ftpClient.connect(ip);
            isConnected = ftpClient.login(user, pwd);
        } catch (IOException e) {
            logger.error("连接服务器失败",e);
        }
        return isConnected;
    }



    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public FTPClient getFtpClient() {
        return ftpClient;
    }

    public void setFtpClient(FTPClient ftpClient) {
        this.ftpClient = ftpClient;
    }
}
