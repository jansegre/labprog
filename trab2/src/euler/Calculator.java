package polycalc;

import java.util.Stack;

public class Calculator {
    private Stack<Polynomial> stack;
    private static final String NEW_LINE = System.getProperty("line.separator");

    public Calculator() {
        stack = new Stack<>();
    }

    // Basic interaction

    // this may seem obvious, and it actually is
    // to add put a number on the calculator use push
    // to remove a number from the calculator use pop
    // to view the whole stack, guess what? getStack

    public void push(Polynomial polynomial) {
        stack.push(polynomial);
    }

    public Polynomial pop() {
        return stack.pop();
    }

    public String toString() {
        String out = "";
        for(Polynomial p : stack) {
            out += p.toString() + NEW_LINE;
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

    public void duplicate() {
        Polynomial p = stack.pop();
        stack.push(p);
        stack.push(new Polynomial(p));
    }

    public void swap() {
        Polynomial y = stack.pop();
        Polynomial x = stack.pop();
        stack.push(y);
        stack.push(x);
    }

    public void sum() {
        Polynomial y = stack.pop();
        Polynomial x = stack.pop();
        stack.push(x.sum(y));
    }

    public void subtract() {
        Polynomial y = stack.pop();
        Polynomial x = stack.pop();
        stack.push(x.subtract(y));
    }

    public void multiply() {
        Polynomial y = stack.pop();
        Polynomial x = stack.pop();
        stack.push(x.multiply(y));
    }

    // this operation hast the restriction of now allowing division
    // by zero, as such it will throw IllegalArgumentException, even
    // though it's not an argument, but from the point of view of the
    // calculator it could be considered as such, this was made for the
    // sake of simplicity so an exception class need not to be created
    public void divide() {
        Polynomial y = stack.pop();
        Polynomial x = stack.pop();
        stack.push(x.divide(y));
    }

    public void power() {
        Polynomial y = stack.pop();
        if (y.degree() != 0 || y.leadCoefficient() != Math.floor(y.leadCoefficient()))
            throw new IllegalArgumentException("power must be an integer scalar");
        Polynomial x = stack.pop();
        int power = y.leadCoefficient().intValue();
        stack.push(x.power(power));
    }
}