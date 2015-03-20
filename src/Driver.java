import java.io.FileNotFoundException;
import java.util.Scanner;

public class Driver {
    public static void main(String[] args) throws FileNotFoundException {
        Matrix matrix = Matrix.getHilbertMatrix(4);
        double[][] bArray = new double[4][1];
        bArray[0][0] = 0.0464159; bArray[1][0] = 0.0464159; bArray[2][0] = 0.0464159; bArray[3][0] = 0.0464159;
        Matrix b = new Matrix(bArray);

        // Decide how to round decimals in matrix; to what extent must error be calculated?
        System.out.println("Original matrix: \n" + matrix);
        System.out.println("Matrix added to itself: \n" + matrix.add(matrix));
        System.out.println("Matrix subtracted from itself: \n" + matrix.subtract(matrix));
        System.out.println("Matrix multiplied by itself: \n" + matrix.multiply(matrix));
        System.out.println("Matrix multiplied by scalar: \n" + matrix.multiply(10));
        System.out.println("Matrix transposed: \n" + matrix.transpose());
        System.out.println("Matrix diagonalized: \n" + matrix.diagonalize());
        System.out.println("Matrix absolute value: \n" + matrix.absoluteValue());
        // Dot product just calls multiply method, so not bothering to test it

        Matrix[] list = new Matrix[2];

        list = matrix.lu_fact();
        System.out.println("LU decomposition of matrix:");
        System.out.println("L\n" + list[0] + "\nU\n" + list[1]);
        System.out.println("To ensure that the LU decomposition is correct, L and U should "
                + "multiply together to form the original matrix. Result of L * U is below.");
        System.out.println(list[0].multiply(list[1]));
        System.out.println("Solving Ax = b using LU decomposition. x value is below:");
        System.out.println(matrix.solve_lu_b(b));

        list = matrix.qr_fact_givens();
        System.out.println("QR factorization of matrix using Givens Rotations:");
        System.out.println("Q\n" + list[0] + "\nR\n" + list[1]);
        System.out.println("To ensure that the QR factorization is correct, Q and R should "
                + "multiply together to form the original matrix. Result of Q * R is below.");
        System.out.println(list[0].multiply(list[1]));
        System.out.println("Solving Ax = b using QR decomposition via Givens rotatios. x value is below:");
        System.out.println(matrix.solve_qr_b_givens(b));

        list = matrix.qr_fact_househ();
        System.out.println("QR factorization of matrix using Householder reflections:");
        System.out.println("Q\n" + list[0] + "\nR\n" + list[1]);
        System.out.println("To ensure that the QR factorization is correct, Q and R should "
                + "multiply together to form the original matrix. Result of Q * R is below.");
        System.out.println(list[0].multiply(list[1]));
        System.out.println("Solving Ax = b using QR decomposition via Householder reflections. x value is below:");
        System.out.println(matrix.solve_qr_b_househ(b));

        // Testing FileParser below
        // Added data.dat file to project folder, so test with that if you need to
        Scanner in = new Scanner(System.in);
        System.out.println("Enter filepath: ");
        String path = in.next();
        System.out.println(FileParser.parseFile(path));
    }
}