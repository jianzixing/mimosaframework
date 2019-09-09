package org.mimosaframework.core.nfs;

import java.io.File;

public class NFSNode {
    private String id;
    private String name;
    private File dir;

    public NFSNode(String id, String name, File dir) {
        this.id = id;
        this.name = name;
        this.dir = dir;

        if (dir == null) {
            throw new IllegalArgumentException("目标文件夹不能为空");
        }
        if (!dir.exists()) {
            boolean succ = dir.mkdirs();
            if (!succ) {
                throw new IllegalArgumentException("创建目标文件夹" + dir.getPath() + "失败");
            }
        } else {
            if (!dir.isDirectory()) {
                throw new IllegalArgumentException("目标文件夹不是一个文件夹");
            }
        }
    }

    public File getDir() {
        return dir;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
