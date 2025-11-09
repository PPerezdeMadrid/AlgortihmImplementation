package solver;
import java.util.*;

import solver.ProblemPrinter;

public final class BinaryCSPSolverCLI {
  public static void main(String[] args) {

    String filepath;
    String output_mode;

    // read the parameters either via command line or via stdin
    if (args.length == 2) {
      filepath = args[0];
      output_mode = args[1].toUpperCase();
    } else {
      // ask the user for the problem instance and algorithm 
      Scanner scanner = new Scanner(System.in);
      System.out.println("Please input the instance file (a path to a .csp file):");
      filepath = scanner.nextLine();
      System.out.println("Please input the output mode (V)erbose/(C)ompact:");
      output_mode = scanner.nextLine().toUpperCase();
      scanner.close();

      // check the parameters
      if (!output_mode.equals("V") && !output_mode.equals("C")) {
        System.out.println("Output mode must be either V or C");
        return;
      }
    }

    // read CSP problem instance
    BinaryCSPReader reader = new BinaryCSPReader();
    BinaryCSP csp = reader.readBinaryCSP(filepath);

    // Create the solver
    ProblemPrinter printer = null;
    if (output_mode.equals("V")) {
      printer = new ProblemPrinter(csp, "verbose");
    } else {
      printer = new ProblemPrinter(csp, "compact");
    }
    // print the problem instance
    System.out.println(printer.toString());
  }
}