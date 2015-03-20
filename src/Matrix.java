

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
        if (!haveEqualDimensions(m.matrix))
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
        if (!haveEqualDimensions(m.matrix))
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
        if (!checkDims(m.matrix))
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
            for(int j = 0; j < numCols; j++)
                result[i][j] = 0;
            result[i][i] = matrix[i][i];
        }
        return (new Matrix(result));
    }

    /**
     * @return absolute value of matrix
     */
    public Matrix absoluteValue(){
        double[][] result = new double[numRows][numCols];
        for(int i = 0; i < numRows; i++)
            for(int j = 0; j < numCols; j++)
                result[i][j] = Math.abs(matrix[i][j]);
        return (new Matrix(result));

    }

    /**
     * Performs LU decomposition of matrix
     * Worked out examples of how to calculate LU decomposition are at the following link
     * https://files.t-square.gatech.edu/access/content/group/gtc-1e04-e0d7-51e4-a454-b328339e73da/examples_LU_Householder.pdf
     * @return matrices l and u
     */
    public Matrix[] lu_fact() {
        // Can LU factorization happen on non-square matrices? Account for this at some point.
        double[][] l = getIdentityArray(numRows);
        // Copy original matrix into u for row reduction and to avoid changing original matrix
        double[][] u = new double[numRows][numCols];
        for (int i = 0; i < numRows; i++)
            System.arraycopy(matrix[i], 0, u[i], 0, numCols);

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
     * Performs a QR factorization of matrix using Givens Rotations where
     * Q = (G_1)^t * (G_2)^t * (G_m)^t where ^t indicates a transpose of a matrix
     * R = G_m * ... * G_2 * G_1 * A
     * Worked out example of how to perform this calculation is on page 9 of the following link
     * https://files.t-square.gatech.edu/access/content/group/gtc-1e04-e0d7-51e4-a454-b328339e73da/2605classnotesWeek6_b.pdf
     * @return matrices Q and R
     */
    public Matrix[] qr_fact_givens() {
        // Copy original matrix into r for row reduction and to avoid changing original matrix
        Matrix q = null;
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
                    Matrix g = new Matrix(getIdentityArray(numRows));
                    g.matrix[i][i] = cosTheta;
                    g.matrix[j][j] = cosTheta;
                    g.matrix[i][j] = sinTheta;
                    g.matrix[j][i] = -sinTheta;
                    r = g.multiply(r);
                    // Sets q equal to G_1 the first time the loop runs, otherwise calculates q as it should
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
        Matrix orig = new Matrix(matrix);
        for(int j = 0; j < numCols; j++){
            for(int i = j + 1; i < numRows; i++){
                if(((double)Math.round(r.matrix[i][j] * 100000) / 100000) != 0){
                    Matrix x = getX(r,i-1,j);
                    Matrix v = getV(x);
                    Matrix u = getU(v);
                    Matrix Ut = u.transpose();
                    Matrix UUt = u.multiply(Ut);
                    Matrix twoUUt = UUt.multiply(2);
                    Matrix I = getIdentityMatrix(twoUUt.numRows);
                    Matrix h = I.subtract(twoUUt);

                    if(!haveEqualDimensions(h,r)){
                        h = padH(h,r);
                    }

                    if(q == null){
                        q = h;
                        r = h.multiply(r);
                    }else {
                        q = q.multiply(h);
                        r = h.multiply(r);
                    }

                }
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

        for(int i = 0; i < m.numRows; i++){
            result += Math.pow(m.matrix[i][0],2);
        }
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
        for(int i = 1; i < m.numRows; i++){
            e.matrix[i][0] = 0.00;
        }
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
        return v.multiply(1/getNorm(v));
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
        for(int i = rowDiff; i < numRows; i++) {
            for (int j = rowDiff; j < numCols; j++) {
                returnMatrix.matrix[i][j] = h.matrix[i - rowDiff][j - rowDiff];
            }
        }
        return returnMatrix;
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
     * A Hilbert matrix is a square matrix whose entries are defined as H_ij = 1 / (i + j - 1)
     * @param dim dimension of Hilbert matrix
     * @return Hilbert matrix represented a 2D array
     */
    public static double[][] getHilbertArray(int dim) {
        double[][] array = new double[dim][dim];
        for (int i = 0; i < dim; i++)
            for (int j = 0; j < dim; j++)
                // Simply adding 1 since arrays are 0 indexed
                array[i][j] = (double) 1 / (i + j + 1);
        return array;
    }

    /**
     * An identity matrix is defined as a matrix with n rows and columns with a diagonal of 1s
     * Method returns a two dimensional representation of the array
     * @param dim dimensions of array
     * @return identity array
     */
    public static double[][] getIdentityArray(int dim) {
        double[][] array = new double[dim][dim];
        for (int i = 0; i < dim; i++)
            array[i][i] = 1;
        return array;
    }

    /**
     * An identity matrix is defined as a matrix with n rows and columns with a diagonal of 1s
     * Method returns a two dimensional representation of the array
     * @param dim dimensions of array
     * @return identity array
     */
    public static Matrix getIdentityMatrix(int dim) {
        Matrix identity = new Matrix(new double[dim][dim]);
        for (int i = 0; i < dim; i++)
            identity.matrix[i][i] = 1;
        return identity;
    }

    /**
     * @return true if two matrices have an equal number of rows and columns
     */
    private boolean haveEqualDimensions(double[][] array) {
        return ((this.numRows == array.length) && (this.numCols == array[0].length));
    }

    /**
     * @return true if number of rows in A is equal to number of columns in B
     */
    private boolean checkDims(double[][] array) {
        return (this.numCols == array.length);
    }

    /**
     * @return true if matrices have equal dimensions
     */
    public boolean haveEqualDimensions(Matrix m, Matrix n){
        return((m.numCols == n.numCols) && (m.numRows == n.numRows));
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
}