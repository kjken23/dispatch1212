package com.algorithm.dispatch;

import java.math.BigInteger;
import java.util.List;

/**
 * 验证
 *
 * @author kj
 */
public class Verify {
    private int n;
    private int t;
    private BigInteger[] array;

    public Verify(int n, int t) {
        this.n = n;
        this.t = t;
        array = new BigInteger[n];
    }

    private boolean judge(BigInteger[] arrayList) {
        if (arrayList.length == 0) {
            return false;
        }
        boolean flag = true;
        for (int i = 0; i < n; i++) {
            int count = 0;
            BigInteger others = BigInteger.valueOf(0L);
            for (int j = 0; j < n; j++) {
                if (j == i) {
                    continue;
                }
                others = others.or(arrayList[j]);
            }
            for (int j = t; j > 0; j--) {
                BigInteger mask = BigInteger.valueOf(1 << (j - 1));
                if (arrayList[i].and(mask).and(others.not().and(mask)).compareTo(BigInteger.ZERO) > 0) {
                    count++;
                }
            }
            if (count < 1) {
                return false;
            }
        }
        return true;
    }

    private boolean verify(BigInteger[] arrayList, int n) {
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
                    arrayList = array;
                    arrayList[n] = DispatchUtils.rotateRight(arrayList[n], i, t);
                }
                if (i == t) {
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
                    arrayList = array;
                    arrayList[n] = DispatchUtils.rotateRight(arrayList[n], i, t);
                }
                if (i == t) {
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

    public BigInteger[] formatAndVerify(List<Integer[]> list) {
        char[][] tempMartix = DispatchUtils.initArray(n, t);
        for (int i = 0, len1 = list.size(); i < len1; i++) {
            int pos = 1;
            for (Integer integer : list.get(i)) {
                pos += integer;
                tempMartix[i][pos % t] = '1';
                pos++;
            }
        }
        BigInteger[] martix = new BigInteger[n];
        for (int i = 0; i < tempMartix.length; i++) {
            martix[i] = new BigInteger(String.valueOf(tempMartix[i]), 2);
        }
        boolean flag = verify(martix, 0);
        if (flag) {
            return martix;
        } else {
            return null;
        }
    }
}
