package inputReader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.util.Arrays;

public class InputReaderQBF {

    private final Integer size;
    private final Integer[][] matrix;

    public InputReaderQBF(final String fileName) throws IOException {
        final Reader file = new BufferedReader(new FileReader(fileName));
        final StreamTokenizer stok = new StreamTokenizer(file);
        stok.nextToken();
        this.size = (int) stok.nval;
        this.matrix = new Integer[this.size][this.size];
        for (int i = 0; i < this.size; ++i) {
            Arrays.fill(this.matrix[i], 0);
        }
        for (int i = 0; i < this.size; ++i) {
            for (int j = i; j < this.size; ++j) {
                stok.nextToken();
                this.matrix[i][j] = (int) Math.round(stok.nval);
            }
        }
    }

    public Integer getSize() {
        return this.size;
    }

    public Integer[][] getMatrix() {
        return this.matrix;
    }

}
