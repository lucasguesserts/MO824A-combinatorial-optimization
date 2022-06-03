package costCoparer;

public class IntegerCostComparer implements CostComparer<Integer> {
    // Singleton

    private static IntegerCostComparer instance = null;

    public static IntegerCostComparer getInstance() {
        if (IntegerCostComparer.instance == null) {
            IntegerCostComparer.instance = new IntegerCostComparer();
        }
        return IntegerCostComparer.instance;
    }

    public Integer getMaxCost() {
        return Integer.MAX_VALUE;
    }

    public Integer getMinCost() {
        return Integer.MIN_VALUE;
    }

    public Boolean isSmaller(final Integer lhs, final Integer rhs) {
        return lhs < rhs;
    }

    private IntegerCostComparer(){}

}
