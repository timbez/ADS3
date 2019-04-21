import clause.Literal;
import container.CNF;
import graph.Graph;
import parser.ClauseParser;
import parser.FileParser;
import sat2.Algorithm2Sat;
import sat2.Interpretation;
import sat2.TruthInterpretationFinder;

import java.io.File;
import java.util.List;

public class Main {

    private static final String RESOURCES_PATH = ".\\resources\\";
    private static final String TEST_FILE_ = "test_.txt";
    private static final String TEST_FILE_1 = "test.txt";
    private static final String TEST_FILE_2 = "test2.txt";
    private static final String TEST_FILE_3 = "test3.txt";
    private static final String TEST_FILE_4 = "test4.txt";
    private static final String PRIKLAD_1 = "priklad1.txt";
    private static final String PRIKLAD_1_ = "priklad1_.txt";
    private static final String PRIKLAD_2 = "priklad2.txt";
    private static final String PRIKLAD_3 = "priklad3.txt";

    public static void main(String[] args) {
        run(TEST_FILE_);
        run(TEST_FILE_1);
        run(TEST_FILE_2);
        run(TEST_FILE_3);
        run(TEST_FILE_4);
        run(PRIKLAD_1);
        run(PRIKLAD_1_);
        run(PRIKLAD_2);
        run(PRIKLAD_3);
    }

    private static void run(String filename) {
        File src = new File(RESOURCES_PATH + filename);
        CNF clauses = getClausesFromFile(src);

        Algorithm2Sat alg = new Algorithm2Sat(clauses);
        boolean satisfiable = alg.isSatisfiable();
        Graph<Literal> implicationsGraph = alg.getGraph();

        System.out.println(filename + ": KNF " + clauses + " je " + (satisfiable ? "SPLNITEĽNÁ" : "NESPLNITEĽNÁ"));

        if (satisfiable) {
            Interpretation interpretation = new TruthInterpretationFinder(implicationsGraph, clauses).find();
            System.out.println(interpretation);
        }
        System.out.println("");
    }

    private static CNF getClausesFromFile(File file) {
        List<String> contents = new FileParser().parse(file);
        // remove first line as it is unnecessary
        contents.remove(0);

        return new ClauseParser().parse(contents);
    }
}
