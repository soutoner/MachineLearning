package ds;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Table {

    private List<String> attributes;    // Attributes for the table
    private List<List<String>> values;  // Actual data of the table
    private int targetColumnIdx;        // By default, last column

    /**
     * @param data List of lists. List of Attributes +  List of rows with data
     * @param targetAttribute Attribute that is being classified
     */
    public Table(List<List<String>> data, String targetAttribute){
        // Save lower cased attributes (to avoid problem in equals)
        this.attributes = data.remove(0).stream().map(String::toLowerCase).collect(Collectors.toList());
        this.values = data;
        this.targetColumnIdx = attributes.indexOf(targetAttribute.toLowerCase());
    }

    /**
     * @param attributes List of attributes
     * @param data List of elements row by row
     * @param targetAttribute Attribute that is being classified
     */
    public Table(List<String> attributes, List<List<String>> data, String targetAttribute){
        this.attributes = attributes;
        this.values = data;
        this.targetColumnIdx = attributes.indexOf(targetAttribute.toLowerCase());
    }

    /**
     * @param attribute Column attribute
     * @param value Value that must appear
     * @return Table with the rows restricted to the rows that has 'value' in column 'attribute'
     */
    public Table restrictValue(String attribute, String value){
        int restrictedColIdx = this.attributes.indexOf(attribute.toLowerCase());

        List<List<String>> restrictedRows = this.values.stream()
                .filter(row -> row.get(restrictedColIdx).equalsIgnoreCase(value))
                .collect(Collectors.toList());

        return new Table(this.attributes, restrictedRows, this.attributes.get(targetColumnIdx));
    }

    /**
     * @param attribute Column attribute
     * @return Table without an specific column
     */
    public Table restrictAttribute(String attribute){
        int restrictedColIdx = this.attributes.indexOf(attribute.toLowerCase());

        List<String> restrictedAttributes = this.attributes.stream()
                .filter(a -> !a.equalsIgnoreCase(attribute))
                .collect(Collectors.toList());

        List<List<String>> restrictedRows = this.values.stream()
                .map(row -> removeFromList(row, restrictedColIdx))
                .collect(Collectors.toList());

        return new Table(restrictedAttributes, restrictedRows, this.attributes.get(targetColumnIdx));
    }

    /**
     * @param attribute Column attribute
     * @return List of Tuples(Desired column_i, Target column_i)
     */
    public List<Tuple<String, String>> getMixedColumn(String attribute){
        return getMixedColumn(attributes.indexOf(attribute.toLowerCase()));
    }

    /**
     * @param idx Column index
     * @return List of Tuples(Desired column_i, Target column_i)
     */
    public List<Tuple<String, String>> getMixedColumn(int idx){
        return values.stream()
                .map(row -> new Tuple<String, String>(row.get(idx), row.get(targetColumnIdx)))
                .collect(Collectors.toList());
    }

    /**
     * @param attribute Column attribute
     * @return Desired column, given the attribute String
     */
    public List<String> getColumn(String attribute){
        return getColumn(attributes.indexOf(attribute.toLowerCase()));
    }

    /**
     * @param idx Column index
     * @return Desired column, given the column idx.
     */
    public List<String> getColumn(int idx){
        return values.stream()
                .map(row -> row.get(idx))
                .collect(Collectors.toList());
    }

    /**
     * @return Column of the target attribute.
     */
    public List<String> getTargetColumn(){
        return getColumn(targetColumnIdx);
    }

    /**
     * @return Attribute list except the target attribute.
     */
    public List<String> getAttributes() {
        return IntStream.range(0, attributes.size())
                .filter(i -> i != targetColumnIdx)
                .mapToObj(attributes::get)
                .collect(Collectors.toList());
    }

    /**
     * @return True if the table is empty (i.e. has no values)
     */
    public boolean isEmpty(){
        return values.isEmpty();
    }

    /**
     * @return String representation of the table.
     */
    public String toString(){
        StringBuilder res = new StringBuilder();

        // Attributes
        res.append("  ").append(attributes).append("\n");
        // Row of data
        IntStream.range(0, values.size())
                .forEach(idx -> res.append(idx).append(" ").append(values.get(idx)).append("\n"));

        return res.toString();
    }

    /**
     * @param list List of elements
     * @param idx Position of the element that's being removed
     * @return List without the given element at position idx
     */
    private static List<String> removeFromList(List<String> list, int idx){
        return IntStream.range(0, list.size())
                .filter(i -> i != idx)
                .mapToObj(list::get)
                .collect(Collectors.toList());
    }
}
