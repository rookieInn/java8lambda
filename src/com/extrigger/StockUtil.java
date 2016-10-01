package com.extrigger;

import java.math.BigDecimal;
import java.util.function.Predicate;

/**
 * Created by gxy on 2016/9/7.
 */
public class StockUtil {

    public static StockInfo getPrice(String ticker) {
        return new StockInfo(ticker, YaHooFinance.getPrice(ticker));
    }

    public static Predicate<StockInfo> isPriceLessThan(int price) {
        return stockInfo -> stockInfo.price.compareTo(BigDecimal.valueOf(price)) < 0;
    }

    public static StockInfo pickHigh(StockInfo stockInfo1, StockInfo stockInfo2) {
        return stockInfo1.price.compareTo(stockInfo2.price) > 0 ? stockInfo1 : stockInfo2;
    }

}
