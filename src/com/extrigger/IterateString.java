package com.extrigger;

public class IterateString {

	public static void main(String[] args) {
		final String str = "w00t";
		str.chars().forEach(System.out::println);
		
		str.chars().forEach(IterateString::printChar);
		
		str.chars().mapToObj(ch -> Character.valueOf((char)ch)).forEach(System.out::println);
		
		str.chars().filter(ch -> Character.isDigit(ch)).forEach(ch -> printChar(ch));
		System.out.println(" ---------  ---------------");
		str.chars().filter(Character::isDigit).forEach(IterateString::printChar);
	}
	
	private static void printChar(int aChar) {
		System.out.println((char)aChar);
	}
	
}