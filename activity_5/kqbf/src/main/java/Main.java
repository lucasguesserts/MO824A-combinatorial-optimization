import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

import main.AbstractMain;
import main.Parameters;

class Main extends AbstractMain {

    private Main() throws IOException {
        super();
        this.makeInstanceList();
        this.makeLocalSearchList();
        this.makeTenureRatioList();
        this.makeMethodVariationList();
        this.makeNumberOfIterationsList();
    }

    public static void main(String[] args) throws IOException {
        final var solver = new Main();
        solver.run();
    }

    private void makeInstanceList() {
        parameters.instanceList = Arrays.asList(
            20,
            40,
            60,
            80,
            100,
            200,
            400
        ).stream().map(n -> makeInstanceName(n)).collect(Collectors.toList());
    }

    private void makeLocalSearchList() {
        this.parameters.localSearchList = Arrays.asList(Parameters.LocalSearchMethod.values());
    }

    private void makeTenureRatioList() {
        this.parameters.tenureRatioList.add(0.2);
        this.parameters.tenureRatioList.add(0.4);
    }

    private void makeMethodVariationList() {
        this.parameters.methodVariationList = Arrays.asList(Parameters.Variation.values());
    }

    private void makeNumberOfIterationsList() {
        this.parameters.numberOfIterationsList.add(1000);
    }

    private String makeInstanceName(final Integer instanceNumber) {
        final String DIR_PATH = "../instances";
        final String PROBLEM_PREFIX = "kqbf";
        return String.format(
            "%s/%s/%s%03d",
            DIR_PATH,
            PROBLEM_PREFIX,
            PROBLEM_PREFIX,
            instanceNumber
        );
    }
}
