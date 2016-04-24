package com.ee5415.group2.ezlife.functions.currency_converter;

/**
 * Created by Harry on 2016/3/3.
 */
public class ConvertInfo {

    private String[] st;
    private static double result1;
    private static double result2;
    private static double result3;
    private static double result4;
    private static double result5;

    public ConvertInfo(String[] result) {
        this.st = result;
        this.result1 = Double.parseDouble(result[0]);
        this.result2 = Double.parseDouble(result[1]);
        this.result3 = Double.parseDouble(result[2]);
        this.result4 = Double.parseDouble(result[3]);
        this.result5 = Double.parseDouble(result[4]);
    }
    public String[] getSt() {
        return st;
    }

    public void setSt(String[] st) {
        this.st = st;
    }
    //    public void setResult(double num){
//        this.result1 *= num;
//        this.result2 *= num;
//        this.result3 *= num;
//        this.result4 *= num;
//        this.result5 *= num;
//    }
    public double getResult(int i){
        String[] st = new String[5];
        st[0] = String.valueOf(result1);
        st[1] = String.valueOf(result2);
        st[2] = String.valueOf(result3);
        st[3] = String.valueOf(result4);
        st[4] = String.valueOf(result5);
        return Double.parseDouble(st[i]);
    }
}

