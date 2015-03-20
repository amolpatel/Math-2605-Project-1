import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Class to parse data file and create Matrix object from data
 * Files must be formatted in such a way that a space or comma indicates a new entry in the matrix
 * For example, the following sample data file represents a 4x4 matrix:
 * 3 4 2 .4432
 * 3.2 -6.5 4 4
 * 1.023123 3 2 9
 * 1 1 1 -19999
 * Can consider moving this to Driver class since it's just one method,
 * but decided to make it a separate class for now to keep things separate and clean
 */
public class FileParser {
    /**
     * Parses text file and returns Matrix representation of data in text file
     * According to the project description, matrices are guaranteed to have dimensions n by n
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
}