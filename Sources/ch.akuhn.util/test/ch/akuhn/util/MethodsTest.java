package ch.akuhn.util;

import static ch.akuhn.util.Methods.newPredicate;
import static org.junit.Assert.*;


import org.junit.Test;

import ch.akuhn.util.blocks.Predicate;

public class MethodsTest {

	public static boolean odd(int n) {
		return n % 2 != 0;
	}

	private int m = 1;
	
	public boolean even() {
		return m  % 2 == 0;
	}
	
	@Test
	public void staticMethod() {
		Predicate<Integer> pred = newPredicate("#odd");
		assertEquals( false, pred.apply(0) );
		assertEquals( true, pred.apply(1) );
		assertEquals( false, pred.apply(2) );
	}

	@Test
	public void virtualMethod() {
		Predicate<String> pred = newPredicate("String#isEmpty");
		assertEquals( false, pred.apply("aaa") );
		assertEquals( true, pred.apply("") );
	}
	
}