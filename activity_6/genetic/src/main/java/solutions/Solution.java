package solutions;

import java.util.ArrayList;

public class Solution<E> extends ArrayList<E> {

    public Double cost = 0.0;
    public Integer weight = 0;

    public Solution() {
        super();
    }

    public Solution(final Solution<E> sol) {
        super(sol);
        cost = sol.cost;
        weight = sol.weight;
    }

    @Override
    public String toString() {
        return String.format(
            "Solution: cost = [%f], weight = [%d], size = [%d], elements = %s",
            this.cost,
            this.weight,
            this.size(),
            super.toString()
        );
    }

}
