package com.extrigger;

public class Asset {

	public enum AssetType{BOND, STOCK};
	private AssetType type;
	private int value;
	
	public Asset(AssetType type, int value) {
		super();
		this.type = type;
		this.value = value;
	}

	public AssetType getType() {
		return type;
	}

	public int getValue() {
		return value;
	}
	
}
