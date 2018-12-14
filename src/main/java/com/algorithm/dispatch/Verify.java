package com.algorithm.dispatch;

import java.util.List;

/**
 * 验证
 * @author kj
 */
public class Verify {
    private int n;
    private int t;
    private Integer[][] array;

    public Verify(int n, int t) {
        this.n = n;
        this.t = t;
        array = new Integer[n][t];
    }

    private static boolean judge(Integer[][] arrayList) {
        if (arrayList.length == 0) {
            return false;
        }
        boolean flag = true;
        for (int i = 0; i < arrayList.length; i++) {
            int count1 = 0;
            for (int j = 0; j < arrayList[i].length; j++) {
                int count2 = 0;
                for (Integer[] array : arrayList) {
                    if (array[j] == 1) {
                        count2++;
                    }
                }
                if (count2 == 1 && arrayList[i][j] == 1) {
                    count1++;
                }
            }
            if (count1 < 1) {
                return false;
            }
        }
        return true;
    }

    private boolean verify(Integer[][] arrayList, int n) {
        if (arrayList.length == 0) {
            return false;
        }
        if (n == 0) {
            DispatchUtils.deepCopy(arrayList, array);
//            System.out.println("当前验证调度方案：");
//            for (Integer[] a : arrayList) {
//                System.out.println(Arrays.toString(a));
//            }
//            System.out.println("----------------------");
        }
        boolean flag = true;
        if (n != arrayList.length - 1) {
            for (int i = 0; i < arrayList[0].length + 1; i++) {
                if (i > 0) {
                    arrayList = array;
                    arrayList[n] = DispatchUtils.moveArrayElement(arrayList[n], i);
                }
                if (i == arrayList[0].length) {
//                    System.out.println("完成第" + (n + 1) + "层");
                    break;
                }
                flag = verify(arrayList, n + 1);
                if (!flag) {
                    return false;
                }
            }
        } else {
            for (int i = 0; i < arrayList[0].length + 1; i++) {
                if (i > 0) {
                    arrayList = array;
                    arrayList[n] = DispatchUtils.moveArrayElement(arrayList[n], i);
                }
                if (i == arrayList[0].length) {
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

    public Integer[][] formatAndVerify(List<Integer[]> list) {
        Integer[][] martix = DispatchUtils.initArray(n,t);
        for (int i = 0, len1 = list.size(); i < len1; i++) {
            int pos = 1;
            for(Integer integer : list.get(i)) {
                pos += integer;
                martix[i][pos % t] = 1;
                pos++;
            }
        }
        boolean flag = verify(martix, 0);
        if (flag) {
            return martix;
        } else {
            return null;
        }
    }
}
