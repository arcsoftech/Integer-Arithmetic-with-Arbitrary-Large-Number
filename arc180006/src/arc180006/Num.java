
// Starter code for lp1.
// Version 1.0 (Monday, Jan 27).

// Change following line to your NetId
package arc180006;

public class Num  implements Comparable<Num> {

    static long defaultBase = 10;  // Change as needed
    long base = defaultBase;  // Change as needed
    long[] arr;  // array to store arbitrarily large integers
    boolean isNegative;  // boolean flag to represent negative numbers
    int len;  // actual number of elements of array that are used;  number is stored in arr[0..len-1]

    public Num(String s) {
    }

    public Num(long x) {
    }

    public static Num add(Num a, Num b) {
	return null;
    }

    public static Num subtract(Num a, Num b) {
	return null;
    }

    public static Num product(Num a, Num b) {
	return null;
    }

    // Use divide and conquer
    public static Num power(Num a, long n) {
	return null;
    }

    // Use binary search to calculate a/b
    public static Num divide(Num a, Num b) {
	return null;
    }

    // return a%b
    public static Num mod(Num a, Num b) {
	return null;
    }

    // Use binary search
    public static Num squareRoot(Num a) {
	return null;
    }


    // Utility functions
    // compare "this" to "other": return +1 if this is greater, 0 if equal, -1 otherwise
    public int compareTo(Num other) {
	return 0;
    }
    
    // Output using the format "base: elements of list ..."
    // For example, if base=100, and the number stored corresponds to 10965,
    // then the output is "100: 65 9 1"
    public void printList() {
    }
    
    // Return number to a string in base 10
    public String toString() {
	return null;
    }

    public long base() { return base; }

    // Return number equal to "this" number, in base=newBase
    public Num convertBase(int newBase) {
	return null;
    }

    // Divide by 2, for using in binary search
    public Num by2() {
	return null;
    }

    // Evaluate an expression in postfix and return resulting number
    // Each string is one of: "*", "+", "-", "/", "%", "^", "0", or
    // a number: [1-9][0-9]*.  There is no unary minus operator.
    public static Num evaluatePostfix(String[] expr) {
	return null;
    }

    // Parse/evaluate an expression in infix and return resulting number
    // Input expression is a string, e.g., "(3 + 4) * 5"
    // Tokenize the string and then input them to parser
	// Implementing this method correctly earns you an excellence credit
    public static Num evaluateExp(String expr) {
	return null;
    }


    public static void main(String[] args) {
	Num x = new Num(999);
	Num y = new Num("8");
	Num z = Num.add(x, y);
	System.out.println(z);
	Num a = Num.power(x, 8);
	System.out.println(a);
	if(z != null) z.printList();
    }
}
