package com.algorithm;

import com.algorithm.dispatch.DispatchUtils;
import com.algorithm.dispatch.FindPatten;
import com.algorithm.dispatch.NormalizePatten;
import com.algorithm.dispatch.Verify;

import java.util.*;
import java.util.concurrent.*;

/**
 * 主程序
 *
 * @author kj
 */
public class App {
    private static List<Integer[]> tmpArr = new ArrayList<>();
    private static List<List<Integer[]>> fit = new ArrayList<>();
    private static Map<List<Integer[]>, Boolean> combineResult = new ConcurrentHashMap<>();

    private static void combine(int index, int k, List<Integer[]> arr) {
        if (k == 1) {
            for (int i = index; i < arr.size(); i++) {
                tmpArr.add(arr.get(i));
                if (ruleOutImpossible(tmpArr)) {
                    List<Integer[]> tmp = new ArrayList<>(tmpArr);
                    combineResult.put(tmp, false);
                }
                tmpArr.remove(arr.get(i));
            }
        } else if (k > 1) {
            for (int i = index; i <= arr.size() - k; i++) {
                //tmpArr都是临时性存储一下
                tmpArr.add(arr.get(i));
                //索引右移，内部循环，自然排除已经选择的元素
                if (ruleOutImpossible(tmpArr)) {
                    combine(i + 1, k - 1, arr);
                }
                //tmpArr因为是临时存储的，上一个组合找出后就该释放空间，存储下一个元素继续拼接组合了
                tmpArr.remove(arr.get(i));
            }
        }
    }

    private static boolean ruleOutImpossible(List<Integer[]> list) {
        if (list.size() == 0) {
            return false;
        } else if (list.size() == 1) {
            return true;
        } else {
            boolean flag = true;
            HashSet<Integer> set = new HashSet<>(Arrays.asList(list.get(0)));
            for (int i = 1; i < list.size(); i++) {
                for (Integer integer : list.get(i)) {
                    flag = set.contains(integer);
                    if (flag) {
                        return false;
                    }
                }
                set.addAll(Arrays.asList(list.get(i)));
            }
        }
        return true;
    }

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

        Date start = new Date();
        // 使用多线程计算整数分割问题
        // 此处有一个疑问，多线程是否有必要，还没有做过测试
        List<Integer[]> pattenList = new ArrayList<>();
        ExecutorService findPattenExecutorService = new ThreadPoolExecutor(n, n, 10, TimeUnit.MINUTES, new LinkedBlockingDeque<>());
        List<Future<Map<Integer, List<Integer[]>>>> futureList = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            FindPatten findPatten = new FindPatten(n - i, t - i);
            futureList.add(findPattenExecutorService.submit(findPatten));
        }
        for (Future<Map<Integer, List<Integer[]>>> future : futureList) {
            try {
                for (Map.Entry<Integer, List<Integer[]>> entry : future.get().entrySet()) {
                    // 对于少于n的结果补0
                    if (entry.getKey() != n) {
                        for (Integer[] ca : entry.getValue()) {
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
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        findPattenExecutorService.shutdown();

        System.out.println("----------整数划分完毕------------");

        //对计算出的整数分割结果进行全排列
        Map<Integer, Integer[]> allSortResult = new HashMap<>();
        for (Integer[] sort : pattenList) {
            DispatchUtils.allSort(sort, 0, sort.length - 1, allSortResult);
        }

        System.out.println("----------划分结果全排列完毕------------");

        //对全排列结果进行归一化
        List<Integer[]> normalizedList = new ArrayList<>();
        for (Map.Entry<Integer, Integer[]> entry : allSortResult.entrySet()) {
            Integer[] normalized = NormalizePatten.normalize(entry.getValue());
            normalizedList.add(normalized);
        }

        System.out.println("----------归一化完毕------------");

        //去除归一化结果中的冗余部分
        Map<Integer, Integer[]> normalizedResult = NormalizePatten.removeRedundancy(normalizedList);

        //对归一化/去除冗余的结果进行组合并进行验证
        Collection<Integer[]> resultCollection = normalizedResult.values();
        List<Integer[]> resultList = new ArrayList<>(resultCollection);

        //组合模式
        combine(0, n, resultList);
        System.out.println("----------模式组合完毕------------");

        ExecutorService verifyExecutorService = new ThreadPoolExecutor(1000, 2000, 15, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
        List<Future<Map.Entry<List<Integer[]>, Boolean>>> verifyList = new ArrayList<>();

        for(Map.Entry<List<Integer[]>, Boolean> entry : combineResult.entrySet()) {
            Verify verify = new Verify(n, t, entry);
            verifyList.add(verifyExecutorService.submit(verify));
        }

        for(Future<Map.Entry<List<Integer[]>, Boolean>> future : verifyList) {
            try {
                combineResult.replace(future.get().getKey(), future.get().getValue());
                if (future.get().getValue()) {
                    fit.add(future.get().getKey());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        verifyExecutorService.shutdown();
        System.out.println("----------模式验证完毕------------");

        if (fit.size() > 0) {
            System.out.println("T = " + t + "时： 共" + fit.size() + "种调度方案");
        } else {
            System.out.println("T = " + t + "时：");
            System.out.println("该情况下没有符合条件的调度方案");
        }
        Date end = new Date();
        long total = end.getTime() - start.getTime();
        if (total < 1000) {
            System.out.println("共耗时" + total + "毫秒");
        } else {
            System.out.println("共耗时" + total / 1000 + "秒");
        }
    }
}
