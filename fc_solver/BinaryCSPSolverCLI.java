package fc_solver;
import java.util.*;

import fc_solver.ProblemPrinter;

public final class BinaryCSPSolverCLI {
  public static void main(String[] args) {

    String filepath;
    String output_mode;
    Boolean run_solver;
    String solver_type;

    // read the parameters either via command line or via stdin
    if (args.length == 4) {
      filepath = args[0];
      output_mode = args[1].toUpperCase();
      run_solver = args[2].equalsIgnoreCase("Y");
      solver_type = args[3].toUpperCase();
    } else {
      // ask the user for the problem instance and algorithm 
      Scanner scanner = new Scanner(System.in);
      System.out.println("Please input the instance file (a path to a .csp file):");
      filepath = scanner.nextLine();
      System.out.println("Please input the output mode (V)erbose/(C)ompact:");
      output_mode = scanner.nextLine().toUpperCase();
      System.out.println("Do you want to run the solver? Y/N");
      run_solver = scanner.nextLine().equalsIgnoreCase("Y");
      if(run_solver){
        System.out.println("What type of Forward Checking do you want? (a) d-way or (b) 2-way");
        solver_type = scanner.nextLine().toUpperCase();
        if (!solver_type.equals("A") && !solver_type.equals("B")) {
          solver_type = "XXXX";
          return;
        }
      }else{
        solver_type = "XXXX";
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
      printer = new ProblemPrinter(csp, "verbose", run_solver, solver_type);
    } else {
      printer = new ProblemPrinter(csp, "compact", run_solver, solver_type);
    }
    // print the problem instance
    System.out.println(printer.toString());
  }
}