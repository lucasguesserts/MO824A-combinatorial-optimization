package tabusearch;

import java.io.IOException;

import inputReader.InputReaderKQBF;

public class TS_KQBF extends TS_QBF {

    public TS_KQBF(
        final Integer tenure,
        final Integer iterations,
        final InputReaderKQBF input
    ) throws IOException {
        super(tenure, iterations, input);
    }

}
