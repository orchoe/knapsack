package com.codingcow.knapsack.service;

import com.codingcow.knapsack.pojo.ScoreAndTime;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Austin Zhang
 * @Date: 2018-09-28 17:53
 * @Email: austin.zhang@dadaabc.com
 */
@Service
public class ReadFile {

    public static volatile double limitTime;
    public static volatile int totalQuestion;
    public static volatile List<ScoreAndTime> questions = new ArrayList<>(); // 题集

    /**
     * 读取输入文本
     */
    public void readText(FileInputStream stream) {
        try {
            questions.clear();
            InputStreamReader read = new InputStreamReader(
                    stream, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(read);
            String lineTxt;
            int i = 0;
            while ((lineTxt = bufferedReader.readLine()) != null) {
                if (i == 0) {
                    limitTime = Double.parseDouble(lineTxt.split(" ")[0]);
                    totalQuestion = Integer.parseInt(lineTxt.split(" ")[1]);
                } else if (!"".equals(lineTxt.trim())) {
                    questions.add(new ScoreAndTime(Double.parseDouble(lineTxt.split(" ")[0]),
                            Double.parseDouble(lineTxt.split(" ")[1])));
                }
                i++;
            }
            bufferedReader.close();
            read.close();
            if (totalQuestion != questions.size()) totalQuestion = questions.size();
        } catch (Exception e) {
            System.out.println("读取文件内容出错!");
            e.printStackTrace();
        }
    }

}
