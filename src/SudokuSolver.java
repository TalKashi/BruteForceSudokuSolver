import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class SudokuSolver {

	static final int INVALID = 0;
	static final int SINGLE_PROBLEM = 1;
	static final int ALL = 2;
	static final int EXIT = 3;
	
	static final String ALL_STRING = "ALL";
	static final String EXIT_STRING = "Q";
	
	int[][] matrix = new int[9][9];
	BufferedReader br = null;
	
	public static void main(String[] args) {
		boolean shouldExit = false;
		SudokuSolver sudoku = new SudokuSolver();
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		 
		System.out.println("Welcome to the Sudoku Solver powered by a Brute Force algorithm");
		  
		try {
			while(!shouldExit) {
				System.out.println("Please enter 81 digits that represent the Sudoku puzzle, or ALL to run over all 10,000 problems, or Q to quit:");
				String line = reader.readLine();
				
				switch(validateInput(line)) {
				case INVALID:
					System.out.println("Invalid input");
					break;
				  case SINGLE_PROBLEM:
					  sudoku.loadMatrixFromString(line);
					  if(!sudoku.solve(0, 0)) {
						  System.err.println("No solution for the given line. Please enter a new valid sudoku puzzle");
					  } else {
						  System.out.println(sudoku);
					  }
					  break;
				  case ALL:
					  sudoku.setFilePath("C:\\temp\\5.txt");
					  final long start = System.nanoTime();
					  int lineNumber = 0;
					  while ((line = sudoku.getNextLine()) != null) {
						  sudoku.loadMatrixFromString(line);
						  if(!sudoku.solve(0, 0)) {
							  System.err.println("No solution for the line " + lineNumber);
						  }
						  lineNumber++;
						  System.out.println("Finished line " + lineNumber);
						  //lp.writeLp("modelSpecific"+x+".lp");
					  }
					  final long end = System.nanoTime();
					  System.out.println("finished in " + formatTime(end - start));
					  sudoku.closeFile();
					  break;
				  case EXIT:
					  shouldExit = true;
					  break;
			}
				

			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		  
		System.out.println("Thanks for using our Brute Force Sudoku solver!");

	}
	
	private static int validateInput(String line) {
		  if(line == null) {
			  return INVALID;
		  }
			
		  if(line.length() == 1) {
			  if(line.toUpperCase().contains(EXIT_STRING)) {
				  return EXIT;
			  } else {
				  return INVALID;
			  }
		  }
			
		  if(line.length() == 3) {
			  if(line.toUpperCase().contains(ALL_STRING)) {
				  return ALL;
			  } else {
				  return INVALID;
			  }
		  }
			
		  if (line.length() != 81) {
			  return INVALID;
		  }
		  char[] arr = line.toCharArray();
		  for(int i = 0; i < arr.length; i++) {
			  if(arr[i] < '0' || arr[i] > '9') {
				  return INVALID;
			  }
		  }
			
		  return SINGLE_PROBLEM;
	  }
	
	private void closeFile() {
		try {
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private String getNextLine() {
		try {
			return br.readLine();
		} catch (IOException e) {
			return null;
		}
	}
	
	private void setFilePath(String path) {
		try {
			br = new BufferedReader(new FileReader(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	void loadMatrixFromString(String matrixStr) {
		char[] charArr = matrixStr.toCharArray();
		
		for(int i = 0; i < matrix.length; i++) {
			for(int j = 0; j < matrix[i].length; j++) {
				matrix[i][j] = charArr[9 * i + j] - 48;
			}
		}
	}
	
	boolean solve(int row, int column) {
		
		// Reached the end of the matrix - Solved!
		if (row == 9) {
			return true;
		}
		
		// If cell is already set, go to the next cell
		if (matrix[row][column] != 0) {
			if (solve(column == 8? (row + 1): row, (column + 1) % 9)) {
				return true;
			}
		} else {
			for (int i = 1; i < 10; i++) {
				// Check if this value violates one of the constraints
				if (!containInRow(row, i) && !containInColumn(column, i) && 
						!containInBox(row, column, i)) {
					// Call next cell
					matrix[row][column] = i;
					if (solve(column == 8? (row + 1): row, (column + 1) % 9)) {
						return true;
					}
				}
			}
			matrix[row][column] = 0;
		}
		return false;
 	}

	private boolean containInColumn(int column, int i) {
		for (int row = 0; row < 9; row++) {
			if (matrix[row][column] == i)
				return true;
		}
		return false;
	}

	private boolean containInBox(int row, int column, int i) {
		row = (row / 3) * 3;
		column = (column / 3) * 3;
	    for(int r = 0; r < 3; r++) {
	    	for(int c = 0; c < 3; c++) {
	    		if (matrix[row + r][column + c] == i) {
	    			return true;
	    		}
	    	}
	    }
		return false;
	}

	private boolean containInRow(int row, int i) {
		for (int col = 0; col < 9; col++) {
			if (matrix[row][col] == i)
				return true;
		}
		return false;
	}
	
	public String toString() {
		
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				sb.append(matrix[i][j]);
			}
			//sb.append("\n");
		}
		return sb.toString();
	}
	
	// A method that converts the nano-seconds to Seconds-Minutes-Hours form
	private static String formatTime(long nanoSeconds)
	{
	    int hours, minutes, remainder, totalSecondsNoFraction, seconds;
	    double totalSeconds;

	    // Calculating hours, minutes and seconds
	    totalSeconds = (double) nanoSeconds / 1000000000.0;
	    String s = Double.toString(totalSeconds);
	    String [] arr = s.split("\\.");
	    totalSecondsNoFraction = Integer.parseInt(arr[0]);
	    hours = totalSecondsNoFraction / 3600;
	    remainder = totalSecondsNoFraction % 3600;
	    minutes = remainder / 60;
	    seconds = remainder % 60;

	    // Formatting the string that conatins hours, minutes and seconds
	    StringBuilder result = new StringBuilder();
	    if (hours < 10) {
	    	result.append("0" + hours);
	    }
	    else {
	    	result.append(hours);
	    }
	    
	    if (minutes < 10) {
	    	result.append(":0" + minutes);
	    }
	    else {
	    	result.append(":" + minutes);
	    }
	    if (seconds < 10) {
	    	result.append(":0" + seconds);
	    }
	    else {
	    	result.append(":" + seconds);
	    }
	    result.append(":" + (nanoSeconds / 10000000));
	    
	    return result.toString();
	}
}
