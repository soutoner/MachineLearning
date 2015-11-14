package id3;

import ds.Node;
import ds.Table;
import ds.Tuple;
import utils.CSVReader;
import utils.Functions;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ID3 {

    private Table data;
    private Node<String> tree;

    /**
     * Create the objects and sets de data and classifying column
     * @param csvFilename
     * @param classColumn
     */
    public ID3(String csvFilename, String classColumn){
        this.data = new Table(CSVReader.Parse(csvFilename));
        this.data.setClassifyingColumn(classColumn);
        this.tree = null;
    }

    /**
     * Run the algorithm and builds the decision tree
     */
    public Node<String> run(){
        // For all the attributes
        List<String> attributes = data.getAttributes();
        // Caculate gains of each one (attr, gain)
        List<Tuple<String,Double>> attrGains = attributes.stream()
                .map(a -> new Tuple<String, Double>(a, Functions.Gain(data, a)))
                .collect(Collectors.toList());
        // Select the highest one
        String maxGainAttr = attrGains.stream().max(new TupleComparator()).get().x;

        // Create tree node with this attribute and the children labels
        List<String> labels = Functions.Values(data.getColumn(maxGainAttr));
        tree = new Node<String>(maxGainAttr, labels);

        for(Tuple<String, Node<String>> labelChild: tree.getChildren()) {
            String label = labelChild.x;
            Node<String> child = labelChild.y;

            dfs(child, this.data.restrict(new Tuple<String, String>(maxGainAttr, label)));
        }

        return tree;
    }

    public void dfs(Node<String> node, Table data){
        // For all the attributes
        List<String> attributes = data.getAttributes();
        if(attributes.size() == 0){ // We are in a leaf node
            long nOfSi = data.classColumn().stream()
                    .filter(v -> v.equalsIgnoreCase("si"))
                    .count();
            if(nOfSi > 0){
                node.setData("Si");
                return;
            } else {
                node.setData("No");
                return;
            }
        } else { // recursive step
            // Caculate gains of each one (attr, gain)
            List<Tuple<String, Double>> attrGains = attributes.stream()
                    .map(a -> new Tuple<String, Double>(a, Functions.Gain(data, a)))
                    .collect(Collectors.toList());
            // Select the highest one
            String maxGainAttr = attrGains.stream().max(new TupleComparator()).get().x;
            // Create tree node with this attribute and the children labels
            List<String> labels = Functions.Values(data.getColumn(maxGainAttr));
            node.setData(maxGainAttr);
            node.setChildren(labels);

            for (Tuple<String, Node<String>> labelChild : node.getChildren()) {
                String label = labelChild.x;
                Node<String> child = labelChild.y;

                dfs(child, data.restrict(new Tuple<String, String>(maxGainAttr, label)));
            }
        }
    }

    public static void main(String [] args){
        ID3 id = new ID3("foo.csv", "AF");
        id.run();
        System.out.println(id.tree);
    }

    /**
     * Comparator for (attribute, gain) tuples.
     */
    static class TupleComparator implements Comparator<Tuple<String, Double>> {

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

}
