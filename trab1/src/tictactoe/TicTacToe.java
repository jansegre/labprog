package tictactoe;

public class TicTacToe {
    public enum Type {EMPTY, PLAYER1, PLAYER2};
    public enum State {VICTORY, TIE, OK, INVALID};

    private Type[][] grid;
    private Type next_player;
    private int plays;

    // the default public constructor will make a grid of size 3, naturally
    // and take the first player
    public TicTacToe(Type player) {
        this(3, player);
    }

    // a more generic version is available, though not tested for orders other than 3
    public TicTacToe(int n, Type player) {
        grid = new Type[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                grid[i][j] = Type.EMPTY;
        next_player = player;
    }

    // this is the main way of interacting with the game and changing its state
    public State play(Type player, int i, int j) {
        // basic check if the move is valid
        if (player != next_player || grid[i][j] != Type.EMPTY || i < 0 || i >= size() || j < 0 || j >= size())
            return State.INVALID;

        // save the move, and change next_player
        grid[i][j] = player;
        next_player = player == Type.PLAYER1 ? Type.PLAYER2 : Type.PLAYER1;
        plays++;

        // check for victory

        // note: it only makes sense to check the current move
        boolean aligned = true;
        // horizontal
        for (int k = 0; k < size(); ++k)
            if (player != grid[i][k]) {
                aligned = false;
                break;
            }
        if (aligned) {
            return State.VICTORY;
        }
        // vertical
        aligned = true;
        for (int k = 0; k < size(); ++k)
            if (player != grid[k][j]) {
                aligned = false;
                break;
            }
        if (aligned) {
            return State.VICTORY;
        }
        // it's easier to check for all diagonals instead of the current
        aligned = true;
        for (int k = 0; k < size(); ++k)
            if (player != grid[k][k]) {
                aligned = false;
                break;
            }
        if (aligned) {
            return State.VICTORY;
        }
        aligned = true;
        for (int k = 0; k < size(); ++k)
            if (player != grid[k][size() - 1 - k]) {
                aligned = false;
                break;
            }
        if (aligned) {
            return State.VICTORY;
        }

        // now it's either ok or tied
        return plays < size() * size() ? State.OK : State.TIE;
    }

    // should draw something like this:
    //
    //    | X | O
    // ---+---+---
    //  O |   | X
    // ---+---+---
    //  X |   | O
    //
    // where player 1 is drawn as X
    public String toString() {
        String out = "";
        for (int i = 0; i < size(); i++) {
            for (int j = 0; j < size(); j++) {
                out += " ";
                switch (grid[i][j]) {
                    case EMPTY: out += " "; break;
                    case PLAYER1: out += "X"; break;
                    case PLAYER2: out += "O"; break;
                    default: out += "?"; break;
                }
                if (j < size() - 1)
                    out += " |";
            }
            out += "\n";
            if (i < size() - 1) {
                for (int j = 1; j < size(); j++)
                    out += "---+";
                out += "---\n";
            }
        }
        return out;
    }

    private int size() {
        return grid.length;
    }
}
