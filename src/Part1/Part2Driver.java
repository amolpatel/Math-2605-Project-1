package Part1;

import java.io.FileNotFoundException;
import java.util.Scanner;

public class Part2Driver {
    static Scanner in = new Scanner(System.in);
    static Matrix matrix;
    static int input;
    static String path;
    static Matrix[] list;

    public static void main(String[] args) throws FileNotFoundException {
        // Decide how to round decimals in matrix; to what extent must error be calculated?
        System.out.println("Welcome to part 2 of the project.");
        while (true) {
            System.out.println("Select an option.");
            System.out.println("1 - Solve Ax=b with Gauss-Seidel from file in [A|b] form.");
            System.out.println("2 - Solve Ax=b with Jacobi from file in [A|b] form.");
            System.out.println("3 - Generate Y0 with X stream from file.");
            System.out.println("4 - Generate Y0 with random X stream.");
            System.out.println("5 - Generate Y1 with X stream from file.");
            System.out.println("6 - Generate Y1 with random X stream.");
            System.out.println("7 - Generate full Y with X stream from file.");
            System.out.println("8 - Generate full Y with random X stream.");
            System.out.println("9 - Generate X with Y stream from file using Gauss-Seidel.");
            System.out.println("10 - Generate X with random Y stream using Gauss-Seidel.");
            System.out.println("11 - Generate X with Y stream from file using Jacobi.");
            System.out.println("12 - Generate X with random Y stream using Jacobi.");
            input = in.nextInt();
            System.out.println("Enter file path. If a file path is not required, just enter any character to move on.");
            path = in.next();
            switch (input) {
                case 1:
                    Matrix[] augmented = FileParser.parseFileWithB(path);
                    System.out.println("A:\n" + augmented[0]);
                    caseOne(augmented);
                    break;
                case 2:
                    Matrix[] augmented2 = FileParser.parseFileWithB(path);
                    System.out.println("A:\n" + augmented2[0]);
                    caseTwo(augmented2);
                    break;
                case 3:
                    Matrix augmented3 = FileParser.parseVectorFile(path);
                    caseThree(augmented3);
                    break;
                case 4:
                    caseFour();
                    break;
                case 5:
                    Matrix augmented5 = FileParser.parseVectorFile(path);
                    caseFive(augmented5);
                    break;
                case 6:
                    caseSix();
                    break;
                case 7:
                    Matrix augmented7 = FileParser.parseVectorFile(path);
                    caseSeven(augmented7);
                    break;
                case 8:
                    caseEight();
                    break;
                case 9:
                    Matrix augmented9 = FileParser.parseVectorFile(path);
                    caseNine(augmented9);
                    break;
                case 10:
                    caseTen();
                    break;
                case 11:
                    Matrix augmented11 = FileParser.parseVectorFile(path);
                    caseEleven(augmented11);
                    break;
                case 12:
                    caseTwelve();
                    break;
            }
        }
    }

    public static void caseOne(Matrix[] list) {
        Matrix A = list[0];
        Matrix b = list[1];
        Matrix X = b.generateInitial();
        float tol = (float) .000000001;
        System.out.println("Tolerance is: "+tol);
        Matrix result = A.gauss_seidel(b, X, tol);
        System.out.println(result);
    }

    public static void caseTwo(Matrix[] list) {
        Matrix A = list[0];
        Matrix b = list[1];
        Matrix X = b.generateInitial();
        float tol = (float) .000000001;
        System.out.println("Tolerance is: "+tol);
        Matrix result = A.jacobi(b, X, tol);
        System.out.println(result);
    }

    public static void caseThree(Matrix matrix) {
        Matrix X = matrix;
        System.out.println("X:\n" + X);
        Matrix A0 = X.getA0();
        Matrix Y0 = A0.multiplyMod(X);
        System.out.println("Y0:\n" + Y0);
    }

    public static void caseFour() {
        System.out.println("Please enter desired length of X");
        Matrix X = Matrix.getXStream(in.nextInt());
        System.out.println("X:\n" + X);
        Matrix A0 = X.getA0();
        Matrix Y0 = A0.multiplyMod(X);
        System.out.println("Y0:\n" + Y0);
    }

    public static void caseFive(Matrix matrix) {
        Matrix X = matrix;
        System.out.println("X:\n" + X);
        Matrix A1 = X.getA1();
        Matrix Y1 = A1.multiplyMod(X);
        System.out.println("Y1:\n" + Y1);
    }

    public static void caseSix() {
        System.out.println("Please enter desired length of X");
        Matrix X = Matrix.getXStream(in.nextInt());
        System.out.println("X:\n" + X);
        Matrix A1 = X.getA1();
        Matrix Y1 = A1.multiplyMod(X);
        System.out.println("Y1:\n" + Y1);
    }

    public static void caseSeven(Matrix matrix) {
        Matrix X = matrix;
        System.out.println("X:\n" + X);
        Matrix A0 = X.getA0();
        Matrix Y0 = A0.multiplyMod(X);
        Matrix A1 = X.getA1();
        Matrix Y1 = A1.multiplyMod(X);
        String[][] result = Matrix.combineY(Y0, Y1);
        System.out.println("Y:");
        for(int i = 0; i < result.length; i++){
            System.out.println(result[i][0]);
        }
    }

    public static void caseEight() {
        System.out.println("Please enter desired length of X");
        Matrix X = Matrix.getXStream(in.nextInt());
        System.out.println("X:\n" + X);
        Matrix A0 = X.getA0();
        Matrix Y0 = A0.multiplyMod(X);
        Matrix A1 = X.getA1();
        Matrix Y1 = A1.multiplyMod(X);
        String[][] result = Matrix.combineY(Y0, Y1);
        System.out.println("Y:");
        for(int i = 0; i < result.length; i++){
            System.out.println(result[i][0]);
        }

    }

    public static void caseNine(Matrix matrix) {
        Matrix Y = matrix;
        System.out.println("Y:\n" + Y);
        Matrix A = Y.getA0();
        Matrix guess = Y.generateInitial();
        float tol = (float) .000000001;
        System.out.println("Tolerance is: "+tol);
        Matrix X = A.gauss_seidel(Y,guess, tol);
        System.out.println("X:\n" + X);
    }

    public static void caseTen() {
        System.out.println("Please enter desired length of Y");
        Matrix Y = Matrix.getYStream(in.nextInt());
        System.out.println("Y:\n" + Y);
        Matrix A = Y.getA0();
        Matrix guess = Y.generateInitial();
        float tol = (float) .000000001;
        System.out.println("Tolerance is: "+tol);
        Matrix X = A.gauss_seidel(Y, guess, tol);
        System.out.println("X:\n" + X);
    }

    public static void caseEleven(Matrix matrix) {
        Matrix Y = matrix;
        System.out.println("Y:\n" + Y);
        Matrix A = Y.getA0();
        Matrix guess = Y.generateInitial();
        float tol = (float) .000000001;
        System.out.println("Tolerance is: "+tol);
        Matrix X = A.jacobi(Y, guess, tol);
        System.out.println("X:\n" + X);
    }

    public static void caseTwelve() {
        System.out.println("Please enter desired length of Y");
        Matrix Y = Matrix.getYStream(in.nextInt());
        System.out.println("Y:\n" + Y);
        Matrix A = Y.getA0();
        Matrix guess = Y.generateInitial();
        float tol = (float) .000000001;
        System.out.println("Tolerance is: "+tol);
        Matrix X = A.jacobi(Y, guess, tol);
        System.out.println("X:\n" + X);
    }
}