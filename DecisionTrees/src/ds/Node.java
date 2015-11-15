package ds;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Node<T> {

    private List<Node<T>> children;     // List of children nodes
    private Node<T> parent;             // Parent node
    private String label;               // Label from parent
    private T data;


    /**
     * Creates a Node given data.
     * @param data
     */
    public Node(T data){
        this.children = new ArrayList<Node<T>>();
        this.parent = null;
        this.label = null;
        this.data = data;
    }

    /**
     * Get the list of children nodes with labels.
     * @return
     */
    public List<Node<T>> getChildren() {
        return children;
    }

    /**
     * Add the child to this node children list with the given label in the relationship.
     * @param child
     */
    public void addChild(Node<T> child, String label) {
        child.parent = this;
        child.label = label;
        this.children.add(child);
    }

    /**
     * Return if a node has children.
     * @return
     */
    public boolean hasChildren(){
        return !children.isEmpty();
    }

    public String toString(){
        return data.toString();
    }

    public String printTree(){
        StringBuilder res = new StringBuilder();

        List<Node<T>> stack = new ArrayList<Node<T>>();
        List<Node<T>> visited = new ArrayList<Node<T>>();
        stack.add(this);
        int level = 0;
        boolean nl = false;

        while(!stack.isEmpty()){
            Node<T> n = stack.get(0);

            if(visited.contains(n)){
                stack.remove(n);
                visited.remove(n);
                level--;
            } else {
                if(nl) {
                    for (int i = 0; i < level; i++)
                        res.append("\t ");
                    nl = false;
                }
                res.append(" -(" + n.label + ")-> " + n.data);
                if(n.hasChildren()){
                    visited.add(n);
                    // Insert children in reverse order
                    List<Node<T>> children = n.getChildren();
                    Collections.reverse(children);
                    stack.addAll(0, children);
                    level++;
                } else {
                    stack.remove(n);
                    res.append("\n"); nl = true;
                }
            }
        }

        res.append("\n");

        return res.toString();
    }

}