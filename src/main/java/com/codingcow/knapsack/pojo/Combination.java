package com.codingcow.knapsack.pojo;

/**
 * @Author: Austin Zhang
 * @Date: 2018-09-27 20:11
 * @Email: austin.zhang@dadaabc.com
 */
public class Combination implements Comparable<Combination> {

    private String binary;
    private Double fitness;

    public String getBinary() {
        return binary;
    }

    public void setBinary(String binary) {
        this.binary = binary;
    }

    public Double getFitness() {
        return fitness;
    }

    public void setFitness(Double fitness) {
        this.fitness = fitness;
    }

    @Override
    public int compareTo(Combination o) {
        return this.getFitness().compareTo(o.getFitness());
    }

}