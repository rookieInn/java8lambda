package com.extrigger;

import java.util.stream.Stream;

/**
 * Created by gxy on 2016/9/7.
 */
public class PickStockFunctional {

    public static void findHighPriced(Stream<String> symbols) {
        StockInfo highPriced = symbols.map(StockUtil::getPrice)
                                      .filter(StockUtil.isPriceLessThan(500))
                                      .reduce(StockUtil::pickHigh)
                                      .get();
        System.out.println("High priced under $500 is " + highPriced);
    }

    public static void main(String[] args) {
        findHighPriced(Tickers.symbols.stream());
    }
}
