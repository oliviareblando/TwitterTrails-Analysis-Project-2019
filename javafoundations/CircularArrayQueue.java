//********************************************************************
//  CircularArrayQueue.java       Java Foundations
//
//  Represents an array implementation of a queue in which neither
//  end of the queue is fixed in the array. The variables storing the
//  indexes of the front and rear of the queue continually increment
//  as elements are removed and added, cycling back to 0 when they
//  reach the end of the array.
//********************************************************************

package javafoundations;

import javafoundations.exceptions.*;

/**
 * A linked implementation of the Queue Interface.
 */
public class CircularArrayQueue<T> implements Queue<T>
{
    private final int DEFAULT_CAPACITY = 3;
    private int front, rear, count;
    private T[] queue;

    /**
     * Constructor
     * Creates an empty queue using the default capacity.
     */
    public CircularArrayQueue()
    {
        front = rear = count = 0;
        queue = (T[]) (new Object[DEFAULT_CAPACITY]);
    }

 
    /**
     * Adds the specified element to the rear of the queue, expanding
     * the capacity of the queue array if necessary.
     * 
     * @param The element to be enqueued into the queue
     */
    public void enqueue (T element)
    {
        if (count == queue.length) {
            expandCapacity();
        }

        queue[rear] = element;
        rear = (rear+1) % queue.length;
        count++;
    }
    
    /**
     * Creates a new array to store the contents of this
     * queue with twice the capacity of the old one.
     */
    public void expandCapacity()
    {
        T[] larger = (T[])(new Object[queue.length*2]);

        for (int index=0; index < count; index++) {
            larger[index] = queue[(front+index) % queue.length];
        }

        front = 0;
        rear = count;
        queue = larger;
    }

    /**
     * Removes and returns the element at the front of the queue.
     * 
     * @return the element removed from the front of the queue.
     * @throws EmptyCollectionException if this queue is empty
     */
    public T dequeue () throws EmptyCollectionException {
        if (count == 0){
            throw new EmptyCollectionException ("Dequeue" + 
                                               "failed. Empty q.");
        }
      
        T temp = queue[front]; 
        front = (front + 1) % queue.length; // move front by 1,
        // wrap around if needed
        count --; 
        
        return temp;
    }

    /**
     * Returns the element at the front of the queue
     * without removing it.
     * 
     * @return the element at the front of the queue
     * @throws EmptyCollectionException if queue is empty
     */
    public T first () throws EmptyCollectionException {
        if (count == 0) {
            throw new EmptyCollectionException ("First failed." + 
                                                " Empty q.");
        }
            
        return queue[front];
    }

     /**
     * Returns the number of elements in the queue.
     * 
     * @return the number of elements in the queue
     */
    public int size() {
        return count;
    }

    /**
     * Returns true if the queue contains no elements,
     * false otherwise.
     * 
     * @return true if the queue is empty, false otherwise.
     */
    public boolean isEmpty() { 
        return (count == 0);
    }

    /**
     * Returns a string representation of the queue.
     * 
     * a string representation of the queue
     */
    public String toString() { 
        String str = "<front of queue>\n";
        for (int i = 0; i < count; i++) {
            int index = (front + i) % queue.length; 
            // shifts index by index of first element to 
            // print in correct order
            T elem = queue[index]; 
            str += "(index: " + index + ")"
                   + " elem:" +  elem + "\n"; 
        }
        return str + "<rear of queue>";
    }

    public static void main(String[] args) {
       CircularArrayQueue<Integer> q = new CircularArrayQueue<Integer>();
        
        // // TESTING empty Queue   
        // System.out.println("TESTING: Empty queue" + 
                           // "(expected: True; actual: " +
                           // q.isEmpty() + ")");               
        
        // // TESTING Enqueue
        // System.out.println("");    
        // System.out.println("TESTING: Enqueuing");
        // q.enqueue(5);
        // q.enqueue(7);
        // q.enqueue(9); 
        // System.out.println(q + "\n");
        // System.out.println("The size is:" + 
                           // "(expected: 3; actual: " 
                           // + q.size() + ")"); 
                           
        // // TESTING Dequeue
        // System.out.println("");    
        // System.out.println("TESTING: Dequeue");
        // System.out.println("First element BEFORE " + 
                           // "dequeueing " + 
                           // "(expected: 5, actual: " + 
                            // q.first() + ")"); 
        // q.dequeue();
        // System.out.println(q + "\n");   
        // System.out.println("First element AFTER " + 
                           // "dequeueing " + 
                           // "(expected: 7, actual: " + 
                           // q.first() + ")"); 
                           
        // // New element wraps around queue
        // System.out.println("");    
        // System.out.println("TESTING: Wrapping Enqueue"); 
        // System.out.println("The size is:" + 
                           // "(expected: 2; actual: " 
                           // + q.size() + ")"); 
        // q.enqueue(13);  
        // System.out.println(q + "\n");
        
        // // New element wraps around queue   
        // System.out.println("TESTING: Wrapping Dequeue"); 
        // System.out.println("First element BEFORE " + 
                           // "dequeueing " + 
                           // "(expected: 7, actual: " + 
                           // q.first() + ")"); 
        // q.dequeue();  
        // System.out.println("First element AFTER " + 
                           // "dequeueing " + 
                           // "(expected: 9, actual: " + 
                           // q.first() + ")"); 
        // System.out.println(q + "\n");
        
        // // TESTING expandCapacity & enqueue   
        // System.out.println("TESTING: expandCapacity");
        // q.enqueue(15); 
        // q.enqueue(17);
        // System.out.println(q + "\n");
        
                     
        // // Dequeue (throws exception)
        // LinkedQueue<Integer> m = new LinkedQueue<Integer>();
        // System.out.println("TESTING: Dequeue an empty queue (expected: throw exception)");
        // m.dequeue();
        // System.out.println(m + "\n");
    }
}