package main;

import java.util.ArrayList;
import java.util.List;

public class Parameters {

    public enum LocalSearchMethod {
        BEST_IMPROVING,
        FIRST_IMPROVING
    }

    public enum Variation {
        NONE,
        INTENSIFICATION_BY_RESTART,
        INTENSIFICATION_BY_RESTART_AND_DIVERSIFICATION_BY_RESTART
    }

    public Integer size;
    public List<String> instanceList = new ArrayList<>();
    public List<LocalSearchMethod> localSearchList = new ArrayList<>();
    public List<Double> tenureRatioList = new ArrayList<>();
    public List<Variation> methodVariationList = new ArrayList<>();
    public List<Integer> numberOfIterationsList = new ArrayList<>();
    public List<Integer> targetValueList = new ArrayList<>();

    public LocalSearchMethod getLocaSearchMethod(final String arg) {
        if (arg.equals(LocalSearchMethod.BEST_IMPROVING.toString().toLowerCase())) {
            return LocalSearchMethod.BEST_IMPROVING;
        } else if (arg.equals(LocalSearchMethod.FIRST_IMPROVING.toString().toLowerCase())) {
            return LocalSearchMethod.FIRST_IMPROVING;
        } else throw new RuntimeException("the Local Search Method provided is invalid");
    }

}
