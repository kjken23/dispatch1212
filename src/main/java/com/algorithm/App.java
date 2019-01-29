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
    private static List<Integer> tmpArr = new ArrayList<>();
    private static List<Integer> tmpArr2 = new ArrayList<>();
    private static int fit = 0;
    private static Map<Integer, Integer[]> resultMap;
    private static Map<Integer, HashSet<Integer>> frontward = new HashMap<>();
    private static Map<Integer, HashSet<Integer>> backward = new HashMap<>();
    private static Map<List<Integer[]>, Boolean> combineResult = new ConcurrentHashMap<>();

    private static void combine(int index, int k, List<Integer> arr, int n) {
        if (k == 1) {
            for (int i = index; i < arr.size(); i++) {
                tmpArr.add(arr.get(i));
                if (ruleOutImpossible(tmpArr)) {
                    List<Integer[]> tmp = new ArrayList<>();
                    for (Integer integer : tmpArr) {
                        tmp.add(resultMap.get(integer));
                    }
                    combineResult.put(tmp, false);
                }
                tmpArr.remove(arr.get(i));
            }
        } else if (k > 1) {
            for (int i = index; i <= arr.size() - k; i++) {
                tmpArr.add(arr.get(i));
                if (ruleOutImpossible(tmpArr)) {
                    combine(i + 1, k - 1, arr, n);
                }
                tmpArr.remove(arr.get(i));
            }
        }
    }

    private static boolean ruleOutImpossible(List<Integer> list) {
        if (list.size() == 0) {
            return false;
        } else if (list.size() == 1) {
            return true;
        } else {
            HashSet<Integer> set = new HashSet<>(frontward.get(list.get(0)));
            for (int i = 1; i < list.size(); i++) {
                for (Integer integer : set) {
                    HashSet<Integer> temp = backward.get(integer);
                    if (temp != null && temp.contains(list.get(i))) {
                        return false;
                    }
                }
                set.addAll(frontward.get(list.get(i)));
            }
        }
        return true;
    }

    private static boolean ruleOutNoRepeat(Integer[] array, int n) {
        Set<Integer> repeat = new HashSet<>(Arrays.asList(array));
        return repeat.size() != n;
    }

    private static HashSet<Integer> combine2(int index, int k, List<Integer> arr, HashSet<Integer> set, int n) {
        if (k == 1) {
            for (int i = index; i < arr.size(); i++) {
                if (i == index) {
                    tmpArr2.add(arr.get(i));
                    int sum = 0;
                    for (Integer d : tmpArr2) {
                        sum += d;
                    }
                    set.add(sum + n);
                    tmpArr2.remove(arr.get(i));
                }
            }
        } else if (k > 1) {
            for (int i = index; i <= arr.size() - k; i++) {
                if (i == index) {
                    tmpArr2.add(arr.get(i));
                    combine2(i + 1, k - 1, arr, set, n);
                    tmpArr2.remove(arr.get(i));
                }
            }
        }
        return set;
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

        //创建间隔索引
        Collection<Integer[]> resultCollection = normalizedResult.values();
        resultMap = new HashMap<>(resultCollection.size());
        List<Integer> resultList = new ArrayList<>();

        int index = 0;
        for (Integer[] array : resultCollection) {
            if(ruleOutNoRepeat(array, n)) {
                resultMap.put(index, array);
                resultList.add(index);
                HashSet<Integer> set = new HashSet<>(Arrays.asList(array));

                Integer[] tempArray = new Integer[array.length];
                System.arraycopy(array, 0, tempArray, 0, array.length);
                for (int i = 2; i < n; i++) {
                    for (int j = 0; j < tempArray.length; j++) {
                        tempArray = DispatchUtils.moveArrayElement(tempArray, 1);
                        HashSet<Integer> sum = combine2(0, i, Arrays.asList(tempArray), new HashSet<>(), i - 1);
                        set.addAll(sum);
                    }
                }
                frontward.put(index, set);
                for (Integer d : set) {
                    HashSet<Integer> temp = backward.get(d) == null ? new HashSet<>() : backward.get(d);
                    temp.add(index);
                    backward.put(d, temp);
                }
                index++;
            }
        }

        //组合模式
        combine(0, n, resultList, n);
        System.out.println("----------模式组合完毕------------");

        if(combineResult.size() > 0) {
            ExecutorService verifyExecutorService = new ThreadPoolExecutor(10, 20, 15, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
            List<Future<Map.Entry<List<Integer[]>, Boolean>>> verifyList = new ArrayList<>();

            for (Map.Entry<List<Integer[]>, Boolean> entry : combineResult.entrySet()) {
                Verify verify = new Verify(n, t, entry);
                verifyList.add(verifyExecutorService.submit(verify));
            }

            for (Future<Map.Entry<List<Integer[]>, Boolean>> future : verifyList) {
                try {
                    combineResult.replace(future.get().getKey(), future.get().getValue());
                    if (future.get().getValue()) {
                        fit++;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            verifyExecutorService.shutdown();
        }
        System.out.println("----------模式验证完毕------------");

        if (fit > 0) {
            System.out.println("T = " + t + "时： 共" + fit + "种调度方案");
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
