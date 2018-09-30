package com.codingcow.knapsack.controller;

import com.codingcow.knapsack.dto.GenerationDto;
import com.codingcow.knapsack.pojo.Combination;
import com.codingcow.knapsack.pojo.ScoreAndTime;
import com.codingcow.knapsack.service.Reproduce;
import com.codingcow.knapsack.service.ReadFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: Austin Zhang
 * @Date: 2018-09-28 17:56
 * @Email: austin.zhang@dadaabc.com
 */
@RestController
public class KnapsackController {

    @Autowired
    private ReadFile readFile;
    @Autowired
    private Reproduce reproduce;


    @RequestMapping("/upload")
    public String uploadTextFile(@RequestParam("file") MultipartFile file) {
        if (!file.isEmpty()) {
            try {
                readFile.readText((FileInputStream) file.getInputStream());
            } catch (Exception e) {
                e.printStackTrace();
                return "failed";
            }
            return "success";
        } else {
            return "file is empty";
        }
    }


    @RequestMapping("/getCharts")
    public Object getCharts(Integer cycle) {
        if (ReadFile.totalQuestion == 0) {
            return "Please upload questions.";
        }
        if (cycle == null) {
            return "Please type in cycles.";
        }
        // 计算返回结果的index，固定返回9张图的数据
        int[] idxs = new int[9];
        idxs[0] = 0;
        idxs[8] = cycle - 1;
        float gap = (float) cycle / 8;
        for (int i = 1; i < 8; i++) {
            if (cycle <= 9)
                idxs[i] = i;
            else
                idxs[i] = Math.round(gap * i) > 1 ? Math.round(gap * i) - 1 : Math.round(gap * i);
        }

        reproduce.reset(); // 清空旧数据
        // 获取结果数据
        List<GenerationDto> list = new ArrayList<>();
        List<String> generation = null;
        int flag = 0;
        for (int i = 0; i < cycle; i++) {
            if (i == 0) {
                generation = reproduce.firstGeneration();
            }
            // 计算每一代的得分和用时
            List<ScoreAndTime> scoreAndTimes = reproduce.calculateScore(generation);
            // 计算适应度
            List<Combination> fitness = reproduce.calculateFitness(scoreAndTimes);
            // 交叉
            List<String> children = reproduce.mating(fitness);
            // 变异，产生新一代
            generation = reproduce.mutation(children);
            if (i == idxs[flag]) {
                GenerationDto dto = new GenerationDto();
                List<String[]> sats = new ArrayList<>();
                for (ScoreAndTime sat : scoreAndTimes) {
                    sats.add(new String[]{"" + sat.getTime(), "" + sat.getScore(), sat.getBinary()});
                }
                dto.setGeneration(sats);
                dto.setSequence(i + 1);
                ScoreAndTime opt = reproduce.getOptimalResults().get(i);
                dto.setOptimalCombination(new String[]{"" + opt.getTime(), "" + opt.getScore(), opt.getBinary()});
                list.add(dto);
                flag++;
            }
        }
        Map<String, Object> result = new HashMap<>();
        result.put("data", list);
        result.put("limitTime", ReadFile.limitTime);
        return result;
    }

}
