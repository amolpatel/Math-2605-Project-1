package Part1;

import java.io.FileNotFoundException;
import java.util.Scanner;

public class Part1Driver {
	public static void main(String[] args) throws FileNotFoundException {
	    Scanner in = new Scanner(System.in);
		Matrix matrix;
		System.out.println("Welcome to part 1 of the project.");
    	while (true)
    	{
    		System.out.println("Select an option, or enter q to quit.");
    		System.out.println("1 - LU decomposition of n x n matrix.");
    		System.out.println("2 - QR decomposition of n x n matrix using Householder reflections.");
    		System.out.println("3 - QR decomposition of n x n matrix using Givens rotations.");
    		System.out.println("4 - Solve Ax = b using LU decomposition and QR factorization (requires augmented matrix input).");
    		System.out.println("5 - Solve Ax = b using Hilbert matrices from n = 2 to n = 20.");
    		String input = in.next();
    		int inputNum;
    		if (input.equalsIgnoreCase("q"))
    			break;
    		else
    			inputNum = Integer.parseInt(input);
            System.out.println("Enter file path. If option 5 was chosen, just enter any character to move on.");
            String path = in.next();
            switch (inputNum) {
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
		Matrix[] list = matrix.lu_fact();
		System.out.println("LU decomposition of matrix:");
		System.out.println("L\n" + list[0] + "\nU\n" + list[1]);
		System.out.println("Error of ||LU - A||_inf:");
		// difference = LU - A
		Matrix difference = (list[0].multiply(list[1])).subtract(matrix);
		System.out.println(difference.getError());
	}

	public static void caseTwo(Matrix matrix) {
		Matrix[] list = matrix.qr_fact_househ();
		System.out.println("QR factorization of matrix using HH Rotations:");
		System.out.println("Q\n" + list[0] + "\nR\n" + list[1]);
		System.out.println("Error of ||QR - A||_inf:");
		// difference = QR - A
		Matrix difference = (list[0].multiply(list[1])).subtract(matrix);
		System.out.println(difference.getError());
	}

	public static void caseThree(Matrix matrix) {
		Matrix[] list = matrix.qr_fact_givens();
		System.out.println("QR factorization of matrix using Givens Rotations:");
		System.out.println("Q\n" + list[0] + "\nR\n" + list[1]);
		System.out.println("Error of ||QR - A||_inf:");
		// difference = QR - A
		Matrix difference = matrix.subtract(list[0].multiply(list[1]));
		System.out.println(difference.getError());
	}

	public static void caseFour(Matrix[] list) {
		System.out.println("Solving Ax = b using LU decomposition. Value of x is below:");
		Matrix x = list[0].solve_lu_b(list[1]);
		System.out.println(x);
		System.out.println("Error in ||Ax - b|| using LU decomposition:");
		// difference = (A * x) - b
		Matrix difference = (list[0].multiply(x)).subtract(list[1]);
		System.out.println(difference.getError() + "\n");

		System.out.println("Solving Ax = b using QR decomposition via Givens rotations. Value of x is below:");
		x = list[0].solve_qr_b_givens(list[1]);
		System.out.println(x);
		System.out.println("Error in ||Ax - b|| using QR decomposition via Givens Rotations:");
		// difference = (A * x) - b
		difference = (list[0].multiply(x)).subtract(list[1]);
		System.out.println(difference.getError() + "\n");

		System.out.println("Solving Ax = b using QR decomposition via Householder reflections. Value of x is below:");
		x = list[0].solve_qr_b_househ(list[1]);
		System.out.println(x);
		System.out.println("Error in ||Ax - b|| using QR decomposition via Householder reflections:");
		// difference = (A * x) - b
		difference = (list[0].multiply(x)).subtract(list[1]);
		System.out.println(difference.getError() + "\n");
	}

	public static void caseFive() throws FileNotFoundException {
		System.out.println("Starting with n = 2.");
		for (int i = 2; i <= 20; i++)
		{
			System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ n = " + i + " ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
			Matrix H = Matrix.getHilbertMatrix(i);
			Matrix b = Matrix.getBVector(i);

			System.out.println("Solution to Hx = b using LU decomposition:");
			Matrix x = H.solve_lu_b(b);
			System.out.println(x);
			System.out.println("Error in ||LU - H||_inf:");
			Matrix[] lu = H.lu_fact();
			Matrix difference = (lu[0].multiply(lu[1])).subtract(H);
			System.out.println(difference.getError());
			System.out.println("Error in ||Hx - b||:");
			difference = (H.multiply(x)).subtract(b);
			System.out.println(difference.getError() + "\n");

			System.out.println("Solution to Hx = b using QR decomposition via Householder reflections:");
			x = H.solve_qr_b_househ(b);
			System.out.println(x);
			System.out.println("Error in ||QR - H||_inf:");
			Matrix[] qr = H.qr_fact_househ();
			difference = (qr[0].multiply(qr[1])).subtract(H);
			System.out.println(difference.getError());
			System.out.println("Error in ||Hx - b||:");
			difference = (H.multiply(x)).subtract(b);
			System.out.println(difference.getError() + "\n");

			System.out.println("Solution to Hx = b using QR decomposition via Givens rotations:");
			x = H.solve_qr_b_givens(b);
			System.out.println(x);
			System.out.println("Error in ||QR - H||_inf:");
			qr = H.qr_fact_givens();
			difference = (qr[0].multiply(qr[1])).subtract(H);
			System.out.println(difference.getError());
			System.out.println("Error in ||Hx - b||:");
			difference = (H.multiply(x)).subtract(b);
			System.out.println(difference.getError() + "\n");
		}
	}
}