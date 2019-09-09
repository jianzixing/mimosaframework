package org.mimosaframework.core.algorithm.gaussian;

import java.util.Random;

public class GaussianUtils {
    /**
     * 0-1之间的数字
     *
     * @return
     */
    public static double random() {
        Random r = new Random();
        double num = r.nextGaussian();
        return num;
    }
}
