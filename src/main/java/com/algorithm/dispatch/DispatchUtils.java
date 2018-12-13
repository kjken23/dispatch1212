package com.algorithm.dispatch;

/**
 * 公共工具类
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

    public static void deepCopy(Byte[][] copyFrom, Byte[][] copyTo) {
        for (int i = 0; i < copyFrom.length; i++) {
            copyTo[i] = copyFrom[i].clone();
        }
    }

    public static Byte[][] initArray(int n, int t) {
        Byte[][] arrayList = new Byte[n][t];
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
}
