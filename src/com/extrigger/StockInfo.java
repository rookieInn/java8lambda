package com.extrigger;

import java.math.BigDecimal;

/**
 * Created by gxy on 2016/9/7.
 */
public class StockInfo {

    public String ticker;
    public BigDecimal price;

    public StockInfo(String symbol, BigDecimal thePrice) {
        ticker = symbol;
        price = thePrice;
    }

    @Override
    public String toString() {
        return String.format("ticker: %s price: %g", ticker, price);
    }
}
