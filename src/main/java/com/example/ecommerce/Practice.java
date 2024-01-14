package com.example.ecommerce;

import java.util.ArrayList;
import java.util.Arrays;

public class Practice {
    static int dp[][];
    ArrayList<String> ls = new ArrayList<>();
    public void substring(String s){
        for(int j=0;j<s.length();j++){
            for (int i = j; i < s.length(); i++) {
                ls.add(s.substring(j, i + 1));
            }
        }
        int count=0;
        for(String str : ls){
            for(int i=0;i<str.length();i++){
                if(str.charAt(i)=='c'){
                    count++;
                }
            }
        }
        System.out.println("count");
    }

    public static int minSorcerer(int[] sorcererPowers, int sakunaPower) {
        int[] dpMatrix = new int[sakunaPower + 1];
        Arrays.fill(dpMatrix, Integer.MAX_VALUE);
        dpMatrix[0] = 0;

        for (int i = 1; i <= sakunaPower; i++) {
            for (int j = 0; j < sorcererPowers.length; j++) {
                if (sorcererPowers[j] <= i) {
                    int subResult = dpMatrix[i - sorcererPowers[j]];
                    if (subResult != Integer.MAX_VALUE && subResult + 1 < dpMatrix[i]) {
                        dpMatrix[i] = subResult + 1;
                    }
                }
            }
        }

        if(dpMatrix[sakunaPower]==Integer.MAX_VALUE){
            return -1;
        }
        return dpMatrix[sakunaPower];
    }

    public static int minimumSubset(int arr[],int k,int i){
        if(k==0){
            return 0;
        }
        if(k<0 || i>=arr.length){
            return Integer.MAX_VALUE;
        }
        if(dp[k][i]!=-1){
            return dp[k][i];
        }
        int subsetResult = minimumSubset(arr,k-arr[i],i);
        if(subsetResult==Integer.MAX_VALUE){
            return dp[k][i]=Integer.MAX_VALUE;
        }
        return dp[k][i]=Math.min((1+subsetResult),minimumSubset(arr,k,i+1));
    }
    public static void main(String a[]){
        int arr[] = {2,5,7};
        int k= 10;
        dp = new int[k+1][arr.length+1];
        for(int dpA[] : dp){
            Arrays.fill(dpA,-1);
        }
        int c = minimumSubset(arr,k,0);
        System.out.println(
                c==Integer.MAX_VALUE ? -1 : c
        );
    }
}
