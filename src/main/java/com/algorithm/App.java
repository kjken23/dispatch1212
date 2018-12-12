package com.algorithm;

import com.algorithm.dispatch.Verify;

import java.util.*;

/**
 * 主程序
 * @author kj
 */
public class App 
{
    public static void main( String[] args )
    {
        Date start = new Date();
        int n = 3;
        int tStart = 12;
        int tMax = 13;
        Map<Integer, List<Byte[][]>> resultMap = new HashMap<>(tMax - tStart);
        for (int t = tStart; t < tMax; t++) {
            Verify verify = new Verify();
            Byte[][] temp = Verify.initArray(n, t);
            verify.setArray(new Byte[n][t]);
            verify.nextArray(temp, 0);
            resultMap.put(t, verify.getFit());
        }
        for (Map.Entry<Integer, List<Byte[][]>> entry : resultMap.entrySet()) {
            if (entry.getValue().size() > 0) {
                System.out.println("T = " + entry.getKey() + "时： 共" + entry.getValue().size() + "种调度方案");
//                for (Byte[][] arrayList : entry.getValue()) {
//                    for (Byte[] a : arrayList) {
//                        System.out.println(Arrays.toString(a));
//                    }
//                    System.out.println("------------------------");
//                }
            } else {
                System.out.println("T = " + entry.getKey() + "时：");
                System.out.println("该情况下没有符合条件的调度方案");
            }
        }
//        Byte[][] temp = new ArrayList<>();
//        Byte[] array1 = new Byte[] {1,0,1,0,1,0,0,0,0,0,0,0,0,0,0};
//        Byte[] array2 = new Byte[] {1,0,0,1,0,0,1,0,0,0,0,0,0,0,0};
//        Byte[] array3 = new Byte[] {1,0,0,0,0,1,0,0,0,0,1,0,0,0,0};
//        temp.add(array1);
//        temp.add(array2);
//        temp.add(array3);
//        verify(temp,0);
        Date end = new Date();
        System.out.println("共耗时" + (end.getTime() - start.getTime()) / 1000 + "秒");
    }
}
