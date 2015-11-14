package utils;

import ds.Table;
import ds.Tuple;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class Functions {

    /**
     * Calculates entropy of the classifier attribute
     * @param data
     * @return
     */
    public static double Entropy(Table data){
        return -Probs(data.classColumn()).stream()
                .reduce(0.0, (ac, p) -> ac + p * Log(p, 2));
    }

    /**
     * Calculates entropy of the given attribute for an specific value
     * @param data
     * @return
     */
    public static double Entropy(Table data, String column, String val){
        return -Probs(data, column, val).stream()
                .reduce(0.0, (ac, p) -> (p!=0) ? ac + p * Log(p, 2) : ac);
    }

    public static Double Gain(Table data, String column){
        List<String> col = data.getColumn(column);
        double colSize = col.size();

        return Entropy(data) - Values(data.getColumn(column)).stream()
                .map(v -> Count(col, v)/colSize * Entropy(data, column, v))
                .reduce(0.0, Double::sum);
    }

    /**
     * Calculate list of probabilities for a given a column.
     * @param column
     * @return
     */
    private static List<Double> Probs(List<String> column){
        double colSize = column.size();

        return Values(column).stream()
                .map(v -> Count(column, v)/colSize)
                .collect(Collectors.toList());
    }

    /**
     * Calculate list of probabilities for a given a column and a specific value.
     * @param column
     * @return
     */
    private static List<Double> Probs(Table data, String column, String val){
        List<String> classes = Values(data.classColumn());
        List<Tuple<String, String>> mixedValues = Values(data.getMixedColumn(column), val);
        double colSize = mixedValues.size();

        return classes.stream()
                .map(c -> CountMixed(mixedValues, c) / colSize)
                .collect(Collectors.toList());
    }

    /**
     * Returns log_b(x)
     * @param x
     * @param b
     * @return
     */
    private static double Log(double x, int b){
        return Math.log(x)/Math.log(b);
    }

    /**
     * Returns ocurrences of val in col
     * @param col
     * @param val
     * @return
     */
    private static long Count(List<String> col, String val){
        return col.stream()
                .filter(s -> s.equals(val))
                .count();
    }

    /**
     * Returns ocurrences of class in mixedColumn (column and classifier column)
     * @param mixedCol
     * @param val
     * @return
     */
    private static long CountMixed(List<Tuple<String,String>> mixedCol, String val){
        return mixedCol.stream()
                .filter(t -> t.y.equals(val))
                .count();
    }


    /**
     * Returns different elements of col
     * @param col
     * @return
     */
    private static List<String> Values(List<String> col){
        return new ArrayList<String>(new HashSet<String>(col));
    }

    /**
     * Returns elements of mixedCol (column and classifier column), filtered by val
     * @param mixedCol
     * @param val
     * @return
     */
    private static List<Tuple<String, String>> Values(List<Tuple<String,String>> mixedCol, String val){
        return mixedCol.stream()
                .filter(t -> t.x.equals(val))
                .collect(Collectors.toList());
    }

    public static void main(String[] args){
        Table t = CSVReader.Parse("foo.csv");

        List<String> column = new ArrayList<String>(){{ add("alto"); add("bajo"); add("alto"); add("medio"); }};

        System.out.println(Functions.Gain(t, "PA"));
    }

}
