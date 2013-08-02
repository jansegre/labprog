package calc;
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

    // Operations

    // the following operations will remove the required
    // amount of numbers from the stack and push the result
    // of the opration to the stack
    //
    // if there are not enough numbers the exception
    // from the stack is forwarded

    public void sum() {
        Double y = (Double) stack.pop();
        Double x = (Double) stack.pop();
        stack.push(x + y);
    }

    public void subtract() {
        Double x, y;
        y = stack.pop();
        x = stack.pop();
        stack.push(x - y);
    }

    public void multiply() {
        Double x, y;
        y = stack.pop();
        x = stack.pop();
        stack.push(x * y);
    }

    public void divide() {
        Double x, y;
        y = stack.pop();
        x = stack.pop();
        stack.push(x / y);
    }

    public void power() {
        Double x, y;
        y = stack.pop();
        x = stack.pop();
        stack.push(Math.pow(x, y));
    }

    public void root() {
        Double x, y;
        y = stack.pop();
        x = stack.pop();
        stack.push(Math.pow(x, 1.0 / y));
    }

    public void inverse() {
        Double x;
        x = stack.pop();
        stack.push(1.0 / x);
    }

    public void sqrt() {
        Double x;
        x = stack.pop();
        stack.push(Math.sqrt(x));
    }

    public void cos() {
        Double x;
        x = stack.pop();
        stack.push(Math.cos(x));
    }

    public void sin() {
        Double x;
        x = stack.pop();
        stack.push(Math.sin(x));
    }

    public void tan() {
        Double x;
        x = stack.pop();
        stack.push(Math.tan(x));
    }
}
