package graph;

import solution.Weight;

public final class NodeSubstitutionTriple {

    public final Integer toRemove;
    public final Integer toAdd;
    public final Weight weightOfSubstitution;

    public NodeSubstitutionTriple(
        final Integer toRemove,
        final Integer toAdd,
        final Weight weightOfSubstitution) {
        this.toRemove = toRemove;
        this.toAdd = toAdd;
        this.weightOfSubstitution = weightOfSubstitution;
        return;
    }

}
