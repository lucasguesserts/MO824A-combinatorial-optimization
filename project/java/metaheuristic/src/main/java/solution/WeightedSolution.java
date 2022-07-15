package solution;

import org.json.JSONObject;

public class WeightedSolution extends ElementsSolution implements Solution {

    private final Weight capacity;
    private Weight weight;

    public WeightedSolution(final Weight capacity) {
        super();
        this.capacity = capacity;
        this.weight = Weight.zero(capacity.size());
        this.assertWeightIsNonNegative(this.capacity);
        this.assertWeightIsNonNegative(this.weight);
        return;
    }

    protected WeightedSolution(final WeightedSolution other) {
        super(other);
        this.capacity = other.getCapacity();
        this.weight = other.weight;
        return;
    }

    protected WeightedSolution(final Weight newCapacity, final WeightedSolution other) {
        super(other);
        this.capacity = newCapacity;
        this.weight = other.weight;
        return;
    }

    public WeightedSolution clone() {
        return new WeightedSolution(this);
    }

    public Solution cloneWithExpandedCapacity(final Weight newCapacity) {
        return new WeightedSolution(newCapacity, this);
    }

    public Weight getCapacity() {
        return this.capacity;
    }

    public Weight getWeight() {
        return this.weight;
    }

    public void addElement(final Integer element, final Weight elementWeight) {
        final var newWeight = this.weight.add(elementWeight);
        this.assertNewWeightDoesNotExceedCapacity(newWeight);
        super.addElement(element);
        this.weight = newWeight;
        return;
    }

    public void removeElement(final Integer element, final Weight elementWeight) {
        final var newWeight = this.weight.subtract(elementWeight);
        this.assertNewWeightDoesNotExceedCapacity(newWeight);
        this.assertWeightIsNonNegative(newWeight);
        super.removeElement(element);
        this.weight = newWeight;
        return;
    }

    public void substituteElement(
        final Integer toRemove,
        final Weight toRemoveWeight,
        final Integer toAdd,
        final Weight toAddWeight
    ) {
        this.assertNoEqualElements(toRemove, toAdd);
        this.removeElement(toRemove, toRemoveWeight);
        this.addElement(toAdd, toAddWeight);
        return;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final var other = (WeightedSolution) obj;
        if (!super.equals(obj)) {
            return false;
        }
        if (!this.capacity.equals(other.capacity)) {
            return false;
        }
        if (!this.weight.equals(other.weight)) {
            return false;
        }
        return true;
    }

    @Override
    public JSONObject toJson() {
        final var obj = super.toJson();
        obj.put("capacity", this.getCapacity().toJson());
        obj.put("weight", this.getWeight().toJson());
        return obj;
    }

    private void assertNewWeightDoesNotExceedCapacity(final Weight newWeight) {
        assert !newWeight.exceeds(this.capacity)
            : String.format(
                "capacity exceeded:\n\tcapacity: %s\n\tnew weight: %s",
                this.capacity.toString(),
                newWeight.toString()
            );
        return;
    }

    private void assertWeightIsNonNegative(final Weight weight) {
        assert weight.getMin() >= 0
            : String.format(
                "Weight is not non-negative, i.e. it has at least one negative value\n%s",
                weight.toString()
            );
        return;
    }

    private void assertNoEqualElements(final Integer lhs, final Integer rhs) {
        assert !lhs.equals(rhs)
            : String.format(
                "Elements %d and %d are equal",
                lhs,
                rhs
            );
        return;
    }

}

