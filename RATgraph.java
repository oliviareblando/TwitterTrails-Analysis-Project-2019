import javafoundations.*;
import java.io.*;
import java.util.*;
/**
 * Reads and generates a bipartite graph from the file:
 * All_Russian-Accounts-in-TT-stories.csv.tsv
 *
 * @author Anushe Sheikh and Olivia Reblando
 * @version 12/03/19
 */
public class RATgraph 
{
    // instance variables
    private AdjListsGraph<Object> graph;

    /**
     * Constructor for objects of class RATgraph
     */
    public RATgraph(String inFileName)
    {
        graph = new AdjListsGraph<Object>();
        readFromFile(inFileName);
    }

    /**
     * Reads file and converts each line into some nodes
     * and edges. 
     *
     * @param  inFileName- the file being read
     */
    public void readFromFile(String inFileName)
    {
        try{
            Scanner scan = new Scanner(new File(inFileName));
            scan.nextLine();

            while(scan.hasNextLine()){
                String line = scan.nextLine();
                String[] lines = line.split("[,\\s]+");
                // Create user: name, user_id, tweetcount, story count
                //User newUser = new User(lines[0], lines[1], lines[2], lines[3]);
                String userNum = "U"+lines[0];
                // create a user node
                graph.addVertex(userNum);

                for(int i = 4; i <lines.length; i++){
                    String storyNum = lines[i]; 
                    graph.addVertex(storyNum);
                    // add edge between each story and each user
                    graph.addEdge(userNum, storyNum);
                }

            }

        }catch(FileNotFoundException e){
            System.out.println(e);
        }

    }
    
   /**
     * Getter for graph 
     *
     * @return  graph - bipartite graph
     */ 
    public AdjListsGraph getGraph(){
        return graph;
    }

    /**
     * toString method converts the graph into a nice
     * looking string
     *
     * @returns String 
     */
    public String toString() {
        return graph.toString();
    }

    /**
     * Main method for testing
     */
    public static void main(String[] args) {
        RATgraph ratty = new RATgraph("All_Russian-Accounts-in-TT-stories.csv.tsv");
        
        System.out.println("***TESTING RATgraph***"); 
        System.out.println(ratty); 
        ratty.getGraph().saveToTGF("RATgraph.tgf"); 
    }
}
