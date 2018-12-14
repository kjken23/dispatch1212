package com.algorithm.dispatch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * 模式寻找
 */
public class FindPatten implements Callable<Map<Integer,List<Integer[]> >> {
    private int n;
    private int t;
    private List<Integer[]> temp = new ArrayList<>();
    private List<Integer[]> result = new ArrayList<>();
    private Map<Integer,List<Integer[]> > resultMap = new HashMap<>();

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

    public Map<Integer, List<Integer[]>> getResultMap() {
        return resultMap;
    }

    public void setResultMap(Map<Integer, List<Integer[]>> resultMap) {
        this.resultMap = resultMap;
    }

    public void split() {
        int m = t - n;
        temp.add(new Integer[]{1,1});
        temp.add(new Integer[]{2});
        if (m == 1) {
            return ;
        } else {
            for (int i = 3; i <= m; i++) {
                int size=temp.size();
                for(int j=0;j<size;j++){
                    Integer[] ca=temp.get(j);
                    Integer[] newca=new Integer[ca.length+1];
                    newca[0]=1;
                    System.arraycopy(ca, 0, newca, 1, ca.length);
                    ca=null;
                    ca=newca;
                    temp.set(j, ca);
                    Integer[] cs=merger(ca);
                    if(cs!=null){
                        temp.add(cs.clone());
                    }
                }
                temp.add(new Integer[]{i});
            }
        }
        for(Integer[] ca:temp){
            if (ca.length == n) {
                result.add(ca);
            }
        }
        resultMap.put(n, result);
    }

    private Integer[] merger(Integer[] ca) {
        if (ca.length <= 2) {
            return null;
        }
        if (ca[0] + ca[1] <= ca[2]) {
            Integer[] rca = new Integer[ca.length - 1];
            rca[0] = ca[0] + ca[1] ;
            System.arraycopy(ca, 2, rca, 1, ca.length - 2);
            return rca;
        }
        return null;
    }

    @Override
    public Map<Integer,List<Integer[]> > call() throws Exception {
        this.split();
        return this.getResultMap();
    }
}
