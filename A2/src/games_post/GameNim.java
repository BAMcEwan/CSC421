import java.util.*;
import java.io.*;

public class GameNim extends Game {

    int WinningScore = 10;
    int LosingScore = -10;
    int NeutralScore = 0;

    public GameNim() {
        currentState = new StateNim();
    }

    public boolean isWinState(State state) {
        StateNim tState = (StateNim) state;

        if (tState.coinsOnTable == 0)
            return true;
        return false;
    }

    //Not needed
    public boolean isStuckState(State state) {
        return false;
    }

    public Set<State> getSuccessors(State state) {

        Set<State> successors = new HashSet<State>();
        StateNim tState = (StateNim) state;
        StateNim successor_state;

        if (isWinState(tState))
            return null;

        //If coins on table is 1 just take the last coin
        if (tState.coinsOnTable == 1) {
            successor_state = new StateNim(tState);
            successor_state.player = tState.player == 0 ? 1 : 0;
            successor_state.coinsOnTable = 0;
            successors.add(successor_state);
        } else { //otherwise add 3 successor states to hashset with all subraction options
            for (int i = 0; i<3; i++) {
                if (tState.coinsOnTable > (i+1)) { //only add successors that don't put coins to <1
                    successor_state = new StateNim(tState);
                    successor_state.player = tState.player == 0 ? 1 : 0;
                    successor_state.coinsOnTable -= (i+1);
                    successors.add(successor_state);
                }
            }
        }
        return successors;
    }

    public double eval(State state) {

        int previous_player = state.player == 0 ? 1 : 0;
        StateNim tState = (StateNim) state;

        if (tState.coinsOnTable == 1)
            return WinningScore;
        else if(tState.coinsOnTable <= 4)
            return LosingScore;

        return NeutralScore;
    }

    public static void main(String[] args) throws Exception {
        Game game = new GameNim();
        Search search = new Search(game);
        int depth = 12;

        //needed to get human's move
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        while (true) {

            StateNim nextState = null;

            switch (game.currentState.player) {

                case 0: //Do Computer stuff here
                    Set<State> successors = game.getSuccessors(game.currentState);
                    int bestVal = -100;
                    for(State successor : successors) {
                        StateNim tState = (StateNim) successor;
                        int newVal = (int)game.eval(tState);
                        if (newVal > bestVal) {
                            nextState = tState;
                            bestVal = newVal;
                        }
                    }
                    nextState.player = 0;
                    System.out.println("Computer: " + nextState);

                    break;

                case 1: //Do Human stuff here
                    //get human's move
                    System.out.print("Enter number of coins to remove (1, 2, or 3)> ");
                    int toRemove = Integer.parseInt( in.readLine() );

                    nextState = new StateNim((StateNim)game.currentState);
                    nextState.player = 1;
                    if (nextState.coinsOnTable < toRemove)
                        nextState.coinsOnTable = 0;
                    else
                        nextState.coinsOnTable -= toRemove;
                    System.out.println("Human: " + nextState);

                    break;
            }

            game.currentState = nextState;
            game.currentState.player = game.currentState.player == 0 ? 1 : 0;
            
            if (game.isWinState(game.currentState)) {
                if (game.currentState.player == 0)
                    System.out.println("Computer Wins!");
                else
                    System.out.println("You Win!");
                break;
            }
        }
    }
}
