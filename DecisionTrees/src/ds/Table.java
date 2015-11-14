package ds;

import java.util.ArrayList;
import java.util.List;

public class Table {

    private List<String> attributes;    // Attributes for each fcolumn
    private String[][] values;          // Actual data of the table


    /**
     * Constructor that initialize the table with the given data.
     * @param data
     */
    public Table(List<List<String>> data){
        int rows = data.size()-1; // We don't count attributes
        int cols = data.get(0).size();

        this.values = new String[rows][cols];
        this.attributes = new ArrayList<String>();

        populate(data);
    }

    /**
     * Return a list of Tuples(Desired column_i, Classifying column_i)
     * @param attr
     * @return
     */
    public List<Tuple<String, String>> getMixedColumn(String attr){
        List<Tuple<String, String>> ret = new ArrayList<Tuple<String,String>>();
        List<String> selectedColumn = getColumn(attr);
        List<String> classColumn = classColumn();

        for(int i=0; i<classColumn().size(); i++){
            ret.add(new Tuple<String, String>(selectedColumn.get(i), classColumn.get(i)));
        }

        return ret;
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
        List<String> col = new ArrayList<String>();

        for(int i=0; i<values.length; i++)
            col.add(values[i][idx]);

        return col;
    }

    /**
     * Return the column that is being classified.
     * @return
     */
    public List<String> classColumn(){
        // TODO: set classifying column
        return getColumn(attributes.size()-1);
    }

    /**
     * Set the attributes list.
     * @param attrs
     */
    public void setAttributes(List<String> attrs){
        attributes = attrs;
    }

    /**
     * Insert the given data into the table.
     * @param data
     */
    public void populate(List<List<String>> data){
        // Set labels
        setAttributes(data.get(0));

        for(int i=1; i<data.size(); i++){
            List<String> row = data.get(i);
            for(int j=0; j<row.size(); j++){
                values[i-1][j] = row.get(j);
            }
        }
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

        for(int i=0; i<values.length; i++){
            String [] row = values[i];

            res.append(i + " | ");
            for(int j=0; j<row.length; j++){
                res.append(values[i][j] + " | ");
            }
            res.append("\n");
        }

        return res.toString();
    }
}
