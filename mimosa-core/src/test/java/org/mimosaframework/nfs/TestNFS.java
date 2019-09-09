package org.mimosaframework.nfs;

import org.mimosaframework.core.nfs.NFSFactory;
import org.mimosaframework.core.nfs.NFSFinishListener;
import org.mimosaframework.core.nfs.NFSNode;
import org.mimosaframework.core.nfs.NFSTransfer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TestNFS {
    public static void main(String[] args) throws InterruptedException {
        List<NFSNode> nfsNodes = new ArrayList<>();
        nfsNodes.add(new NFSNode("n1", "n1", new File("D:\\BaiduNetdiskDownload\\copy\\d1")));
        nfsNodes.add(new NFSNode("n2", "n2", new File("D:\\BaiduNetdiskDownload\\copy\\d2")));
        nfsNodes.add(new NFSNode("n3", "n3", new File("D:\\BaiduNetdiskDownload\\copy\\d3")));
        NFSTransfer transfer = NFSFactory.newQueueInstance(nfsNodes, 5);
        transfer.addFinishListener(new NFSFinishListener() {
            @Override
            public void notifyTransfer(NFSNode nfsNode, File sourceFile, File destFile) {
                System.out.println("拷贝文件 从" + sourceFile.getPath() + "到" + destFile.getPath());
            }
        });

        transfer.asyncTransfer(new File("C:\\Users\\yangankang\\Downloads\\1.txt"));
        transfer.asyncTransfer(new File("C:\\Users\\yangankang\\Downloads\\2.txt"));
        transfer.asyncTransfer(new File("C:\\Users\\yangankang\\Downloads\\3.otf"));
        transfer.asyncTransfer(new File("C:\\Users\\yangankang\\Downloads\\4.zip"));
        transfer.asyncTransfer(new File("C:\\Users\\yangankang\\Downloads\\5.zip"));
        transfer.asyncTransfer(new File("C:\\Users\\yangankang\\Downloads\\6.exe"));
        transfer.asyncTransfer(new File("C:\\Users\\yangankang\\Downloads\\7.png"));
        //Thread.sleep(Integer.MAX_VALUE);
    }
}
