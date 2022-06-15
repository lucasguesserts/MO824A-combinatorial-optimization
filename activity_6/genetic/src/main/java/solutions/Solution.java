package solutions;

import java.util.ArrayList;

public class Solution<E> extends ArrayList<E> {

    public Double cost = Double.POSITIVE_INFINITY;

    public Solution() {
        super();
    }

    public Solution(final Solution<E> sol) {
        super(sol);
        cost = sol.cost;
    }

    @Override
    public String toString() {
        return String.format(
            "Solution: cost = [%f], size = [%d], elements = %s",
            cost,
            this.size(),
            super.toString()
        );
    }

}
