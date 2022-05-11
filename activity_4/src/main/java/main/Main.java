package main;

import java.io.IOException;

import problems.kqbf.KQBF;
import problems.kqbf.solvers.GRASP_KQBF;

public class Main {

    public static void main(String[] args) throws IOException {
        KQBF.solve("instances/qbf/qbf040");
        GRASP_KQBF.staticSolve("instances/kqbf/kqbf100");
    }

}
