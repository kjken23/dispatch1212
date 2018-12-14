package com.algorithm.dispatch;

import java.util.Arrays;
import java.util.Map;

/**
 * 公共工具类
 *
 * @author kj
 */
public class DispatchUtils {
    public static Integer[] moveArrayElement(Integer[] array, int k) {
        if (k == 0) {
            return array;
        }
        int length = array.length;
        // 右移newk + n * length个位置，和右移newk个位置效果是一样的
        int newk = k % length;
        Integer[] newArray = new Integer[length];
        // 重复length次把元素从旧位置移到新位置
        for (int i = 0; i < length; i++) {
            // 求出元素新的位置
            int newPosition = (i + newk) % length;
            newArray[newPosition] = array[i];
        }
        return newArray;
    }

    public static void deepCopy(Integer[][] copyFrom, Integer[][] copyTo) {
        for (int i = 0; i < copyFrom.length; i++) {
            copyTo[i] = copyFrom[i].clone();
        }
    }

    public static Integer[][] initArray(int n, int t) {
        Integer[][] arrayList = new Integer[n][t];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < t; j++) {
                if (j == 0) {
                    arrayList[i][j] = 1;
                } else {
                    arrayList[i][j] = 0;
                }
            }
        }
        return arrayList;
    }

    public static void allSort(Integer[] array, int begin, int end, Map<Integer, Integer[]> result) {
        if (begin == end) {
            Integer[] temp = array.clone();
            int hash = Arrays.hashCode(temp);
            if (!result.containsKey(hash)) {
                result.put(hash, temp);
            }
            return;
        }
        //把子数组的第一个元素依次和第二个、第三个元素交换位置
        for (int i = begin; i <= end; i++) {
            swap(array, begin, i);
            allSort(array, begin + 1, end, result);
            //交换回来
            swap(array, begin, i);
        }
    }

    private static void swap(Integer[] array, int a, int b) {
        int tem = array[a];
        array[a] = array[b];
        array[b] = tem;
    }

    public static int getIndex(Integer[] arr, int value) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == value) {
                return i;
            }
        }
        return -1;
    }


}
