package com.codingcow.knapsack.dto;

import com.codingcow.knapsack.pojo.ScoreAndTime;

import java.util.List;

/**
 * @Author: Austin Zhang
 * @Date: 2018-09-28 20:33
 * @Email: austin.zhang@dadaabc.com
 */
public class GenerationDto<T> {

    private int sequence;
    private List<T> generation;
    private String[] optimalCombination;

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public List<T> getGeneration() {
        return generation;
    }

    public void setGeneration(List<T> generation) {
        this.generation = generation;
    }

    public String[] getOptimalCombination() {
        return optimalCombination;
    }

    public void setOptimalCombination(String[] optimalCombination) {
        this.optimalCombination = optimalCombination;
    }

}
