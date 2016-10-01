package com.extrigger;

import java.math.BigDecimal;
import java.util.stream.Collectors;

/**
 * Created by gxy on 2016/9/7.
 */
public class Stocks100 {

    public static void main(String[] args) {
        final BigDecimal HUNDRED = new BigDecimal("100");
        System.out.println("Stocks priced over $100 are " +
            Tickers.symbols
                   .stream()
                   .filter(symbol -> YaHooFinance.getPrice(symbol).compareTo(HUNDRED) > 0)
                   .sorted()
                   .collect(Collectors.joining(", "))
        );
    }




}
