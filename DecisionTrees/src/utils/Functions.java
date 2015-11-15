package utils;

import ds.Table;
import ds.Tuple;
import id3.ID3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class Functions {

    /**
     * Calculates entropy of the classifier attribute.
     *
     * @param data (S)
     * @return Entropy(S)
     */
    public static double Entropy(Table data){
        List<Double> probabilities = Probabilities(data.getTargetColumn());

        return Entropy(probabilities);
    }

    /**
     * Calculates entropy of a given column (by attribute name), and a specific value.
     * @param data (S)
     * @param column (PA)
     * @param value ("Alta")
     * @return Entropy(S_PA='Alta')
     */
    public static double Entropy(Table data, String column, String value){
        List<Double> probabilities = Probabilities(data, column, value);

        return Entropy(probabilities);
    }

    /**
     * Calculates the gain of the given data and column.
     * @param data (S)
     * @param column (PA)
     * @return Gain(S, PA)
     */
    public static Double Gain(Table data, String column){
        List<String> col = data.getColumn(column);
        double colSize = col.size();

        return Entropy(data) - Values(data.getColumn(column)).stream()
                .map(v -> Occurrences(col, v)/colSize * Entropy(data, column, v))
                .reduce(0.0, Double::sum);
    }

    /**
     * Returns values of a column without duplication.
     * @param column
     * @return
     */
    public static List<String> Values(List<String> column){
        // Non-duplciated values
        List<String> values = new ArrayList<String>(new HashSet<String>(column));

        // Calculate values and occurrences
        List<Tuple<String, Long>> occurrences = values.stream()
                .map(v -> new Tuple<String, Long>(v, Occurrences(column, v)))
                .collect(Collectors.toList());

        // Sort list by occurrences
        Collections.sort(occurrences, new ID3.LongTupleComparator());


        // Convert to a (ordered) list of values without repetition
        return occurrences.stream().map(t -> t.x).collect(Collectors.toList());
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
     * Calculate the entropy for the given list of probabilities.
     * @param probabilities
     * @return
     */
    private static double Entropy(List<Double> probabilities){
        return -probabilities.stream()
                .reduce(0.0, (ac, p) -> ac + p * Log(p, 2));
    }

    /**
     * Calculate list of probabilities (occurrences of each value) for a given a column.
     * @param column
     * @return
     */
    private static List<Double> Probabilities(List<String> column){
        double colSize = column.size();

        return Values(column).stream()
                .map(v -> Occurrences(column, v) / colSize)
                .filter(p -> p != 0.0)
                .collect(Collectors.toList());
    }

    /**
     * Calculate list of probabilities for a given a column and a specific value.
     * @param column
     * @return
     */
    private static List<Double> Probabilities(Table data, String column, String val){
        List<String> classes = Values(data.getTargetColumn());
        List<Tuple<String, String>> mixedValues = Values(data.getMixedColumn(column), val);
        double colSize = mixedValues.size();

        return classes.stream()
                .map(c -> OccurrencesTuple(mixedValues, c) / colSize)
                .filter(p -> p != 0)
                .collect(Collectors.toList());
    }

    /**
     * Returns elements of mixedColumn (column and classifier column), filtered by value.
     * @param mixedColumn
     * @param value
     * @return
     */
    private static List<Tuple<String, String>> Values(List<Tuple<String,String>> mixedColumn, String value){
        return mixedColumn.stream()
                .filter(t -> t.x.equalsIgnoreCase(value))
                .collect(Collectors.toList());
    }

    /**
     * Returns occurrences of value in the given column.
     * @param column
     * @param value
     * @return
     */
    private static long Occurrences(List<String> column, String value){
        return column.stream()
                .filter(s -> s.equalsIgnoreCase(value))
                .count();
    }

    /**
     * Returns occurrences of class in mixedColumn (column and classifier column).
     * @param mixedColumn
     * @param value
     * @return
     */
    private static long OccurrencesTuple(List<Tuple<String,String>> mixedColumn, String value){
        return mixedColumn.stream()
                .filter(t -> t.y.equalsIgnoreCase(value))
                .count();
    }

    public static void main(String[] args){
        Table t = new Table(CSVReader.Parse("foo.csv"));
        t.setTargetAttribute("AF");
        Table t1 = t.restrictValue("PA", "alta");
        Table t2 = t1.restrictAttribute("PA");

        System.out.println("T2 - " + Functions.Entropy(t2));
        System.out.println("AS=Alto " + Functions.Entropy(t2, "AS", "alto"));
        System.out.println("AS=Bajo " + Functions.Entropy(t2, "AS", "bajo"));
        System.out.println("IC=Alto " + Functions.Entropy(t2, "IC", "alto"));
        System.out.println("IC=Bajo " + Functions.Entropy(t2, "IC", "bajo"));
        System.out.println("AA=no " + Functions.Entropy(t2, "AA", "no"));
        System.out.println("AA=si " + Functions.Entropy(t2, "AA", "si"));
        System.out.println("OA=no " + Functions.Entropy(t2, "OA", "no"));
        System.out.println("OA=si " + Functions.Entropy(t2, "OA", "si"));

        System.out.println(t2);
    }

}
