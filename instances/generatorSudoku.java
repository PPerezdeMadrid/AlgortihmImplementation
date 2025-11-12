package instances;

import java.io.FileWriter;
import java.io.IOException;

public class generatorSudoku {

    public static void main(String[] args) {
        int n = 4; // Tamaño del Sudoku (4x4)
        int blockSize = 2; // Tamaño del bloque (2x2)

        // Matriz del Sudoku (0 = celda vacía, los demás = valor fijo)
        // Esta configuración produce una única solución
        int[][] puzzle = {
            {4, 3, 0, 0},
            {2, 1, 0, 0},
            {0, 0, 1, 2},
            {0, 0, 3, 4}
        };

        String filename = "instances/Sudoku4x4.csp";

        try (FileWriter fw = new FileWriter(filename)) {
            // Cabecera
            fw.write("// 4x4 Sudoku CSP with fixed clues\n\n");
            fw.write("// Number of variables:\n");
            fw.write(n * n + "\n\n");

            // Dominios de las variables
            fw.write("// Domains of the variables: 1.. (inclusive)\n");
            for (int row = 0; row < n; row++) {
                for (int col = 0; col < n; col++) {
                    int value = puzzle[row][col];
                    if (value == 0) {
                        fw.write("1, " + n + "\n"); // dominio completo
                    } else {
                        fw.write(value + ", " + value + "\n"); // valor fijo
                    }
                }
            }
            fw.write("\n");

            // Restricciones binarias
            fw.write("// constraints (vars indexed from 0, allowed tuples):\n");

            for (int var1 = 0; var1 < n * n; var1++) {
                int row1 = var1 / n;
                int col1 = var1 % n;

                for (int var2 = var1 + 1; var2 < n * n; var2++) {
                    int row2 = var2 / n;
                    int col2 = var2 % n;

                    // Si están en la misma fila, columna o bloque => restricción de desigualdad
                    if (row1 == row2 || col1 == col2 ||
                        (row1 / blockSize == row2 / blockSize && col1 / blockSize == col2 / blockSize)) {

                        fw.write("c(" + var1 + ", " + var2 + ")\n");

                        // Tuplas permitidas (valores distintos)
                        for (int v1 = 1; v1 <= n; v1++) {
                            for (int v2 = 1; v2 <= n; v2++) {
                                if (v1 != v2) {
                                    fw.write(v1 + ", " + v2 + "\n");
                                }
                            }
                        }
                        fw.write("\n");
                    }
                }
            }

            fw.write("// END\n");
            fw.flush();
            System.out.println("✅ Archivo CSP generado correctamente en: " + filename);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
