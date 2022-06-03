package inputReader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.util.Arrays;

public class InputReaderQBF {

    protected StreamTokenizer stok;
    protected Integer size;
    protected Integer[][] matrix;

    public InputReaderQBF(final String fileName) throws IOException {
        final Reader file = new BufferedReader(new FileReader(fileName));
        this.stok = new StreamTokenizer(file);
        this.construct();
    }

    public Integer getSize() {
        return this.size;
    }

    public Integer[][] getMatrix() {
        return this.matrix;
    }

    public void printMatrix() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                System.out.print(String.format("%3d ", this.matrix[i][j]));
            }
            System.out.println();
        }
    }

    // Beware that the order in which you use the methods below matter a lot!

    protected void construct() throws IOException {
        // override this method in child classes
        this.size = this.readInteger();
        this.matrix = this.allocateMatrix();
        this.fillMatrixWithZeros();
        this.readMatrix();
    }

    protected Integer readInteger() throws IOException {
        this.stok.nextToken();
        return (int) this.stok.nval;
    }

    protected Integer[][] allocateMatrix() {
        return new Integer[this.size][this.size];
    }

    protected void fillMatrixWithZeros() {
        for (int i = 0; i < this.size; ++i) {
            Arrays.fill(this.matrix[i], 0);
        }
    }

    protected void readMatrix() throws IOException {
        for (int i = 0; i < this.size; ++i) {
            for (int j = i; j < this.size; ++j) {
                this.stok.nextToken();
                this.matrix[i][j] = (int) Math.round(stok.nval);
            }
        }
    }

}
