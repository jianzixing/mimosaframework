package org.mimosaframework.core.nfs;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NFSQueueTransfer implements NFSTransfer {
    private static final Log logger = LogFactory.getLog(NFSQueueTransfer.class);
    /**
     * 多生产者，单消费者
     */
    private ConcurrentLinkedQueue<NFSQueueObject> queue = new ConcurrentLinkedQueue<NFSQueueObject>();
    private List<NFSNode> nodes;
    private ExecutorService service;
    private NFSFinishListener listener;

    public NFSQueueTransfer(List<NFSNode> nodes, int threadCount) {
        this.nodes = nodes;
        service = Executors.newFixedThreadPool(threadCount);
        for (int i = 0; i < threadCount; i++) {
            service.execute(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        try {
                            NFSQueueObject object = queue.poll();
                            if (object != null) {
                                if (logger.isDebugEnabled()) {
                                    logger.debug("take one nfs object from queue");
                                }
                                int skip = object.getSkipIndex();
                                List<NFSNode> nfsNodes = object.getNodes();
                                File file = object.getSourceFile();
                                for (int i = 0; i < nfsNodes.size(); i++) {
                                    if (i != skip) {
                                        NFSNode nfsNode = nfsNodes.get(i);
                                        File destDir = nfsNode.getDir();
                                        if (destDir != null) {
                                            File dest = new File(destDir.getPath() + File.separator + file.getName());
                                            NFSUtils.copy(file, dest);

                                            if (logger.isDebugEnabled()) {
                                                logger.debug("take one nfs object from queue");
                                            }

                                            if (listener != null) {
                                                listener.notifyTransfer(nfsNode, file, dest);
                                            }
                                        }
                                    }
                                }
                            } else {
                                Thread.sleep(100);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    }

    private void checkNodes() {
        if (nodes == null || nodes.size() == 0) {
            throw new IllegalArgumentException("请先设置NFS的数据节点");
        }
    }

    @Override
    public void setNodes(List<NFSNode> nodes) {
        this.nodes = nodes;
    }

    @Override
    public void addFinishListener(NFSFinishListener listener) {
        this.checkNodes();
        this.listener = listener;
    }

    @Override
    public void syncFirstTransfer(File file) throws IOException {
        this.checkNodes();
        NFSNode nfsNode = nodes.get(0);
        File destDir = nfsNode.getDir();
        if (destDir == null) {
            throw new IllegalArgumentException("目标文件夹不能为空");
        }
        File dest = new File(destDir.getPath() + File.separator + file.getName());
        NFSUtils.copy(file, dest);
        if (listener != null) {
            listener.notifyTransfer(nfsNode, file, dest);
        }

        if (nodes.size() > 1) {
            NFSQueueObject object = new NFSQueueObject();
            object.setNodes(nodes);
            object.setSkipIndex(0);
            object.setSourceFile(file);
            queue.add(object);
        }
    }

    @Override
    public void asyncTransfer(File file) {
        this.checkNodes();
        NFSQueueObject object = new NFSQueueObject();
        object.setNodes(nodes);
        object.setSourceFile(file);
        queue.add(object);
    }

    @Override
    public void syncTransfer(File file) throws IOException {
        this.checkNodes();

        for (NFSNode nfsNode : nodes) {
            File destDir = nfsNode.getDir();
            if (destDir == null) {
                throw new IllegalArgumentException("目标文件夹不能为空");
            }
            File dest = new File(destDir.getPath() + File.separator + file.getName());
            NFSUtils.copy(file, dest);

            if (listener != null) {
                listener.notifyTransfer(nfsNode, file, dest);
            }
        }
    }

    @Override
    public void release() {
        if (service != null) {
            service.shutdown();
        }
    }
}
