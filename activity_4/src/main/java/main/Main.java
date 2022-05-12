package main;

import java.io.IOException;

import problems.kqbf.GRASP_KQBF;
import problems.kqbf.KQBF;

public class Main {

    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            System.out.println("usage: gradle run --args=\"ALGORITHM INSTANCE\"");
            System.out.println("ALGORITHM: naive, grasp");
            System.out.println("INSTANCE: instances/qbf/*, instances/kqbf/*");
            return;
        }
        if (args[0].equals("naive")) {
            KQBF.staticSolve(args[1]);
        }
        else if (args[0].equals("grasp")) {
            GRASP_KQBF.staticSolve(args[1]);
        }
        return;
    }

}
