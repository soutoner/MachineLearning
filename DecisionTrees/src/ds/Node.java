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
     * Initialize node with given data
     * @param data Data hold by the node
     */
    public Node(T data){
        this.children = new ArrayList<Node<T>>();
        this.parent = null;
        this.label = null;
        this.data = data;
    }

    /**
     * * Add the child to this node children list with the given label in the relationship
     * @param child Node to be added as a child
     * @param label Label of the parent-relationship
     */
    public void addChild(Node<T> child, String label) {
        child.parent = this;
        child.label = label;
        this.children.add(child);
    }

    /**
     * @return List of children nodes
     */
    public List<Node<T>> getChildren() {
        return children;
    }

    /**
     * @return True if node has children.
     */
    public boolean hasChildren(){
        return !children.isEmpty();
    }

    /**
     * @return String representation of the Node
     */
    public String toString(){
        return data.toString();
    }

    /**
     * @return String representation of the tree starting by this node as root
     */
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