package com.algorithm;

import com.algorithm.dispatch.DispatchUtils;
import com.algorithm.dispatch.FindPatten;
import com.algorithm.dispatch.NormalizePatten;

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
        List<Integer[]> pattenList = new ArrayList<>();
        ExecutorService executorService = new ThreadPoolExecutor(n,n,10, TimeUnit.MINUTES, new LinkedBlockingDeque<>());
        List<Future<Map<Integer,List<Integer[]> >>> futureList = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            FindPatten findPatten = new FindPatten(n - i, t - i);
            futureList.add(executorService.submit(findPatten));
        }
        for (Future<Map<Integer,List<Integer[]> >> future : futureList) {
            try {
                for(Map.Entry<Integer, List<Integer[]>> entry : future.get().entrySet()) {
                    // 对于少于n的结果补0
                    if (entry.getKey() != n) {
                        for(Integer[] ca:entry.getValue()) {
                            Integer[] newca = new Integer[n];
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
        Map<Integer, Integer[]> allSortResult = new HashMap<>();
        for(Integer[] sort : pattenList) {
            DispatchUtils.allSort(sort, 0, sort.length - 1, allSortResult);
        }
        List<Integer[]> normalizedList = new ArrayList<>();
        for(Map.Entry<Integer, Integer[]> entry : allSortResult.entrySet()) {
            Integer[] normalized = NormalizePatten.normalize(entry.getValue());
            normalizedList.add(normalized);
        }
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
        for(Integer[] array : normalizedResult.values()) {
            System.out.println(Arrays.toString(array));
            System.out.println("----------------------");
        }
    }
}
