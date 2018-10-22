/*
 * State for the Nim board. For the given version of Nim the main part
 * of the game state is how many coins are left on the table. This is tracked
 * by the coinsOnTable variable.
 */

public class StateNim extends State {

    public int coinsOnTable;

    //default init, set player to 1 (Human) and coinsOnTable to 13 (start val)
    public StateNim() {
        coinsOnTable = 13;
        player = 1;
    }

    //init with coinsOnTable and player set to given state
    public StateNim(StateNim state) {
        coinsOnTable = state.coinsOnTable;
        player = state.player;
    }

    //toString just returns the current game state (number of coins on table)
    public String toString() {
        return "Coins currently on table: " + coinsOnTable + "\n";
    }
}
