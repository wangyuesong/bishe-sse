package sse.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.SocketException;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;

import sse.exception.SSEException;
import sse.service.impl.StudentDocumentServiceImpl;

/**
 * @Project: sse
 * @Title: FtpTool.java
 * @Package sse.utils
 * @Description: TODO
 * @author YuesongWang
 * @date 2015年5月8日 下午1:32:57
 * @version V1.0
 */
public class FtpTool {
    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(FtpTool.class);

    // FTP Server configuration
    private final String serverIp = "127.0.0.1";
    private final int serverPort = 21;
    private final int fileType = FTP.BINARY_FILE_TYPE;
    private final String ftpUserName = "sseftp";
    private final String ftpPassword = "123";

    private FTPClient ftpClient;

    
    public FTPFile[] listFiles() throws IOException {
        return ftpClient.listFiles();
    }

    public int getReplyCode() {
        return ftpClient.getReplyCode();
    }

    public boolean completePendingCommand() throws IOException {
        return ftpClient.completePendingCommand();
    }

    public InputStream retrieveFileStream(String remote) throws IOException {
        // return ftpClient.retrieveFileStream(convertUTF8ToISO(remote));
        return ftpClient.retrieveFileStream(convertUTF8ToISO(remote));
    }

    public boolean deleteFile(String pathname) throws IOException {
        return ftpClient.deleteFile(convertUTF8ToISO(pathname));
    }

    public boolean storeFile(String remote, InputStream local) throws IOException {
        return ftpClient.storeFile(convertUTF8ToISO(remote), local);
    }

    public boolean changeWorkingDirectory(String pathname) throws IOException {
        return ftpClient.changeWorkingDirectory(convertUTF8ToISO(pathname));
    }

    public boolean makeDirectory(String pathname) throws IOException {
        return ftpClient.makeDirectory(convertUTF8ToISO(pathname));
    }

    public FtpTool()
    {
        try {
            connectAndLoginFtpServer();
        } catch (SocketException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void connectAndLoginFtpServer() throws SocketException, IOException {
        ftpClient = new FTPClient();
        // First connect
        ftpClient.connect(serverIp, serverPort);
        int reply = ftpClient.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply))
        {
            ftpClient.disconnect();
            throw new SSEException("FTP Server 不接受连接");
        }
        // Then login
        if (!ftpClient.login(ftpUserName, ftpPassword))
        {
            ftpClient.disconnect();
            throw new SSEException("用户名密码错误");
        }
        // Set file type and misc
        ftpClient.setControlEncoding("UTF-8");
        ftpClient.setFileType(fileType);
        ftpClient.enterLocalPassiveMode();
    }

    private String convertUTF8ToISO(String utf8String)
    {
        try {
            return new String(utf8String.getBytes("UTF-8"), "iso-8859-1");
        } catch (UnsupportedEncodingException e) {
            logger.error("不支持的字符集", e);
        }
        return null;
    }

}
