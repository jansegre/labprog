package calc;
import java.io.*;
import java.util.EmptyStackException;

public class Application {
    public static void main(String[] args) throws IOException {
        Calculator calc = new Calculator();
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        boolean quit = false;
        while(!quit) {
            System.out.print("> ");
            String input = br.readLine();
            try {
                switch(input) {
                    case "del":
                    case "drop":
                    case "pop":
                        calc.pop();
                        break;
                    case "+":
                    case "plus":
                    case "sum":
                        calc.sum();
                        break;
                    case "-":
                    case "minus":
                    case "subtract":
                        calc.subtract();
                        break;
                    case "*":
                    case "times":
                    case "multiply":
                        calc.multiply();
                        break;
                    case "/":
                    case "div":
                    case "divide":
                        calc.divide();
                        break;
                    case "^":
                    case "**":
                    case "pow":
                    case "power":
                        calc.power();
                        break;
                    case "log":
                    case "ln":
                        calc.log();
                        break;
                    case "log10":
                        calc.log10();
                        break;
                    case "sin":
                    case "sen":
                        calc.sin();
                        break;
                    case "cos":
                        calc.cos();
                        break;
                    case "tan":
                    case "tg":
                        calc.tan();
                        break;
                    case "sinh":
                    case "senh":
                        calc.sinh();
                        break;
                    case "cosh":
                        calc.cosh();
                        break;
                    case "inv":
                    case "inverse":
                        calc.inverse();
                        break;
                    case "!":
                    case "fac":
                    case "factorial":
                        calc.factorial();
                        break;
                    case "q":
                    case "quit":
                        quit = true;
                        System.out.println("quitting... bye!");
                        break;
                    default:
                        try {
                            calc.push(Double.parseDouble(input));
                        } catch (NumberFormatException nfe) {
                            System.out.println("not a number or unkown command");
                        }
                        break;
                }
            } catch (EmptyStackException ese) {
                System.out.println("not enough numbers for that operation");
            } catch (IllegalArgumentException iae) {
                System.out.println(iae.getMessage());
            }
            System.out.println("---");
            System.out.print(calc.toString());
        }
    }
}
