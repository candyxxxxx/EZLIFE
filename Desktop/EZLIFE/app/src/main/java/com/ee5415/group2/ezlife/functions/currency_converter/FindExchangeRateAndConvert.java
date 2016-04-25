package com.ee5415.group2.ezlife.functions.currency_converter;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Created by Harry on 2016/3/3.
 */
public class FindExchangeRateAndConvert {
    protected double findExchangeRateAndConvert(String from, String to) throws Exception{

        //Yahoo Finance API

        URL url = new URL("http://finance.yahoo.com/d/quotes.csv?f=l1&s=" + from + to + "=X");

        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
        String line = reader.readLine();

        if (line.length() > 0) {
            return Double.parseDouble(line);
        }
        reader.close();

        return 0;
    }
}
