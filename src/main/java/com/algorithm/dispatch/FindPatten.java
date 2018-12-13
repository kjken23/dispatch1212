package com.algorithm.dispatch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * 模式寻找
 */
public class FindPatten implements Callable<Map<Integer,List<int[]> >> {
    private int n;
    private int t;
    private List<int[]> temp = new ArrayList<>();
    private List<int[]> result = new ArrayList<>();
    private Map<Integer,List<int[]> > resultMap = new HashMap<>();

    public FindPatten(int n, int t) {
        this.n = n;
        this.t = t;
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    public int getT() {
        return t;
    }

    public void setT(int t) {
        this.t = t;
    }

    public Map<Integer, List<int[]>> getResultMap() {
        return resultMap;
    }

    public void setResultMap(Map<Integer, List<int[]>> resultMap) {
        this.resultMap = resultMap;
    }

    public void split() {
        int m = t - n;
        temp.add(new int[]{1,1});
        temp.add(new int[]{2});
        if (m == 1) {
            return ;
        } else {
            for (int i = 3; i <= m; i++) {
                int size=temp.size();
                for(int j=0;j<size;j++){
                    int[] ca=temp.get(j);
                    int[] newca=new int[ca.length+1];
                    newca[0]=1;
                    System.arraycopy(ca, 0, newca, 1, ca.length);
                    ca=null;
                    ca=newca;
                    temp.set(j, ca);
                    int[] cs=merger(ca);
                    if(cs!=null){
                        temp.add(cs.clone());
                    }
                }
                temp.add(new int[]{i});
            }
        }
        for(int[] ca:temp){
            if (ca.length == n) {
                result.add(ca);
            }
        }
        resultMap.put(n, result);
    }

    private int[] merger(int[] ca) {
        if (ca.length <= 2) {
            return null;
        }
        if (ca[0] + ca[1] <= ca[2]) {
            int[] rca = new int[ca.length - 1];
            rca[0] = ca[0] + ca[1] ;
            System.arraycopy(ca, 2, rca, 1, ca.length - 2);
            return rca;
        }
        return null;
    }

    @Override
    public Map<Integer,List<int[]> > call() throws Exception {
        this.split();
        return this.getResultMap();
    }
}
