package org.mimosaframework.core.nfs;

import java.io.File;
import java.util.List;

public class NFSQueueObject {
    private int skipIndex = -1;
    private List<NFSNode> nodes;
    private File sourceFile;

    public void setNodes(List<NFSNode> nodes) {
        this.nodes = nodes;
    }

    public List<NFSNode> getNodes() {
        return nodes;
    }

    public int getSkipIndex() {
        return skipIndex;
    }

    public void setSkipIndex(int skipIndex) {
        this.skipIndex = skipIndex;
    }

    public File getSourceFile() {
        return sourceFile;
    }

    public void setSourceFile(File sourceFile) {
        this.sourceFile = sourceFile;
    }
}
