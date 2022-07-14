package solution;

import org.json.JSONObject;

public class WeightedSolution extends ElementsSolution implements Solution {

    private final Weight capacity;
    private Weight weight;

    public WeightedSolution(final Weight capacity) {
        super();
        this.capacity = capacity;
        this.weight = Weight.zero(capacity.size());
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
    }

}

