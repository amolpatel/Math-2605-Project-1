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
     * @return diagonal of matrix
     */
    public Matrix diagonalize(){
		Matrix result = new Matrix(numRows, numCols);
        for(int i = 0; i < numRows; i++)
			result.matrix[i][i] = matrix[i][i];
        return result;
    }

    /**
     * @return matrix containing absolute values of each entry in original matrix
     */
    public Matrix absoluteValue() {
		Matrix result = new Matrix(numRows, numCols);
        for(int i = 0; i < numRows; i++)
			for(int j = 0; j < numCols; j++)
				result.matrix[i][j] = Math.abs(matrix[i][j]);
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
		Matrix y = forwardSubstitution(list[0], b);
		// Solve Ux = y using backwards substitution since U is a upper triangular matrix
		Matrix x = backwardSubstitution(list[1], y);
		return x;
	}

	public Matrix solve_qr_b_givens(Matrix b) {
		Matrix[] list = this.qr_fact_givens();
		Matrix x = backwardSubstitution(list[1], list[0].transpose().multiply(b));
		return x;
	}

	public Matrix solve_qr_b_househ(Matrix b) {
		Matrix[] list = this.qr_fact_househ();
		Matrix x = backwardSubstitution(list[1], list[0].transpose().multiply(b));
		return x;
	}

	/**
	 * Solves equation Ax = b
	 * @param a upper triangular, n by n matrix
	 * @param b n by 1 vector
	 * @return matrix solution of system
	 */
	private Matrix forwardSubstitution(Matrix a, Matrix b) {
		Matrix x = new Matrix(a.numCols, 1);
		double total;
		for (int i = 0; i < a.numCols; i++)
		{
			total = 0;
			for (int j = 0; j < i; j++)
				total += a.matrix[i][j] * x.matrix[j][0];
			double x_n = (b.matrix[i][0] - total) / a.matrix[i][i];
			x.matrix[i][0] = x_n;
		}
		return x;
	}

	private Matrix backwardSubstitution(Matrix a, Matrix b) {
		Matrix x = new Matrix(a.numCols, 1);
		double total;
		for (int i = a.numCols - 1; i >= 0; i--)
		{
			total = 0;
			for (int j = a.numCols - 1; j > i; j--)
				total += a.matrix[i][j] * x.matrix[j][0];
			double x_n = (b.matrix[i][0] - total) / a.matrix[i][i];
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
        Matrix r = new Matrix(matrix);
        for(int j = 0; j < numCols; j++)
			for(int i = j + 1; i < numRows; i++)
				if(((double)Math.round(r.matrix[i][j] * 100000) / 100000) != 0) {
                    Matrix x = getX(r, i - 1, j);
                    Matrix v = getV(x);
                    Matrix u = getU(v);
                    Matrix Ut = u.transpose();
                    Matrix UUt = u.multiply(Ut);
                    Matrix twoUUt = UUt.multiply(2);
                    Matrix I = getIdentityMatrix(twoUUt.numRows);
                    Matrix h = I.subtract(twoUUt);

                    if(!h.haveEqualDimensions(r))
						h = padH(h,r);

                    if (q == null) {
                        q = h;
                        r = h.multiply(r);
                    } else {
                        q = q.multiply(h);
                        r = h.multiply(r);
                    }

                }
        Matrix[] list = new Matrix[2];
        list[0] = q;
        list[1] = r;
        return list;
    }

    /**
     * Finds a norm of a vector given as array
     * @return norm of vector
     */
    public double getNorm(Matrix m){
        double result = 0;

        for(int i = 0; i < m.numRows; i++)
			result += Math.pow(m.matrix[i][0], 2);
        result = Math.sqrt(result);
        return result;
    }

    /**
     * Returns correct size of e vector multiplied by norm of x in HouseHolder
     * @return vector e
     */
    public Matrix getE(Matrix m){
        Matrix e = new Matrix(new double[m.numRows][1]);
        e.matrix[0][0] = getNorm(m);
        return e;
    }

    /**
     * Returns v vector in HouseHolder
     * v = x + e * norm(x)
     * @return vector e
     */
    public Matrix getV(Matrix x){
        Matrix v;
        Matrix e = getE(x);
        v = e.add(x);
        return v;
    }

    /**
     * Returns u bar vector in Householder
     */
    public Matrix getU(Matrix v){
        return v.multiply(1 / getNorm(v));
    }

    /**
     * Finds x bar vector used in Householder
     * @param Matrix m, index i, index j
     * @return double array
     */
    public Matrix getX(Matrix m, int row, int col){
        Matrix x = new Matrix(new double[m.numRows-row][1]);
        int tempRow = row;
        for(int i = row; i < numRows; i++){
            x.matrix[i-tempRow][0] = m.matrix[row][col];
            row++;
        }
        return x;
    }

    /**
     * Pad h vector with identity
     *
     */
    public Matrix padH(Matrix h, Matrix r){
        Matrix returnMatrix;
        returnMatrix = getIdentityMatrix(r.numRows);
        int rowDiff = r.numCols - h.numCols;
        for(int i = rowDiff; i < numRows; i++)
			for(int j = rowDiff; j < numCols; j++)
				returnMatrix.matrix[i][j] = h.matrix[i - rowDiff][j - rowDiff];
        return returnMatrix;
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
        Matrix A = new Matrix(numRows, numRows);
        for(int row = 0; row < numRows; row++){
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
        Matrix A1 = new Matrix(numRows, numRows);
        for(int row = 0; row < numRows; row++){
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
    public Matrix getXStream(){
        Random rand = new Random();
        Matrix X = new Matrix(rand.nextInt(5)+4,1);
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
    public Matrix getYStream(){
        Random rand = new Random();
        Matrix Y = new Matrix(rand.nextInt(5)+4,1);
        for(int i = 0; i < Y.numRows; i++){
            Y.matrix[i][0] = rand.nextInt(2);
        }
        return Y;
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
    public Matrix gauss_seidel(Matrix input, Matrix y, Matrix initialX){
        Matrix x_k = initialX;
        Matrix b = y;
        Matrix x_k_1 = null;
        Matrix A = input;
        Matrix L = A.lower();
        Matrix U = A.upper();
        Matrix D = A.diagonal();

        for(int i = 0; i < 100; i++){
            Matrix LHS = L.add(D);
            Matrix negativeU = U.multiply(-1);
            Matrix RHS = negativeU.multiply(x_k);
            RHS = RHS.add(b);
            x_k_1 = forwardSubstitution(LHS,RHS);
            x_k = x_k_1;
        }
        return x_k_1;
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
            for(int j = numCols-1; j <= i+1; j++){
                result.matrix[i][j] = temp.matrix[i][j];
            }
        }
        return temp;
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