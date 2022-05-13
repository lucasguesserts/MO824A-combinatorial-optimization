import java.io.IOException;

import problems.kqbf.KQBF;

public class Main {

    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("usage: gradle run --args=\"INSTANCE\"");
            System.out.println("INSTANCE: instances/qbf/*, instances/kqbf/*");
            return;
        }
        else {
            KQBF.staticSolve(args[0]);
        }
        return;
    }

}
