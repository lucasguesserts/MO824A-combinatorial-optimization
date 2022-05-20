package costCoparer;

public interface CostComparer<V extends Number> {

    V getMaxCost();
    V getMinCost();
    Boolean isSmaller(final V lhs, final V rhs);

}
