package com.extrigger;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class Iteration {

	public static void main(String[] args) {
		List<String> friends = Arrays.asList("Brian", "Nate", "Neal", "Raju", "Sara", "Scott");
		
		//function1
		for (int i = 0; i < friends.size(); i++) {
			System.out.println(friends.get(i));
		}
		
		//function2
		for (String friend : friends) {
			System.out.println(friend);
		}
		
		//function3 internal iterator
		friends.forEach(new Consumer<String>(){
			@Override
			public void accept(String name) {
				System.out.println(name);
			}
		});
		
		//function3
		friends.forEach((String name) -> System.out.println(name));
		
		//function3 without type information
		friends.forEach((name) -> System.out.println(name));
		
		//function3 single-parameter
		friends.forEach(name -> System.out.println(name));
		
		//function3 
		friends.forEach(System.out::println);
	}
	
}
