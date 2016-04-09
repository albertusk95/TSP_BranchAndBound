package bin;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.io.IOException;

public class ReadFileEksternal {
	
	private String path_file;
	private int[][] data_matrix = new int[30][30];
	private int matrixRow, matrixCol;
	
	//constructor
	public ReadFileEksternal(String _path_file) {
		path_file = _path_file;
	}
	
	//getter
	public int getMatrixRow() {
		return matrixRow;
	}
	public int getMatrixCol() {
		return matrixCol;
	}
	public int[][] getDataMatrix() {
		return data_matrix;
	}
	
	//method
	public void startReadingFile() throws IOException {
		String line, token;
		int counter = 0, nbtoken = 0, count_row = 0, count_col = 0;
		StringTokenizer tokenizer;
		BufferedReader input = null;
		input = new BufferedReader(new FileReader(path_file));
		line = input.readLine();
				
		while (line != null) { 
			tokenizer = new StringTokenizer(line, " ");		// delimiternya " "
  
			if (counter == 0) {// menghitung banyak token (jumlah kolom)
				matrixCol = tokenizer.countTokens();
				counter = 1;
			}
			
			count_col = 0;
			while (tokenizer.hasMoreTokens()) {// proses token 
				token = tokenizer.nextToken();
				data_matrix[count_row][count_col] = Integer.parseInt(token);
				count_col++;
			}
			count_row++;
			line = input.readLine(); 
		}// endofwhileline
		
		matrixRow = count_row;
		matrixCol = count_col;
		
		System.out.println("Baris: " + matrixRow);
		System.out.println("Kolom: " + matrixCol);
		
		for (int i = 0; i < matrixRow; i++) {
			for (int j = 0; j < matrixCol; j++) {
				System.out.print(data_matrix[i][j] + " ");
			}
			System.out.println();
		}
		
		
	}
	
}
 