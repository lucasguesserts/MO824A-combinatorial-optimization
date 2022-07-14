package solution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONObject;

public class Weight {

    private final List<Integer> values;

    public Weight(final List<Integer> values) {
        assertNoNegativeValues(values);
        this.values = Collections.unmodifiableList(values);
    }

    public static Weight zero(final Integer size) {
        return new Weight(zeroList(size));
    }

    public Integer size() {
        return this.values.size();
    }

    public Double getMax() {
        return Collections.max(this.values).doubleValue();
    }

    public Double getNorm2() {
        Double accumulator = 0.0;
        for(final var v : this.values) {
            accumulator += v * v;
        }
        accumulator = Math.sqrt(accumulator);
        return accumulator;
    }

    public Weight add(final Weight other) {
        this.assertSameSize(other);
        final List<Integer> sum = new ArrayList<>(this.values.size());
        for (int i = 0; i < this.values.size(); ++i) {
            sum.add(this.values.get(i) + other.values.get(i));
        }
        return new Weight(sum);
    }

    public Weight subtract(final Weight other) {
        this.assertSameSize(other);
        final List<Integer> sum = new ArrayList<>(this.values.size());
        for (int i = 0; i < this.values.size(); ++i) {
            sum.add(this.values.get(i) - other.values.get(i));
        }
        return new Weight(sum);
    }

    public Boolean exceeds(final Weight other) {
        /**
         * `this` exceeds `other` if there is any value in
         * `this` that is greater than the respective entry
         * in `other`.
         * In a more mathematical way:
         *   this.exceeds(other) is true
         *   <=>
         *   exist i such that: this.values.get(i) > other.values.get(i))
         */
        this.assertSameSize(other);
        for (int i = 0; i < this.values.size(); ++i) {
            if (this.values.get(i) > other.values.get(i)) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    @Override
    public boolean equals(Object obj) {
        // https://www.sitepoint.com/implement-javas-equals-method-correctly/
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Weight other = (Weight) obj;
        if (!this.values.equals(other.values)) {
            return false;
        }
        return true;
    }

    public JSONObject toJson() {
        final var obj = new JSONObject();
        obj.put("values", this.values);
        return obj;
    }

    @Override
    public String toString() {
        return this.toJson().toString(4);
    }

    private void assertSameSize(final Weight other) {
        assert this.values.size() == other.values.size()
            : String.format(
                "weights must have the same size to be added, but got %d and %d",
                this.values.size(),
                other.values.size()
            );
    }

    private static List<Integer> zeroList(final Integer size) {
        final List<Integer> values = new ArrayList<>(size);
        for (int i = 0; i < size; ++i) {
            values.add(0);
        }
        return values;
    }

    private static void assertNoNegativeValues(final List<Integer> values) {
        for (int i = 0; i < values.size(); ++i) {
            assert values.get(i) >= 0
                : String.format(
                    "Got unexpected negative value:\n\tvalue: %d\n\tposition: %d\n\tvalues: %s",
                    values.get(i),
                    i,
                    values
                );
        }
    }

}
