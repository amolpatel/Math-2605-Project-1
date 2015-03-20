import java.io.FileNotFoundException;
import java.util.Scanner;

public class Driver {
    public static void main(String[] args) throws FileNotFoundException {
        double[][] array = Matrix.getHilbertArray(4);
        Matrix matrix = new Matrix(array);

        // Decide how to round decimals in matrix; to what extent must error be calculated?
        System.out.println("Original matrix: \n" + matrix);
        System.out.println("Matrix added to itself: \n" + matrix.add(matrix));
        System.out.println("Matrix subtracted from itself: \n" + matrix.subtract(matrix));
        System.out.println("Matrix multiplied by itself: \n" + matrix.multiply(matrix));
        System.out.println("Matrix multiplied by scalar: \n" + matrix.multiply(10));
        System.out.println("Matrix transposed: \n" + matrix.transpose());
        // Dot product just calls multiply method, so not bothering to test it

        Matrix[] list = new Matrix[2];
        list = matrix.lu_fact();
        System.out.println("LU decomposition of matrix:");
        System.out.println("L\n" + list[0] + "\nU\n" + list[1]);
        System.out.println("To ensure that the LU decomposition is correct, L and U should "
                + "multiply together to form the original matrix. Result of L * U is below.");
        System.out.println(list[0].multiply(list[1]));
        System.out.println("HERE I AM + \n" + matrix);

        list = matrix.qr_fact_givens();
        System.out.println("QR factorization of matrix using Givens Rotations:");
        System.out.println("Q\n" + list[0] + "\nR\n" + list[1]);
        System.out.println("To ensure that the QR factorization is correct, Q and R should "
                + "multiply together to form the original matrix. Result of L * U is below.");
        System.out.println(list[0].multiply(list[1]));


        list = matrix.qr_fact_hh();
        System.out.println("QR factorization of matrix using HH Rotations:");
        System.out.println("Q\n" + list[0] + "\nR\n" + list[1]);
        System.out.println("To ensure that the QR factorization is correct, Q and R should "
                + "multiply together to form the original matrix. Result of L * U is below.");
        System.out.println(list[0].multiply(list[1]));




        System.out.println("Matrix diagonalized: \n" + matrix.diagonalize());
        System.out.println("Matrix absolute value: \n" + matrix.absoluteValue());

        // Testing FileParser below
        // Added data.dat file to project folder, so test with that if you need to
        Scanner in = new Scanner(System.in);
        System.out.println("Enter filepath: ");
        String path = in.next();
        System.out.println(FileParser.parseFile(path));
    }
}