package org.mimosaframework.core.nfs;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface NFSTransfer {

    /**
     * 配置NFS的数据节点，如果不存在无法完成拷贝工作
     *
     * @param nodes
     */
    void setNodes(List<NFSNode> nodes);

    /**
     * 多个节点拷贝时，每个节点拷贝完成就通知用户一次
     *
     * @param listener
     */
    void addFinishListener(NFSFinishListener listener);

    /**
     * 同步拷贝第一个节点，其他节点异步同步
     *
     * @param file
     * @return
     */
    void syncFirstTransfer(File file) throws IOException;

    /**
     * 全部异步拷贝
     *
     * @param file
     */
    void asyncTransfer(File file);

    /**
     * 全部同步拷贝
     *
     * @param file
     */
    void syncTransfer(File file) throws IOException;

    void release();
}
