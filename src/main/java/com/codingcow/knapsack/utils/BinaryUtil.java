package com.codingcow.knapsack.utils;

import com.codingcow.knapsack.service.ReadFile;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * @Author: Austin Zhang
 * @Date: 2018-09-28 18:13
 * @Email: austin.zhang@dadaabc.com
 */
public class BinaryUtil {

    /**
     * 生成一定数量的不重复的二进制随机数
     */
    public static Set<String> generateRandomBinaryDigits(int length, int num) {
        Set<String> digits = new HashSet<>();
        Random random = new Random();
        for (; ; ) {
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < length; j++) {
                if (random.nextBoolean())
                    sb.append(1);
                else
                    sb.append(0);
            }
            digits.add(sb.toString());
            if (digits.size() == num) break;
        }
        return digits;
    }
}
