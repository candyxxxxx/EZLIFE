package com.ee5415.group2.ezlife.functions.currency_converter;

import java.text.DecimalFormat;

/**
 * Created by Harry on 2016/3/3.
 */
public class ConvertHandler {
    private String value;
    private double num;
    private ConvertInfo ci;

    public ConvertHandler(){
    }

    public ConvertInfo getCi() {
        return ci;
    }

    public void convertHandler() {
        double result1 = 0;
        double result2 = 0;
        double result3 = 0;
        double result4 = 0;
        double result5 = 0;
        String[] st = new String[5];
        try {
            DecimalFormat df = new DecimalFormat("#.####");
            FindExchangeRateAndConvert fac = new FindExchangeRateAndConvert();
            st[0] = String.valueOf(df.format(fac.findExchangeRateAndConvert("HKD", "CNY")));
            st[1] = String.valueOf(df.format(fac.findExchangeRateAndConvert("HKD", "USD")));
            st[2] = String.valueOf(df.format(fac.findExchangeRateAndConvert("HKD", "GBP")));
            st[3] = String.valueOf(df.format(fac.findExchangeRateAndConvert("HKD", "EUR")));
            st[4] = String.valueOf(df.format(fac.findExchangeRateAndConvert("HKD", "JPY")));
            for (int i = 0; i < 5; i++)
                System.out.println(st[i]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ci = new ConvertInfo(st);
    }
}

