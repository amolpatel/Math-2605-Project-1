import java.io.FileNotFoundException;
import java.util.Scanner;

public class Driver {
	public static void main(String[] args) throws FileNotFoundException {
		Matrix hilbert = Matrix.getHilbertMatrix(4);
		double[][] bArray = new double[4][1];
		bArray[0][0] = 0.0464159; bArray[1][0] = 0.0464159; bArray[2][0] = 0.0464159; bArray[3][0] = 0.0464159;
		Matrix b = new Matrix(bArray);

		// Decide how to round decimals in hilbert; to what extent must error be calculated?
		System.out.println("Original matrix: \n" + hilbert);
		System.out.println("Matrix added to itself: \n" + hilbert.add(hilbert));
		System.out.println("Matrix subtracted from itself: \n" + hilbert.subtract(hilbert));
		System.out.println("Matrix multiplied by itself: \n" + hilbert.multiply(hilbert));
		System.out.println("Matrix multiplied by scalar: \n" + hilbert.multiply(10));
		System.out.println("Matrix transposed: \n" + hilbert.transpose());
        System.out.println("Matrix diagonalized: \n" + hilbert.diagonalize());
        System.out.println("Matrix absolute value: \n" + hilbert.absoluteValue());
		// Dot product just calls multiply method, so not bothering to test it

		Matrix[] list = new Matrix[2];
		list = hilbert.lu_fact();
		System.out.println("LU decomposition of matrix:");
		System.out.println("L\n" + list[0] + "\nU\n" + list[1]);
		System.out.println("To ensure that the LU decomposition is correct, L and U should "
				+ "multiply together to form the original hilbert. Result of L * U is below.");
		System.out.println(list[0].multiply(list[1]));
		System.out.println("Testing LU solver. Value of x is: ");
		System.out.println(hilbert.solve_lu_b(b));

		list = hilbert.qr_fact_givens();
		System.out.println("QR factorization of matrix using Givens Rotations:");
		System.out.println("Q\n" + list[0] + "\nR\n" + list[1]);
		System.out.println("To ensure that the QR factorization is correct, Q and R should "
				+ "multiply together to form the original hilbert. Result of L * U is below.");
		System.out.println(list[0].multiply(list[1]));

		// Testing FileParser below
        // Added data.dat file to project folder, so test with that if you need to
        Scanner in = new Scanner(System.in);
        System.out.println("To read in data from file, enter the full filepath: ");
        String path = in.next();
        System.out.println(FileParser.parseFile(path));
	}
}