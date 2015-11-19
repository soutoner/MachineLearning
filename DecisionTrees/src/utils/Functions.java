package utils;

import ds.Table;
import ds.Tuple;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class Functions {

    /**
     * @param data (S)
     * @return Entropy(S) - Entropy of the target attribute.
     */
    public static double Entropy(Table data){
        return Entropy(Probabilities(data.getTargetColumn()));
    }

    /**
     * @param data (S)
     * @param attribute (PA)
     * @param value ("Alta")
     * @return Entropy(S_PA eq Alta)
     */
    public static double Entropy(Table data, String attribute, String value){
        return Entropy(Probabilities(data, attribute, value));
    }

    /**
     * @param data (S)
     * @param attribute (PA)
     * @return Gain(S, PA)
     */
    public static Double Gain(Table data, String attribute){
        List<String> column = data.getColumn(attribute);
        double columnSize = column.size();

        return Entropy(data) - Values(column).stream()
                .map(value -> Occurrences(column, value)/columnSize * Entropy(data, attribute, value))
                .reduce(0.0, Double::sum);
    }

    /**
     * @param column List of elements
     * @return Values of a column without duplication
     */
    public static List<String> Values(List<String> column){
        return new ArrayList<String>(new HashSet<String>(column));
    }

    /**
     * @param x Number
     * @param b base
     * @return log_b(x)
     */
    private static double Log(double x, int b){
        return Math.log(x)/Math.log(b);
    }

    /**
     * @param probabilities List of probabilities
     * @return Entropy for the given list of probabilities
     */
    private static double Entropy(List<Double> probabilities){
        return -probabilities.stream()
                .reduce(0.0, (ac, p) -> ac + p * Log(p, 2));
    }

    /**
     * @param column List of elements
     * @return List of probabilities (occurrences of each value / total) for a given a column
     */
    private static List<Double> Probabilities(List<String> column){
        double columnSize = column.size();

        return Values(column).stream()
                .map(value -> Occurrences(column, value)/columnSize)
                .filter(p -> p != 0.0)
                .collect(Collectors.toList());
    }

    /**
     * @param data Examples
     * @param attribute Name of the attribute
     * @param value Value that must match
     * @return List of probabilities for a given a column and a specific value
     */
    private static List<Double> Probabilities(Table data, String attribute, String value){
        List<String> classes = Values(data.getTargetColumn());
        List<Tuple<String, String>> mixedValues = Values(data.getMixedColumn(attribute), value);
        double columnSize = mixedValues.size();

        return classes.stream()
                .map(c -> OccurrencesTuple(mixedValues, c) / columnSize)
                .filter(p -> p != 0)
                .collect(Collectors.toList());
    }

    /**
     * @param mixedColumn (Column, target column)
     * @param value Value that filters column
     * @return Elements of mixedColumn, filtered by value
     */
    private static List<Tuple<String, String>> Values(List<Tuple<String,String>> mixedColumn, String value){
        return mixedColumn.stream()
                .filter(t -> t.x.equalsIgnoreCase(value))
                .collect(Collectors.toList());
    }

    /**
     * @param column List of elements
     * @param value Value that should match
     * @return Occurrences of value in the given column
     */
    private static int Occurrences(List<String> column, String value){
        return Collections.frequency(column, value);
    }

    /**
     * @param mixedColumn (Column, target column)
     * @param value Value that should match in target column
     * @return Occurrences of value in mixedColumn
     */
    private static long OccurrencesTuple(List<Tuple<String,String>> mixedColumn, String value){
        return mixedColumn.stream()
                .filter(t -> t.y.equalsIgnoreCase(value))
                .count();
    }

}
