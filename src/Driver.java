import java.io.FileNotFoundException;
import java.util.Scanner;

public class Driver {
    static Scanner in = new Scanner(System.in);
    static Matrix matrix;
    static Matrix matrix1;
    static double[][] initialVector;
    static double tol;
    static int input;
    static String path;
    static String path1;
    static String path2;
    static Matrix[] list;

	public static void main(String[] args) throws FileNotFoundException {
        // Decide how to round decimals in matrix; to what extent must error be calculated?
	    System.out.println("Welcome to part 3 of the project.");
		System.out.println("Enter the file path for matrix A.");
		path1 = in.next();
		System.out.println("Enter the file path for initial eigenvector");
		path2 = in.next();
		System.out.println("Enter the tol");
		tol = Double.parseDouble(in.next());
		matrix1 = FileParser.parseFile(path1);
		initialVector = FileParser.parseVectorFile(path2, matrix1.getNumRows());
		PowerMethod powerMethod = new PowerMethod(matrix1, tol, initialVector);
		powerMethod.power_method();

    	
	
			
	
    }

	public static void caseOne(Matrix matrix) {
		list = matrix.lu_fact();
		System.out.println("LU decomposition of matrix:");
		System.out.println("L\n" + list[0] + "\nU\n" + list[1]);
		System.out.println("To ensure that the LU decomposition is correct, L and U should "
				+ "multiply together to form the original matrix. Result of L * U is below.");
		System.out.println(list[0].multiply(list[1]));
	}

	public static void caseTwo(Matrix matrix) {
		list = matrix.qr_fact_househ();
		System.out.println("QR factorization of matrix using HH Rotations:");
		System.out.println("Q\n" + list[0] + "\nR\n" + list[1]);
		System.out.println("To ensure that the QR factorization is correct, Q and R should "
				+ "multiply together to form the original matrix. Result of Q * R is below.");
		System.out.println(list[0].multiply(list[1]));
	}

	public static void caseThree(Matrix matrix) {
		list = matrix.qr_fact_givens();
		System.out.println("QR factorization of matrix using Givens Rotations:");
		System.out.println("Q\n" + list[0] + "\nR\n" + list[1]);
		System.out.println("To ensure that the QR factorization is correct, Q and R should "
				+ "multiply together to form the original matrix. Result of Q * R is below.");
		System.out.println(list[0].multiply(list[1]));
	}

	public static void caseFour(Matrix[] list) {
		System.out.println("Solving Ax = b using LU decomposition. Value of x is below:");
		System.out.println(list[0].solve_lu_b(list[1]));
		System.out.println("Solving Ax = b using QR decomposition via Givens rotations. x value is below:");
		System.out.println(list[0].solve_qr_b_givens(list[1]));
		System.out.println("Solving Ax = b using QR decomposition via Householder reflections. x value is below:");
		System.out.println(list[0].solve_qr_b_househ(list[1]));
	}

	public static void caseFive() throws FileNotFoundException {
		System.out.println("Starting with n = 2.");
		for (int i = 2; i <= 20; i++)
		{
			System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ n = " + i + " ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
			Matrix A = Matrix.getHilbertMatrix(i);
			Matrix b = Matrix.getBVector(i);
			Matrix[] list = {A, b};
			caseFour(list);
		}
	}
}
