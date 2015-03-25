import java.io.FileNotFoundException;
import java.util.Scanner;

public class Driver {
    static Scanner in = new Scanner(System.in);
    static Matrix matrix;
    static int input;
    static String path;
    static Matrix[] list;

	public static void main(String[] args) throws FileNotFoundException {
        // Decide how to round decimals in matrix; to what extent must error be calculated?
		System.out.println("Welcome to part 1 of the project.");
    	while (true)
    	{
    		System.out.println("Select an option.");
    		System.out.println("1 - LU decomposition using n x n matrices.");
    		System.out.println("2 - QR factorization using Householder reflections.");
    		System.out.println("3 - QR factorization using Givens rotations.");
    		System.out.println("4 - Solve Ax = b using LU decomposition or QR factorization.");
    		System.out.println("5 - Hilbert matrices from n = 2 to n = 20");
    		input = in.nextInt();
            System.out.println("Enter file path. If a file path is not required, just enter any character to move on.");
            path = in.next();
            switch (input) {
            	case 1: matrix = FileParser.parseFile(path);
            			caseOne(matrix);
            			break;
            	case 2: matrix = FileParser.parseFile(path);
            			caseTwo(matrix);
        				break;
            	case 3: matrix = FileParser.parseFile(path);
            			caseThree(matrix);
            			break;
            	case 4: Matrix[] augmented = FileParser.parseFileWithB(path);
            			caseFour(augmented);
            			break;
            	case 5: caseFive();
            			break;
            }
    	}
    }

	public static void caseOne(Matrix matrix) {
		list = matrix.lu_fact();
		System.out.println("LU decomposition of matrix:");
		System.out.println("L\n" + list[0] + "\nU\n" + list[1]);
		System.out.println("Error of ||LU - A||_inf:");
		// difference = LU - A
		Matrix difference = matrix.subtract(list[0].multiply(list[1]));
		System.out.println(difference.getMaxNorm());
	}

	public static void caseTwo(Matrix matrix) {
		list = matrix.qr_fact_househ();
		System.out.println("QR factorization of matrix using HH Rotations:");
		System.out.println("Q\n" + list[0] + "\nR\n" + list[1]);
		// difference = QR - A
		Matrix difference = matrix.subtract(list[0].multiply(list[1]));
		System.out.println(difference.getMaxNorm());
	}

	public static void caseThree(Matrix matrix) {
		list = matrix.qr_fact_givens();
		System.out.println("QR factorization of matrix using Givens Rotations:");
		System.out.println("Q\n" + list[0] + "\nR\n" + list[1]);
		// difference = QR - A
		Matrix difference = matrix.subtract(list[0].multiply(list[1]));
		System.out.println(difference.getMaxNorm());
	}

	public static void caseFour(Matrix[] list) {
		System.out.println("Solving Ax = b using LU decomposition. Value of x is below:");
		Matrix x = list[0].solve_lu_b(list[1]);
		System.out.println(x);
		System.out.println("Error in solving Ax = b using LU decomposition (Ax - b):");
		// difference = (A * x) - b
		Matrix difference = (list[0].multiply(x)).subtract(list[1]);
		System.out.println(difference.getMaxNorm() + "\n");

		System.out.println("Solving Ax = b using QR decomposition via Givens rotations. x value is below:");
		System.out.println(list[0].solve_qr_b_givens(list[1]));
		System.out.println("Error in solving Ax = b using QR decomposition via Givens Rotations:");
		// difference = (A * x) - b
		difference = (list[0].multiply(x)).subtract(list[1]);
		System.out.println(difference.getMaxNorm() + "\n");

		System.out.println("Solving Ax = b using QR decomposition via Householder reflections. x value is below:");
		System.out.println(list[0].solve_qr_b_househ(list[1]));
		System.out.println("Error in solving Ax = b using Householder reflections:");
		// difference = (A * x) - b
		difference = (list[0].multiply(x)).subtract(list[1]);
		System.out.println(difference.getMaxNorm() + "\n");
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