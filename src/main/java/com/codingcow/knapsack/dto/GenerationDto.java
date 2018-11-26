package com.codingcow.knapsack.dto;

import java.util.List;

/**
 * @Author: Austin Zhang
 * @Date: 2018-09-28 20:33
 */
public class GenerationDto<T> {

    private int sequence;
    private List<T> generation;
    private T optimalCombination;

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

    public T getOptimalCombination() {
        return optimalCombination;
    }

    public void setOptimalCombination(T optimalCombination) {
        this.optimalCombination = optimalCombination;
    }

}
