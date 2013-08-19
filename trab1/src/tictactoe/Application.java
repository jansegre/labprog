package tictactoe;

import java.io.*;
import java.util.EmptyStackException;

public class Application {
    // IOException may be thrown by BufferedReader and is left untreated
    // as such it has to be explicit after main signature
    public static void main(String[] args) throws IOException {
        // one calculator is instantiated and used all along this app
        TicTacToe game;
        // as well as a single BufferedReader to read lines from stdin
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        // since a break inside the switch will not break the while loop
        // we use a boolean flag to indicate whether we should terminate de main loop or not
        boolean quit = false;
        String input;
        while (!quit) {
            boolean chose_player = false;
            TicTacToe.Type player = TicTacToe.Type.EMPTY;
            while (!chose_player) {
                System.out.print("Choose X or O: ");
                input = br.readLine();
                switch(input) {
                    case "x":
                    case "X":
                        player = TicTacToe.Type.PLAYER1;
                        chose_player = true;
                        break;
                    case "o":
                    case "O":
                    case "0":
                        player = TicTacToe.Type.PLAYER2;
                        chose_player = true;
                        break;
                }
            }

            game = new TicTacToe(player);
            boolean game_over = false;
            while (!game_over) {
                System.out.print(game.toString());
                boolean valid_move = false;
                while (!valid_move) {
                    boolean valid_input = false;
                    int i = 0, j = 0;
                    while (!valid_input) {
                        System.out.print("move: ");
                        input = br.readLine();
                        if (input == "q") {
                            System.out.println("quitting... bye!");
                            return;
                        }
                        String[] ints = input.split("[\\s,;]+");
                        if (ints.length == 2) {
                            try {
                                j = Integer.parseInt(ints[0]);
                                i = Integer.parseInt(ints[1]);
                                valid_input = true;
                            } catch (NumberFormatException nfe) {
                                System.out.println("could not parse the entered number");
                            }
                        } else {
                            System.out.println("should input two numbers: i, j");
                        }
                    }
                    valid_move = true;
                    switch (game.play(player, i, j)) {
                        case INVALID:
                            System.out.println("that move is not possible");
                            valid_move = false;
                            break;
                        case VICTORY:
                            System.out.print(game.toString());
                            String symbol = player == TicTacToe.Type.PLAYER1 ? "X" : "O";
                            System.out.println("congratulations, '" + symbol + "' won!");
                            game_over = true;
                            break;
                        case TIE:
                            System.out.print(game.toString());
                            System.out.println("tie game!");
                            game_over = true;
                            break;
                        case OK:
                            break;
                        default:
                            System.out.println("congratulations, you've made the impossible!");
                            valid_move = false;
                    }
                }
                player = player == TicTacToe.Type.PLAYER1 ? TicTacToe.Type.PLAYER2 : TicTacToe.Type.PLAYER1;
            }

            System.out.print("play again (y/[n])? ");
            input = br.readLine();
            quit = true;
            switch (input) {
                case "y":
                case "Y":
                    quit = false;
                    break;
                default:
                    System.out.println("quiting... bye!");
            }
        }
    }
}
