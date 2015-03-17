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