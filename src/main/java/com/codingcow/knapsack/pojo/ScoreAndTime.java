package com.codingcow.knapsack.pojo;

/**
 * @Author: Austin Zhang
 * @Date: 2018-09-27 19:42
 */
public class ScoreAndTime implements Comparable<ScoreAndTime> {

    private String binary;
    private Double score;
    private Double time;

    public ScoreAndTime() {
    }

    public ScoreAndTime(Double score, Double time) {
        this.score = score;
        this.time = time;
    }

    public ScoreAndTime(String binary, Double score, Double time) {
        this.binary = binary;
        this.score = score;
        this.time = time;
    }

    public String getBinary() {
        return binary;
    }

    public void setBinary(String binary) {
        this.binary = binary;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Double getTime() {
        return time;
    }

    public void setTime(Double time) {
        this.time = time;
    }

    @Override
    public int compareTo(ScoreAndTime o) {
        return this.getScore().compareTo(o.getScore());
    }
}
