package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import arc180006.Num;

class NumTest {

//	@BeforeAll
//	static void setUpBeforeClass() throws Exception {
//	}
//
//	@Test
//	void testDivide() { // 0 / a
//		Num a = new Num("0");
//        Num b = new Num("4000000000000000000000000000000");
//        Assertions.assertTrue(Num.divide(a, b).compareTo(new Num("0")) == 0);
//	}
//	
//	@Test
//	void testDivide2() { // a<b, a/b = 0
//		Num a = new Num("222222222222222222222222222222");
//        Num b = new Num("400000099999999999999999999990");
//        Assertions.assertTrue(Num.divide(a, b).compareTo(new Num("0")) == 0);
//	}
//	
//	@Test
//	void testDivide3() { // a/1
//		Num a = new Num("222222222222222222222222222222");
//        Num b = new Num("1");
//        Assertions.assertTrue(Num.divide(a, b).compareTo(a) == 0);
//	}
//	
//	@Test
//	void testDivide4() { // 0/0
//		Num a = new Num("0");
//        Num b = new Num("0");
//        Assertions.assertThrows( Error.class, () -> Num.divide(a, b));
//	}
//	
//	@Test
//	void testDivide5() { // a/0
//		Num a = new Num("999999999999999999999999999999");
//        Num b = new Num("0");
//        Assertions.assertThrows( Error.class, () -> Num.divide(a, b));
//	}
//	
//	@Test
//	void testDivide6() {
//		Num a = new Num ("99");
//		Num b = new Num ("3");
//		Assertions.assertTrue(Num.divide(a, b).compareTo(new Num("33")) == 0);
//	}
//	
//	@Test
//	void testDivide7() {
//		Num a = new Num ("99999999999999999999");
//		Num b = new Num ("7");
//		Assertions.assertTrue(Num.divide(a, b).compareTo(new Num("14285714285714285714")) == 0);
//	}
//	
//	@Test
//	void testDivide8() {
//		Num a = new Num ("999999999999999999991111111111111111111122222222222222222222");
//		Num b = new Num ("7");
//		Assertions.assertTrue(Num.divide(a, b).compareTo(new Num("142857142857142857141587301587301587301588888888888888888888")) == 0);
//	}
//
//	
//	@Test
//	void testDivide9() {
//		Num a = new Num ("999999999999999999991111111111111111111122222222222222222222");
//		Num b = new Num ("10000");
//		Assertions.assertTrue(Num.divide(a, b).compareTo(new Num("99999999999999999999111111111111111111112222222222222222")) == 0);
//	}
//	
//	@Test
//	void testDivide10() {
//		Num a = new Num ("999999999999999999991111111111111111111122222222222222222222");
//		Num b = new Num ("10001");
//		Assertions.assertTrue(Num.divide(a, b).compareTo(new Num("99990000999900009998111299981112999811131109111311091113")) == 0);
//	}
//	
//	@Test
//	void testDivide11() {
//		Num a = new Num ("1111111111111111");
//		Num b = new Num ("1234567");
//		Assertions.assertTrue(Num.divide(a, b).compareTo(new Num("900000657")) == 0);
//	}
//	
//	@Test
//	void testDivide12t1() {
//		Num a = new Num ("999999999999999999991111111111111111111122222222222222222222");
//		Num b = new Num ("1234567");
//		
//		Assertions.assertTrue(Num.divide(a, b).compareTo(
//				new Num("810,000,591,300,431,649,307,903,994,769,916,182,038,821,888,339,978,488".replace("," , ""))) == 0);
//	}
	
	@Test
	void testDivide13() {
		Num a = new Num ("1111111111111111111111111111111");
		Num b = new Num ("1234567");
		Assertions.assertTrue(Num.divide(a, b).compareTo(
				new Num("900,000,657,000,479,610,350,115".replace("," , ""))) == 0);
	}
	

}
