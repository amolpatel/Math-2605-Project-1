package Part3;

import java.io.FileNotFoundException;
import java.util.Scanner;

import Part1.FileParser;
import Part1.Matrix;

public class Part3Driver {
	public static void main(String[] args) throws FileNotFoundException {
		Scanner in = new Scanner(System.in);
	    System.out.println("Welcome to part 3 of the project.");
	    while (true)
	    {
			System.out.println("To quit, enter q now. Otherwise, enter the file path for matrix A.");
			String path1 = in.next();
			if (path1.equalsIgnoreCase("q"))
				break;
			System.out.println("Enter the file path for initial eigenvector");
			String path2 = in.next();
			System.out.println("Enter the tol");
			double tol = Double.parseDouble(in.next());
			Matrix matrix1 = FileParser.parseFile(path1);
			double[][] initialVector = FileParser.parseVectorFile(path2, matrix1.getNumRows());
			PowerMethod powerMethod = new PowerMethod(matrix1, tol, initialVector);
			powerMethod.power_method();
	    }
	    in.close();
    }
}