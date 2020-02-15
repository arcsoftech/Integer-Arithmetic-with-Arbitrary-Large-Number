
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

    /**
     * Convert Number string to long array of type Num
     * 
     * @param s- Number string
     */

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

    /**
     * Convert given long integer to long arrya with provided base
     * 
     * @param x - Long Integer
     */
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

    /**
     * This method takes decision about which helper method to be called to perform
     * addition
     * 
     * @param a - Number 1
     * @param b - Number 2
     * @return Sum of the two numbers
     */
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

    /**
     * This method takes decision about which helper method to be called to perform
     * subtraction
     * 
     * @param a - Number 1
     * @param b - Number 2
     * @return difference of the two numbers
     */
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

    /**
     * This method perform product of two integer using karatsuba fast
     * multiplication
     * 
     * @param a - Number 1
     * @param b - Number 2
     * @return Product of the two numbers
     */
    public static Num product(Num a, Num b) {
        Num out = null;
        if (a.getList().size() == 0 || b.getList().size() == 0) {
            return new Num(0L);
        }
        out = karatsuba(a, b);

        out.isNegative = a.isNegative ^ b.isNegative;

        return out;
    }

    /**
     * Implementation of karatsuba fast multiplication
     * 
     * @param a - Number 1
     * @param b - Number 2
     * @return Product of the two numbers
     */
    private static Num karatsuba(Num a, Num b) {
        // Single number multiplication- Base Case
        if (b.getList().size() == 1) {
            return multiply(a, b.getList().get(0));
        }

        // Recursive method
        int m = b.getList().size() / 2;
        Num a1 = new Num();
        a1.getList().addAll(a.getList().subList(m, a.getList().size())); // First half of a
        Num a2 = new Num();
        a2.getList().addAll(a.getList().subList(0, m)); // Second half of b
        Num b1 = new Num();
        b1.getList().addAll(b.getList().subList(m, b.getList().size())); // First half of b
        Num b2 = new Num();
        b2.getList().addAll(b.getList().subList(0, m)); // Second half of b

        // Karatsubna terms
        Num e = Num.product(a1, b1);
        Num f = Num.product(a2, b2);
        Num ef = Num.product(Num.add(a1, a2), Num.add(b1, b2));
        Num efFinal = multiplyBase(Num.subtract(ef, Num.add(e, f)), m);
        Num temp = Num.add(multiplyBase(e, 2 * m), efFinal);

        return Num.add(temp, f);

    }

    /**
     * Perform multiplication of Integer to the base with power n
     * 
     * @param a - Integer of type Num
     * @param n - Power of base to be multipled by a
     * @return - Integer of type Num
     */
    private static Num multiplyBase(Num a, long n) {
        Num out = new Num();
        List<Long> outList = out.getList();
        outList.addAll(a.getList());
        for (long i = 0; i < n; i++) {
            outList.add(0, 0L);
        }
        return out;
    }

    /**
     * Single Integer multiplication
     * 
     * @param a - Integer 1
     * @param b - Integer 2
     * @return - Integer of type Num
     */
    private static Num multiply(Num a, Long b) {
        Num out = new Num();
        List<Long> outList = out.getList();
        Iterator<Long> num1Iterator = a.getList().iterator();
        Long carry = 0L;
        while (num1Iterator.hasNext()) {
            Long prod = (next(num1Iterator) * b) + carry;
            List<Long> prodList = toBase(prod, base);
            outList.add(prodList.get(0));
            carry = prodList.size() > 1 ? prodList.get(1) : 0L;
        }
        if (carry > 0) {
            outList.add(carry);
        }
        return out;
    }

    /**
     * Convert given number to the given base
     * 
     * @param number
     * @param base
     * @return - Integer of type Num
     */
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

    /**
     * Function to give power of Integer number n using divide and conquer
     * 
     * @param a - Integer Number
     * @param n - Power
     * @return - Integer of type Num
     */
    public static Num power(Num a, long n) {
        if (n < 0) {
            // nNum = subtract(new Num(0), nNum);
            // a = divide(new Num(1), a);
            return new Num(0); // we do not deal with fraction, only int division
        }
        return powerHelper(a, n);
    }

    /**
     * Method to generate power of number using divide and conquer
     * 
     * @param a
     * @param n
     * @return - Integer of type Num
     */
    private static Num powerHelper(Num a, long n) {
        // base
        if (n == 0)
            return new Num(1);

        // logic
        Num temp = powerHelper(a, n / 2);
        if (n % 2 == 0)
            return product(temp, temp);
        else
            return product(a, product(temp, temp));
    }

    /**
     * Method to divide two numbers
     * 
     * @param a - Number 1
     * @param b - Number 2
     * @return
     */
    public static Num divide(Num a, Num b) {
        return null;
    }

    /**
     * Method to return mod of two number
     * 
     * @param a
     * @param b
     * @return
     */
    public static Num mod(Num a, Num b) {
        return null;
    }

    /**
     * Method to find square root of two number using binary search
     * 
     * @param a - Number
     * @return - Integer of type Num
     */
    public static Num squareRoot(Num a) {
        // Edge Case
        if (a == null)
            return null;

        // Logic
        Num l = new Num(0), h = a.by2();

        while (l.compareTo(h) <= 1) {
            Num m = add(l, subtract(h, l).by2()); // calculate the mid point between l and h

            Num x = product(m, m); // keep a temporary variable equal to the square of m

            if (x.compareTo(a) == 0)
                return x; // found square root

            else if (x.compareTo(a) < 0) // x less than target
                l = add(m, new Num(1));

            else // x more than target
                h = subtract(m, new Num(1));
        }

        return null;
    }

    /**
     * Method to compare number wit its own instance
     * 
     * @return int(0,1,-1)
     */
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
        return null;
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

    /**
     * Utility methdo to convert string list to java Queue
     * 
     * @param expr
     * @return
     */
    private static Queue<String> convertStringListToQueue(String[] expr) {
        Queue<String> queue = new LinkedList<>(Arrays.asList(expr));
        return queue;
    }

    /**
     * Method to check precedence level of the given operator
     * 
     * @param op - Operator
     * @return Precedence level(0,1,2,-1)
     */
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

    /**
     * Compare precedence of two operator
     * 
     * @param opr1 - Operator 1
     * @param opr2 - Operator 2
     * @return Int(0,1,-1)
     */
    private static int comparePrecedence(char opr1, char opr2) {
        if (precedenceLevel(opr1) == precedenceLevel(opr2))
            return 0;
        else if (precedenceLevel(opr1) > precedenceLevel(opr2))
            return 1;
        else
            return -1;
    }

    /**
     * Evaluate an expression in postfix and return resulting number Each string is
     * one of: "*", "+", "-", "/", "%", "^", "0", or a number: [1-9][0-9]*. There is
     * no unary minus operator
     * 
     * @param expr - Expression as String List
     * @return - Integer of type Num
     */
    public static Num evaluatePostfix(String[] expr) {
        Queue<String> qt = convertStringListToQueue(expr);
        return evaluatePostfix(qt);
    }

    /**
     * Evaluate an expression in postfix and return resulting number Each string is
     * one of: "*", "+", "-", "/", "%", "^", "0", or a number: [1-9][0-9]*. There is
     * no unary minus operator
     * 
     * @param expr - Expression as String Queue
     * @return - Integer of type Num
     */
    public static Num evaluatePostfix(Queue<String> qt) {
        Stack<Num> stack = new Stack<>();
        String operatorRegex = "[+-/*//]"; // Regex to detect operator
        String operandRegex = "([0-9])*"; // Regex to detect operand i.e Integers
        Pattern operandPatten = Pattern.compile(operandRegex);
        Pattern operatorPattern = Pattern.compile(operatorRegex);
        Num out = null;
        while (!qt.isEmpty()) {
            String t = qt.remove();

            // Pushes all the operand to the stack
            if (operandPatten.matcher(t).matches()) {
                stack.push(new Num(t));
            }

            // Pop operand on detection of operator and perform the respective numbers .
            // All the operations are binary
            else if (operatorPattern.matcher(t).matches()) {

                Num b = stack.pop();
                Num a = stack.pop();
                switch (t) {

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
                if (stack.size() == 1 && qt.isEmpty()) {
                    break;
                } else {
                    stack.push(out);
                }

            }

        }
        if (out == null) {
            throw new NullPointerException("Invalid postfix expression");
        }
        return out;

    }

    /**
     * Method to convert infix operations to Postfix
     * 
     * @param expr - Expr as String list
     * @return - QUeue
     */
    public static Queue<String> infixToPostfix(String[] expr) {
        Queue<String> qt = convertStringListToQueue(expr);
        Queue<String> outputQueue = new LinkedList<>();
        Stack<String> stack = new Stack<>();
        String operatorRegex = "[+-/*//]"; // Regex to detect operator
        String operandRegex = "([0-9])*"; // Regex to detect operand i.e Integers
        Pattern operandPatten = Pattern.compile(operandRegex);
        Pattern operatorPattern = Pattern.compile(operatorRegex);
        while (!qt.isEmpty()) {
            String t = qt.remove();
            if (operandPatten.matcher(t).matches()) {
                outputQueue.add(t);
            } else if (operatorPattern.matcher(t).matches()) {
                int precedence = !stack.isEmpty() ? comparePrecedence(stack.peek().charAt(0), t.charAt(0)) : -1;
                if (stack.isEmpty() || precedence < 0 || t == "(") {
                    stack.push(t);
                } else {
                    outputQueue.add(stack.pop());
                    stack.push(t);
                }

            } else if (t == ")") {
                while (stack.peek() != "(") {
                    outputQueue.add(stack.pop());
                }
                stack.pop();
            } else {
                stack.push(t);
            }
        }
        while (!stack.isEmpty()) {
            outputQueue.add(stack.pop());
        }

        return outputQueue;
    }

    /**
     * Parse/evaluate an expression in infix and return resulting number. Input
     * expression is a string, e.g., "(3 + 4) * 5" Tokenize the string and then
     * input them to parser
     * 
     * @param expr - Expression in string
     * @return - Integer of type Num
     */
    public static Num evaluateExp(String expr) {
        expr.replaceAll("\\s+", "");
        Queue<String> queue = new LinkedList<>(Arrays.asList(expr));
        return evalE(queue);
    }

    /**
     * Parse/evaluate an expression in infix and return resulting number.
     * 
     * @param expr - Expression as string list
     * @return -Integer of type Num
     */
    public static Num evaluateInfix(String[] expr) {
        Queue<String> queue = convertStringListToQueue(expr);
        return evalE(queue);
    }

    /**
     * Method that gives the list of Integer type Num
     * 
     * @return - List
     */
    public List<Long> getList() {
        return list;
    }

    /**
     * Helper method to subtract smaller number from bigger number and the adjusting
     * the sign
     * 
     * @param a - Ineteger 1
     * @param b - Ineteger 2
     * @return - Integer of type Num
     */
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

    /**
     * Helper method to two add two number by providing the list of big integer with
     * given base
     * 
     * @param a - Integer 1
     * @param b - Integer 2
     * @return - Integer of type Num
     */
    private static Num addHelper(Num a, Num b) {
        Num out = new Num();
        add(a.getList(), b.getList(), out.getList(), Num.base);
        return out;
    }

    /**
     * Return tyhe next element in iterator
     * 
     * @param iterator - List Iterator
     * @return -
     */
    private static Long next(Iterator<Long> iterator) {
        return iterator.hasNext() ? iterator.next() : 0L;
    }

    /**
     * Method to add two Number list and give the ouput based on given base.
     * 
     * @param num1List - Integer 1 List
     * @param num2List - Integer 2 List
     * @param out      - Output List
     * @param base     - base
     */
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

    /**
     * Method to subtract two Number list and give the ouput based on given base.
     * 
     * @param num1List - Integer 1 List
     * @param num2List - Integer 2 List
     * @param out      - Output List
     * @param base     - base
     */
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

    /**
     * Method to add to compare two numbers without sign
     * 
     * @param num1List - Integer 1 List
     * @param num2List - Integer 2 List
     * @return int(0,1,-1)
     */
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

    /**
     * Grammer Rule E
     * 
     * @param qt
     * @return - Integer of type Num
     */
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

    /**
     * Grammer Rule T
     * 
     * @param qt
     * @return - Integer of type Num
     */
    private static Num evalT(Queue<String> qt) {
        Num val1 = evalF(qt);
        if (!qt.isEmpty()) {
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

    /**
     * Grammer Rule F
     * 
     * @param qt
     * @return - Integer of type Num
     */
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
        Num x = new Num("8099999999999999999999999999");
        Num y = new Num("809999999999999999999999999999999999");
        Num z = Num.add(x, y);
        Num d = Num.subtract(x, y);
        Num e = Num.product(x, y);
        Num f = Num.evaluateInfix(
                new String[] { "(", "(", "98765432109876543210987654321", "+", "5432109876543210987654321", "*",
                        "345678901234567890123456789012", ")", "*", "246801357924680135792468013579", "+",
                        "12345678910111213141516171819202122", "*", "(", "191817161514131211109876543210", "-", "13579",
                        "*", "24680", ")", ")", "*", "7896543", "+", "157984320" });
        Num g = Num.evaluatePostfix(new String[] { "98765432109876543210987654321", "5432109876543210987654321",
                "345678901234567890123456789012", "*", "+", "246801357924680135792468013579", "*",
                "12345678910111213141516171819202122", "191817161514131211109876543210", "13579", "24680", "*", "-",
                "*", "+", "7896543", "*", "157984320", "+" });
        System.out.println(d);
        System.out.println(z);
        Num a = Num.power(x, 8);
        System.out.println(a);
        if (z != null)
            z.printList();
    }

}
