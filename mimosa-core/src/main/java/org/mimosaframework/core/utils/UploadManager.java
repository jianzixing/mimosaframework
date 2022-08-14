package org.mimosaframework.core.utils;

import org.mimosaframework.core.exception.ModuleException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;
import java.util.*;

public class UploadManager {
    private String path;
    private List<String> allowFile;
    private String url;
    private Map<String, Long> maxSize;
    private boolean throwOutError = true;

    public boolean isThrowOutError() {
        return throwOutError;
    }

    public void setThrowOutError(boolean throwOutError) {
        this.throwOutError = throwOutError;
    }

    public Map<String, Long> getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(Map<String, Long> maxSize) {
        this.maxSize = maxSize;
    }

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
        return upload(request, null);
    }

    public List<FileItem> upload(HttpServletRequest request, Callback callback) {
        if (request instanceof MultipartHttpServletRequest) {
            Map<String, MultipartFile> fileMap = ((MultipartHttpServletRequest) request).getFileMap();
            if (fileMap != null && fileMap.size() > 0) {
                Iterator<Map.Entry<String, MultipartFile>> iterator = fileMap.entrySet().iterator();
                List<FileItem> lists = new ArrayList<>();
                while (iterator.hasNext()) {
                    MultipartFile file = iterator.next().getValue();
                    String fileName = file.getOriginalFilename().toLowerCase();
                    FileItem fileItem = new FileItem();
                    String fileType = null;
                    if (fileName.indexOf(".") >= 0) {
                        String[] s1 = fileName.split("\\.");
                        fileType = s1[s1.length - 1];
                    }
                    long size = file.getSize();
                    if (this.maxSize != null) {
                        Long max = this.maxSize.get(fileType);
                        if (max != null && size > max) {
                            if (this.throwOutError) {
                                throw new ModuleException("file_size_max", "文件太大禁止上传");
                            } else {
                                fileItem.setErrorCode(-150);
                                continue;
                            }
                        }
                    }

                    lists.add(fileItem);
                    fileItem.setUrl(this.url);
                    fileItem.setMultipartFile(file);
                    fileItem.setOldFileName(file.getOriginalFilename());
                    if (fileType != null) {
                        fileItem.setType(fileType);
                        if (!this.allowFile.contains(fileType.toLowerCase())) {
                            if (this.throwOutError) {
                                throw new ModuleException("file_not_allow", "文件" + fileType + "格式不允许上传");
                            } else {
                                fileItem.setErrorCode(-100);
                                continue;
                            }
                        }
                        String newFileName = UUID.randomUUID().toString().replaceAll("-", "") + "." + fileType;
                        fileItem.setFileName(newFileName);
                        fileItem.setSize(file.getSize());

                        if (callback != null) {
                            InputStream inputStream = null;
                            try {
                                inputStream = file.getInputStream();
                                callback.write(fileItem, inputStream);
                            } catch (IOException e) {
                                throw new ModuleException("file_write_error", "文件写入出错", e);
                            } finally {
                                if (inputStream != null) {
                                    try {
                                        inputStream.close();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        } else {
                            String filePath = path;
                            File dest = new File(filePath + File.separator + newFileName);
                            if (!dest.getParentFile().exists()) {
                                dest.getParentFile().mkdirs();
                            }
                            fileItem.setFile(dest);
                            try {
                                file.transferTo(dest);
                            } catch (IOException e) {
                                if (this.throwOutError) {
                                    throw new ModuleException("file_write_error", "文件写入出错", e);
                                } else {
                                    e.printStackTrace();
                                    fileItem.setErrorCode(-110);
                                    fileItem.setThrowable(e);
                                    continue;
                                }
                            }
                        }
                    } else {
                        if (this.throwOutError) {
                            throw new ModuleException("file_not_type", "文件必须包含格式后缀名");
                        } else {
                            fileItem.setErrorCode(-120);
                            continue;
                        }
                    }
                }
                return lists;
            }
        }
        return null;
    }

    public int uploadFileCount(HttpServletRequest request) {
        if (request instanceof MultipartHttpServletRequest) {
            int c = 0;
            if (((MultipartHttpServletRequest) request).getMultiFileMap() != null) {
                Set<Map.Entry<String, List<MultipartFile>>> set = ((MultipartHttpServletRequest) request).getMultiFileMap().entrySet();
                if (set != null) {
                    for (Map.Entry<String, List<MultipartFile>> entry : set) {
                        if (entry.getValue() != null) {
                            c += entry.getValue().size();
                        }
                    }
                }
            }
            return c;
        }
        return 0;
    }

    public String getUploadFileType(HttpServletRequest request) {
        if (request instanceof MultipartHttpServletRequest) {
            if (((MultipartHttpServletRequest) request).getMultiFileMap() != null) {
                List<MultipartFile> list = ((MultipartHttpServletRequest) request).getMultiFileMap().entrySet().iterator().next().getValue();
                if (list != null && list.size() > 0) {
                    MultipartFile file = list.get(0);
                    String name = file.getOriginalFilename();
                    String[] s1 = name.split("\\.");
                    return s1.length > 1 ? s1[s1.length - 1] : null;
                }
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
        private String domain;
        private String url = "";
        private String path;
        private String thumb;
        private long size;
        private long thumbSize;

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

        public String getDomain() {
            return domain;
        }

        public void setDomain(String domain) {
            this.domain = domain;
        }

        public String getFileUrl() {
            return this.url + fileName;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public long getSize() {
            return size;
        }

        public void setSize(long size) {
            this.size = size;
        }

        public String getThumb() {
            return thumb;
        }

        public void setThumb(String thumb) {
            this.thumb = thumb;
        }

        public long getThumbSize() {
            return thumbSize;
        }

        public void setThumbSize(long thumbSize) {
            this.thumbSize = thumbSize;
        }
    }

    public interface Callback {
        void write(FileItem item, InputStream stream);
    }
}
