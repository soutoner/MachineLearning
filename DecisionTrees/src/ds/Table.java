package ds;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Table {

    private List<String> attributes;    // Attributes for the table
    private List<List<String>> values;  // Actual data of the table
    private int targetColumnIdx;        // By default, last column

    /**
     * Constructor that initialize the table with the given data (attributes + values)
     * @param data
     */
    public Table(List<List<String>> data){
        this.attributes = data.remove(0);
        this.values = data;
        this.targetColumnIdx = data.size()-1;
    }

    /**
     * Constructor that initialize the table given the attributes, data, and target attribute.
     * @param data
     */
    public Table(List<String> attributes, List<List<String>> data, String target){
        this.attributes = attributes;
        this.values = data;
        this.targetColumnIdx = attributes.indexOf(target);
    }

    /**
     * Return a table with the rows limited by a given value in a given attribute.
     * @param attribute
     * @param value
     * @return
     */
    public Table restrictValue(String attribute, String value){
        int restrictedColIdx = this.attributes.indexOf(attribute);

        List<List<String>> restrictedRows = this.values.stream()
                .filter(row -> row.get(restrictedColIdx).equalsIgnoreCase(value))
                .collect(Collectors.toList());

        return new Table(this.attributes, restrictedRows, this.attributes.get(targetColumnIdx));
    }

    /**
     * Returns a table without an specific column given attribute.
     * @param attribute
     * @return
     */
    public Table restrictAttribute(String attribute){
        int restrictedColIdx = this.attributes.indexOf(attribute);

        List<String> restrictedAttributes = this.attributes.stream()
                .filter(a -> !a.equalsIgnoreCase(attribute))
                .collect(Collectors.toList());

        List<List<String>> restrictedRows = this.values.stream()
                .map(row -> rowWithout(row, restrictedColIdx))
                .collect(Collectors.toList());

        return new Table(restrictedAttributes, restrictedRows, this.attributes.get(targetColumnIdx));
    }

    /**
     * Removes from a row the given element at position idx
     * @param row
     * @param idx
     * @return
     */
    private List<String> rowWithout(List<String> row, int idx){
        List<String> ret = new ArrayList<String>();

        for(int i=0;i<row.size();i++){
            if(i != idx)
                ret.add(row.get(i));
        }

        return ret;
    }

    /**
     * Return a list of Tuples(Desired column_i, Target column_i)
     * @param attr
     * @return
     */
    public List<Tuple<String, String>> getMixedColumn(String attr){
        return getMixedColumn(attributes.indexOf(attr));
    }

    /**
     * Return a list of Tuples(Desired column_i, Target column_i)
     * @param idx
     * @return
     */
    public List<Tuple<String, String>> getMixedColumn(int idx){
        return values.stream()
                .map(row -> new Tuple<String, String>(row.get(idx), row.get(targetColumnIdx)))
                .collect(Collectors.toList());
    }

    /**
     * Return the desired column, given the attribute String.
     * @param attr
     * @return
     */
    public List<String> getColumn(String attr){
        return getColumn(attributes.indexOf(attr));
    }

    /**
     * Return the desired column, given the column idx.
     * @param idx
     * @return
     */
    public List<String> getColumn(int idx){
        return values.stream()
                .map(row -> row.get(idx))
                .collect(Collectors.toList());
    }

    /**
     * Return the target column.
     * @return
     */
    public List<String> getTargetColumn(){
        return getColumn(targetColumnIdx);
    }

    /**
     * Get the attributes list except the target attribute.
     * @return
     */
    public List<String> getAttributes() {
        return attributes.stream()
                .filter(a -> attributes.indexOf(a) != targetColumnIdx)
                .collect(Collectors.toList());
    }

    /**
     * Set the target attribute.
     * @param attribute
     */
    public void setTargetAttribute(String attribute){
        targetColumnIdx = attributes.indexOf(attribute);
    }

    /**
     * Return if the table is empty (i.e. has no values)
     * @return
     */
    public boolean isEmpty(){
        return values.isEmpty();
    }

    /**
     * Returns a String representation of the table.
     * @return
     */
    public String toString(){
        StringBuilder res = new StringBuilder();

        res.append("    ");
        for(String attr: attributes){
            res.append(attr + " ");
        }
        res.append("\n");

        for(int i=0; i<values.size(); i++){
            res.append(i + " " + values.get(i) + " ");
            res.append("\n");
        }

        return res.toString();
    }

}
