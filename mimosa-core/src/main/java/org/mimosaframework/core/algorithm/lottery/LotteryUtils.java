package org.mimosaframework.core.algorithm.lottery;

import org.mimosaframework.core.utils.RandomUtils;

import java.math.BigDecimal;
import java.util.List;

public class LotteryUtils {
    public static <T> Lottery<T> randomLottery(List<Lottery<T>> lotteries) {
        BigDecimal bg = new BigDecimal(100);
        for (int i = 0; i < lotteries.size(); i++) {
            double odds = lotteries.get(i).getOdds();
            BigDecimal last = bg.subtract(new BigDecimal(odds));
            double start = last.doubleValue();
            double finish = bg.doubleValue();
            lotteries.get(i).setStart(start);
            lotteries.get(i).setFinish(finish);
            bg = last;
        }

        String number = RandomUtils.randomDecimalNumber(1, 100, 8);
        for (Lottery lottery : lotteries) {
            double i = Double.parseDouble(number);
            if (i <= lottery.getFinish() && i > lottery.getStart()) {
                return lottery;
            }
        }
        return null;
    }

    public static boolean isLottery(int odds) {
        String number = RandomUtils.randomDecimalNumber(1, 100, 8);
        double i = Double.parseDouble(number);
        if (i <= odds && i > 0) {
            return true;
        }
        return false;
    }
}
