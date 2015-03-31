package Part1;
import java.util.Random;
/**
 * Matrix class can be used to represent a matrix or vector as
 * a two dimensional array and supports various operations
 */
public class Matrix {
	private double[][] matrix;
	private final int numRows;
	private final int numCols;

	public Matrix(int rows, int cols) {
		matrix = new double[rows][cols];
		numRows = rows;
		numCols = cols;
	}

	public Matrix(double[][] array) {
		this.matrix = array;
		numRows = matrix.length;
		numCols = matrix[0].length;
	}

    public int getRows(){
        return this.numRows;
    }

    /**
     * @return matrix result of adding two matrices together
     */
    public Matrix add(Matrix m) {
        if (!haveEqualDimensions(m))
            throw new IllegalArgumentException("Matrices cannot be added.");

        Matrix result = new Matrix(numRows, numCols);
        for (int i = 0; i < numRows; i++)
            for (int j = 0; j < numCols; j++)
                result.matrix[i][j] = this.matrix[i][j] + m.matrix[i][j];
        return result;
    }

    /**
     * @return matrix result of subtracting two matrices
     */
    public Matrix subtract(Matrix m) {
        if (!haveEqualDimensions(m))
            throw new IllegalArgumentException("Matrices cannot be subtracted.");

        Matrix result = new Matrix(numRows, numCols);
        for (int i = 0; i < numRows; i++)
            for (int j = 0; j < numCols; j++)
                result.matrix[i][j] = this.matrix[i][j] - m.matrix[i][j];
        return result;
    }

    /**
     * @param m matrix to multiply by
     * @return matrix result of multiplying two matrices
     */
    public Matrix multiply(Matrix m) {
        if (!checkDims(m))
            throw new IllegalArgumentException("Matrices cannot be multiplied.");

        Matrix result = new Matrix(numRows, m.numCols);
        for (int i = 0; i < numRows; i++)
            for (int j = 0; j < m.numCols; j++)
                for (int k = 0; k < numCols; k++)
                    result.matrix[i][j] += matrix[i][k] * m.matrix[k][j];
        return result;
    }

    /**
     * @param scalar number to multiply every entry in matrix by
     * @return matrix result of scalar times matrix
     */
    public Matrix multiply(double scalar) {
        Matrix result = new Matrix(numRows, numCols);
        for (int i = 0; i < numRows; i++)
            for (int j = 0; j < numCols; j++)
                result.matrix[i][j] = matrix[i][j] * scalar;
        return result;
    }

	/**
	 * @return dot product of two vectors
	 */
	public Matrix dotProduct(Matrix m) { return multiply(m); }

	/**
	 * @return transpose of matrix
	 */
	public Matrix transpose() {
		Matrix result = new Matrix(numCols, numRows);
		for (int i = 0; i < numRows; i++)
			for (int j = 0; j < numCols; j++)
				result.matrix[j][i] = matrix[i][j];
		return result;
	}

	/**
	 * Performs LU decomposition of matrix
	 * Worked out examples of how to calculate LU decomposition are at the following link
	 * https://files.t-square.gatech.edu/access/content/group/gtc-1e04-e0d7-51e4-a454-b328339e73da/examples_LU_Householder.pdf
	 * @return matrices l and u
	 */
	public Matrix[] lu_fact() {
		// Can LU factorization happen on non-square matrices? Account for this at some point.
		Matrix l = getIdentityMatrix(numRows);
		// Copy original matrix into u for row reduction and to avoid changing original matrix
		Matrix u = new Matrix(numRows, numCols);
		for (int i = 0; i < numRows; i++)
			System.arraycopy(matrix[i], 0, u.matrix[i], 0, numCols);

		for (int j = 0; j < numCols - 1; j++)
			for (int i = j + 1; i < numRows; i++)
				if (u.matrix[i][j] != 0)
				{
					double scalar = u.matrix[i][j]/u.matrix[j][j];
					// Putting scalar in l because this is equivalent to the inverse of the matrix G_n at each step
					l.matrix[i][j] = scalar;
					u.rowOperation(j, i, scalar);
				}
		Matrix[] list = new Matrix[2];
		list[0] = l;
		list[1] = u;
		return list;
	}

	/**
	 * Solves Ax = b where A = LU
	 * @param b matrix used when solving for x
	 * @return matrix solution of system
	 */
	public Matrix solve_lu_b(Matrix b) {
		Matrix[] list = this.lu_fact();
		// Solve Ly = b using forward substitution since L is an lower triangular matrix
		Matrix y = list[0].forwardSubstitution(b);
		// Solve Ux = y using backwards substitution since U is a upper triangular matrix
		Matrix x = list[1].backwardSubstitution(y);
		return x;
	}

	/**
	 * Solves system Ax = b using Givens rotations where A is a n by n matrix and b is a n by 1 matrix
	 * @param b vector to solve equation with
	 * @return solution vector to system
	 */
	public Matrix solve_qr_b_givens(Matrix b) {
		Matrix[] list = this.qr_fact_givens();
		Matrix x = list[1].backwardSubstitution(list[0].transpose().multiply(b));
		return x;
	}

	public Matrix solve_qr_b_househ(Matrix b) {
		Matrix[] list = this.qr_fact_househ();
		Matrix x = list[1].backwardSubstitution(list[0].transpose().multiply(b));
		return x;
	}

	/**
	 * Solves equation Ax = b using forward substitution
	 * @param a upper triangular, n by n matrix
	 * @param b n by 1 vector
	 * @return matrix solution of system
	 */
	private Matrix forwardSubstitution(Matrix b) {
		Matrix x = new Matrix(numCols, 1);
		double total;
		for (int i = 0; i < numCols; i++)
		{
			total = 0;
			for (int j = 0; j < i; j++)
				total += matrix[i][j] * x.matrix[j][0];
			double x_n = (b.matrix[i][0] - total) / matrix[i][i];
			x.matrix[i][0] = x_n;
		}
		return x;
	}

	/**
	 * Solves equation Ax = b using backward substitution
	 * @param a lower triangular, n by n matrix
	 * @param b n by 1 vector
	 * @return matrix solution of system
	 */
	private Matrix backwardSubstitution(Matrix b) {
		Matrix x = new Matrix(numCols, 1);
		double total;
		for (int i = numCols - 1; i >= 0; i--)
		{
			total = 0;
			for (int j = numCols - 1; j > i; j--)
				total += matrix[i][j] * x.matrix[j][0];
			double x_n = (b.matrix[i][0] - total) / matrix[i][i];
			x.matrix[i][0] = x_n;
		}
		return x;
	}

	/**
	 * Performs a QR factorization of a square matrix using Givens Rotations where
	 * Q = (G_1)^t * (G_2)^t * (G_m)^t where ^t indicates a transpose of a matrix
	 * R = G_m * ... * G_2 * G_1 * A
	 * Worked out example of how to perform this calculation is on page 9 of the following link
	 * https://files.t-square.gatech.edu/access/content/group/gtc-1e04-e0d7-51e4-a454-b328339e73da/2605classnotesWeek6_b.pdf
	 * @return matrices Q and R
	 */
	public Matrix[] qr_fact_givens() {
		// Can QR factorization using Givens be done on non-square matrices?
		Matrix q = null;
		// Copy original matrix into r for row reduction and to avoid changing original matrix
		Matrix r = new Matrix(matrix);
		for (int j = 0; j < numCols; j++)
			for (int i = j + 1; i < numRows; i++)
				if (r.matrix[i][j] != 0)
				{
					double x = r.matrix[j][j];
					double y = r.matrix[i][j];
					// cos theta = x/sqrt(x^2 + y^2)
					double cosTheta = x / (Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2)));
					// sin theta = -y/sqrt(x^2 + y^2)
					double sinTheta = -y / (Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2)));
					Matrix g = getIdentityMatrix(numRows);
					g.matrix[i][i] = cosTheta;
					g.matrix[j][j] = cosTheta;
					g.matrix[i][j] = sinTheta;
					g.matrix[j][i] = -sinTheta;
					r = g.multiply(r);
					// Sets q equal to "G_1" the first time the loop runs, otherwise calculates q as it should
					if (q == null)
						q = g.transpose();
					else
						q = q.multiply(g.transpose());
				}
		Matrix[] list = new Matrix[2];
		list[0] = q;
		list[1] = r;
		return list;
	}

    /**
     * Performs QR factorization of a square matrix using HouseHolder Reflections where
     * Q = (H_1 * H_2 * ... H_m-1)
     * R = ((H_m-1 * ... H_3 * H_2 * H_1) * A)
     * Worked out example of how to perform this calculation is on page 9 of the following link
     * https://files.t-square.gatech.edu/access/content/group/gtc-1e04-e0d7-51e4-a454-b328339e73da/2605classnotesWeek6_b.pdf
     * @return matrices Q and R
     */
    public Matrix[] qr_fact_househ(){
        Matrix q = null;
        // Copy original matrix into r for row reduction and to avoid changing original matrix
        Matrix r = new Matrix(numRows, numCols);
        for (int i = 0; i < numRows; i++)
            System.arraycopy(matrix[i], 0, r.matrix[i], 0, numCols);

        for (int j = 0; j < numCols; j++)
            for (int i = j + 1; i < numRows; i++)
                if (Math.abs(r.matrix[i][j]) > 1E-15)
                {
                    Matrix x_n = new Matrix(numRows - j, 1);
                    for (int k = 0, count = 0; j + k < r.numRows; k++, count++)
                        x_n.matrix[count][0] = r.matrix[j + k][j];
                    double x_n_norm = x_n.getNorm();
                    x_n.matrix[0][0] -= x_n_norm;
                    x_n_norm = x_n.getNorm();
                    Matrix u_n = x_n.multiply(1 / x_n_norm);
                    Matrix identity = getIdentityMatrix(u_n.numRows);
                    Matrix rightStuff = u_n.multiply(u_n.transpose()).multiply(2);
                    Matrix result = identity.subtract(rightStuff);
                    Matrix padded = padH(result, getIdentityMatrix(r.numRows));
                    r = padded.multiply(r);
                    // Sets q equal to "H_n" the first time the loop runs, otherwise calculates q as it should
                    if (q == null)
                        q = padded;
                    else
                        q = q.multiply(padded);
                } else
                    r.matrix[i][j] = 0;

        Matrix[] list = new Matrix[2];
        list[0] = q;
        list[1] = r;
        return list;
    }

    /**
     * The norm of a vector is defined as the square root of the result of adding each entry in the vector squared
     * For example, the vector [x_1, x_2, x_3] has a norm defined as sqrt((x_1)^2 + (x_2)^2 + (x_3)^2)
     * @return norm of vector
     */
    public double getNorm() {
    	if (this.numCols != 1)
    		throw new IllegalArgumentException("Matrix must be an n by 1 matrix.");
    	double total = 0;
    	for (int i = 0; i < numRows; i++)
			total += Math.pow(matrix[i][0], 2);
    	return Math.sqrt(total);
    }


    /**
     * For the purpose of this project, the error is described as the matrix entry with the highest absolute value
     * @return max norm
     */
    public double getError() {
        // Initialize variable with some value in matrix
        double max = Math.abs(matrix[0][0]);
        for (int i = 0; i < numRows; i++)
            for (int j = 0; j < numCols; j++)
                if (Math.abs(matrix[i][j]) > max)
                    max = Math.abs(matrix[i][j]);
        return max;
    }

    /**
     * Places matrix passed as parameter inside an identity matrix; used for constructing H
     * matrices in Householder reflections. The matrix will be placed in the lower right hand
     * corner of the identity matrix
     * @return identity matrix containing vector h
     */
    private Matrix padH(Matrix innerMatrix, Matrix identity) {
        int dimDiff = identity.numCols - innerMatrix.numCols;
        for(int i = dimDiff; i < identity.numRows; i++)
            for (int j = dimDiff; j < identity.numCols; j++)
                identity.matrix[i][j] = innerMatrix.matrix[i - dimDiff][j - dimDiff];
        return identity;
    }

    /**
     * Returns a n by 1 b vector in form b = (0.1 ^ (n/3)) * (1, 1,... , 1)^t
     * @param dim number of rows
     * @return b vector containing entries as described above
     */
    public static Matrix getBVector(int dim) {
        Matrix b = new Matrix(dim, 1);
        double entry = Math.pow(.1, (double) dim / 3);
        for (int i = 0; i < b.numRows; i++)
            b.matrix[i][0] = entry;
        return b;
    }

    /**
     * A Hilbert matrix is a square matrix whose entries are defined as H_ij = 1 / (i + j - 1)
     * @param dim dimension of Hilbert matrix
     * @return Hilbert matrix represented a 2D array
     */
    public static Matrix getHilbertMatrix(int dim) {
        Matrix hilbert = new Matrix(dim, dim);
        for (int i = 0; i < dim; i++)
            for (int j = 0; j < dim; j++)
                // Simply adding 1 since arrays are 0 indexed
                hilbert.matrix[i][j] = (double) 1 / (i + j + 1);
        return hilbert;
    }

	/**
	 * An identity matrix is defined as a matrix with n rows and columns with a diagonal of 1s
	 * Method returns a two dimensional representation of the array
	 * @param dim dimensions of array
	 * @return identity array
	 */
	public static Matrix getIdentityMatrix(int dim) {
		Matrix identity = new Matrix(dim, dim);
		for (int i = 0; i < dim; i++)
			identity.matrix[i][i] = 1;
		return identity;
	}

    /**
     * Generate A0
     */
    public Matrix getA0(){
        Matrix A = new Matrix(this.numRows, this.numRows);
        for(int row = 0; row < this.numRows; row++){
            for(int col = 0; col < A.numCols; col++){
                if(row >= col){
                    if(row == col || row == col + 2 || row == col + 3){
                        A.matrix[row][col] = 1.00;
                    }
                }
                else{
                    A.matrix[row][col] = 0.00;
                }
            }
        }
        return A;
    }

    /**
     * Generate A1
     */
    public Matrix getA1(){
        Matrix A1 = new Matrix(this.numRows, this.numRows);
        for(int row = 0; row < this.numRows; row++){
            for(int col = 0; col < A1.numCols; col++){
                if(row >= col){
                    if(row == col || row == col + 1 || row == col + 3){
                        A1.matrix[row][col] = 1.00;
                    }
                }
                else{
                    A1.matrix[row][col] = 0.00;
                }
            }
        }
        return A1;
    }

    /**
     * Generate random X stream
     */
    public static Matrix getXStream(int length){
        Random rand = new Random();
        Matrix X = new Matrix(length,1);
        for(int i = 0; i < X.numRows; i++){
            X.matrix[i][0] = rand.nextInt(2);
            if(i == X.numRows - 1 || i == X.numRows - 2 || i == X.numRows - 3){
                X.matrix[i][0] = 0;
            }
        }
        return X;
    }

    /**
     * Generate random Y stream
     */
    public static Matrix getYStream(int length){
        Random rand = new Random();
        Matrix Y = new Matrix(length,1);
        for(int i = 0; i < Y.numRows; i++){
            Y.matrix[i][0] = rand.nextInt(2);
        }
        return Y;
    }

    /**
     * Combine Y0 and Y1
     */
    public static String[][] combineY(Matrix Y0, Matrix Y1){
        String[][] result = new String[Y0.numRows][1];
        int tempInt, tempInt2;
        String temp,temp2, fullString;
        for(int i = 0; i < Y0.numRows; i++){
            tempInt = (int) Y0.matrix[i][0];
            tempInt2 = (int) Y1.matrix[i][0];
            temp = Integer.toString(tempInt);
            temp2 = Integer.toString(tempInt2);
            fullString = temp+temp2;
            result[i][0] = (fullString);
        }
        return result;
    }

    /**
     * Generate initial X0 stream
     * @return vector with all 0's length as A and b
     */
    public Matrix generateInitial(){
        Matrix result = new Matrix(this.getRows(),1);
        for(int i = 0; i < this.getRows()-1; i++){
            result.matrix[i][0] = 0.0;
        }
        return result;
    }

    /**
     * Add 3 zero's to end of X stream
     */
    public Matrix addZeros(){
        Matrix result = new Matrix(this.numRows+3,this.numCols);
        for(int i = 0; i < numRows; i++){
            result.matrix[i][0] = this.matrix[i][0];
        }
        result.matrix[result.numRows - 3][0] = 0.00;
        result.matrix[result.numRows - 2][0] = 0.00;
        result.matrix[result.numRows - 1][0] = 0.00;
        return result;
    }


    /**
     * Modded matrix multiplication for Convolutional Code
     * @param m matrix to multiply by
     * @return matrix result of multiplying two matrices % 2
     */
    public Matrix multiplyMod(Matrix m) {
        if (!checkDims(m))
            throw new IllegalArgumentException("Matrices cannot be multiplied.");
        Matrix result = new Matrix(numRows, m.numCols);
        for (int i = 0; i < numRows; i++)
            for (int j = 0; j < m.numCols; j++)
                for (int k = 0; k < numCols; k++) {
                    result.matrix[i][j] += matrix[i][k] * m.matrix[k][j];
                    result.matrix[i][j] = result.matrix[i][j] % 2;
                }
        return result;
    }

    /**
     * Gauss-Seidel
     * @return
     */
    public Matrix gauss_seidel(Matrix y, Matrix initialX, float tol){
        boolean isBinary = false;
        boolean error;
        int iterations = 0;

        if(isBinary(this)){
            isBinary = true;
        }

        Matrix x_k = initialX;
        Matrix b = y;
        Matrix x_k_1 = initialX;
        Matrix A = this;
        Matrix L = A.lower();
        Matrix U = A.upper();
        Matrix D = A.diagonal();

        while(iterations <= 100){
            if(isBinary){
                Matrix LHS = L.add(D);
                Matrix negativeU = U;
                Matrix RHS = negativeU.multiply(x_k);
                RHS = RHS.add(b);
                x_k_1 = LHS.forwardSubstitution(RHS);
                error = checkError(x_k,x_k_1) < tol;
                if(error){
                    System.out.println("Method converges after "+iterations+" iteration(s).");
                    return (x_k_1.finalMod());
                }else{
                    x_k = x_k_1;
                }
                iterations++;
            }
            else{
                Matrix LHS = L.add(D);
                Matrix negativeU = U.multiply(-1);
                Matrix RHS = negativeU.multiply(x_k);
                RHS = RHS.add(b);
                x_k_1 = LHS.forwardSubstitution(RHS);
                error = checkError(x_k,x_k_1) < tol;
                if(error){
                    System.out.println("Method converges after "+iterations+" iteration(s).");
                    return (x_k_1);
                }else{
                    x_k = x_k_1;
                }
                iterations++;
            }
        }
        System.out.println("Method DOES NOT converge after "+iterations+" iteration(s).");
        return x_k_1;
    }

    /**
     * Gauss-Seidel
     * @return
     */
    public Matrix jacobi(Matrix y, Matrix initialX, float tol){
        boolean isBinary = false;
        boolean error;
        int iterations = 0;

        if(isBinary(this)){
            isBinary = true;
        }

        Matrix x_k = initialX;
        Matrix b = y;
        Matrix x_k_1 = initialX;
        Matrix A = this;
        Matrix L = A.lower();
        Matrix U = A.upper();
        Matrix D = A.diagonal();

        while(iterations <= 100){
            if(isBinary){
                Matrix LHS = D;
                Matrix L_U = L.add(U);
                Matrix RHS = L_U.multiply(x_k);
                RHS = RHS.add(b);
                x_k_1 = LHS.forwardSubstitution(RHS);
                error = checkError(x_k,x_k_1) < tol;
                if(error){
                    System.out.println("Method converges after "+iterations+" iteration(s).");
                    return (x_k_1.finalMod());
                }else{
                    x_k = x_k_1;
                }
                iterations++;
            }
            else{
                Matrix LHS = D;
                Matrix L_U = L.add(U);
                Matrix RHS = L_U.multiply(-1);
                RHS = RHS.multiply(x_k);
                RHS = RHS.add(b);
                x_k_1 = LHS.forwardSubstitution(RHS);
                error = checkError(x_k,x_k_1) < tol;;
                if(error){
                    System.out.println("Method converges after "+iterations+" iteration(s).");
                    return (x_k_1);
                }else{
                    x_k = x_k_1;
                }
                iterations++;
            }
        }
        System.out.println("Method DOES NOT converge after "+iterations+" iteration(s).");
        return x_k_1;
    }

    /**
     * Check error in GS or Jacobi
     */
    public float checkError(Matrix x_k, Matrix x_k_1){
        return (getFloatNorm(x_k.subtract(x_k_1)));
    }

    /**
     * Finds a norm of a vector given as array
     * @return norm of vector
     */
    public float getFloatNorm(Matrix m){
        float result = 0;

        for(int i = 0; i < m.numRows; i++) {
            result += Math.pow(m.matrix[i][0], 2);
        }
        result = (float) Math.sqrt(result);
        return result;
    }

    /**
     * Mod after "x" iterations if Binary Matrix
     */
    public Matrix finalMod(){
        Matrix result = this;
        for(int i = 0; i < numRows; i++){
            result.matrix[i][0] = (Math.abs(this.matrix[i][0])) % 2;
        }
        return result;
    }


    /**
     * Check if Matrix is binary matrix
     */
    public static boolean isBinary(Matrix m){
        boolean flag = true;
        for(int i = 0; i < m.getRows(); i++){
            for(int j = 0; j < m.getRows(); j++){
                if(m.matrix[i][j] != 0.00 && m.matrix[i][j] != 1.00){
                    return false;
                }
            }
        }
        return flag;
    }

    /**
     * Get lower triangular of Matrix m
     */
    public Matrix lower(){
        Matrix result = new Matrix(numRows,numCols);
        Matrix temp = this;
        for(int i = 1; i < numRows; i++){
            for(int j = 0; j <= i - 1; j++){
                result.matrix[i][j] = temp.matrix[i][j];
            }
        }
        return result;
    }

    /**
     * Get upper triangular of Matrix m
     */
    public Matrix upper(){
        Matrix result = new Matrix(numRows,numCols);
        Matrix temp = this;
        for(int i = 0; i < numRows-1; i++){
            for(int j = numCols-1; j >= i+1; j--){
                result.matrix[i][j] = temp.matrix[i][j];
            }
        }
        return result;
    }

    /**
     * Get diagonal of Matrix m
     */
    public Matrix diagonal(){
        Matrix result = new Matrix(numRows,numCols);
        Matrix temp = this;
        for(int i = 0; i < numRows; i++){
            for(int j = 0; j < numRows; j++){
                if(i == j){
                    result.matrix[i][j] = temp.matrix[i][j];
                }
                else{
                    result.matrix[i][j] = 0;
                }
            }
        }
        return result;
    }

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < numRows; i++)
		{
			for (int j = 0; j < numCols; j++)
				sb.append(matrix[i][j] + "\t\t\t");

			sb.append("\n");
		}
		return sb.toString();
	}

	/**
	 * Performs row operation on array based on parameters
	 * @param array array on which row operation is being performed
	 * @param pivot pivot row of array
	 * @param row row whose values are being changed
	 * @param scalar number to multiply the pivot row by and add to specified row
	 * @return result of row operation
	 */
	private void rowOperation(int pivot, int row, double scalar) {
		for (int j = 0; j < matrix[0].length; j++)
			matrix[row][j] += (matrix[pivot][j] * scalar * -1);
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
}