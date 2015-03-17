
public class Driver {

	public static void main(String[] args) {
		double[][] array = new double[3][3];
		int count = 0;
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 3; j++)
			{
				array[i][j] = count;
				count++;
			}

		Matrix matrix = new Matrix(array);
		System.out.println("Original matrix: \n" + matrix);
		System.out.println("Matrix added to itself: \n" + matrix.add(matrix));
		System.out.println("Matrix subtracted from itself: \n" + matrix.subtract(matrix));
		System.out.println("Matrix multiplied by itself: \n" + matrix.multiply(matrix));
		System.out.println("Matrix transposed: \n" + matrix.transpose());
		// Dot product just calls multiply method, so not bothering to test it
	}
}
