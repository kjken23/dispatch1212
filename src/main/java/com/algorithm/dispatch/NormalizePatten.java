package com.algorithm.dispatch;

import java.util.Arrays;

/**
 * 模式归一化
 */
public class NormalizePatten {
    public static Integer[] normalize(Integer[] array) {
        Integer[] temp = array.clone();
        Arrays.sort(temp);
        int minPos = DispatchUtils.getIndex(array, temp[0]);
        return DispatchUtils.moveArrayElement(array, array.length - minPos);
    }
}
