package com.algorithm.dispatch;

import java.util.ArrayList;
import java.util.List;

/**
 * 模式选择
 */
public class SelectPatten {
    private static List<Integer[]> tmpArr = new ArrayList<>();

    public static void combine(int index, int k, List<Integer[]> arr, List<List<Integer[]>> result) {
        if (k == 1) {
            for (int i = index; i < arr.size(); i++) {
                tmpArr.add(arr.get(i));
                result.add(tmpArr);
                tmpArr.remove(arr.get(i));
            }
        } else if (k > 1) {
            for (int i = index; i <= arr.size() - k; i++) {
                //tmpArr都是临时性存储一下
                tmpArr.add(arr.get(i));
                //索引右移，内部循环，自然排除已经选择的元素
                combine(i + 1, k - 1, arr, result);
                //tmpArr因为是临时存储的，上一个组合找出后就该释放空间，存储下一个元素继续拼接组合了
                tmpArr.remove((Object) arr.get(i));
            }
        } else {
            return;
        }
    }
}
