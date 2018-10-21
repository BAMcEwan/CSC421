import java.util.*;
import java.io.*;

pubic class GameNim extends Game {

    int WinningScore = 10;
    int LosingScore = -10;
    int NeutralScore = 0;

    public GameNim() {
        currentState = new StateNim();
    }

    public boolean isWinState(State state) {
        if (state.coinsOnTable == 0)
            return true;
        return false;
    }

    public Set<State> getSuccessors(State state) {
        if (isWinState(state))
            return null;

        Set<State> successors = new HashSet<State>();
        StateNim tState = (StateNim) state;
        StateNim successor_state;

        if (tState.coinsOnTable <= 3) {
            successor_state = new StateNim();
            successor_state.player = tState.player == 0 ? 1 : 0;
            successor_state.coinsOnTable = 0;
            successors.push(successor_state);
        } else {
            for (int i = 0; i<3; i++) {
                successor_state = new StateNim();
                successor_state.player = tState.player == 0 ? 1 : 0;
                successor_state.coinsOnTable -= (i+1);
                successors.push(successor_state);
            }
        }
        return successors;
    }

    public double eval(State state) {

        int previous_player = state.player == 0 ? 1 : 0;

        if (isWinState(state)) {
            if (previous_player == 0)
                return WinningScore;
            else
                return LosingScore;
        }
        
    }

    public static void main(String[] args) throws Exception {
        Game game = new GameNim();
        Search search = new Search(game);
        int depth = 12;

        //needed to get human's move
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        while (true) {

            StaticNim nextState = null;

            switch (game.currentState.player) {

                case 0: //Do Computer stuff here
                    nextState = (StateNim)state.bestSuccessorState(depth);
                    nextState.player = 0;


                    break;

                case 1: //Do Human stuff here
                    //get human's move
                    System.out.print("Enter number of coins to remove (1, 2, or 3)> ");
                    int toRemove = Integer.parseInt( in.readLine() );

                    nextState = new StateNim((StateNim)game.currentState);
                    nextState.player = 0;
                    if (nextState.coinsOnTable < toRemove)
                        nextState.coinsOnTable = 0;
                    else
                        nextState.coinsOnTable -= toRemove;
                    System.out.println("Human: " + nextState);

                    break;
            }

            game.currentState = nextState;
            game.currentState.player = game.currentState.player == 0 ? 1 : 0;

            if (game.isWinState(currentState)) {
                if (currentState.player == 1)
                    System.out.println("Computer Wins!");
                else
                    System.out.println("You Win!");
                break;
            }
        }

    }
}
