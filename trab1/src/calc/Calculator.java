package calc;
import java.math.BigInteger;
import java.util.Stack;

public class Calculator {
    private Stack<Double> stack;

    public Calculator() {
        stack = new Stack<Double>();
    }

    // Basic interaction

    // this may seem obvious, and it actually is
    // to add put a number on the calculator use push
    // to remove a number from the calculator use pop
    // to view the whole stack, guess what? getStack

    public void push(double number) {
        stack.push(new Double(number));
    }

    public double pop() {
        return (double) stack.pop();
    }

    public Stack<Double> getStack() {
        return stack;
    }

    public String toString() {
        String out = new String();
        for(Double x : stack) {
            out += x.toString() + "\n";
        }
        return out;
    }

    // Operations

    // the following operations will remove the required
    // amount of numbers from the stack and push the result
    // of the opration to the stack
    //
    // if there are not enough numbers the exception
    // from the stack is forwarded

    public void sum() {
        Double y = stack.pop();
        Double x = stack.pop();
        stack.push(x + y);
    }

    public void subtract() {
        Double y = stack.pop();
        Double x = stack.pop();
        stack.push(x - y);
    }

    public void multiply() {
        Double y = stack.pop();
        Double x = stack.pop();
        stack.push(x * y);
    }

    // this operation hast the restriction of now allowing division
    // by zero, as such it will throw IllegalArgumentException, even
    // though it's not an argument, but from the point of view of the
    // calculator it could be considered as such, this was made for the
    // sake of simplicity so an exception class need not to be created
    public void divide() throws IllegalArgumentException {
        Double y = stack.pop();
        Double x = stack.pop();
        /*if (y == 0.0) {
            stack.push(x);
            stack.push(y);
            throw new IllegalArgumentException("division by zero not allowed");
        }*/
        stack.push(x / y);
    }

    public void power() {
        Double y = stack.pop();
        Double x = stack.pop();
        stack.push(Math.pow(x, y));
    }

    public void root() {
        Double y = stack.pop();
        Double x = stack.pop();
        stack.push(Math.pow(x, 1.0 / y));
    }

    public void inverse() {
        Double x = stack.pop();
        x = stack.pop();
        stack.push(1.0 / x);
    }

    public void sqrt() {
        Double x = stack.pop();
        stack.push(Math.sqrt(x));
    }

    public void cos() {
        Double x = stack.pop();
        stack.push(Math.cos(x));
    }

    public void sin() {
        Double x = stack.pop();
        stack.push(Math.sin(x));
    }

    public void tan() {
        Double x = stack.pop();
        stack.push(Math.tan(x));
    }

    public void cosh() {
        Double x = stack.pop();
        stack.push(Math.cosh(x));
    }

    public void sinh() {
        Double x = stack.pop();
        stack.push(Math.sinh(x));
    }

    public void log() {
        Double x = stack.pop();
        stack.push(Math.log(x));
    }

    public void log10() {
        Double x = stack.pop();
        stack.push(Math.log10(x));
    }

    // similarly to divide(), this function throws IllegalArgumentException,
    // this time is when the last number on the stack is not exactly an integer
    public void factorial() throws IllegalArgumentException {
        Double x = stack.pop();
        if (x != Math.floor(x)) {
            stack.push(x);
            throw new IllegalArgumentException("the given number is not an integer");
        }
        BigInteger i = _factorial(x.intValue());
        stack.push(i.doubleValue());
    }

    private BigInteger _factorial(Integer n) {
        BigInteger out = new BigInteger("1");
        for (Integer i = 2; i <= n; i++) {
            out = out.multiply(new BigInteger(i.toString()));
        }
        return out;
    }
}
