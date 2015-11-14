package ds;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Table {

    private List<String> attributes;    // Attributes for each column
    private String[][] values;          // Actual data of the table
    private int classifyingColumn;      // By default, last column


    /**
     * Constructor that initialize the table with the given data.
     * @param data
     */
    public Table(List<List<String>> data){
        int rows = data.size()-1; // We don't count attributes
        int cols = data.get(0).size();

        this.values = new String[rows][cols];
        this.attributes = new ArrayList<String>();
        this.classifyingColumn = cols-1;

        populate(data);
    }

    /**
     * Insert the given data into the table.
     * @param data
     */
    public void populate(List<List<String>> data){
        // Set labels
        attributes = data.get(0);

        for(int i=1; i<data.size(); i++){
            List<String> row = data.get(i);
            for(int j=0; j<row.size(); j++){
                values[i-1][j] = row.get(j);
            }
        }
    }

    public Table restrict(Tuple<String, String> restriction){
        List<List<String>> restrictedTable = new ArrayList<List<String>>();

        String attribute = restriction.x;
        String value = restriction.y;
        int restrictedColumnIdx = attributes.indexOf(attribute);

        // Add restricted attributes
        List<String> restrictedAttrs = attributes.stream()
                .filter(a -> !a.equalsIgnoreCase(attribute))
                .collect(Collectors.toList());
        restrictedTable.add(restrictedAttrs);

        // Add restricted values
        for(int i=0;i<values.length; i++) {
            if(values[i][restrictedColumnIdx].equalsIgnoreCase(value)){
                String[] row = values[i];
                List<String> restrictedRow = new ArrayList<String>();

                for (int j = 0; j < row.length; j++) {
                    if(j != restrictedColumnIdx)
                        restrictedRow.add(values[i][j]);
                }

                restrictedTable.add(restrictedRow);
            }
        }


        return new Table(restrictedTable);
    }

    /**
     * Return a list of Tuples(Desired column_i, Classifying column_i)
     * @param attr
     * @return
     */
    public List<Tuple<String, String>> getMixedColumn(String attr){
        return getMixedColumn(attributes.indexOf(attr));
    }

    /**
     * Return a list of Tuples(Desired column_i, Classifying column_i)
     * @param idx
     * @return
     */
    public List<Tuple<String, String>> getMixedColumn(int idx){
        List<Tuple<String, String>> mixedCol = new ArrayList<Tuple<String,String>>();

        for(int i=0; i<values.length; i++){
            mixedCol.add(new Tuple<String, String>(values[i][idx], values[i][classifyingColumn]));
        }

        return mixedCol;
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

        for(int i=0; i<values.length; i++){
            col.add(values[i][idx]);
        }

        return col;
    }

    /**
     * Return the column that is being classified.
     * @return
     */
    public List<String> classColumn(){
        return getColumn(classifyingColumn);
    }

    /**
     * Get the attributes list except the classifying attribute.
     * @return
     */
    public List<String> getAttributes() {
        return attributes.stream()
                .filter(a -> attributes.indexOf(a) != classifyingColumn)
                .collect(Collectors.toList());
    }

    /**
     * Set the classifying column to the given attribute.
     * @param attribute
     */
    public void setClassifyingColumn(String attribute){
        classifyingColumn = attributes.indexOf(attribute);
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
