package com.algorithm.dispatch;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * 验证
 *
 * @author kj
 */
public class Verify implements Callable<Map.Entry<List<Integer[]>, Boolean>> {
    private int n;
    private int t;
    private Map.Entry<List<Integer[]>, Boolean> map;
    private Long[] array;

    public Verify(int n, int t, Map.Entry<List<Integer[]>, Boolean> map) {
        this.n = n;
        this.t = t;
        this.map = map;
        array = new Long[n];
    }

    private boolean judge(Long[] arrayList) {
        if (arrayList.length == 0) {
            return false;
        }
        long mask = (long)((2 << t) - 1);
        for (int i = 0; i < n; i++) {
            long others = 0L;
            for (int j = 0; j < n; j++) {
                if (j == i) {
                    continue;
                }
                others = others | arrayList[j];
            }
            if(((arrayList[i] & mask) & (~others & mask)) <= 0L) {
                return false;
            }
        }
        return true;
    }

    private boolean verify(Long[] arrayList, int n) {
        if (arrayList.length == 0) {
            return false;
        }
        if (n == 0) {
            array = arrayList.clone();
//            System.out.println("当前验证调度方案：");
//            for (Integer[] a : arrayList) {
//                System.out.println(Arrays.toString(a));
//            }
//            System.out.println("----------------------");
        }
        boolean flag = true;
        if (n != arrayList.length - 1) {
            for (int i = 0; i < t + 1; i++) {
                if (i > 0) {
                    arrayList[n] = DispatchUtils.rotateRight(arrayList[n], 1, t);
                }
                if (i == t) {
                    arrayList = array;
//                    System.out.println("完成第" + (n + 1) + "层");
                    break;
                }
                flag = verify(arrayList, n + 1);
                if (!flag) {
                    return false;
                }
            }
        } else {
            for (int i = 0; i < t + 1; i++) {
                if (i > 0) {
                    arrayList[n] = DispatchUtils.rotateRight(arrayList[n], 1, t);
                }
                if (i == t) {
                    arrayList = array;
//                    System.out.println("完成最内层");
                    break;
                }
                flag = judge(arrayList);

                if (!flag) {
//                    for (Integer[] a : arrayList) {
//                        System.out.println(Arrays.toString(a));
//                    }
//                    System.out.println("该情况下验证不通过\n");
                    return false;
                }
            }
        }
//        if(n == 0) {
//            System.out.println("调度方案：");
//            for (Integer[] a : arrayList) {
//                System.out.println(Arrays.toString(a));
//            }
//            System.out.println("验证通过！");
//        }
        return true;
    }

    private boolean formatAndVerify(List<Integer[]> list) {
        char[][] tempMartix = DispatchUtils.initArray(n, t);
        for (int i = 0, len1 = list.size(); i < len1; i++) {
            int pos = 1;
            for (Integer integer : list.get(i)) {
                pos += integer;
                tempMartix[i][pos % t] = '1';
                pos++;
            }
        }
        Long[] martix = new Long[n];
        for (int i = 0; i < tempMartix.length; i++) {
            martix[i] = Long.valueOf(String.valueOf(tempMartix[i]), 2);
        }
        return verify(martix, 0);
    }

    @Override
    public Map.Entry<List<Integer[]>, Boolean> call() throws Exception {
        map.setValue(this.formatAndVerify(map.getKey()));
        return map;
    }
}
