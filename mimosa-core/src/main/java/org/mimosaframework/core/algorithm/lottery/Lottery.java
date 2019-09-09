package org.mimosaframework.core.algorithm.lottery;

public class Lottery<T> {
    private T award;
    private double odds;
    private double start;
    private double finish;

    public Lottery(T award, double odds) {
        this.award = award;
        this.odds = odds;
        this.start = start;
        this.finish = finish;
    }

    public void setStart(double start) {
        this.start = start;
    }

    public void setFinish(double finish) {
        this.finish = finish;
    }

    public T getAward() {
        return award;
    }

    public double getOdds() {
        return odds;
    }

    public double getStart() {
        return start;
    }

    public double getFinish() {
        return finish;
    }
}