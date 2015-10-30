package utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class Main {

	public static void main(String[] args) throws IOException {
		CSVReader cs = new CSVReader();
		String datos[][] = null;
			
		BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
			
		System.out.println("Inserta la ruta al fichero csv acabado en .csv y pulsa Intro");			
		String path = bf.readLine();
		
		System.out.println("Inserta el número de filas del csv y pulsa Intro");
		String rows = bf.readLine();
			
		System.out.println("Inserta el número de columnas del csv y pulsa Intro");
		String cols = bf.readLine();
			
		datos = cs.readCSV(path,Integer.parseInt(rows),Integer.parseInt(cols));
		
		for(int i = 0;i <2;i++){
			
			for(int j = 0;j<3;j++){
					
				System.out.print(datos[i][j]+" ");
			}
				
			System.out.println("");
		}
	}
}

