package org.mimosaframework.core.utils;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;
import java.util.*;

public class UploadManager {
    private String path;
    private List<String> allowFile;
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<String> getAllowFile() {
        return allowFile;
    }

    public void setAllowFile(List<String> allowFile) {
        this.allowFile = allowFile;
    }

    public List<FileItem> upload(HttpServletRequest request) {
        if (request instanceof MultipartHttpServletRequest) {
            Map<String, MultipartFile> fileMap = ((MultipartHttpServletRequest) request).getFileMap();
            if (fileMap != null && fileMap.size() > 0) {
                Iterator<Map.Entry<String, MultipartFile>> iterator = fileMap.entrySet().iterator();
                List<FileItem> lists = new ArrayList<>();
                while (iterator.hasNext()) {
                    FileItem fileItem = new FileItem();
                    lists.add(fileItem);
                    fileItem.setUrl(this.url);
                    MultipartFile file = iterator.next().getValue();
                    fileItem.setMultipartFile(file);
                    String fileName = file.getOriginalFilename().toLowerCase();
                    fileItem.setOldFileName(file.getOriginalFilename());
                    if (fileName.indexOf(".") >= 0) {
                        String type = fileName.split("\\.")[1];
                        fileItem.setType(type);
                        if (!this.allowFile.contains(type.toLowerCase())) {
                            fileItem.setErrorCode(-100);
                            continue;
                        }
                        String newFileName = UUID.randomUUID().toString().replaceAll("-", "") + "." + type;
                        String filePath = path;
                        File dest = new File(filePath + File.separator + newFileName);
                        if (!dest.getParentFile().exists()) {
                            dest.getParentFile().mkdirs();
                        }
                        fileItem.setFileName(newFileName);
                        fileItem.setFile(dest);
                        try {
                            file.transferTo(dest);
                        } catch (IOException e) {
                            e.printStackTrace();
                            fileItem.setErrorCode(-110);
                            fileItem.setThrowable(e);
                            continue;
                        }
                    } else {
                        fileItem.setErrorCode(-120);
                        continue;
                    }
                }
                return lists;
            }
        }
        return null;
    }

    public boolean downloadImage(HttpServletResponse response, String fileName) {
        String[] s1 = fileName.split("\\.");
        String type = s1.length > 1 ? s1[0] : "jpeg";
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/" + type);
        return this.download(response, fileName);
    }

    public boolean download(HttpServletResponse response, String fileName) {
        try {
            File file = new File(path + File.separator + fileName);
            if (file.exists()) {
                FileInputStream stream = new FileInputStream(file);
                try {
                    OutputStream out = response.getOutputStream();
                    WritableByteChannel channel = Channels.newChannel(out);
                    FileChannel fileChannel = stream.getChannel();
                    fileChannel.transferTo(0, file.length(), channel);
                    out.flush();
                    return true;
                } finally {
                    if (stream != null) stream.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String getRootUploadPath() {
        String path = System.getProperty("user.dir");
        File pf1 = new File(path + File.separator + "target");
        if (pf1.exists()) {
            path = pf1.getPath();
        }
        path = path + File.separator + "upload";
        return path;
    }

    public static final class FileItem {
        private MultipartFile multipartFile;
        private File file;
        private String oldFileName;
        private String fileName;
        private String type;
        private int errorCode;
        private Throwable throwable;
        private String url = "";

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            if (url != null) this.url = url;
        }

        public MultipartFile getMultipartFile() {
            return multipartFile;
        }

        public void setMultipartFile(MultipartFile multipartFile) {
            this.multipartFile = multipartFile;
        }

        public File getFile() {
            return file;
        }

        public void setFile(File file) {
            this.file = file;
        }

        public String getOldFileName() {
            return oldFileName;
        }

        public void setOldFileName(String oldFileName) {
            this.oldFileName = oldFileName;
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getErrorCode() {
            return errorCode;
        }

        public void setErrorCode(int errorCode) {
            this.errorCode = errorCode;
        }

        public Throwable getThrowable() {
            return throwable;
        }

        public void setThrowable(Throwable throwable) {
            this.throwable = throwable;
        }


        public String getFileUrl() {
            return this.url + fileName;
        }
    }
}
