
import java.util.*;
import java.io.IOException;
import java.util.Hashtable;
import javafoundations.*;
import java.net.*;
/**
 * Reads "RATgraph.tgf" and runs a few methods to investigate 
 * the graph. 
 *
 * @author Anushe Sheikh & Olivia Reblando
 * @version 12/5
 */
public class Investigate
{
    /**
     * Retrieves data (html code) from a web server using the Scanner class
     * 
     * @param storyCode - storyID of story whose title we w
     * @returns title - title of the story
     */
    public static String getStoryTitle(String storyCode) {
        String title = "";
        try {
            String TTrails = "http://twittertrails.wellesley.edu" + 
                "/~trails/stories/title.php?id=";
            URL u = new URL(TTrails + storyCode); 

            Scanner urlScan = new Scanner(u.openStream()).useDelimiter("<");
            // delimiter helps avoid unwanted html code

            while(urlScan.hasNext()) {
                urlScan.nextLine();
                title += urlScan.nextLine();
            }

        } catch (IOException ex) {
            System.out.println(ex);
        }
        return title;
    }

    /**
     * Compare the length of the iterator created by a DFS from each user
     * to determine which user is in the largest connected component
     * 
     * @param ratty - RATGraph object that contains bipartite graph variable
     * @returns mostConnected - user and size of largest connected component
     */
    public static String findMostConnectedUser(RATgraph ratty){
        int longestDFS = 0;
        String mostConnected = ""; 
        int currentDFS = 0;
        String currentUser = "";

        Vector<String> users = getUsers(ratty);
        for (int i = 0; i < users.size(); i++ ) {
            currentUser = users.get(i);
            currentDFS = ratty.getGraph().DFS(currentUser).size(); // 
            // if iterator returned by DFS size is larger than longestDFS so far
            if(currentDFS >= longestDFS){ 
                longestDFS = currentDFS;
                mostConnected += currentUser + "," + longestDFS + "\n";
            }

        }
        return mostConnected;
    }

    /**
     * Compare the length of the iterator created by a DFS from each story
     * to determine which story is in the largest connected component
     * 
     * @param ratty - RATGraph object that contains bipartite graph variable
     * @returns mostConnected - story and size of DFS
     */
    public static String findMostConnectedStory(RATgraph ratty){
        int longestDFS = 0;
        String mostConnected = ""; 
        int currentDFS = 0;
        String currentStory = "";

        Vector<String> stories = getStories(ratty);
        for (int i = 0; i < stories.size(); i++ ) {
            currentStory = stories.get(i);
            currentDFS = ratty.getGraph().DFS(currentStory).size(); // length of iterator
            // returned by DFS from currentStory
            if(currentDFS >= longestDFS){
                longestDFS = currentDFS;
                mostConnected += currentStory +": " + getStoryTitle(currentStory) + 
                ", " + longestDFS + "\n";
            }

        }
        return mostConnected;
    }

    /**
     * Determine the most central user in the largest connected component. 
     * A central user is one that has the total smallest sum of distances 
     * from itself to every other node
     * 
     * @param ratty - RATGraph object that contains bipartite graph variable
     * @returns mostCentral - user and size of BFS
     */
    public static String findMostCentralUser(RATgraph ratty){
        String mostCentral = " "; 
        int shortestSumSoFar = 1000000000; 
        String currentUser = "";
        String otherNode = "";

        Vector<String> users = getUsers(ratty);
        for(int i = 0; i< users.size(); i++) {
            currentUser = users.get(i);

            Vector<String> vertices = ratty.getGraph().getVertices();
            int sumSoFar = 0;
            for (int j = 0; j < vertices.size(); j++) {
                otherNode = vertices.get(j);
                // calculate the sum of distance from node i to node j
                int currentBFS = ratty.getGraph().BFSFind(currentUser, otherNode).size();
                sumSoFar += currentBFS; 

            }
            // if the sum of the distances from itself to every other node is smaller
            // than the smallestSumSoFar, replace it 
            if (sumSoFar <= shortestSumSoFar) {
                shortestSumSoFar = sumSoFar;
                mostCentral += currentUser + "," + shortestSumSoFar + "\n";
            }
        }
        return mostCentral;
    }

    /**
     * Determine the most central story in the largets connected component. 
     * A central story is one that has the total smallest sum of distances 
     * from itself to every other vertex
     * 
     * @param ratty - RATGraph object that contains bipartite graph variable
     * @returns mostConnected - story and size of DFS
     */
    public static String findMostCentralStory(RATgraph ratty){
        String mostCentral = " "; 
        int shortestSumSoFar = 1000000000; 
        String currentStory = "";
        String otherNode = "";

        Vector<String> stories = getStories(ratty);
        for(int i = 0; i < stories.size(); i++) {
            currentStory = stories.get(i);
            int sumSoFar = 0; // total sum of distances from node i 
            //to every other node
            Vector<String> vertices = ratty.getGraph().getVertices();
            for (int j = 0; j < vertices.size(); j++) {
                otherNode = vertices.get(j);
                // calculate the sum of distance from node i to node j
                int currentBFS = ratty.getGraph().BFSFind(currentStory, otherNode).size();
                sumSoFar += currentBFS; 
            }
            // if the sum of the distances from itself to every other node is smaller
            // than the smallestSumSoFar, replace it 
            if (sumSoFar <= shortestSumSoFar) {
                shortestSumSoFar = sumSoFar;
                mostCentral += currentStory + "," + shortestSumSoFar + ",";
            }
        }
        return mostCentral;
    }

    /** Compare the length of the iterator created by a DFS from each story
     * to determine which story is in the largest connected component
     * @param ratty - RATGraph object that contains bipartite graph variable, 
     *        topNum - number ofmost active users to be displayed
     * @returns result - array of most active users
     */
    public static Vector<String> getUserLeaderboard(RATgraph ratty, int topNum){
        //create a collection to hold all the activity scores
        Vector<Integer> activityScores = new Vector<Integer>();
        //create hashtable to hold the users with their respective scores
        Hashtable<Integer, LinkedList<String>> activityWithUser = new Hashtable<Integer, LinkedList<String>>();
        //create Linkedlist to hold the returned LinkedList from later 
        //getSuccessors() calls
        LinkedList<String> currentSuccessors = new LinkedList<String>();
        //loop through all of the user accounts in RATgraph object ratty
        for (int i = 0; i < getUsers(ratty).size(); i++){
            //get the current user's successors and hold its size
            String currentUser = getUsers(ratty).get(i);
            currentSuccessors = ratty.getGraph().getSuccessors(currentUser);
            int currentScore = currentSuccessors.size();
            activityScores.add(currentScore);
            //if the hashtable already contains the activity score, the user with the same score is added to the linkedlist value 
            if(activityWithUser.contains(currentScore)){
                activityWithUser.get(currentScore).add(currentUser);
            }
            //if the hashtable does not yet contain any users with the activity 
            //score,create a new LinkedList and its score as the key
            else{
                LinkedList<String> temp = new LinkedList<String>();
                temp.add(currentUser);
                activityWithUser.put(currentScore, temp);
            }
        }
        //instantiate a HeapSort object so that non-static sortInDescending order
        //method can be called
        HeapSort<Integer> sorter = new HeapSort<Integer>();
        //sort the Vector of activity scores
        Vector<Integer> sortedScores = sorter.sortInDescending(activityScores);
        Vector<String> result = new Vector<String>();
        //loop through the top scores wanted by driver and retrieve users in
        //LinkedList using the scores in sortedScores as keys
        for(int i = 0; i < topNum; i++){
            int score = sortedScores.get(i);
            String intoResult = Integer.toString(i+1);
            LinkedList<String> users = activityWithUser.get(score); 
            for (int j = 0; j <users.size(); j++) {
                intoResult+= "," + users.get(j);
            }
            intoResult += "," + Integer.toString(score) + "\n";
            //populate the Vector with Strings of "Rank, Screen name, Score"
            result.add(intoResult);
        }
        return result;
    }

    /**Compare the length of the iterator created by a DFS from each story
     * to determine which story is in the largest connected component
     * @param ratty - RATGraph object that contains bipartite graph variable, 
     *        topNum - number of most story users to be displayed
     * @returns result - array of most active stories 
     */
    public static Vector<String> getStoryLeaderboard(RATgraph ratty, int topNum){
        //create a collection to hold all the popularity scores
        Vector<Integer> popularityScores = new Vector<Integer>();
        //create hashtable to hold the stories with their respective scores
        Hashtable<Integer, LinkedList<String>> popularityWithStory = new Hashtable<Integer, LinkedList<String>>();
        //create Linkedlist to hold the returned LinkedList from later 
        //getSuccessors() calls
        LinkedList<String> currentSuccessors = new LinkedList<String>();
        //loop through all of the stories in RATgraph object ratty
        for (int i = 0; i < getStories(ratty).size(); i++){
            String currentStory = getStories(ratty).get(i);
            //get the current story's successors and hold its size
            currentSuccessors = ratty.getGraph().getSuccessors(currentStory);
            int currentScore = currentSuccessors.size();
            //check if this size has been seen before, if not add to the Vector
            //of scores 
            if(!popularityScores.contains(currentScore)){
                popularityScores.add(currentScore);
            }
            //if the hashtable already contains a popularity score, the story 
            // with the same score is added to the linkedlist value associated
            //with the popularity score key
            if(popularityWithStory.contains(currentScore)){
                if (!popularityWithStory.get(currentScore).contains(currentStory)){
                    popularityWithStory.get(currentScore).add(currentStory);
                }
            }
            //if the hashtable does not yet contain any stories with the popularity score,
            //meaning its a new score, create a new LinkedList with the 
            //current story as the elemnet and its score as the key

            else{
                LinkedList<String> temp = new LinkedList<String>();
                temp.add(currentStory);

                popularityWithStory.put(currentScore, temp);
            }
        }
        //instantiate a HeapSort object so that non-static 
        //sortInDescending order method can be called
        HeapSort<Integer> sorter = new HeapSort<Integer>();
        //sort the Vector of activity scores
        Vector<Integer> sortedScores = sorter.sortInDescending(popularityScores);
        Vector<String> result = new Vector<String>();
        //loop through the top scores wanted by driver and retrieve stories 
        //in the LinkedList value
        //using the scores in sortedScores as keys
        for(int i = 0; i < topNum; i++){
            int score = sortedScores.get(i);
            System.out.println("Sorted scores" + sortedScores);
            String intoResult = Integer.toString(i+1);
            LinkedList<String> stories = popularityWithStory.get(score); 
            for (int j = 0; j <stories.size(); j++) {
                intoResult+= "," + stories.get(j);
                intoResult += ":" + getStoryTitle(stories.get(j));
            }
            intoResult += "," + Integer.toString(score) + "\n";
            //populate the Vector with Strings of "Rank, Story title, Score"
            result.add(intoResult);
        }
        return result;
    }

    /**
     * Helper method that creates a Vector of all users in vertices 
     * 
     * @param ratty - RATGraph object that contains bipartite graph variable, 
     * @returns users - Vector of all users
     */
    public static Vector<String> getUsers(RATgraph ratty){
        Vector<String> users = new Vector<String>();
        Vector<String> allVerts = ratty.getGraph().getVertices();
        for(int i = 0; i < allVerts.size(); i++){
            if(isUser(allVerts.get(i))){
                users.add(allVerts.get(i));
            }
        }
        return users;
    }

    /**
     * Helper method that creates a Vector of all stories in vertices 
     * 
     * @param ratty - RATGraph object that contains bipartite graph variable, 
     * @returns stories - Vector of all stories
     */
    public static Vector<String> getStories(RATgraph ratty){
        Vector<String> stories = new Vector<String>();
        Vector<String> allVerts = ratty.getGraph().getVertices();
        for(int i = 0; i < allVerts.size(); i++){
            if(isStory(allVerts.get(i))){
                stories.add(allVerts.get(i));
            }
        }
        return stories;
    }

    /**
     * Helper method that checks if a vertex is a user
     * 
     * @param s - String
     * @returns true if String is a user
     */
    public static boolean isUser(String s){
        char c = s.toUpperCase().charAt(0);
        return (c =='U');
    }

    /**
     * Helper method that checks if a vertex is a story 
     * 
     * @param s - String 
     * @returns true if String is a story 
     */
    public static boolean isStory(String s){
        char c = s.toUpperCase().charAt(0);
        return (c != 'U');
    }

    /**
     * Main method for testing
     */
    public static void main(String[] args)
    {
        // read RATgraph.tgf
        RATgraph ratty = new RATgraph("All_Russian-Accounts-in-TT-stories.csv.tsv");
        ratty.getGraph().saveToTGF("RATgraph.tgf"); 

        // find user with the largest connected component
        //System.out.println("**Most connected User**"); 
        //System.out.println(findMostConnectedUser(ratty));

        // find story with the largest connected component
        System.out.println(); 
        //System.out.println("**Most connected Story**"); 
        //System.out.println(findMostConnectedStory(ratty));

        //testing most central user 
        System.out.println(); 
        //System.out.println("**Most Central User**"); 
        //System.out.println(findMostCentralUser(ratty));

        System.out.println(); 
        //System.out.println("**Most Central Story**"); 
        //System.out.println(findMostCentralStory(ratty));

        

        // find most popular users
        System.out.println(); 
        System.out.println("***User Leaderboard***"); 
        System.out.println("Expected line 1: 1,Jenn_Abrams,144"); 
        System.out.println(getUserLeaderboard(ratty, 5));

        // find most popular stories
        System.out.println();     
        System.out.println("***Story Leaderboard***"); 
        System.out.println(getStoryLeaderboard(ratty, 20));

        // print vertices
        System.out.println();     
        System.out.println("*** Number of vertices ***"); 
        System.out.println(ratty.getGraph().getNumVertices());

        // testing getStoryTitle method
        System.out.println(); 
        System.out.println("TESTING getStoryTitle()"); 
        System.out.println(getStoryTitle("147909124"));
        System.out.println(getStoryTitle("2701461461"));

       

    }
}
