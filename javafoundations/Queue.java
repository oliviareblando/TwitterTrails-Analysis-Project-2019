package javafoundations;
/**
 * Defines the interface to a queue ADT.
 * @version 2019.10.25
 */

public interface Queue<T>
{
/**
     * Adds the specified element to the rear of the queue.
     * 
     * @param The element to be enqueued into the queue
     */ 
   public void enqueue (T element);

/**
     * Removes and returns the element at the front of the queue.
     * 
     * @return the element removed from the front of the queue.
     */
   public T dequeue();

   /**
     * Returns the element at the front of the queue
     * without removing it.
     * @return the element at the front of the queue
     */
   public T first();

   /**
     * Returns true if the queue contains no elements,
     *  false otherwise.
     * 
     * @return true if the queue is empty, false otherwise.
     */
   public boolean isEmpty();

   /**
     * Returns the number of elements in the queue.
     * 
     * the number of elements in the queue
     */
   public int size();

   /**
     * Returns a string representation of the queue.
     * 
     * a string representation of the queue
     */
   public String toString();
}
