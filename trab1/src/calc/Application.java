package calc;
import java.io.*;
import java.util.EmptyStackException;

public class Application {
    // IOException may be thrown by BufferedReader and is left untreated
    // as such it has to be explicit after main signature
    public static void main(String[] args) throws IOException {
        // one calculator is instantiated and used all along this app
        Calculator calc = new Calculator();
        // as well as a single bufferedreader to read lines from stdin
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        // since a break inside the switch will not break the while loop
        // we use a boolean flag to indicate whether we should terminate de main loop or not
        boolean quit = false;
        while(!quit) {
            // the main loop consists basically of this steps:
            // read input from the user (read with a nice > promtp)
            System.out.print("> ");
            String input = br.readLine();
            // enclosed with a try to catch common exceptions such as empty stack or
            // illegal numbers (division by zero for instance), opposed to using one
            // try catch for every operation that may throw an exception
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
                        // every input that is not an explicit command
                        // is either a number to enter the stack
                        // or a misstyped/unkown command
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
            // after processing the input, print a separator followed by
            // a snapshot of the current calculator stack
            System.out.println("---");
            System.out.print(calc.toString());
        }
    }
}
