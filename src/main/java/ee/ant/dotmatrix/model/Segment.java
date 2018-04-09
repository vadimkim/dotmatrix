package ee.ant.dotmatrix.model;

import java.util.BitSet;

public class Segment {
    private int columns;
    private BitSet matrix[];


    public Segment(int columns) {
        this.columns = columns;
        this.matrix = new BitSet[columns];
        for (int i=0; i < columns; i++) {
            matrix[i] = new BitSet(8);
        }
    }

    public int getColumns() {
        return columns;
    }

    public BitSet[] getMatrix() {
        return matrix;
    }
}
