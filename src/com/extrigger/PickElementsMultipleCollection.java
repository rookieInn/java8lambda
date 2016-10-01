package com.extrigger;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class PickElementsMultipleCollection {

	public static void main(String[] args) {
		//Reusing lambda Expressions
		final List<String> friends = Arrays.asList("Brian", "Nate", "Neal", "Raju", "Sara", "Scott");
		final List<String> comrades = Arrays.asList("Kate", "Ken", "Nick", "Paula", "Zach");
		final List<String> editors = Arrays.asList("Brian", "Jackie", "John", "Mike");
		
		//function1
		final long countFriendsStartN = friends.stream().filter(name -> name.startsWith("N")).count();
		final long countComradesStartN = comrades.stream().filter(name -> name.startsWith("N")).count();
		final long countEditorsStartN = editors.stream().filter(name -> name.startsWith("N")).count();
		System.out.println(String.format("%d %d %d", countFriendsStartN, countComradesStartN, countEditorsStartN));
		
		//function2
		Predicate<String> startsWithN = e -> e.startsWith("N");
		final long countFriendsStartN1 = friends.stream().filter(startsWithN).count();
		final long countComradesStartN1 = comrades.stream().filter(startsWithN).count();
		final long countEditorsStartN1 = editors.stream().filter(startsWithN).count();
		System.out.println(String.format("%d %d %d", countFriendsStartN1, countComradesStartN1, countEditorsStartN1));
	}
	
}
