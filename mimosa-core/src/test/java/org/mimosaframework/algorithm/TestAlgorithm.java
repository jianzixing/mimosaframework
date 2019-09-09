package org.mimosaframework.algorithm;

import org.junit.Test;
import org.mimosaframework.core.algorithm.gaussian.GaussianUtils;
import org.mimosaframework.core.algorithm.lottery.Lottery;
import org.mimosaframework.core.algorithm.lottery.LotteryUtils;
import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.core.utils.RandomUtils;

import java.math.BigDecimal;
import java.util.*;

public class TestAlgorithm {
    @Test
    public void test1() {
        Map<String, Integer> map = new LinkedHashMap<>();
        for (int j = 0; j < 1000; j++) {
            List<Lottery<ModelObject>> lotteries = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                ModelObject o = new ModelObject();
                double odds = 0;
                if (i == 0) odds = 10;
                if (i == 1) odds = 20;
                if (i == 2) odds = 30;
                if (i == 3) odds = 15;
                if (i == 4) odds = 25;
                o.put("odds", "odds:" + odds);
                lotteries.add(new Lottery(o, odds));
            }
            Lottery<ModelObject> lottery = LotteryUtils.randomLottery(lotteries);
            Integer count = map.get(lottery.getAward().getString("odds"));
            if (count == null) count = 0;
            count = count + 1;
            map.put(lottery.getAward().getString("odds"), count);
            System.out.println(lottery.getAward());
        }

        Iterator<Map.Entry<String, Integer>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Integer> entry = iterator.next();
            BigDecimal bd = (new BigDecimal(entry.getValue())).divide(new BigDecimal(1000));
            bd.setScale(4, BigDecimal.ROUND_DOWN);
            System.out.println(entry.getKey() + " = " + bd.doubleValue());
        }
    }

    @Test
    public void test2() {
        Map<String, Integer> map = new TreeMap<>();
        for (int i = 0; i < 10000; i++) {
            double d = GaussianUtils.random();
            String s = "Other";
            if (d >= -3 && d < -2.5) s = "A:-2.5";
            if (d >= -2.5 && d < -2) s = "B:-2";
            if (d >= -2 && d < -1.5) s = "C:-1.5";
            if (d >= -1.5 && d < -1) s = "D:-1";
            if (d >= -1 && d < -0.5) s = "E:-0.5";
            if (d >= -0.5 && d < 0) s = "F:0";
            if (d >= 0 && d < 0.5) s = "G:0.5";
            if (d >= 0.5 && d < 1) s = "H:1";
            if (d >= 1.5 && d < 2) s = "I:1.5";
            if (d >= 2 && d < 2.5) s = "J:2";
            if (d >= 2.5 && d < 3) s = "K:2.5";
            Integer c = map.get(s);
            if (c == null) c = 0;
            c = c + 1;
            map.put(s, c);
        }

        Iterator<Map.Entry<String, Integer>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Integer> entry = iterator.next();
            BigDecimal bd = (new BigDecimal(entry.getValue())).divide(new BigDecimal(10000));
            bd.setScale(4, BigDecimal.ROUND_DOWN);
            System.out.println(entry.getKey() + " = " + bd.doubleValue());
        }
    }

    @Test
    public void test3() {
        for (int i = 0; i < 100; i++) {
            String s = RandomUtils.randomDecimalNumber(1, 100, 5);
            System.out.println(s);
        }
    }
}
