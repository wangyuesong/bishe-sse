package sse.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;

import sse.controller.LoginController;

/**
 * 该类为FTP相关的工具类
 * 
 * @author wxq
 * 
 */
public class FtpUtil {
    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(FtpUtil.class);

    FTPClientConfig config = null;
    FTPClient ftpClient = null;
    String serverIp = "";// ftp服务器ip地址
    int serverPort = 0;// ftp服务器端口
    String usrName = "";// ftp用户名
    String usrPwd = "";// ftp用户密码
    String baseWorkDirectory = "";// 基本工作目录
    int fileType = FTP.BINARY_FILE_TYPE;// 上传下载文件方式，默认使用二进制流
    // 本地编码字符串编码
    String localCharset = "UTF-8";
    // FTP服务器端字符串编码
    String serverCharset = "ISO-8859-1";

    // public static void main(String[] args)
    // {
    // FtpUtil ftpUtil = new FtpUtil("127.0.0.1", 21, "sseftp", "123", "/Users/sseftp/Desktop/", FTP.BINARY_FILE_TYPE);
    // ftpUtil.setFtpClientConfigByDefault();
    // ftpUtil.connectServer();
    // File f = new File("/Users/yuesongwang/Desktop/abc 2.txt");
    // ftpUtil.uploadFileToFtpServer("", "try3.txt", f);
    // }

    public FtpUtil(String serverIp, int serverPort, String usrName,
            String usrPwd, String baseWorkDirectory, int fileType) {
        super();
        this.serverIp = serverIp;
        this.serverPort = serverPort;
        this.usrName = usrName;
        this.usrPwd = usrPwd;
        this.baseWorkDirectory = baseWorkDirectory;
        this.fileType = fileType;
    }

    public boolean enterLocalPassiveMode()
    {
        if (this.ftpClient != null)
        {
            ftpClient.enterLocalPassiveMode();
            return true;
        }
        else
            return false;
    }

    /**
     * 
     * 使用默认值，生成FTPClientConfig对象
     */
    public void setFtpClientConfigByDefault() {
        config = new FTPClientConfig();
    }

    /**
     * 使用参数指定的值生成FTPClientConfig对象
     * 
     * @param isParamsActived
     *            标记是否使用后面参数，false则不使用，且使用默认值构造一个FTPClientConfig对象
     * @param osType
     *            FTPClientConfig.SYST_NT FTPClientConfig.SYST_UNIX
     * @param serverLanguageCode
     * @param defaultDateFormatStr
     * @param recentDateFormatStr
     * @param serverTimeZoneId
     * @return
     */
    public void setFtpClientConfig(String osType, String serverLanguageCode,
            String defaultDateFormatStr, String recentDateFormatStr,
            String serverTimeZoneId) {
        try {
            if (!osType.equals("")) {
                config = new FTPClientConfig(osType);
            }
            if (!serverLanguageCode.equals("")) {
                config.setServerLanguageCode(serverLanguageCode);
            }
            if (!defaultDateFormatStr.equals("")) {

                config.setDefaultDateFormatStr(defaultDateFormatStr);
            }
            if (!recentDateFormatStr.equals("")) {

                config.setRecentDateFormatStr(recentDateFormatStr);
            }
            if (!serverTimeZoneId.equals("")) {

                config.setServerTimeZoneId(serverTimeZoneId);
            }

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    /**
     * 连接FTP服务器，并登录，切换至基本工作目录(通常为当前用户的根目录)
     * 
     */
    public void connectServer() {
        this.ftpClient = new FTPClient();
        try {
            if (config != null) {
                ftpClient.configure(config);
                int reply;
                ftpClient.connect(serverIp, serverPort);
                ftpClient.setFileType(fileType);
                reply = ftpClient.getReplyCode();
                if (!FTPReply.isPositiveCompletion(reply)) {
                    ftpClient.disconnect();
                    System.out.println("FTP server refused connection");
                } else {
                    // set file type
                    if (ftpClient.login(usrName, usrPwd)) {
                        System.out.println("login success");
                    }
                    ftpClient.changeWorkingDirectory(baseWorkDirectory);
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
        }
    }

    public boolean uploadFileToFtpServerByStream(String subDirectory, String storeName, InputStream inputStream)
    {
        boolean isUploadSuccess = false;
        try {
            subDirectory = new String(subDirectory.getBytes(localCharset), serverCharset);
            storeName = new String(storeName.getBytes(localCharset), serverCharset);
            storeName = new String(storeName.getBytes(localCharset),
                    serverCharset);
            if (ftpClient.storeFile(storeName, inputStream))
            {
                isUploadSuccess = true;
                System.out.println("upload file to FTP server success");
            }

        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO: handle exception
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return isUploadSuccess;

    }

    /**
     * 上传文件至Ftp用户根目录下的指定目录，如果subDirectory.equals("")为true， 则上传文件存放到当前用户的根目录
     * 
     * @param subDirectory
     *            子目录
     * @param storeName
     *            上传文件在FTP服务器上的存储名字
     * @param file
     *            上传文件
     * @return
     */
    public boolean uploadFileToFtpServer(String subDirectory, String storeName,
            File file) {
        // 上传文件成功标记
        boolean isUploadSuccess = false;
        FileInputStream fin = null;
        try {
            if (file.exists()) {
                subDirectory = new String(subDirectory.getBytes(localCharset), serverCharset);
                storeName = new String(storeName.getBytes(localCharset), serverCharset);
                storeName = new String(storeName.getBytes(localCharset),
                        serverCharset);
                storeName = this.handleStoreName(subDirectory, storeName);
                fin = new FileInputStream(file);
                // Stores a file on the server using the given name and taking
                // input from the given InputStream.
                if (ftpClient.storeFile(storeName, fin)) {
                    isUploadSuccess = true;
                    System.out.println("upload file to FTP server success");
                }
            } else {
                System.out.println("upload file does not exsit");
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        } finally {
            if (fin != null) {
                try {
                    fin.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return isUploadSuccess;
    }

    /**
     * 上传字符串至Ftp用户根目录下的指定目录下的指定文件，如果subDirectory.equals("")为true，
     * 则上传文件存放到当前用户的根目录
     * 
     * @param subDirectory
     *            子目录,
     * @param storeName
     *            上传的字符串在FTP服务器上存储文件的名字
     * @param uploadStr
     *            上传的字符串
     * @return
     */
    public boolean uploadStringToFtpServer(String subDirectory,
            String storeName, String uploadStr) {
        // 上传成功标记
        boolean isUploadSuccess = false;
        ByteArrayInputStream bais = null;
        try {
            if (uploadStr != null) {
                subDirectory = new String(subDirectory.getBytes(localCharset), serverCharset);
                storeName = new String(storeName.getBytes(localCharset), serverCharset);
                storeName = this.handleStoreName(subDirectory, storeName);
                bais = new ByteArrayInputStream(uploadStr.getBytes());
                // Stores a file on the server using the given name and taking
                // input from the given InputStream.
                if (ftpClient.storeFile(storeName, bais)) {
                    isUploadSuccess = true;
                    System.out.println("upload String to FTP server success");
                }
            } else {
                System.out.println("upload String is null");
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        } finally {
            if (bais != null) {
                try {
                    bais.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return isUploadSuccess;
    }

    /**
     * 从FTP服务器下载文件到本地指定路径，当subDirectory.equals("")时，则在当前用户的根目录下去找要下载的文件
     * 
     * @param subDirectory
     *            ftp服务器上存放要下载文件的子目录
     * @param fileName
     *            下载文件的名字
     * @param localPath
     *            本地存放路径
     * @return 下载成功，返回true
     */
    public boolean downFileFromFtpServer(String subDirectory, String fileName,
            String localPath) {
        FileOutputStream fos = null;
        boolean isDownloadSuccess = false;
        try {
            subDirectory = new String(subDirectory.getBytes(localCharset), serverCharset);
            String baseWorkDir = ftpClient.printWorkingDirectory();
            if (!subDirectory.equals("")) {
                baseWorkDir = baseWorkDir + "/" + subDirectory;
            }
            ftpClient.changeWorkingDirectory(baseWorkDir);

            fos = new FileOutputStream(localPath + "/" + fileName);
            fileName = new String(fileName.getBytes(localCharset),
                    serverCharset);
            // Retrieves a named file from the server and writes it to the given
            // OutputStream.
            if (ftpClient.retrieveFile(fileName, fos)) {
                isDownloadSuccess = true;
                System.out.println("download from FTP server success");
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return isDownloadSuccess;
    }

    /**
     * 下载Ftp服务器上指定目录下的所有文件（不包含文件夹）到本机的指定目录，子目录subDirectory.equals("")时， 则指定目录就是用户的根目录
     * 
     * @param subDirectory
     *            ftp服务器上包含文件的子目录
     * @param localPath
     * @return
     */
    public boolean downloadFilesFromFtpServer(String subDirectory,
            String localPath) {
        boolean isDownloadSuccess = false;
        FileOutputStream fos = null;
        try {
            subDirectory = new String(subDirectory.getBytes(localCharset), serverCharset);
            String baseWorkDir = ftpClient.printWorkingDirectory();
            if (!subDirectory.equals("")) {
                baseWorkDir = baseWorkDir + "/" + subDirectory;
            }
            ftpClient.changeWorkingDirectory(baseWorkDir);
            FTPFile[] files = ftpClient.listFiles();
            if (files != null && files.length > 0) {
                // 下载目录下所有文件
                for (FTPFile file : files) {
                    if (file.isFile()) {
                        String fileName = new String(file.getName().getBytes(
                                serverCharset), localCharset);
                        fos = new FileOutputStream(localPath + "/" + fileName);
                        if (ftpClient.retrieveFile(file.getName(), fos)) {
                            System.out.println("download file: " + fileName
                                    + " success");
                        }
                        fos.flush();
                    }
                }
            }
            isDownloadSuccess = true;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return isDownloadSuccess;
    }

    /**
     * 删除ftp服务器上的指定目录下的某个文件
     * 
     * @param subDirectory
     *            子目录
     * @param fileName
     *            文件名
     * @return 删除成功，返回true
     */
    public boolean deleteFileInFtpServer(String subDirectory, String fileName) {
        boolean isDeleteSuccess = false;
        try {
            subDirectory = new String(subDirectory.getBytes(localCharset), serverCharset);
            fileName = new String(fileName.getBytes(localCharset),
                    serverCharset);
            fileName = this.handleStoreName(subDirectory, fileName);
            if (ftpClient.deleteFile(fileName)) {
                isDeleteSuccess = true;
                System.out.println("delete file on ftp server success");
            } else {
                System.out.println("delete file on ftp server fail");
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return isDeleteSuccess;
    }

    /**
     * 删除指定目录下的所有文件，如果folderName为“”，则删除subDirectory下的所有文件（不包括文件夹）。
     * 
     * @param subDirectory
     *            子目录
     * @param folderName
     *            文件夹名称
     * @return
     */
    public boolean deleteFilesInFtpServer(String subDirectory, String folderName) {
        boolean isDeleteSuccess = false;
        try {
            String baseWorkDir = ftpClient.printWorkingDirectory();
            if (!subDirectory.equals("")) {
                subDirectory = new String(subDirectory.getBytes(localCharset), serverCharset);
                baseWorkDir = baseWorkDir + "/" + subDirectory;
            }
            if (!folderName.equals("")) {
                folderName = new String(folderName.getBytes(localCharset), serverCharset);
                baseWorkDir = baseWorkDir + "/" + folderName;
            }
            ftpClient.changeWorkingDirectory(baseWorkDir);
            FTPFile[] files = ftpClient.listFiles();
            if (files != null) {
                for (FTPFile file : files) {
                    ftpClient.deleteFile(file.getName());
                }
                isDeleteSuccess = true;
            }
            System.out.println("delete files in ftp server success");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            System.out.println("delete files in ftp server exception: " + e.getMessage());
        }
        return isDeleteSuccess;
    }

    /**
     * 在用户的根目录下创建指定文件夹,如果subDirectory是一个目录则依次创建各级文件夹。如果文件夹存在，则返回false
     * 
     * @param subDirectory
     *            子目录
     * @return 成功返回true
     */
    public boolean createDirInBaseWorkDir(String subDirectory) {
        boolean isCreateSuccess = false;
        try {
            if (!subDirectory.equals("")) {
                subDirectory = new String(subDirectory.getBytes(localCharset), serverCharset);
                if (ftpClient.makeDirectory(subDirectory)) {
                    isCreateSuccess = true;
                    System.out
                            .println("create new directory in base work directory success");
                } else {
                    System.out
                            .println("create new directory fail,the directory exsited");
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return isCreateSuccess;
    }

    /**
     * 删除用户根目录下的指定文件夹，如果subDirectory是一个路径，则删除最低级的那个文件夹；如果文件夹不存在则返回false。
     * 如果文件夹不为空，则返回false
     * 
     * @param subDirectory
     * @return
     */
    public boolean rmDirInBaseWorkDir(String subDirectory) {
        boolean isRmDirSuccess = false;
        try {
            subDirectory = new String(subDirectory.getBytes(localCharset), serverCharset);
            if (ftpClient.removeDirectory(subDirectory)) {
                isRmDirSuccess = true;
                System.out
                        .println("remove directory in base work directory success");
            } else {
                System.out
                        .println("remove directory in base work directory fail");
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return isRmDirSuccess;
    }

    /**
     * 该方法用于处理文件时，对子目录和文件名进行处理
     * 
     * @param subDirectory
     *            子目录
     * @param storeName
     *            文件名
     * @return 返回处理后可能带有路径的文件名
     */
    private String handleStoreName(String subDirectory, String storeName) {
        // 子目录是否存在标记
        boolean isSubDirectoryExsit = false;
        try {
            // 此处判断是否要生成子目录，存在则不创建
            FTPFile[] dirs = ftpClient.listDirectories();
            if (dirs != null && dirs.length > 0) {
                for (int i = 0; i < dirs.length; i++) {
                    if (dirs[i].getName().equals(subDirectory)) {
                        isSubDirectoryExsit = true;
                    }
                    break;
                }
            }
            dirs = null;
            if (!isSubDirectoryExsit && !subDirectory.equals("")) {
                ftpClient.makeDirectory(subDirectory);
                storeName = subDirectory + "/" + storeName;
            }
            if (isSubDirectoryExsit && !subDirectory.equals("")) {
                storeName = subDirectory + "/" + storeName;
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return storeName;
    }

}