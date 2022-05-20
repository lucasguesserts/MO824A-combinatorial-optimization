package tabuSearch;

import java.io.IOException;

import inputReader.InputReaderKQBF;

public class TabuSearchKQBF extends TabuSearchQBF {

    public TabuSearchKQBF(
        final Integer tenure,
        final Integer iterations,
        final InputReaderKQBF input
    ) throws IOException {
        super(tenure, iterations, input);
    }

}
