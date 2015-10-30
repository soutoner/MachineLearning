package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class CSVReader {

	private String[][] data;
/**
 * Insert data from csv	in a matrix
 * @param pathname
 * @param rows
 * @param cols
 * @return
 * @throws FileNotFoundException
 */
	public String[][] readCSV(String pathname,int rows,int cols) throws FileNotFoundException{
		File f = new File("/Users/fran/Documents/workspace/ID3/src/utils/csv.csv");
		
		Scanner text = new Scanner(f);//Scanner for reading a row/line
		
		Scanner line = new Scanner("");//Scanner for reading the attributes of a line
		
		data = new String[rows][cols];//Matrix for data
		
		data = insertDataOnMatrix(data,text,line);//Call to auxiliary function
		
		//Close scanners
		line.close();
		
		text.close();
		
		return data;
	}
/**
 * Auxiliary function for readCSV
 * @param data
 * @param text
 * @param line
 * @return 
 */
	private String[][] insertDataOnMatrix(String[][] data,Scanner text,Scanner line) {
		//Indexes for iterating over the matrix
		int row = 0;
		int col = 0;
		
		//We take a row
		while(text.hasNextLine()){
			
			line = new Scanner(text.nextLine());//Scanner with the rowString
			
			line.useDelimiter(",");//Attributes separated by commas
			
			//We take values of a row/line
			while(line.hasNext()){
				
				data[row][col] = line.next();//Insert attribute on a position in the matrix
				
				col++;
			}
			//Updating indexes
			col = 0;
			row++;
		}
		return data;
	}
	
}
