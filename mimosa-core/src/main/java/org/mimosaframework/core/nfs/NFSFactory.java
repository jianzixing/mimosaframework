package org.mimosaframework.core.nfs;

import java.util.List;

public class NFSFactory {
    public static NFSTransfer newQueueInstance(List<NFSNode> nodes, int threadCount) {
        return new NFSQueueTransfer(nodes, threadCount);
    }
}
