
// Starter code for lp1.
// Version 1.0 (Monday, Jan 27).

// Change following line to your NetId
package arc180006;

import java.util.*;

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
        out = karatsuba(a,b);

        
        out.isNegative = a.isNegative ^ b.isNegative;

        return out;
    }
    private static Num karatsuba(Num a, Num b){
        if(b.getList().size() == 1){
            return multiply(a,b.getList().get(0));
        }
        int m = b.getList().size()/2;
        Num a1 = new Num();
        a1.getList().addAll(a.getList().subList(m, a.getList().size()));
        Num a2 = new Num();
        a2.getList().addAll(a.getList().subList(0,m));
        Num b1 = new Num();
        b1.getList().addAll(b.getList().subList(m, b.getList().size()));
        Num b2 = new Num();
        b2.getList().addAll(b.getList().subList(0,m));

        Num e = Num.product(a1,b1);
        Num f = Num.product(a2, b2);
        Num ef = Num.product(Num.add(a1, a2),Num.add(b1,b2));
        Num efFinal= multiplyBase(Num.subtract(ef, Num.add(e,f)),m);
        Num temp = Num.add(multiplyBase(e,2*m),efFinal);
        return Num.add(temp,f);

    }
    private static Num multiplyBase(Num a, long n){
        Num out = new Num();
        List<Long> outList = out.getList();
        outList.addAll(a.getList());
        for(long i = 0; i < n; i++){
            outList.add(0,0L);
        }
        return out;
    }

    private static Num multiply(Num a, Long b){
        Num out = new Num();
        List<Long> outList = out.getList();
        Iterator<Long> num1Iteration = a.getList().iterator();
        Long carry = 0L;
        while(num1Iteration.hasNext()){
            Long prod = (next(num1Iteration) * b) + carry;
            List<Long> prodList = toBase(prod, base);
            outList.add(prodList.get(0));
            carry = prodList.size() > 1 ? prodList.get(1) : 0L;
        }
        if(carry > 0){
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
    // compare "this" to "other": return +1 if this is greater, 0 if equal, -1
    // otherwise
    public int compareTo(Num num2) {
        if(this.isNegative && !num2.isNegative)
        {
            return -1;
        }
        else if(!this.isNegative && num2.isNegative)
        {
            return 1;
        }
        else if(this.isNegative && num2.isNegative)
        {
            int out = compareList(this.getList(), num2.getList());
            if(out!=0)
            {
                return out*-1;
            }
            return out;
        }
        else{
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

    // Evaluate an expression in postfix and return resulting number
    // Each string is one of: "*", "+", "-", "/", "%", "^", "0", or
    // a number: [1-9][0-9]*. There is no unary minus operator.
    public static Num evaluatePostfix(String[] expr) {
        return null;
    }

    public static Num evaluateInfix(String[] expr) {
        return null;
    }

    // Parse/evaluate an expression in infix and return resulting number
    // Input expression is a string, e.g., "(3 + 4) * 5"
    // Tokenize the string and then input them to parser
    // Implementing this method correctly earns you an excellence credit
    public static Num evaluateExp(String expr) {
        return null;
    }

    public List<Long> getList() {
        return list;
    }

    private static Num differenceHelper(Num a, Num b) {
        Num out = new Num();
        int compareOut = compareList(a.getList(), b.getList());
        if (compareOut > 0) {
            subtract(a.getList(), b.getList(),out.getList());
        } else if (compareOut < 0) {
            subtract(b.getList(), a.getList(),out.getList());
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

    private static Long next(Iterator<Long> iterator){
        return iterator.hasNext()? iterator.next() : 0L;
    }
    
    private static void add(List<Long> num1List, List<Long> num2List, List<Long> out, long base) {
        
        Iterator<Long> num1Iterator = num1List.iterator();
    	Iterator<Long> num2Iterator = num2List.iterator();
    	Long carry = 0L;
    	while(num1Iterator.hasNext() || num2Iterator.hasNext() || carry > 0){
            Long sum = next(num1Iterator) + next(num2Iterator) + carry;
            if (sum >= base) {
                carry = 1L;
                sum -= base;
            }
            else {
                carry = 0L;
            }
            out.add(sum);
    	}

    }

    private static void subtract(List<Long> num1List, List<Long> num2List, List<Long> outList) {
        Iterator<Long> num1Iterator = num1List.iterator();
        Iterator<Long> num2Iterator = num2List.iterator();
        Long borrow = 0L;
        while(num1Iterator.hasNext() || num2Iterator.hasNext() || borrow > 0){
            Long a = next(num1Iterator);
            Long b = next(num2Iterator);
            Long out;
            if(a>=b)
            {
                out= a-(b+borrow);
                borrow = 0L;
            }
            else
            {
                out = (a+base) - b; 
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

    /**
     * Main Function- Driver Code
     * 
     * @param args
     */
    public static void main(String[] args) {
        Num x = new Num("999999");
        Num y = new Num(80);
        Num z = Num.add(x, y);
        Num d = Num.subtract(x, y);
        Num e = Num.product(x, y);
        System.out.println(d);
        System.out.println(z);
        Num a = Num.power(x, 8);
        System.out.println(a);
        if (z != null)
            z.printList();
    }

}
