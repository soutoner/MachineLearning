package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

public class CSVReader{

    public static List<List<String>> Parse(String filename){
        Scanner sc = null;
        List<List<String>> data = new ArrayList<List<String>>();

        try {
            sc = new Scanner(new File(filename));
            sc.useDelimiter("\n");

            while(sc.hasNext()){
                List<String> row = Arrays.asList(sc.next().split(","));

                // Trim whitespaces and add to the table
                data.add(row.stream().map(String::trim).collect(Collectors.toList()));
            }
        } catch (FileNotFoundException e) {
            System.err.println("Check the path of the file or format.");
            e.printStackTrace();
        } finally {
            if(sc != null)
                sc.close();
        }

        return data;
    }
}
