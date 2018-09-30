import java.util.HashSet;
import java.util.Set;
import java.lang.Math;

public class ProblemJugs extends Problem {
	
    static final int twelveGal = 0;
    static final int eightGal = 1;
    static final int threeGal = 2;

    static final int[] gallonMax = {12, 8, 3};
    
	boolean goal_test(Object state) {
        StateJugs jug_state = (StateJugs) state;
        
        if (jug_state.jugArray[twelveGal]==1 || jug_state.jugArray[eightGal]==1 || jug_state.jugArray[threeGal]==1)
            return true;
        else return false;
	}
  
    Set<Object> getSuccessors(Object state) {
    	
        Set<Object> set = new HashSet<Object>();
        StateJugs jug_state = (StateJugs) state;
        
        //Let's create without any constraint except no null actions, then remove the illegal ones
        
        //fill 12 gallon
        fill(twelveGal, jug_state, set);

        //fill 8 gallon
        fill(eightGal, jug_state, set);

        //fill 3 gallon
        fill(threeGal, jug_state, set);

        //pour from 12 to 8
        pour(twelveGal, eightGal, jug_state, set);

        //pour from 12 to 3
        pour(twelveGal, threeGal, jug_state, set);

        //pour 12 on ground
        dump(twelveGal, jug_state, set);

        //pour from 8 to 3
        pour(eightGal, threeGal, jug_state, set);

        //pour from 8 to 12
        pour(eightGal, twelveGal, jug_state, set);

        //pour 8 on ground
        dump(eightGal, jug_state, set);

        //pour from 3 to 12
        pour(threeGal, twelveGal, jug_state, set);

        //pour from 3 to 8
        pour(threeGal, eightGal, jug_state, set);

        //pour 3 on ground
        dump(threeGal, jug_state, set);
        
        return set;
    }

    private void fill(int inJug, StateJugs jug_state, Set<Object> set){
        StateJugs successor_state = new StateJugs(jug_state);
        if(successor_state.jugArray[inJug] != gallonMax[inJug]){ // Avoid identical state
            successor_state.jugArray[inJug] = gallonMax[inJug];
            if (isValid(successor_state)) set.add(successor_state);
        }
    }

    private void dump(int outJug, StateJugs jug_state, Set<Object> set){
        StateJugs successor_state = new StateJugs(jug_state);
        if(successor_state.jugArray[outJug] != 0){ // Avoid identical state
            successor_state.jugArray[outJug] = 0;
            if (isValid(successor_state)) set.add(successor_state);
        }
    }

    private void pour(int outJug, int inJug, StateJugs jug_state, Set<Object> set){
        StateJugs successor_state = new StateJugs(jug_state);
        if(successor_state.jugArray[outJug] != 0){ // Can't pour from empty jug
            int difference = gallonMax[inJug] - successor_state.jugArray[inJug];
            if(difference != 0){ // Avoid identical state
                int fillAmount = Math.min(successor_state.jugArray[outJug], difference);
                successor_state.jugArray[inJug] += fillAmount;
                successor_state.jugArray[outJug] -= fillAmount;
                if (isValid(successor_state)) set.add(successor_state);
            }
        }
    }
    
    private boolean isValid(StateJugs state)
    {   
        for (int i=0; i<3; i++){
            //Checking to see if any element of the array is negative 
            if (state.jugArray[i] < 0) return false;
        
            //Checking to see if the jugs are filled more than their maximums
            if(state.jugArray[i] > gallonMax[i]) return false;
        }
                
        return true;
    }
	
	double step_cost(Object fromState, Object toState) {
        StateJugs from = (StateJugs) fromState;
        StateJugs to = (StateJugs) toState;

        int toReturn = 0;
        
        for(int i = 0; i < 3; i++){
            toReturn = Math.abs(to.jugArray[i] - from.jugArray[i]);
            if(toReturn > 0)
                break; // Stop after first difference found to not double-count pours
        }
        return toReturn;
    }

	public double h(Object state) { return 0; }


	public static void main(String[] args) throws Exception {
		ProblemJugs problem = new ProblemJugs();
		int[] jugArray = {0,0,0};
		problem.initialState = new StateJugs(jugArray); 
		
		Search search  = new Search(problem);
		
		System.out.println("BreadthFirstTreeSearch:\t\t" + search.BreadthFirstTreeSearch());

		System.out.println("BreadthFirstGraphSearch:\t" + search.BreadthFirstGraphSearch());

        System.out.println("DepthFirstTreeSearch:\t" + search.DepthFirstTreeSearch());
        
        System.out.println("DepthFirstGraphSearch:\t" + search.DepthFirstGraphSearch());
        
        System.out.println("IterativeDeepeningTreeSearch:\t" + search.IterativeDeepeningTreeSearch());
        
        System.out.println("IterativeDeepeningGraphSearch:\t" + search.IterativeDeepeningGraphSearch());
	}
}
