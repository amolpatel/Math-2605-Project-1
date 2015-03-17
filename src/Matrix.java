import java.util.Arrays;

/**
 * Matrix class can be used to represent a matrix or vector as
 * a two dimensional array and supports various operations
 */
public class Matrix {
	private double[][] matrix;
	private final int numRows;
	private final int numCols;

	public Matrix(double[][] array) {
		this.matrix = array;
		numRows = matrix.length;
		numCols = matrix[0].length;
	}

	/**
	 * @return matrix result of adding two matrices together
	 */
	public Matrix add(Matrix m) {
		if (!haveEqualDimensions(m))
			throw new IllegalArgumentException("Matrices cannot be added.");

		double[][] result = new double[numRows][numCols];
		for (int i = 0; i < numRows; i++)
			for (int j = 0; j < numCols; j++)
				result[i][j] = this.matrix[i][j] + m.matrix[i][j];
		return (new Matrix(result));
	}

	/**
	 * @return matrix result of subtracting two matrices
	 */
	public Matrix subtract(Matrix m) {
		if (!haveEqualDimensions(m))
			throw new IllegalArgumentException("Matrices cannot be subtracted.");

		double[][] result = new double[numRows][numCols];
		for (int i = 0; i < numRows; i++)
			for (int j = 0; j < numCols; j++)
				result[i][j] = this.matrix[i][j] - m.matrix[i][j];
		return (new Matrix(result));
	}

	/**
	 * @param m matrix to multiply by
	 * @return matrix result of multiplying two matrices
	 */
	public Matrix multiply(Matrix m) {
		if (!checkDims(m))
			throw new IllegalArgumentException("Matrices cannot be multiplied.");

		double[][] result = new double[numRows][m.numCols];
		for (int i = 0; i < numRows; i++)
			for (int j = 0; j < m.numCols; j++)
				for (int k = 0; k < numCols; k++)
					result[i][j] += matrix[i][k] * m.matrix[k][j];
		return (new Matrix(result));
	}

	/**
	 * @param scalar number to multiply every entry in matrix by
	 * @return matrix result of scalar times matrix
	 */
	public Matrix multiply(double scalar) {
		double[][] result = new double[numRows][numCols];
		for (int i = 0; i < numRows; i++)
			for (int j = 0; j < numCols; j++)
				result[i][j] = matrix[i][j] * scalar;
		return (new Matrix(result));
	}

	/**
	 * @return dot product of two vectors
	 */
	public Matrix dotProduct(Matrix m) { return multiply(m); }

	/**
	 * @return transpose of matrix
	 */
	public Matrix transpose() {
		double[][] result = new double[numCols][numRows];
		for (int i = 0; i < numRows; i++)
			for (int j = 0; j < numCols; j++)
				result[j][i] = matrix[i][j];
		return (new Matrix(result));
	}

    /**
     * @return diagonal of matrix
     */
    public Matrix diagonalize(){
        double[][] result = new double[numRows][numCols];
        for(int i = 0; i < numRows; i++){
            for(int j = 0; j < numCols; j++){
                result[i][j] = 0;
            }
            result[i][i] = matrix[i][i];
        }
        return (new Matrix(result));
    }

    /**
     * @return absolute value of matrix
     */
    public Matrix absoluteValue(){
        double[][] result = new double[numRows][numCols];
        for(int i = 0; i < numRows; i++){
            for(int j = 0; j < numCols; j++){
                result[i][j] = Math.abs(matrix[i][j]);
            }
        }
        return (new Matrix(result));

    }

	/**
	 * Performs LU decomposition of matrix
	 * Worked out examples of how to calculate LU decomposition are at the following link
	 * https://files.t-square.gatech.edu/access/content/group/gtc-1e04-e0d7-51e4-a454-b328339e73da/examples_LU_Householder.pdf
	 * @return matrices l and u in array
	 */
	public Matrix[] lu_fact() {
		// Can LU factorization happen on non-square matrices? Account for this at some point.
		double[][] l = getIdentityArray(numRows);
		// Copy original matrix into u for row reduction
		double[][] u = new double[numRows][numCols];
		for (int i = 0; i < numRows; i++)
			u[i] = Arrays.copyOf(matrix[i], numCols);

		for (int j = 0; j < numCols - 1; j++)
			for (int i = j + 1; i < numRows; i++)
				if (u[i][j] != 0)
				{
					double scalar = u[i][j]/u[j][j];
					// Putting scalar in l because this is equivalent to the inverse of the matrix G_n at each step
					l[i][j] = scalar;
					u = rowOperation(u, j, i, scalar);
				}
		Matrix[] list = new Matrix[2];
		list[0] = new Matrix(l);
		list[1] = new Matrix(u);
		return list;
	}

	/**
	 * Performs row operation on array based on parameters
	 * @param array array on which row operation is being performed
	 * @param pivot pivot row of array
	 * @param row row whose values are being changed
	 * @param scalar number to multiply the pivot row by and add to specificed row
	 * @return result of row operation
	 */
	private static double[][] rowOperation(double[][] array, int pivot, int row, double scalar) {
		for (int j = 0; j < array[0].length; j++)
			array[row][j] += (array[pivot][j] * scalar * -1);
		return array;
	}

	/**
	 * An identity matrix is defined as a matrix with n rows and columns with a diagonal of 1s
	 * Method returns a two dimensional representation of the array
	 * @param dim dimensions of array
	 * @return identity array
	 */
	public double[][] getIdentityArray(int dim) {
		double[][] array = new double[dim][dim];
		for (int i = 0; i < dim; i++)
			for (int j = 0; j < dim; j++)
				if (i == j)
					array[i][j] = 1;
		return array;
	}

	/**
	 * @return true if two matrices have an equal number of rows and columns
	 */
	private boolean haveEqualDimensions(Matrix m) {
		return ((this.numRows == m.numRows) && (this.numCols == m.numCols));
	}

	/**
	 * @return true if number of rows in A is equal to number of columns in B
	 */
	private boolean checkDims(Matrix m) {
		return (this.numCols == m.numRows);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < numRows; i++)
		{
			for (int j = 0; j < numCols; j++)
				sb.append(matrix[i][j] + "\t");

			sb.append("\n");
		}
		return sb.toString();
	}
}