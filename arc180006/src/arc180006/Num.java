
// Starter code for lp1.
// Version 1.0 (Monday, Jan 27).

// Change following line to your NetId
package arc180006;

import java.util.*;
import java.util.regex.Pattern;

public class Num implements Comparable<Num> {

    static long defaultBase = 10; // Change as needed
    static long base = 100000L; // Change as needed
    long[] arr; // array to store arbitrarily large integers
    boolean isNegative; // boolean flag to represent negative numbers
    static boolean karatsuba = false;
    int len; // actual number of elements of array that are used; number is stored in
             // arr[0..len-1]

    private List<Long> list;

    Num() {
        list = new LinkedList<>();
    }

    public Num(String s) {
        if (s.length() == 0) {
            throw new NullPointerException("Invalid number");
        }
        list = new ArrayList<>();
        Num Base = new Num(10);
        char[] arr = new StringBuilder(s).toString().toCharArray();
        Num num = this;
        for (char current : arr) {
            if (current == '-') {
                this.isNegative = true;
            } else {
                Num first = Num.product(num, Base);
                Num second = new Num(current - '0');
                num = Num.add(first, second);
            }
        }
        this.list = num.list;
    }

    public Num(long x) {
        list = new LinkedList<>();
        long quotient = base + 1, remainder = 0;
        if (x < 0) {
            this.isNegative = true;
            x = Math.abs(x);
        }
        if (x < base) {
            list.add(x);
        } else {
            while (x >= base) {
                quotient = x / base;
                remainder = x % base;
                list.add(remainder);
                x = quotient;
            }
            if (quotient != 0) {
                list.add(quotient);
            }
        }
    }

//    public Num(long x) {
//        isNegative = false;
//
//		// When x is Negative, making it positive with flag isNegative as true
//		if (x < 0) {
//			this.isNegative = true;
//			x = -1 * x;
//		}
//		double lengthArr = 0;
//
//		// When x = 0, lengthArr = -Infinity
//		if (x == 0) {
//			lengthArr = 1;
//		}
//		// No of digits in this.Num = log_base (x) + 1 = log(x)/log(base) + 1
//		else {
//			lengthArr = ((Math.log((double) x)) / (Math.log((double) base))) + 1;
//		}
//
//		this.len = (int) lengthArr; // Real to integer (floor function)
//		arr = new long[this.len];
//
//		long n = x;
//		// Iteratively pass appropriate digit to this.arr (array of long)
//		for (int i = 0; i < len; i++) {
//			arr[i] = n % base();
//			n /= base();
//		}
//    }

    public static Num add(Num a, Num b) {
        Num out = null;
        if (a.isNegative && !b.isNegative) {
            out = differenceHelper(b, a);
        } else if (!a.isNegative && b.isNegative) {
            out = differenceHelper(a, b);
        } else if (a.isNegative && b.isNegative) {
            out = addHelper(b, a);
            out.isNegative = true;
        } else {
            out = addHelper(a, b);
        }
        return out;
    }

    public static Num subtract(Num a, Num b) {
        Num out = null;
        if (a.isNegative && !b.isNegative) {
            out = addHelper(a, b);
            out.isNegative = true;
        } else if (!a.isNegative && b.isNegative) {
            out = addHelper(a, b);
        } else if (a.isNegative && b.isNegative) {
            out = differenceHelper(b, a);
        } else {
            out = differenceHelper(a, b);
        }
        return out;
    }

    public static Num product(Num a, Num b) {
        Num out = null;
        if (a.getList().size() == 0 || b.getList().size() == 0) {
            return new Num(0L);
        }
        // karatsuba implementation
        out = karatsuba(a, b);

        out.isNegative = a.isNegative ^ b.isNegative;

        return out;
    }

    private static Num karatsuba(Num a, Num b) {
        if (b.getList().size() == 1) {
            return multiply(a, b.getList().get(0));
        }
        int m = b.getList().size() / 2;
        Num a1 = new Num();
        a1.getList().addAll(a.getList().subList(m, a.getList().size()));
        Num a2 = new Num();
        a2.getList().addAll(a.getList().subList(0, m));
        Num b1 = new Num();
        b1.getList().addAll(b.getList().subList(m, b.getList().size()));
        Num b2 = new Num();
        b2.getList().addAll(b.getList().subList(0, m));

        Num e = Num.product(a1, b1);
        Num f = Num.product(a2, b2);
        Num ef = Num.product(Num.add(a1, a2), Num.add(b1, b2));
        Num efFinal = multiplyBase(Num.subtract(ef, Num.add(e, f)), m);
        Num temp = Num.add(multiplyBase(e, 2 * m), efFinal);
        return Num.add(temp, f);

    }

    private static Num multiplyBase(Num a, long n) {
        Num out = new Num();
        List<Long> outList = out.getList();
        outList.addAll(a.getList());
        for (long i = 0; i < n; i++) {
            outList.add(0, 0L);
        }
        return out;
    }

    private static Num multiply(Num a, Long b) {
        Num out = new Num();
        List<Long> outList = out.getList();
        Iterator<Long> num1Iteration = a.getList().iterator();
        Long carry = 0L;
        while (num1Iteration.hasNext()) {
            Long prod = (next(num1Iteration) * b) + carry;
            List<Long> prodList = toBase(prod, base);
            outList.add(prodList.get(0));
            carry = prodList.size() > 1 ? prodList.get(1) : 0L;
        }
        if (carry > 0) {
            outList.add(carry);
        }

        return out;
    }

    private static List<Long> toBase(Long number, long base) {

        List<Long> list = new LinkedList<>();

        if (number < base) {
            list.add(number);
            if (number == 0L) {
                list.add(0L);
            }
            return list;
        }
        Long quotient = number;
        Long remainder = 0L;
        while (quotient >= base) {
            quotient = number / base;
            remainder = number % base;
            list.add(remainder);
            number = quotient;
        }
        list.add(quotient);
        return list;
    }

    // Use divide and conquer
    public static Num power(Num a, long n) {
		if(n < 0)
		{
//			nNum = subtract(new Num(0), nNum);
//			a = divide(new Num(1), a);
			return new Num(0); // we do not deal with fraction, only int division
		}
		return powerHelper(a, n);
    }

    //use divide and conquer
    private static Num powerHelper(Num a, long n)
    {
        //base
        if(n == 0) return new Num(1);

        //logic
        Num temp = powerHelper(a, n / 2);
        if(n % 2 == 0)
            return product(temp,temp);
        else
            return product(a, product(temp,temp));
    }

    // Ucalculate quotient of a divides b (integer division)
//    public static Num divide(Num a, Num b) {
//    	// corner cases
//    	if (b.compareTo(new Num(0)) == 0) { // b = 0
//    		throw new Error("cannot divide to 0");
//    	}
//    	if (b.compareTo(new Num(1)) == 0) { // b = 1
//    		return a;
//    	}
//    	if (b.compareTo(a) == 0) { // a = b
//    		return new Num(1);
//    	}
//    	if (b.compareTo(a) > 0) { // b > a
//    		return new Num(0);
//    	}
//    	StringBuilder resultString = new StringBuilder();
//    	
//    	do {
//        	Num estimate = getSmallestLargerNumber(a, b);
//        	
//        	if (a.compareTo(product(b, estimate)) > 0) { // a > b * estimate
//        		estimate = subtract(estimate, new Num(1));
//        	}
//        	a = subtract(a, product(b, estimate));
//        	resultString.append(estimate.toString());
//    	} while (b.compareTo(a) <= 0); // do until b > a
//    	
//        return new Num(resultString.toString());
//    }
//    
//    public static Num getSmallestLargerNumber(Num a, Num b) {
//    	// precondition: Num a > Num b
//    	// postcondition: a smallest Num c part of a and larger than Num b
//    	Num result = new Num();
//    	int fromIndex = b.getList().size() - a.getList().size();
//    	result.getList().addAll(a.getList().subList(fromIndex, b.getList().size()-1));
//    	
//    	if (result.compareTo(b) < 0) {
//    		result.getList().add(0, a.getList().get(fromIndex-1));
//    	}
//    	return result;
//    }
    
    public static Num divide(Num a, Num b) {
    	boolean isNegative = a.isNegative ^ b.isNegative;
    	a.isNegative = false;
    	b.isNegative = false;
    	//take only absolute
    	// corner cases
    	if (b.compareTo(new Num(0)) == 0) { // b = 0
    		throw new Error("cannot divide to 0");
    	}
    	if (a.compareTo(new Num(0)) == 0) { // a = 0
    		return new Num(0);
    	}
    	if (b.compareTo(new Num(1)) == 0) { // b = 1
    		return a;
    	}
    	if (b.compareTo(a) == 0) { // a = b
    		return new Num(1);
    	}
    	if (b.compareTo(a) > 0) { // b > a
    		return new Num(0);
    	}
    	Num count = new Num(0);
    	Num one = new Num(1);
    	do {
        	a = subtract (a, b);
        	count = add (count , one);
    	} while (b.compareTo(a) <= 0); // do until a < b
    	count.isNegative = isNegative;
        return count;
    }

    // return a%b
    public static Num mod(Num a, Num b) {
    	if (b.compareTo(new Num(0))==0) {
    		return null;
    	}
    	int compare = a.compareTo(b);
    	return compare == 0 ? new Num(0) 
    			: compare < 0 ? a 
    			: subtract (a, product(b, divide(a, b)));
    }

    // Use binary search
    public static Num squareRoot(Num a) {
        //Edge Case
        if(a == null) return null;

        //Logic
        Num l = new Num(0), h = a.by2();

        while(l.compareTo(h) <= 1)
        {
            Num m = add(l, subtract(h, l).by2()); // calculate the mid point between l and h

            Num x =  product(m, m);  // keep a temporary variable equal to the square of m

            if(x.compareTo(a) == 0) return x; //found square root

            else if(x.compareTo(a) < 0) // x less than target
                l = add(m, new Num(1));

            else  //x more than target
                h = subtract(m, new Num(1));
        }
        
        return null;
    }

    // Utility functions
    // compare "this" to "other": return +1 if this is greater, 0 if equal, -1
    // otherwise
    public int compareTo(Num num2) {
        if (this.isNegative && !num2.isNegative) {
            return -1;
        } else if (!this.isNegative && num2.isNegative) {
            return 1;
        } else if (this.isNegative && num2.isNegative) {
            int out = compareList(this.getList(), num2.getList());
            if (out != 0) {
                return out * -1;
            }
            return out;
        } else {
            return compareList(this.getList(), num2.getList());
        }
    }

    // Output using the format "base: elements of list ..."
    // For example, if base=100, and the number stored corresponds to 10965,
    // then the output is "100: 65 9 1"
    public void printList() {
    }

    // Return number to a string in base 10
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Long item : this.list) {
        	stringBuilder.insert(0, item);
        }
        return stringBuilder.toString().strip();
    }

    public long base() {
        return base;
    }

    // Return number equal to "this" number, in base=newBase
    public Num convertBase(int newBase) {
        return null;
    }

    // Divide by 2, for using in binary search
    public Num by2() {
        return null;
    }

    private static Queue<String> convertStringListToQueue(String[] expr){
        Queue<String> queue = new LinkedList<>(Arrays.asList(expr));
        return queue;
    }

    private static int precedenceLevel(char op) {
        switch (op) {
            case '+':
            case '-':
                return 0;
            case '*':
            case '/':
                return 1;
            case '^':
                return 2;
            case '(':
                return -1;
            default:
                throw new IllegalArgumentException("Operator unknown: " + op);
        }
    }
    private static int comparePrecedence(char opr1,char opr2){
        if(precedenceLevel(opr1)==precedenceLevel(opr2))
            return 0;
        else if(precedenceLevel(opr1)>precedenceLevel(opr2))
            return 1;
        else
            return -1;
    }
    // Evaluate an expression in postfix and return resulting number
    // Each string is one of: "*", "+", "-", "/", "%", "^", "0", or
    // a number: [1-9][0-9]*. There is no unary minus operator.
    public static Num evaluatePostfix(String[] expr) {
        Queue<String> qt = convertStringListToQueue(expr);
        return evaluatePostfix(qt);
    }
    public static Num evaluatePostfix(Queue<String> qt){
        Stack<Num> stack= new Stack<>();
        String operatorRegex="[+-/*//]";
        String operandRegex="([0-9])*";
        Pattern operandPatten = Pattern.compile(operandRegex);
        Pattern operatorPattern = Pattern.compile(operatorRegex);
        Num out = null;
        while(!qt.isEmpty())
        {
            String t = qt.remove();
            if (operandPatten.matcher(t).matches())
            {
                stack.push(new Num(t));
            } 
            else if(operatorPattern.matcher(t).matches())
            {
                
                Num b = stack.pop();
                Num a = stack.pop();
                switch(t)
                {
                   
                    case "+":
                        out = Num.add(a, b);
                        break;
                    case "-":
                        out = Num.subtract(a, b);
                        break;
                    case "*":
                        out = Num.product(a, b);
                        break;
                    case "/":
                        out = Num.divide(a, b);
                        break;
                    case "^":
                        // out = Num.power(a, b);
                        break;
                }
                if(stack.size()==1 && qt.isEmpty()){
                    break;
                }
                else{
                    stack.push(out);
                }
                
            }

        }
        if(out == null)
        {
            throw new NullPointerException("Invalid postfix expression");
        }
        return out;


    }
    public static Queue<String> infixToPostfix(String[] expr) {
        Queue<String> qt = convertStringListToQueue(expr);
        Queue<String> outputQueue = new LinkedList<>();
        Stack<String> stack= new Stack<>();
        String operatorRegex="[+-/*//^]";
        String operandRegex="([0-9])*";
        Pattern operandPatten = Pattern.compile(operandRegex);
        Pattern operatorPattern = Pattern.compile(operatorRegex);
        while(!qt.isEmpty())
        { 
            String t = qt.remove();
            if (operandPatten.matcher(t).matches())
            {
                outputQueue.add(t);
            } 
            else if(operatorPattern.matcher(t).matches())
            {
                int precedence =  !stack.isEmpty()?comparePrecedence(stack.peek().charAt(0),t.charAt(0)):-1;
                if(stack.isEmpty() || precedence<0  || t=="("){
                    stack.push(t);
                }
                else{
                    outputQueue.add(stack.pop());
                    stack.push(t);
                }
                
            }
            else if(t==")") 
            {
                while(stack.peek()!="(")
                {
                    outputQueue.add(stack.pop());
                }
                stack.pop();
            }
            else{
                stack.push(t);
            }
        }
        while(!stack.isEmpty())
        {
            outputQueue.add(stack.pop());
        }

        return outputQueue;
    }

    // Parse/evaluate an expression in infix and return resulting number
    // Input expression is a string, e.g., "(3 + 4) * 5"
    // Tokenize the string and then input them to parser
    // Implementing this method correctly earns you an excellence credit
    public static Num evaluateExp(String expr) {
        expr.replaceAll("\\s+","");
        Queue<String> queue = new LinkedList<>(Arrays.asList(expr));
        return evalE(queue);
    }

    public static Num evaluateInfix(String[] expr) {
        Queue<String> queue = convertStringListToQueue(expr);
        return evalE(queue);
    }

    public List<Long> getList() {
        return list;
    }

    private static Num differenceHelper(Num a, Num b) {
        Num out = new Num();
        int compareOut = compareList(a.getList(), b.getList());
        if (compareOut > 0) {
            subtract(a.getList(), b.getList(), out.getList());
        } else if (compareOut < 0) {
            subtract(b.getList(), a.getList(), out.getList());
            out.isNegative = true;
        } else {
            out = new Num(0L);
        }
        return out;
    }

    private static Num addHelper(Num a, Num b) {
        Num out = new Num();
        add(a.getList(), b.getList(), out.getList(), Num.base);
        return out;
    }

    private static Long next(Iterator<Long> iterator) {
        return iterator.hasNext() ? iterator.next() : 0L;
    }

    private static void add(List<Long> num1List, List<Long> num2List, List<Long> out, long base) {

        Iterator<Long> num1Iterator = num1List.iterator();
        Iterator<Long> num2Iterator = num2List.iterator();
        Long carry = 0L;
        while (num1Iterator.hasNext() || num2Iterator.hasNext() || carry > 0) {
            Long sum = next(num1Iterator) + next(num2Iterator) + carry;
            if (sum >= base) {
                carry = 1L;
                sum -= base;
            } else {
                carry = 0L;
            }
            out.add(sum);
        }

    }

    private static void subtract(List<Long> num1List, List<Long> num2List, List<Long> outList) {
        Iterator<Long> num1Iterator = num1List.iterator();
        Iterator<Long> num2Iterator = num2List.iterator();
        Long borrow = 0L;
        while (num1Iterator.hasNext() || num2Iterator.hasNext() || borrow > 0) {
            Long a = next(num1Iterator);
            Long b = next(num2Iterator);
            Long out;
            if (a >= b) {
                out = a - (b + borrow);
                borrow = 0L;
            } else {
                out = (a + base) - b;
                borrow = 1L;
            }
            outList.add(out);

        }

    }

    private static int compareList(List<Long> num1List, List<Long> num2List) {
        int out = 0;
        Iterator<Long> iterator1 = num1List.iterator();
        Iterator<Long> iterator2 = num2List.iterator();
        while (iterator1.hasNext() || iterator2.hasNext()) {
            Long a = iterator1.hasNext() ? iterator1.next() : 0L;
            Long b = iterator2.hasNext() ? iterator2.next() : 0L;
            if (a > b) {
                out = 1;
            } else if (a < b) {
                out = -1;
            }
        }
        return out;
    }

    private static Num evalE(Queue<String> qt) {
        Num val1 = evalT(qt);
            while (!qt.isEmpty() && ((qt.peek().equals("+")) || (qt.peek().equals("-")))) {
                String oper = qt.remove();
                Num val2 = evalT(qt);
                if (oper.equals("+"))
                    val1 = Num.add(val1, val2);
                else
                    val1 = Num.subtract(val1, val2);
            }
        
        return val1;
    }

    private static Num evalT(Queue<String> qt) {
        Num val1 = evalF(qt);
        if (!qt.isEmpty())
        {
            while ((qt.peek().equals("*")) || (qt.peek().equals("/"))) {
                String oper = qt.remove();
                Num val2 = evalF(qt);
                if (oper.equals("*"))
                    val1 = Num.product(val1, val2);
                else
                    val1 = Num.divide(val1, val2);
            }
        }
     
        return val1;
    }

    private static Num evalF(Queue<String> qt) {
        Num val;
        if ((qt.peek().equals("("))) {
            qt.remove();
            val = evalE(qt);
            qt.remove(); // ")""
        } else {
            String num = qt.remove();
            val = new Num(num);
        }
        return val;
    }

    /**
     * Main Function- Driver Code
     * 
     * @param args
     */
    public static void main(String[] args) {
          Num x = new Num("80");
          Num y = new Num("3");
          System.out.println("=" + divide(x, y).toString());
          System.out.println("mod" + mod(x, y).toString());
//        Num z = Num.add(x, y);
//        Num d = Num.subtract(x, y);
//        Num e = Num.product(x, y);
//        Num f = Num.evaluateInfix(new String[] { "(", "(", "98765432109876543210987654321", "+", "5432109876543210987654321",
//                        "*", "345678901234567890123456789012", ")", "*", "246801357924680135792468013579", "+",
//                        "12345678910111213141516171819202122", "*", "(", "191817161514131211109876543210", "-", "13579",
//                        "*", "24680", ")", ")", "*", "7896543", "+", "157984320" });
//        Num g = Num.evaluatePostfix(new String[] { "98765432109876543210987654321",  "5432109876543210987654321", "345678901234567890123456789012", "*", "+", "246801357924680135792468013579", "*", "12345678910111213141516171819202122", "191817161514131211109876543210", "13579", "24680", "*", "-", "*", "+", "7896543", "*", "157984320", "+" });
//        System.out.println(d);
//        System.out.println(z);
//        Num a = Num.power(x, 8);
//        System.out.println(a);
//        if (z != null)
//            z.printList();
    }

}
