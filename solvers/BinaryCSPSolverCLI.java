package solvers;
import java.util.*;

import solvers.ProblemPrinter;

public final class BinaryCSPSolverCLI {
  public static void main(String[] args) {

    String filepath;
    String output_mode;
    Boolean run_solver;
    String solver_option;

    // read the parameters either via command line or via stdin
    if (args.length == 4) {
      filepath = args[0];
      output_mode = args[1].toUpperCase();
      run_solver = args[2].trim().equalsIgnoreCase("Y");
      solver_option = args[3].toUpperCase(); // HARDCODED
    } else {
      // ask the user for the problem instance and algorithm 
      Scanner scanner = new Scanner(System.in);
      System.out.println("Please input the instance file (a path to a .csp file):");
      filepath = scanner.nextLine();
      System.out.println("Please input the output mode (V)erbose/(C)ompact:");
      output_mode = scanner.nextLine().toUpperCase();
      System.out.println("Do you want to run the solver? Y/N");
      run_solver = scanner.nextLine().trim().equalsIgnoreCase("Y");
      solver_option="";
      if(run_solver){
        System.out.println("Choose your solver: Backtracking (a), BnB (b), Forward Checking d-way (c)");
        solver_option = scanner.nextLine().toUpperCase();
        if (!solver_option.equalsIgnoreCase("A") &&
            !solver_option.equalsIgnoreCase("B") &&
            !solver_option.equalsIgnoreCase("C")) {
            System.out.println("Chosen algorithm not available");
            return;
        }

      }
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
      printer = new ProblemPrinter(csp, "verbose", run_solver, solver_option);
    } else {
      printer = new ProblemPrinter(csp, "compact", run_solver, solver_option);
    }
    // print the problem instance
    System.out.println(printer.toString());
  }
}