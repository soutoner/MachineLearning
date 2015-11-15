package id3;

import ds.Node;
import ds.Table;
import ds.Tuple;
import utils.CSVReader;
import utils.Functions;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ID3 {

    private Table data;

    /**
     * Create the objects and sets de data and classifying column
     * @param csvFilename
     * @param targetAttribute
     */
    public ID3(String csvFilename, String targetAttribute){
        this.data = new Table(CSVReader.Parse(csvFilename));
        this.data.setTargetAttribute(targetAttribute);
    }

    /**
     * ID3 (Examples)
     *  If all the examples have the same targetAttribute value,
     *      Return a leaf with that value.
     *  Else if the set of attributes is empty,
     *      Return a leaf with the most common targetAttribute value.
     *  Else Begin
     *      A ← The Attribute that best classifies examples.
     *      Decision Node N for Attribute = A.
     *      For each possible value, vi, of A:
     *          Let Examples(vi) be the subset of examples that have the value vi for A
     *          If Examples(vi) is empty,
     *              add to N a new leaf child node with the most common targetAttribute value.
     *          Else add to N ID3 (Examples(vi) – {A})
     *      End
     *      Return decision node N
     *  End
     * @param examples
     * @return
     */
    public Node<String> id3(Table examples){
        if(allExamplesPositive(examples)){
            return new Node<String>("Si");
        } else if (allExamplesNegative(examples)){
            return new Node<String>("No");
        } else if (examples.getAttributes().isEmpty()){
            return new Node<String>(mostCommonValue(examples));
        } else {
            String A = maxGainAttribute(examples);
            Node<String> decision = new Node<String>(A);

            for(String value: Functions.Values(examples.getColumn(A))){
                Table examplesVi = examples.restrictValue(A, value);

                if(examplesVi.isEmpty()){
                    decision.addChild(new Node<String>(mostCommonValue(examplesVi)), value);

                } else {
                    decision.addChild(id3(examplesVi.restrictAttribute(A)), value);
                }
            }

            return decision;
        }
    }

    /**
     * Return the attribute with the maximum gain.
     * @param examples
     * @return
     */
    public String maxGainAttribute(Table examples){
        // For all the attributes
        List<String> attributes = examples.getAttributes();
        // Caculate gains of each one (attr, gain)
        List<Tuple<String,Double>> attrGains = attributes.stream()
                .map(a -> new Tuple<String, Double>(a, Functions.Gain(examples, a)))
                .collect(Collectors.toList());
        // Select the highest one
        return attrGains.stream().max(new DoubleTupleComparator()).get().x;
    }

    /**
     * Return if all the values in the target column are positive.
     * @param examples
     * @return
     */
    public boolean allExamplesPositive(Table examples) {
        return examples.getTargetColumn().stream()
                .filter(v -> !v.equalsIgnoreCase("Si"))
                .count() == 0;
    }

    /**
     * Return if all the values in the target column are negative.
     * @param examples
     * @return
     */
    public boolean allExamplesNegative(Table examples) {
        return examples.getTargetColumn().stream()
                .filter(v -> !v.equalsIgnoreCase("No"))
                .count() == 0;
    }

    /**
     * Return the most common value in the target column.
     * @param examples
     * @return
     */
    public String mostCommonValue(Table examples){
        Map<String, Long> occurrences = examples.getTargetColumn().stream()
                .collect(Collectors.groupingBy(w -> w, Collectors.counting()));

        Map.Entry<String, Long> maxEntry = null;

        for (Map.Entry<String, Long> entry : occurrences.entrySet()){
            if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0) {
                maxEntry = entry;
            }
        }

        return maxEntry.getKey();
    }

    public static void main(String [] args){
        ID3 id = new ID3("foo.csv", "AF");
        System.out.println(id.id3(id.data).printTree());
    }

    /**
     * Comparator for (attribute, gain) tuples.
     */
    static class DoubleTupleComparator implements Comparator<Tuple<String, Double>>{
        /**
         * Compare two tuples (attribute, gain). Uses gains as ordering.
         * @param t1
         * @param t2
         * @return
         */
        public int compare(Tuple<String, Double> t1, Tuple<String, Double> t2){
            return t1.y.compareTo(t2.y);
        }
    }

    /**
     * Comparator for (attribute, occurrences) tuples.
     */
    static public class LongTupleComparator implements Comparator<Tuple<String, Long>>{
        /**
         * Compare two tuples (attribute, occurrences). Uses occurrences as ordering.
         * @param t1
         * @param t2
         * @return
         */
        public int compare(Tuple<String, Long> t1, Tuple<String, Long> t2){
            return -t1.y.compareTo(t2.y);
        }
    }



}
