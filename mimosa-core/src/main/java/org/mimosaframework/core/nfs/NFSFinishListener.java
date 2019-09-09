package org.mimosaframework.core.nfs;

import java.io.File;

public interface NFSFinishListener {
    void notifyTransfer(NFSNode nfsNode, File sourceFile, File destFile);
}
