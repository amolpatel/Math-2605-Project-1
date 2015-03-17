

public class Driver {

	public static void main(String[] args) {
		double[][] array = new double[3][3];
		int count = 1;
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 3; j++)
			{
				array[i][j] = count * 2;
				count++;;
			}
		// Setting array[1][1] to 100 so the matrix isn't a singular matrix
		array[1][1] = 100;
		Matrix matrix = new Matrix(array);

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
	}
}
