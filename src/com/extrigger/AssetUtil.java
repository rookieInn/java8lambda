package com.extrigger;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class AssetUtil {

	public static int totalAssetValues(List<Asset> assets) {
		return assets.stream()
				     .mapToInt(Asset::getValue)
				     .sum();
 	}
	
	public static int totalBondValues(List<Asset> assets) {
		return assets.stream()
				     .mapToInt(asset -> asset.getType() == Asset.AssetType.BOND ? asset.getValue() : 0)
				     .sum();
	}

	public static int totalStockValues(List<Asset> assets) {
		return assets.stream()
				     .mapToInt(asset -> asset.getType() == Asset.AssetType.STOCK ? asset.getValue() : 0)
				     .sum();
	}
	
	public static int totalAssetValues(List<Asset> assets, Predicate<Asset> assetSelector) {
		return assets.stream()
				     .filter(assetSelector)
				     .mapToInt(Asset::getValue)
				     .sum();
	}	
	
	
	public static void main(String[] args) {
		final List<Asset> assets = Arrays.asList(
				new Asset(Asset.AssetType.BOND, 1000),
				new Asset(Asset.AssetType.BOND, 2000),
				new Asset(Asset.AssetType.STOCK, 3000),
				new Asset(Asset.AssetType.STOCK, 4000)
				);

		System.out.println("Total of all assets: " + totalAssetValues(assets));
		System.out.println("Total of bonds: " + totalBondValues(assets));
		System.out.println("Total of stocks: " + totalStockValues(assets));
		
		
		//refactor version
		System.out.println("Total of all assets: " + totalAssetValues(assets, a -> true));
		System.out.println("Total of bonds: " + totalAssetValues(assets, a -> a.getType() == Asset.AssetType.BOND));
		System.out.println("Total of stocks: " + totalAssetValues(assets, a -> a.getType() == Asset.AssetType.STOCK));
		
	}
	
}
