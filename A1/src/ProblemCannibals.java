import java.util.HashSet;
import java.util.Set;

public class ProblemCannibals extends Problem {
	
    static final int cannL = 0;
    static final int missL = 1;
    static final int boatL = 2;
    static final int cannR = 3;
    static final int missR = 4;
    static final int boatR = 5;
    
	boolean goal_test(Object state) {
        StateCannibals can_state = (StateCannibals) state;
        
        if (can_state.canArray[cannR]==3 && can_state.canArray[missR]==3 && can_state.canArray[boatR]==1)
            return true;
        else return false;
	}
  
    Set<Object> getSuccessors(Object state) {
    	
        Set<Object> set = new HashSet<Object>();
        StateCannibals can_state = (StateCannibals) state;
        
        //Let's create without any constraint, then remove the illegal ones
        
        //one cannibal only from left to right
        moveRight(0, 1, can_state, set);

        //one cannibal only from right to left
        moveLeft(0, 1, can_state, set);
        
        //two cannibals from left to right
        moveRight(0, 2, can_state, set);
        
        //two cannibals from right to left 
        moveLeft(0, 2, can_state, set);
        
        //one missionary only from left to right 
        moveRight(1, 0, can_state, set);
        
        //one missionary only from right to left 
        moveLeft(1, 0, can_state, set);
        
        //two missionaries from left to right 
        moveRight(2, 0, can_state, set);
        
        //two missionaries from right to left 
        moveLeft(2, 0, can_state, set);
        
        //one cannibal and one missionary from left to right 
        moveRight(1, 1, can_state, set);
        
        //one cannibal and one missionary from right to left 
        moveLeft(1, 1, can_state, set);

        return set;
    }

    private StateCannibals moveCommon(int numMissionaries, int numCannibals, StateCannibals can_state){
        StateCannibals successor_state = new StateCannibals(can_state);
        successor_state.canArray[cannL] -= numCannibals;
        successor_state.canArray[cannR] += numCannibals;
        successor_state.canArray[missL] -= numMissionaries;
        successor_state.canArray[missR] += numMissionaries;
        return successor_state;
    }

    private void moveRight(int numMissionaries, int numCannibals, StateCannibals can_state, Set<Object> set){
        StateCannibals successor_state = moveCommon(numMissionaries, numCannibals, can_state);
        successor_state.canArray[boatL] -= 1;
        successor_state.canArray[boatR] += 1;
        if (isValid(successor_state)) set.add(successor_state);
    }
    
    private void moveLeft(int numMissionaries, int numCannibals, StateCannibals can_state, Set<Object> set){
        StateCannibals successor_state = moveCommon(-numMissionaries, -numCannibals, can_state);
        successor_state.canArray[boatL] += 1;
        successor_state.canArray[boatR] -= 1;
        if (isValid(successor_state)) set.add(successor_state);
    }

    private boolean isValid(StateCannibals state)
    {   
        //Checking to see if any element of the array is negative 
        for (int i=0; i<6; i++)
            if (state.canArray[i] < 0) return false;
        
        //Checking to see if the numbers of cannibals, missionaries, and boat 
        //are more then 3,3,1 respectively
        if (state.canArray[cannL] > 3 || state.canArray[cannR] > 3) return false;
        if (state.canArray[missL] > 3 || state.canArray[missR] > 3) return false;
        if (state.canArray[boatL] > 1 || state.canArray[boatR] > 1) return false;
        
        //Now, checking if cannibals out number missionaries
        if (state.canArray[cannL] > state.canArray[missL] && state.canArray[missL] > 0) return false;
        if (state.canArray[cannR] > state.canArray[missR] && state.canArray[missR] > 0) return false;
        
        return true;
    }
	
	double step_cost(Object fromState, Object toState) { return 1; }

	public double h(Object state) { return 0; }


	public static void main(String[] args) throws Exception {
		ProblemCannibals problem = new ProblemCannibals();
		int[] canArray = {3,3,1,0,0,0};
		problem.initialState = new StateCannibals(canArray); 
		
		Search search  = new Search(problem);
		
		System.out.println("BreadthFirstTreeSearch:\t\t" + search.BreadthFirstTreeSearch());

		System.out.println("BreadthFirstGraphSearch:\t" + search.BreadthFirstGraphSearch());

        System.out.println("DepthFirstTreeSearch:\t" + search.DepthFirstTreeSearch());

        System.out.println("DepthFirstGraphSearch:\t" + search.DepthFirstGraphSearch());

        System.out.println("IterativeDeepeningTreeSearch:\t" + search.IterativeDeepeningTreeSearch());

        System.out.println("IterativeDeepeningGraphSearch:\t" + search.IterativeDeepeningGraphSearch());
    }
}
