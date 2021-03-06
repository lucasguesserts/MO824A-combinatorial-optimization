package solutions;

import java.util.ArrayList;

public class Solution<E> extends ArrayList<E> {

    public Integer cost = Integer.MAX_VALUE;

    public Solution() {
        super();
    }

    public Solution(Solution<E> sol) {
        super(sol);
        cost = sol.cost;
    }

    @Override
    public String toString() {
        return "{cost: " + cost + ", size: " + this.size() + ", elements:" + super.toString() + "}";
    }

}

