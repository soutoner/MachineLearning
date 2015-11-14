package ds;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Node<T> {

    private List<Tuple<String, Node<T>>> children;  // List of children (node, relationship label)
    private Node<T> parent;                        // Parent node
    private T data;


    /**
     * Constructor of Node given the data and the children labels.
     * @param data
     * @param labels
     */
    public Node(T data, List<String> labels) {
        this(data, labels, null);
    }

    /**
     * Constructor of Node given the data and the parent node
     * @param data
     * @param parent
     */
    public Node(T data, Node<T> parent) {
        this(data, null, parent);
    }

    /**
     * Constructor of Node given the data, children labels and the parent node.
     * @param data
     * @param labels
     * @param parent
     */
    public Node(T data, List<String> labels, Node<T> parent) {
        this.children = populateChildren(labels);
        this.parent = parent;
        this.data = data;
    }

    /**
     * Add a bunch of children given the label of their relationship.
     * @param labels
     */
    private List<Tuple<String, Node<T>>> populateChildren(List<String> labels){
        if(labels != null)
            return labels.stream()
                    .map(l -> new Tuple<String, Node<T>>(l, new Node<T>(null, this)))
                    .collect(Collectors.toList());
        else
            return new ArrayList<Tuple<String, Node<T>>>();
    }

    /**
     * Get the list of children nodes.
     * @return
     */
    public List<Tuple<String,Node<T>>> getChildren() {
        return children;
    }

    /**
     * Set the list of children nodes given the list of labels.
     * @return
     */
    public void setChildren(List<String> labels) {
        children = populateChildren(labels);
    }

    public void setData(T data) {
        this.data = data;
    }

    public String toString(){
        StringBuilder res = new StringBuilder();

        res.append("** " + data + " **");
        for(Tuple<String, Node<T>> child: getChildren()){
            res.append(child.y);
        }

        return res.toString();
    }

}