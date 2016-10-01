package com.extrigger;

import java.math.BigDecimal;
import java.util.function.Predicate;

/**
 * Created by gxy on 2016/9/7.
 */
public class PickStockImperativeClubbed {

    public static void main(String[] args) {
        StockInfo highPriced = new StockInfo("", BigDecimal.ZERO);
        Predicate<StockInfo> isPriceLessThan500 = StockUtil.isPriceLessThan(500);

        for (String symbol : Tickers.symbols) {
            StockInfo stockInfo = StockUtil.getPrice(symbol);
            if(isPriceLessThan500.test(stockInfo))
                highPriced = StockUtil.pickHigh(highPriced, stockInfo);
        }

        System.out.println("High priced under $500 is " + highPriced);
    }


}
