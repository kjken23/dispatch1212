package com.algorithm;

import com.algorithm.dispatch.FindPatten;

import java.util.*;
import java.util.concurrent.*;

/**
 * 主程序
 *
 * @author kj
 */
public class App {
    public static void main(String[] args) {
        // 输入n和T
        Scanner sc = new Scanner(System.in);
        System.out.print("请输入n：");
        int n = sc.nextInt();
        System.out.print("请输入T：");
        int t = sc.nextInt();
        System.out.println("--------------------------");
        if (n > t) {
            System.out.println("请检查输入的n和T的值！");
            return;
        }

        // 使用多线程计算整数分割问题
        // 此处有一个疑问，多线程是否有必要，还没有做过测试
        List<int[]> pattenList = new ArrayList<>();
        ExecutorService executorService = new ThreadPoolExecutor(n,n,10, TimeUnit.MINUTES, new LinkedBlockingDeque<>());
        List<Future<Map<Integer,List<int[]> >>> futureList = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            FindPatten findPatten = new FindPatten(n - i, t - i);
            futureList.add(executorService.submit(findPatten));
        }
        for (Future<Map<Integer,List<int[]> >> future : futureList) {
            try {
                for(Map.Entry<Integer, List<int[]>> entry : future.get().entrySet()) {
                    // 对于少于n的结果补0
                    if (entry.getKey() != n) {
                        for(int[] ca:entry.getValue()) {
                            int[] newca = new int[n];
                            for (int i = 0; i < n - entry.getKey(); i++) {
                                newca[i] = 0;
                            }
                            System.arraycopy(ca, 0, newca, n - entry.getKey(), ca.length);
                            pattenList.add(newca);
                        }
                    } else {
                        pattenList.addAll(entry.getValue());
                    }
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        executorService.shutdown();
        for(int[] a : pattenList) {
            System.out.println(Arrays.toString(a));
        }
    }
}
