package solutions;

import java.util.ArrayList;

public class Solution extends ArrayList<Integer> {

	public Integer cost = Integer.MAX_VALUE;

	public Solution() {
		super();
	}

	public Solution(Solution sol) {
		super(sol);
		cost = sol.cost;
	}

	@Override
	public String toString() {
		return "Solution: cost=[" + cost + "], size=[" + this.size() + "], elements=" + super.toString();
	}

}

