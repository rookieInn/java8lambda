package com.extrigger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PickElements {

	public static void main(String[] args) {
		//Finding Elements 
		//Pick the ones that start with the letter N.
		
		List<String> friends = Arrays.asList("Brian", "Nate", "Neal", "Raju", "Sara", "Scott");
		
		//function1
		final List<String> startsWithN = new ArrayList<>();
		for (String name : friends) {
			if(name.startsWith("N")){
				startsWithN.add(name);
			}
		}
		
		System.out.println(startsWithN);
		
		//function using filter() method
		final List<String> startsWithN1 = 
				friends.stream()
					   .filter(name -> name.startsWith("N"))
					   .collect(Collectors.toList());
		System.out.println(String.format("Found %d names", startsWithN1.size()));
	}
	
}
