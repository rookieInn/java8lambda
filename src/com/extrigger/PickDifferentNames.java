package com.extrigger;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class PickDifferentNames {

	public static Predicate<String> checkIfStartsWith(final String letter) {
		return name -> name.startsWith(letter);
	}
	
	public static void main(String[] args) {
		//Duplication in Lambda Expression
		final List<String> friends = Arrays.asList("Brian", "Nate", "Neal", "Raju", "Sara", "Scott");
		
		//function1
		final Predicate<String> startsWithN = name -> name.startsWith("N");
		final Predicate<String> startsWithB = name -> name.startsWith("B");
		
		final long countFriendsStartN = friends.stream().filter(startsWithN).count();
		final long countFriendsStartB = friends.stream().filter(startsWithB).count();
		
		System.out.println(String.format("%d %d", countFriendsStartN, countFriendsStartB));
		
		//function2
		final long countFriendsStartN1 = friends.stream().filter(checkIfStartsWith("N")).count();
		final long countFriendsStartB1 = friends.stream().filter(checkIfStartsWith("B")).count();
		System.out.println(String.format("%d %d", countFriendsStartN1, countFriendsStartB1));
		
		//function3 Refactoring to Narrow Scope
		//unsing a Function class
		final Function<String, Predicate<String>> startsWithLetter =
				(String letter) -> {
					Predicate<String> checkStartsWith = (String name) -> name.startsWith(letter);
					return checkStartsWith;
				};
		
		final Function<String, Predicate<String>> startsWithLetter1 = (String letter) -> (String name) -> name.startsWith(letter);
		
		final Function<String, Predicate<String>> startsWithLetter2 = l -> n -> n.startsWith(l);
	
		final long countFrinedsStartN2 = friends.stream().filter(startsWithLetter2.apply("N")).count();
		final long countFrinedsStartB2 = friends.stream().filter(startsWithLetter2.apply("B")).count();
		System.out.println(String.format("%d %d", countFrinedsStartN2, countFrinedsStartB2));
	}
	
}
