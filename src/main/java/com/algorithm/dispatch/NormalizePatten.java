package com.algorithm.dispatch;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 模式归一化
 * @author kj
 */
public class NormalizePatten {
    public static Integer[] normalize(Integer[] array) {
        Integer[] temp = array.clone();
        Arrays.sort(temp);
        int minPos = DispatchUtils.getIndex(array, temp[0]);
        return DispatchUtils.moveArrayElement(array, array.length - minPos);
    }

    public static Map<Integer, Integer[]> removeRedundancy(List<Integer[]> normalizedList) {
        Map<Integer, Integer[]> normalizedResult = new HashMap<>();
        for(Integer[] normalized : normalizedList) {
            boolean flag = true;
            int hash = Arrays.hashCode(normalized);
            for (int i = 0; i < normalized.length; i++) {
                Integer[] temp = normalized.clone();
                int tempHash = Arrays.hashCode(DispatchUtils.moveArrayElement(temp, i));
                if(normalizedResult.containsKey(tempHash)) {
                    flag = false;
                    break;
                }
            }
            if(!flag) {
                continue;
            }
            normalizedResult.put(hash, normalized);
        }
        return normalizedResult;
    }
}
