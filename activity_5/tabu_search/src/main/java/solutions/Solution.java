package solutions;

import java.util.ArrayList;

public class Solution<E, V extends Number> extends ArrayList<E> {

	private V cost;

	public Solution(final V cost) {
		super();
        this.cost = cost;
	}

	public Solution(Solution<E, V> sol) {
		super(sol);
		this.cost = sol.cost;
	}

    public V getCost() {
        return this.cost;
    }

	@Override
	public String toString() {
		return String.format("Solution: {cost: %f, size: %f, elements: [%s]",
            this.cost,
            this.size(),
            super.toString()
        );
	}

}

