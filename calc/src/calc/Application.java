package calc;

public class Application {
    public static void main(String[] args) {
        Calculator calc = new Calculator();
        calc.push(1.5);
        calc.push(5.5);
        calc.push(10.5);
        calc.sum();
        calc.push(2.0);
        calc.divide();
        System.out.println(calc.pop());
        System.out.println(calc.pop());
    }
}
