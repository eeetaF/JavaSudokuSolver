import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Main {
    public class Solver {
        private static int[][] solution;

        // I wish there were pointers in java. This property is used as a pointer.
        private static int current_count;

        private static boolean[] project (boolean[] arr1, boolean[] arr2, boolean[] arr3) {
            current_count = 0;
            boolean[] result = new boolean[9];
            for (int i = 0; i < 9; i++) {
                if (!arr1[i] && !arr2[i] && !arr3[i]) {
                    result[i] = true;
                    current_count++;
                }
            }
            return result;
        }
        private static int findId(int[][] matrix, boolean[][][]may_be, boolean[][]horizontal_lines,
                                                    boolean[][]vertical_lines, boolean[][]square_lines) {
            int id = -1;
            int max_count = 10;
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    if (matrix[i][j] == 9) {
                        may_be[i][j] = project(horizontal_lines[i], vertical_lines[j], square_lines[(i / 3) * 3 + j / 3]);
                        if (current_count < max_count) {
                            max_count = current_count;
                            id = i * 9 + j;
                        }
                    }
                }
            }
            if (max_count == 10)
                solution = matrix;
            if (max_count == 0)
                return -1;
            return id;
        }
        private static int[][] copy2Dint (int[][] arr) {
            int[][] copied = new int[arr.length][arr[0].length];
            for (int i = 0; i < arr.length; i++) {
                for (int j = 0; j < arr[i].length; j++) {
                    copied[i][j] = arr[i][j];
                }
            }
            return copied;
        }
        private static boolean[][] copy2Dbool (boolean[][] arr) {
            boolean[][] copied = new boolean[arr.length][arr[0].length];
            for (int i = 0; i < arr.length; i++) {
                for (int j = 0; j < arr[i].length; j++) {
                    copied[i][j] = arr[i][j];
                }
            }
            return copied;
        }
        private static boolean[][][] copy3Dbool (boolean[][][] arr) {
            boolean[][][] copied = new boolean[arr.length][arr[0].length][arr[0][0].length];
            for (int i = 0; i < arr.length; i++) {
                for (int j = 0; j < arr[i].length; j++) {
                    for (int k = 0; k < arr[i][j].length; k++) {
                        copied[i][j][k] = arr[i][j][k];
                    }
                }
            }
            return copied;
        }
        public static ArrayList<Integer> findTrues(boolean[] arr) {
            ArrayList<Integer> list = new ArrayList<>();
            for (int i = 0; i < arr.length; i++) {
                if (arr[i])
                    list.add(i);
            }
            return list;
        }
        private static void placeDigit(int[][] matrix, boolean[][][]may_be, boolean[][] horizontal_lines,
                                            boolean[][] vertical_lines, boolean[][] square_lines, int digit, int current_i, int current_j) {
            horizontal_lines[current_i][digit] = true;
            vertical_lines[current_j][digit] = true;
            square_lines[(current_i / 3) * 3 + current_j / 3][digit] = true;
            matrix[current_i][current_j] = digit;
            int id = findId(matrix, may_be, horizontal_lines, vertical_lines, square_lines);
            if (id != -1) {
                findSolution(matrix, may_be, horizontal_lines, vertical_lines, square_lines, id);
            }
        }
        private static void findSolution(int[][] matrix, boolean[][][]may_be, boolean[][]horizontal_lines,
                                            boolean[][]vertical_lines, boolean[][]square_lines, int id) {
            int current_i = id / 9;
            int current_j = id % 9;
            ArrayList<Integer> current_digits = findTrues(may_be[current_i][current_j]);
            for (int digit : current_digits) {
                placeDigit(copy2Dint(matrix), copy3Dbool(may_be), copy2Dbool(horizontal_lines), copy2Dbool(vertical_lines),
                            copy2Dbool(square_lines), digit, current_i, current_j);
            }
        }
        public static int[][] startSolver(int[][] matrix) {
            boolean [][] horizontal_lines = new boolean[9][9];
            boolean [][] vertical_lines = new boolean[9][9];
            boolean [][] square_lines = new boolean[9][9];
            boolean [][][] may_be = new boolean[9][9][9];
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    int current_digit = matrix[i][j];
                    if (current_digit != 9) {
                        horizontal_lines[i][current_digit] = true;
                        vertical_lines[j][current_digit] = true;
                        square_lines[(i / 3) * 3 + j / 3][current_digit] = true;
                    }
                }
            }
            int id = findId(matrix, may_be, horizontal_lines, vertical_lines, square_lines);
            findSolution(copy2Dint(matrix), copy3Dbool(may_be), copy2Dbool(horizontal_lines), copy2Dbool(vertical_lines),
                                copy2Dbool(square_lines), id);
            return solution;
        }
    }
    public static int[][] readMatrixFromFile(String filePath) {
        int[][] matrix = new int[9][9];

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            int row = 0;

            while ((line = br.readLine()) != null && row < 9) {
                String[] elements = line.split(" ");
                for (int col = 0; col < elements.length && col < 9; col++) {
                    if (elements[col].equals("-")) {
                        matrix[row][col] = 9;
                    } else {
                        matrix[row][col] = Integer.parseInt(elements[col])  - 1;
                    }
                }
                row++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return matrix;
    }
    public static void printMatrix(int[][] matrix) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
    }
    public static void decodeMatrix(int[][] matrix) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (matrix[i][j] == 9) {
                    matrix[i][j] = 0;
                }
                else {
                    matrix[i][j] += 1;
                }
            }
        }
    }

    public static void main(String[] args) {
        int[][] initialMatrix = readMatrixFromFile("tests/test2.txt");
        long startTime = System.nanoTime();
        int[][] result = new int[9][9];
        for (int i = 0; i < 100; i++)
            result = Solver.startSolver(initialMatrix);
        long endTime = System.nanoTime();
        long duration = endTime - startTime;
        System.out.println("Time taken: " + (duration / (1_000_000 * 100)) + " ms");
        decodeMatrix(result);
        printMatrix(result);
    }
}
