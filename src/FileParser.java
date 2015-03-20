import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Class to parse data file and create Matrix from data file
 * Files must be formatted in such a way that a space or comma indicates a new entry in the matrix
 * Can consider moving this to Driver class since it's just one method,
 * but decided to make it a separate class for now to keep things separate and clean
 */
public class FileParser {
    /**xx
     * Parses text file and returns Matrix representation of data in file
     * According to the project description, matrices are guaranteed to have dimensions n by n
     * The following sample data file represents a 4 x 4 matrix:
     * 3 4 2 .4432
     * 3.2 -6.5 4 4
     * 1.023123 3 2 9
     * 1 1 1 -19999
     * Does not account for incorrect input in any way; consider adding at later point in time
     * @param filePath path to file containing data
     * @return Matrix object of data
     * @throws FileNotFoundException
     */
    public static Matrix parseFile(String filePath) throws FileNotFoundException {
        Scanner file = new Scanner(new File(filePath));
        String line = file.nextLine();
        // Find dimensions of matrix from first line of file
        String[] stringArray = line.split(",| ");
        int matrixDim = stringArray.length;
        double[][] doubleArray = new double[matrixDim][matrixDim];
        for (int i = 0; i < stringArray.length; i++)
            doubleArray[0][i] = Double.parseDouble(stringArray[i]);
        // Parse remainder of file and add to double array
        for (int i = 1; i < matrixDim; i++)
            for (int j = 0; j < matrixDim; j++)
                doubleArray[i][j] = file.nextDouble();
        file.close();
        return (new Matrix(doubleArray));
    }

    /**
     * Parses text file and returns list of matrices formed from data in file
     * The following sample data file represents a 4 x 4 and 4 x 1 matrix:
     * 3 4 2 .4432 1
     * 3.2 -6.5 4 4 2
     * 1.023123 3 2 9 3
     * 1 1 1 -19999 4
     * @param filePath path to file containing data
     * @return list of matrices formed from data
     * @throws FileNotFoundException
     */
    public static Matrix[] parseFileWithB(String filePath) throws FileNotFoundException {
    	Scanner file = new Scanner(new File(filePath));
        String line = file.nextLine();
        // Find dimensions of matrix from first line of file
        String[] stringArray = line.split(",| ");
        int matrixADim = stringArray.length - 1;

        double[][] matrixArray = new double[matrixADim][matrixADim];
        double[][] vectorArray = new double[matrixADim][1];
        // Copy first row of text file into arrays
        for (int i = 0; i < matrixADim; i++)
        	matrixArray[0][i] = Double.parseDouble(stringArray[i]);
        vectorArray[0][0] = Double.parseDouble(stringArray[matrixADim]);

        // Parse remainder of file and add to double both arrays
        for (int i = 1; i < matrixADim; i++)
        {
            for (int j = 0; j < matrixADim; j++)
            	matrixArray[i][j] = file.nextDouble();
            vectorArray[i][0] = file.nextDouble();
        }
        file.close();
        Matrix[] list = new Matrix[2];
        list[0] = new Matrix(matrixArray);
        list[1] = new Matrix(vectorArray);
        return list;
    }
}