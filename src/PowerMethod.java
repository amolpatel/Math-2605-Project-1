/**
 * Part 3
 * Find largest eigenvalue and corresponding eigenvector
 * using power method
 */

public class PowerMethod extends Matrix {

	private Matrix vectorNorm;
	private double tol;
	private Matrix matrixA;

	public PowerMethod(Matrix matrixA, double tol, double[][] approxVector) {
		super(approxVector); //vector is now matrix
		this.tol = tol;
		this.matrixA = matrixA;
	}

	
	public void power_method() {
		Matrix nextVector = new Matrix(this.getNumRows(), this.getNumCols());
		int i = 0;
		double difference = 0;
		double nextEigenValue = 0;
		while(!(i > 100) || difference <= this.tol) {
			//(A*u0) / eigen0 = u1
			if (!(this.getNumRows() == matrixA.getNumCols())) {
				throw new IllegalArgumentException("Matrices cannot be multiplied.");
			}
			double firstNum = this.get(1,0); //get first number of current
			for (int n = 0; n < matrixA.getNumRows(); n++) {
				for (int j = 0; j < matrixA.getNumCols(); j++) {
					for (int k = 0; k < this.getNumRows(); k++) {
						this.set(n,0, (this.get(n,0) + matrixA.get(n,j) * this.get(k,0)));
					}
				}
			}
			//now divide each number in vector by previous eigenvalue
			for (int j = 0; j < this.getNumRows(); i++) {
				this.set(j, 0, this.get(j,0) / firstNum);
			}
			
			//check for error - we now have Ak; either get Ak-1 or Ak+1
			if (!(this.getNumRows() == matrixA.getNumCols())) {
				throw new IllegalArgumentException("Matrices cannot be multiplied.");
			}
			double firstNumm = this.get(1,0); //get first number of current
			for (int n = 0; n < matrixA.getNumRows(); n++) {
				for (int j = 0; j < matrixA.getNumCols(); j++) {
					for (int k = 0; k < this.getNumRows(); k++) {
						nextVector.set(n, 0, (nextVector.get(n,0) + matrixA.get(n,j) * this.get(k,0)));
					}
				}
			}
			//now divide each number in vector by previous eigenvalue
			for (int j = 0; j < nextVector.getNumRows(); i++) { 
				nextVector.set(j, 0, nextVector.get(j, 0) / firstNumm);
			} 
			//I now have next vector and current vector to check against each other

			//perform check
			double currEigenValue = this.get(1,0);
			nextEigenValue = nextVector.get(1,0);
			double multiply;
			difference = nextEigenValue - currEigenValue;
			if (difference < 0) {
				multiply = difference * 2;
				difference = difference - multiply;
			}
			i++;
			
		}
		if (i > 100) {
			System.out.println("Method does not converge after 100 iterations");
		} else {
			System.out.println("The maximum eigenvalue is " + nextEigenValue);
			System.out.println("The number of iterations it took for this is " + i);
			System.out.println("The maximum eigenvector is ");
			for (int b = 0; b < nextVector.getNumRows(); b++) {
				System.out.println(nextVector.get(b,0));
			}

		}
		return;
	}
}	 
