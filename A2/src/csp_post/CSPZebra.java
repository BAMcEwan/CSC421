import java.util.ArrayList;

public class CSPZebra extends CSP {
	
	final String[] nums = { "1", "2", "3", "4", "5" };

	final String[] people = { "Englishman",	"Spaniard",	"Ukrainian",	"Norwegian",	"Japanese" };
	final String[] colour = { "Red",		"Green",	"Ivory",		"Yellow",		"Blue" };
	final String[] animal = { "Dog",		"Snails",	"Fox",			"Horse",		"Zebra" };
	final String[] drinks = { "Coffee",		"Tea",		"Milk",			"Orange Juice",	"Water" };
	final String[] cigars = { "Old-Gold",	"Kools",	"Chesterfield",	"Lucky-Strike",	"Parliament" };

	final String[][] types = {people, colour, animal, drinks, cigars};

	public boolean isGood(Object X, Object Y, Object x, Object y) {
		//if X is not even mentioned in by the constraints, just return true
		//as nothing can be violated
		if(!C.containsKey(X))
			return true;
		
		//check to see if there is an arc between X and Y
		//if there isn't an arc, then no constraint, i.e. it is good
		if(!C.get(X).contains(Y))
			return true;
		
		// One of each type to house
		for(int i = 0; i < types.length; i++){
			String[] type = types[i];
			for(int j = 0; j < type.length; j++){
				for(int k = 0; k < type.length; k++){
					if(X.equals(type[j]) && Y.equals(type[k]) && x.equals(y)){
						return false;
					}
				}
			}
		}

		// Englishman lives in red house
		if(X.equals(people[0]) && Y.equals(colour[0]) && !x.equals(y))
			return false;

		// Spaniard own dog
		if(X.equals(people[1]) && Y.equals(animal[0]) && !x.equals(y))
			return false;

		// Coffee drank in green house
		if(X.equals(drinks[0]) && Y.equals(colour[1]) && !x.equals(y))
			return false;

		// Ukrainian drinks tea
		if(X.equals(people[2]) && Y.equals(drinks[1]) && !x.equals(y))
			return false;

		// Green house is directly to the right of ivory house
		if(X.equals(colour[1]) && Y.equals(colour[2]) && !this.isDirectlyRightOf((String)x, (String)y))
			return false;

		// Old-Gold smoker owns snails
		if(X.equals(cigars[0]) && Y.equals(animal[1]) && !x.equals(y))
			return false;

		// Kools smoked in yellow house
		if(X.equals(cigars[1]) && Y.equals(colour[3]) && !x.equals(y))
			return false;

		// Chesterfield smoker lives next to fox owner
		if(X.equals(cigars[2]) && Y.equals(animal[2]) && !this.adjacentHomes((String)x, (String)y))
			return false;

		// Kools smoked in house next to horse house
		if(X.equals(cigars[1]) && Y.equals(animal[3]) && !this.adjacentHomes((String)x, (String)y))
			return false;

		// Lucky-Strike smoke drinks orange juice
		if(X.equals(cigars[3]) && Y.equals(drinks[3]) && !x.equals(y))
			return false;

		// Japanese smokes Parliament
		if(X.equals(people[4]) && Y.equals(cigars[4]) && !x.equals(y))
			return false;

		// Norwegian lives next to blue house
		if(X.equals(people[3]) && Y.equals(colour[4]) && !this.adjacentHomes((String)x, (String)y))
			return false;
		
		return true;
	}
	
	private boolean isDirectlyRightOf(String x, String y){
		int xInt = Integer.parseInt(x);
		int yInt = Integer.parseInt(y);

		return (xInt - yInt == 1);
	}

	private boolean isDirectlyLeftOf(String x, String y){
		int xInt = Integer.parseInt(x);
		int yInt = Integer.parseInt(y);

		return (xInt - yInt == -1);
	}

	private boolean adjacentHomes(String x, String y){
		return isDirectlyRightOf(x, y) || isDirectlyLeftOf(x, y);
	}

	private void initBidirectionalArcs(){
		for(int i = 0; i < types.length; i++){
			String[] type = types[i];
			for(String str : type){
				// Add from same variable type but avoid duplicated pairs
				for(String str2 : type){
					if(str != str2)
						this.addBidirectionalArc((Object)str, (Object)str2);
				}

				// Add from other variable types that haven't already been paired
				for(int j = i + 1; j < types.length; j++){
					String[] otherType = types[j];
					for(String oth : otherType){
						this.addBidirectionalArc((Object)str, (Object)oth);
					}
				}
			}
		}
	}

	public void initDomains(){
		for(Object X : people){
			// Norwegian lives in the house on the left
			String[] domain = ((String)X == people[3]) ? new String[]{nums[0]} : new String[]{nums[1], nums[2], nums[3], nums[4]};
			this.addDomain(X, domain);
		}
		for(Object X : colour){
			// Green house is directly to the right of the ivory house. (Therefore ivory house cannot be furthest right)
			String[] domain = ((String)X == colour[2]) ? new String[]{nums[0], nums[1], nums[2], nums[3]} : nums;
			this.addDomain(X, domain);
		}
		for(Object X : drinks){
			// House in the middle drinks milk
			String[] domain = ((String)X == drinks[2]) ? new String[]{nums[2]} : new String[]{nums[0], nums[1], nums[3], nums[4]};
			this.addDomain(X, domain);
		}
		for(Object X : animal) 
			this.addDomain(X, nums);
		for(Object X : cigars) 
			this.addDomain(X, nums);
	}

	public static void main(String[] args) throws Exception {
		CSPZebra csp = new CSPZebra();
		csp.initBidirectionalArcs();
		csp.initDomains();
		
		Search search = new Search(csp);
		System.out.println(search.BacktrackingSearch());
	}
}