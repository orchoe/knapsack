package com.codingcow.knapsack.service;

import com.codingcow.knapsack.pojo.Combination;
import com.codingcow.knapsack.pojo.Couple;
import com.codingcow.knapsack.pojo.ScoreAndTime;
import com.codingcow.knapsack.utils.BinaryUtil;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @Author: Austin Zhang
 * @Date: 2018-09-27 17:29
 */
@Service
public class Reproduce {

    private static int totalNumber; // 组合数量
    private static List<ScoreAndTime> optimalResults = new ArrayList<>(); // 每一代的最优结果集


    /**
     * 生成初代
     */
    public List<String> firstGeneration() {
        // 生成totalNumber个totalQuestion长度的二进制随机数
        totalNumber = ReadFile.totalQuestion < 10 ? 1 << ReadFile.totalQuestion : 1 << 10;
        Set<String> digits = BinaryUtil.generateRandomBinaryDigits(ReadFile.totalQuestion, totalNumber);
        return new ArrayList<>(digits);
    }


    /**
     * 计算每组花费的总时间和总分数，返回list以score排序
     */
    public List<ScoreAndTime> calculateScore(List<String> generation) {
        List<ScoreAndTime> coms = new ArrayList<>();
        for (String gene : generation) {
            if (gene.indexOf("1") == -1) continue; // 排除全部为0的情况
            ScoreAndTime combine = new ScoreAndTime();
            combine.setBinary(gene);
            double totalScore = 0;
            double totalTime = 0;
            for (int i = 0; i < gene.length(); i++) {
                if (gene.charAt(i) == 49) { // 该数位值为1
                    totalScore += ReadFile.questions.get(i).getScore();
                    totalTime += ReadFile.questions.get(i).getTime();
                }
            }
            combine.setScore(totalScore);
            combine.setTime(totalTime);
            coms.add(combine);
        }
        // 降序排列coms
        Collections.sort(coms, Collections.reverseOrder());

        // 获取最佳组合的得分和总时间
        for (ScoreAndTime com : coms) {
            if (com.getTime() <= ReadFile.limitTime) {
                optimalResults.add(new ScoreAndTime(com.getBinary(), com.getScore(), com.getTime()));
                break;
            }
        }
        return coms;
    }


    /**
     * 计算适应度，筛选前适应度的前90%
     */
    public List<Combination> calculateFitness(List<ScoreAndTime> coms) {
        List<Combination> combinations = new ArrayList<>();
        for (ScoreAndTime com : coms) {
            Double t = com.getTime();
            Double s = com.getScore();
            // 计算适应度
            ScoreAndTime opt = optimalResults.get(optimalResults.size() - 1);
            double v = (ReadFile.limitTime / t) * (s / opt.getScore()) *
                    (t < opt.getTime() ? t / opt.getTime() : opt.getTime() / t);
            Combination combination = new Combination();
            combination.setBinary(com.getBinary());
            combination.setFitness(v);
            combinations.add(combination);
        }
        // 以适应度降序排序
        Collections.sort(combinations, Collections.reverseOrder());

        // select 移除末尾的10%
        int size = combinations.size();
        for (int i = size - 1; i >= (int) (size * .9); i--) {
            combinations.remove(i);
        }
        // 多添加一个最优组合
        combinations.add(combinations.get(0));
        return combinations;
    }


    /**
     * 交叉 轮盘赌算法
     */
    public List<String> mating(List<Combination> combinations) {
        double totalFitness = 0;
        // 总适应度
        for (Combination combination : combinations) {
            totalFitness += combination.getFitness();
        }

        // 获取亲本集
        List<Couple> couples = new ArrayList<>();
        for (int i = 0; i < totalNumber / 2; i++) {
            Couple couple = new Couple();
            for (int j = 0; j < 2; j++) {
                double flag = 0;
                // 生成[0, totalFitness)之间的随机值
                double random = Math.random() * totalFitness;
                for (Combination combination : combinations) {
                    if (random <= (flag += combination.getFitness())) {
                        if ((j & 1) != 1)
                            couple.setMaleParent(combination.getBinary());
                        else
                            couple.setFemaleParent(combination.getBinary());
                        break;
                    }
                }
            }
            couples.add(couple);
        }

        // 交换片段，产生后代
        List<String> children = new ArrayList<>();
        for (Couple couple : couples) {
            // 生成0.2 ~ 0.8的随机数
            double rand = .2 + Math.random() * .6;
            // 计算需要交换的片段长度
            int segmentLength = (int) (Math.round(ReadFile.totalQuestion * rand));
            // 确定交换片段的起始位点
            int position = (int) (Math.random() * (ReadFile.totalQuestion - segmentLength));
            // 交换片段
            StringBuilder msb = new StringBuilder(couple.getMaleParent());
            StringBuilder fsb = new StringBuilder(couple.getFemaleParent());
            String msub = msb.substring(position, position + segmentLength);
            String fsub = fsb.substring(position, position + segmentLength);
            msb.replace(position, position + segmentLength, fsub);
            fsb.replace(position, position + segmentLength, msub);
            children.add(msb.toString());
            children.add(fsb.toString());
        }
        return children;
    }


    /**
     * 变异
     */
    public List<String> mutation(List<String> childern) {
        // 在所有个体中筛选1%变更不超过5%的片段
        int num = (int) (Math.round(childern.size() * .01)); // 需要变异的数量
        for (int i = 0; i < num; i++) {
            // 获取突变个体
            int index = (int) (Math.random() * childern.size());
            StringBuilder target = new StringBuilder(childern.get(index));
            // 变异片段长度
            int length = (int) (Math.round(ReadFile.totalQuestion * Math.random() * .05));
            if (length < 1) length = 1;
            // 变异起始位置
            int pos = (int) (Math.random() * (ReadFile.totalQuestion - length));
            // 变异
            for (int j = pos; j < pos + length; j++) {
                if (target.charAt(j) == 48)
                    target.replace(j, j + 1, "1");
                else
                    target.replace(j, j + 1, "0");
            }
            childern.remove(index);
            childern.add(target.toString());
        }
        return childern;
    }


    /**
     * 获取最优结果集
     */
    public List<ScoreAndTime> getOptimalResults() {
        return optimalResults;
    }


    /**
     * 清空最优结果集
     */
    public void reset() {
        optimalResults.clear();
    }
}
