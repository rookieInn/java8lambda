
package com.extrigger;

import java.math.BigDecimal;

import org.junit.Test;

import junit.framework.Assert;

public class CalculateNAVTest {

	@Test
	public void computeStockWorth() {
		CalculateNAV calculateNAV = new CalculateNAV(ticker -> new BigDecimal("6.01"));
		
		BigDecimal expected = new BigDecimal("6010.00");
		Assert.assertEquals(0, calculateNAV.computeStockWorth("GOOG", 1000).compareTo(expected));
		
		
	}
	
	
}
