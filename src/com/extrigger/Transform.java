package com.extrigger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Transform {

	public static void main(String[] args) {
		
		//convert a list of names to all capital letters
		
		List<String> friends = Arrays.asList("Brian", "Nate", "Neal", "Raju", "Sara", "Scott");
		
		// require the empty list
		final List<String> uppercaseNames = new ArrayList<String>();
		//function1  
		for (String name : friends) {
			uppercaseNames.add(name.toUpperCase());
		}
		
		//function2 use internal iterator forEach()
		friends.forEach(name -> uppercaseNames.add(name.toUpperCase()));
		uppercaseNames.forEach(System.out::println);
		
		//using lambda expression
		friends.stream()
			   .map(name -> name.toUpperCase())
			   .forEach(name -> System.out.print(name + " "));
		
		System.out.println();
		//input string output integer
		friends.stream()
			   .map(name -> name.length())
			   .forEach(count -> System.out.print(count + " "));
		
		//using method references
		friends.stream()
			   .map(String::toUpperCase)
			   .forEach(System.out::println);
		
	}
	
}
