package javafoundations;
import java.util.Vector;
import java.util.LinkedList;
import java.util.*;
import java.io.*;

/**
 * Implements the Graph interface. 
 *
 * @author Anushe Sheikh & Olivia Reblando
 * @version 12/7/19
 */
public class AdjListsGraph<T> implements Graph<T>
{
    // instance variables
    private Vector<T> vertices; 
    private Vector<LinkedList<T>> arcs; // each linked list will 
    // hold the set of adjacent vertices to a certain vertex

    /** 
     * Constructor 
     */ 
    public AdjListsGraph(){
        vertices = new Vector<T>();
        arcs = new Vector<LinkedList<T>>();
    }

    /** 
     * Conducts a Breadth-First Search. Never visists a vertex more than once
     * 
     * @param startVertex - vertex that the BFS will begin on 
     * */
    public LinkedList<T> BFS(T startVertex){
        CircularArrayQueue<T> q = new CircularArrayQueue<T>();
        LinkedList<T> iter = new LinkedList<T>();
        Vector<T> visitedVerts =  new Vector<T>();
        
        // early return if the start vertex doesn't exist
        if(! vertices.contains(startVertex)){
            return iter;
        }
        
        visitedVerts.add(startVertex);
        q.enqueue(startVertex); //added the starting vertex to queue
        while(! q.isEmpty()){
            T current = q.first(); 
            LinkedList<T> successors = getSuccessors(current); // get the 
            // successors of the vertex at front and add them to queue
            for(int i = 0; i < successors.size(); i++){
                T currentSuccessor = successors.get(i);
                if(!visitedVerts.contains(currentSuccessor)){
                    q.enqueue(currentSuccessor);
                    visitedVerts.add(currentSuccessor);
                }
            }
            // once each successor has been added, the vertex is marked as
            // visited (added to iterator) and is dequeueued
            iter.add(current);
            q.dequeue();
        }
        return iter;
    }
        /** 
     * Conducts a Breadth-First Search to find a given vertex. Never visists a vertex more than once
     * 
     * @param startVertex - vertex that the BFS will begin on 
     * */
    public LinkedList<T> BFSFind(T origin, T destination){
        CircularArrayQueue<T> q = new CircularArrayQueue<T>();
        LinkedList<T> iter = new LinkedList<T>();
        Vector<T> visitedVerts =  new Vector<T>();
        
        // early return if the start vertex doesn't exist or if origin vertex is same as ddestination
        if(! vertices.contains(origin) || origin.equals(destination)){
            return iter;
        }
        
        visitedVerts.add(origin);
        q.enqueue(origin); //added the starting vertex to queue
        //stops searching if the destination vertex has been found
        while(! q.isEmpty() && !iter.contains(destination)){
            T current = q.first(); 
            LinkedList<T> successors = getSuccessors(current); // get the 
            // successors of the vertex at front and add them to queue
            for(int i = 0; i < successors.size(); i++){
                T currentSuccessor = successors.get(i);
                if(!visitedVerts.contains(currentSuccessor)){
                    q.enqueue(currentSuccessor);
                    visitedVerts.add(currentSuccessor);
                }
            }
            // once each successor has been added, the vertex is marked as
            // visited (added to iterator) and is dequeueued
            iter.add(current);
            q.dequeue();
        }
        return iter;
    }
    
   /** 
     * Conducts a Depth-First Search by visiting all the vertices that it 
     * can reach starting at some vertex. Never visists a vertex more than once
     * 
     * @param startVertex
     * */
    public LinkedList<T> DFS(T startVertex){
        T currentVertex;
        ArrayStack<T> traversalStack = new ArrayStack<T>();
        LinkedList<T> iter = new LinkedList<T>();
        Vector<T> visitedVerts =  new Vector<T>();
        boolean foundASuccessor;
        
        // early return if the start vertex doesn't exist
        if(! vertices.contains(startVertex)){
            return iter;
        }

        traversalStack.push(startVertex); 
        iter.add(startVertex);
        visitedVerts.add(startVertex); // mark vertex as visited

        // while all possible vertices have not been visited
        while(!traversalStack.isEmpty()){
            currentVertex = traversalStack.peek();  
            foundASuccessor = false; 
            LinkedList<T> successors = getSuccessors(currentVertex); // get 
            // successors of element at the top of the stack
            for(int i = 0; i < successors.size() && !foundASuccessor; i++){
                T currentSuccessor = successors.get(i);
                if(! visitedVerts.contains(currentSuccessor)){
                    traversalStack.push(currentSuccessor);
                    iter.add(currentSuccessor);
                    visitedVerts.add(currentSuccessor);
                    foundASuccessor = true;
                }

            }
            
            // if no successors that have not been visited can be found 
            // pop the element from the stack
            if (!foundASuccessor && !traversalStack.isEmpty()){
                traversalStack.pop();
            }
        }

        return iter;
    }

    /** 
     * Getter for Vector that contains all vertices
     * 
     * @return vertices
     */  
    public Vector<T> getVertices(){
        return vertices;
    }

    /** 
     * Returns a boolean indicating whether this graph is empty.
     * A graph is empty when it contains no vertices
     *  
     * @return true if this graph is empty, false otherwise.
     */
    public boolean isEmpty(){
        return vertices.size() == 0;
    }

    /** 
     * Getter for number of vertices in the graph
     * 
     * @return the number of vertices in this graph
     */
    public int getNumVertices(){
        return vertices.size();
    }

    /** 
     * Getter for number of arcs in graph
     * 
     * @return ret the number of arcs in this graph
     */
    public int getNumArcs(){
        int ret = 0;
        for(int i = 0; i < arcs.size(); i++){ // test
            ret += arcs.get(i).size();
        }
        return ret;
    }

    /** 
     * Determines if an arc (direct connection) exists between the 
     * first and second vertex. 
     *
     * @return true or false
     */
    public boolean isArc (T vertex1, T vertex2){
        // checks if vertex exists before checking if arc exists
        if (vertices.contains(vertex1)){
            // find index of vertex in vertices before searching 
            // whetherthe linked list contains the second vertex 
            int index = vertices.indexOf(vertex1); 
            return arcs.get(index).contains(vertex2);
        }
        return false;
    }

    /** 
     * Determines if an edge exists between the vertex1 and vertex2, eg.
     * an arc exists from vertex1 and vertex2, and an arc exists from 
     * vertex2 to vertex1.
     *  
     * @return true or false
     */
    public boolean isEdge (T vertex1, T vertex2){
        return isArc(vertex1, vertex2) && isArc(vertex2, vertex1);
    }

    /** 
     * Returns true if the graph is undirected, that is, for every
     * pair of nodes i,j for which there is an arc, the opposite arc
     * is also present in the graph, false otherwise.  
     * 
     * @return true if the graph is undirected, false otherwise
     */
    public boolean isUndirected(){
        for(int i = 0; i < vertices.size(); i++)
            for(int j = 0; j < arcs.get(i).size(); j++){
                if(! isEdge (vertices.get(i), arcs.get(i).get(j))){
                    return false;
                }
            }
        return true;
    }

    /** 
     * Adds the given vertex to this graph. If the given vertex already
     * exists, the graph does not change
     * 
     * @param vertex 
     */
    public void addVertex (T vertex){
        if(!vertices.contains(vertex)){
            vertices.add(vertex);
            arcs.add(new LinkedList<T>());
        }
    }

    /** 
     * Removes the given vertex from this graph. If the given vertex 
     * does not exist, the graph does not change.
     * 
     * @param vertex
     */
    public void removeVertex (T vertex){
        if(vertices.contains(vertex)){
            int index = vertices.indexOf(vertex);
            arcs.remove(index);
            vertices.remove(index);
        }
    }

    /** 
     * Inserts an arc between two given vertices of this graph.
     * If at least one of the vertices does not exist, the graph 
     * is not changed.
     * 
     * @param vertex1 - the origin of new arc, vertex2 - the 
     *        destination of new arc
     */
    public void addArc (T vertex1, T vertex2){
        if(vertices.contains(vertex1) &&
           vertices.contains(vertex2) && 
           !isArc(vertex1, vertex2)){ // prevents duplicates
            arcs.get(vertices.indexOf(vertex1)).add(vertex2);
        }
    }

    /** 
     * Removes the arc between two given vertices of this graph.
     * If one of the two vertices does not exist in the graph,
     * the graph does not change.
     * 
     * @param the origin of the arc to be removed from this graph
     * @param the destination of the arc to be removed from this graph
     * 
     */
    public void removeArc (T vertex1, T vertex2){
        if(vertices.contains(vertex1) &&
           vertices.contains(vertex2) && 
           isArc(vertex1, vertex2)){ // prevents dupliates
            int indexOfVertex1 = vertices.indexOf(vertex1);
            int index = arcs.get(indexOfVertex1).indexOf(vertex2);
            arcs.get(indexOfVertex1).remove(index);
        }
    }

    /** 
     * Inserts the edge between the two given vertices of this graph,
     * if both vertices exist and edge does not already exist,
     * else the graph is not changed.
     * 
     * @param the origin of the edge to be added to this graph
     * @param the destination of the edge to be added to this graph
     * 
     */
    public void addEdge (T vertex1, T vertex2){
        addArc(vertex1,  vertex2);
        addArc(vertex2,  vertex1);
    }

    /** 
     * Removes the edge between the two given vertices of this graph,
     * if both vertices exist, else the graph is not changed.
     * 
     * @param the origin of the edge to be removed from this graph
     * @param the destination of the edge to be removed from this graph
     * 
     */
    public void removeEdge (T vertex1, T vertex2){
        removeArc(vertex1,  vertex2);
        removeArc(vertex2,  vertex1);
    }

    /** 
     * Return all the vertices, in this graph, adjacent to the given 
     * vertex.
     * 
     * @param A vertex in the graph whose successors will be returned.
     * @return A LinkedList containing all the vertices x in the graph,
     * for which an arc exists from the given vertex to x (vertex -> x).
     *
     */
    public LinkedList<T> getSuccessors(T vertex){
        int index = vertices.indexOf(vertex);
        return arcs.get(index);
    }

    /** 
     * Return all the vertices x, in this graph, that precede a given
     * vertex.
     * 
     * @param A vertex in the graph whose predecessors will be returned.
     * @return LinkedList containing all the vertices x in the graph,
     * for which an arc exists from x to the given vertex (x -> vertex).
     * 
     */
    public LinkedList<T> getPredecessors(T vertex){
        // create linked list to store pred
        LinkedList<T> temp = new LinkedList<T>(); 
        // iterates through arc vector
        for(int i = 0; i < arcs.size() ; i++) {
            // if vertex is in current linked list
            // add the vertext of the linked list to temp
            if(arcs.get(i).indexOf(vertex) != -1){
                temp.add(vertices.get(i));
            }
        }
        return temp;
    }
    
    /** 
     * Writes this graph into a file in the TGF format.
     * 
     * @param the name of the file where this graph will be written 
     * in the TGF format.
     * */
    public void saveToTGF(String tgf_file_name){
        //need to fix
        try { 
            PrintWriter writer = new PrintWriter(new File(tgf_file_name));
            // add 1 to index so vertices begin from 1 not 0
            for(int index = 0; index < vertices.size(); index ++){
                writer.println(index + 1 + " " + vertices.get(index)); 
            }
            writer.println("#");
            for(int i = 0; i < vertices.size(); i++){
                LinkedList<T> successors = getSuccessors(vertices.get(i));
                for(int j = 0; j < successors.size(); j++){
                    int successor = vertices.indexOf(successors.get(j)) + 1;
                    writer.println(i + 1 + " "+ successor);
                }
            }
            writer.close();
        } catch (IOException ex){
            System.out.println(ex);
        }
    }

    /** 
     * Returns a string representation of this graph, containing its 
     * vertices and arcs
     * 
     * @return String 
     */
    public String toString(){
        String ret = "Vertices: \n";
        ret += vertices.toString();
        ret += "\nEdges: ";
        for(int i = 0; i < vertices.size(); i++){
            ret += "\nfrom " + vertices.get(i) +": "; 
            ret += arcs.get(i);
        }
        return ret;
    }

    /** 
     * Main method for Testing
     * 
     * @return String 
     */
    public static void main(String[] args){
        AdjListsGraph<String> bipartite  = new AdjListsGraph<String>();
        
        System.out.println("***TESTING***");
        System.out.println();
        
        System.out.println("isEmpty()--> expected: true, actual: " + bipartite.isEmpty());
  
        bipartite.addVertex("A");
        bipartite.addVertex("B");
        bipartite.addVertex("C");
        bipartite.addVertex("D");
        bipartite.addVertex("E");
        bipartite.addVertex("F");

        bipartite.addEdge("A", "B");
        bipartite.addEdge("A", "F");
        bipartite.addEdge("C", "D");
        bipartite.addEdge("C", "F");
        bipartite.addEdge("E", "B");
        
        System.out.println();
        System.out.println("***TESTING getSuccessors() and getPredecessors()***");
        System.out.println("expected: [B, F], actual: " + bipartite.getSuccessors("A")); 
        System.out.println("expected: [A, E], actual: " + bipartite.getPredecessors("B")); 

        System.out.println();
        System.out.println();
        System.out.println("*** TESTING BFS ON BIPARTITE ***");  
        System.out.println("A--> expected: [A, B, F, E, C, D] , actual: " + bipartite.BFS("A")); 
        System.out.println("B--> expected: [B, A, E, F, C, D] , actual: " + bipartite.BFS("B"));
        System.out.println("C--> expected: [C, D, F, A, B, E], actual: " + bipartite.BFS("D")); 
        System.out.println("TESTING DFS ON BIPARTITE (UNDIRECTED)");
        System.out.println("A--> expected:[A, B, E, F, C, D], actual: " + bipartite.DFS("A")); 
        System.out.println("B--> expected:[B, A, F, C, D, E], actual: " + bipartite.DFS("B"));
        System.out.println("C--> expected:[C, D, F, A, B, E], actual: " + bipartite.DFS("D"));

        System.out.println(""); 
        System.out.println("TESTING saveToTGF");
        bipartite .saveToTGF("Bipartite.txt");

        System.out.println();
        System.out.println();
        
        AdjListsGraph<String> tree = new AdjListsGraph<String>();
        tree.addVertex("A");
        tree.addVertex("B");
        tree.addVertex("C");
        tree.addVertex("D");
        tree.addVertex("E");
        
        tree.addEdge("A", "B");
        tree.addEdge("A", "C");
        tree.addEdge("B", "D");
        tree.addEdge("B", "E");
        System.out.println("***TESTING BFS ON TREE(UNDIRECTED)***");
        System.out.println("A--> expected: [A, B, C, D, E], actual: " + tree.BFS("A"));
        System.out.println("***TESTING BFS Finder ON TREE(UNDIRECTED)***");
        System.out.println("A to C--> expected: [A, B, C], actual: " + tree.BFSFind("A", "C"));
        
        System.out.println("***TESTING DFS ON TREE(UNDIRECTED)***");
        System.out.println("A--> expected: [A, B, D, E, C], actual: " + tree.DFS("A"));
        
        tree.saveToTGF("Tree.tgf");
        
        AdjListsGraph<String> discon = new AdjListsGraph<String>();
        discon.addVertex("A");
        discon.addVertex("B");
        discon.addVertex("C");
        
        discon.addEdge("A", "B");
        System.out.println();
        System.out.println();
        System.out.println("***TESTING BFS ON Disconnected(UNDIRECTED)***");
        System.out.println("A--> expected: [A, B], actual: " + discon.BFS("A"));
        System.out.println("C--> expected: [C], actual: " + discon.BFS("C"));
        
        System.out.println("***TESTING DFS ON Disconnected(UNDIRECTED)***");
        System.out.println("A--> expected: [A, B], actual: " + discon.DFS("A"));
        System.out.println("C--> expected: [C], actual: " + discon.DFS("C"));
        
        discon.saveToTGF("Disconnected.tgf");
        
        AdjListsGraph<String> cycle = new AdjListsGraph<String>();
        cycle.addVertex("A");
        cycle.addVertex("B");
        cycle.addVertex("C");
        
        cycle.addEdge("A", "B");
        cycle.addEdge("A", "C");
        cycle.addEdge("B", "C");
        System.out.println();
        System.out.println();
        System.out.println("***TESTING BFS ON Cycle (UNDIRECTED)***");
        System.out.println("A--> expected: [A, B, C], actual: " + cycle.BFS("A"));
        System.out.println("C--> expected: [C, A, B], actual: " + cycle.BFS("C"));
        System.out.println("***TESTING DFS ON Cycle (UNDIRECTED)***");
        System.out.println("A--> expected: [A, B, C], actual: " + cycle.DFS("A"));
        System.out.println("C--> expected: [C, A, B], actual: " + cycle.DFS("C"));
        cycle.saveToTGF("Cycle.tgf");
        
    }
}
