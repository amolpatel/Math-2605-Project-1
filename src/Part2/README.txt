To compile and run the code for part 2, please ensure a recent version of Java is installed on your computer.

Navigate to the directory src/Part2.
After that, the code can be compiled via the following command:
javac -cp ../ Part2Driver.java

That will create the necessary .class files, and the code can then be run with the command:
java -cp ../ Part2/Part2Driver

The system will print options for choosing which method you would like to run.

** PLEASE READ CAREFULLY **

Options 1 and 2 read an augmented matrix from a file in the form [A|b] with space used as a delimiter between columns and return used as a delimiter between rows.

Options 3, 5, 7, 9, 10, 13, and 14 read a vector b from a file in the following form which is either a horizontal/vertical row/column with space/return used as a delimiter:
1
0
1   OR  1 0 1 0 1
0
1

Options 11 and 15 read a full Y output stream vector in the following form which is a VERTICAL matrix with a space between each column:
For example, Y = (10, 01, 01, 10, 10, 01) should be written as:
1 0
0 1
0 1
1 0
1 0
0 1

Some of the options may ask for user input such as desired length of random input stream X/output stream Y, choice of A0/A1, as well as tolerance levels.


Included in this folder are a PDF and Word document of the written component associated with part 2 (included both in case one is easier to read).

** All code for Part 3 is located within Matrix.java **
