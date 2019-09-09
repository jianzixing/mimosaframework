package org.mimosaframework.core.utils.parallel;

import java.util.List;

public interface ParallelItem<T> {
    void done(int time, List<T> list);
}
