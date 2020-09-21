package javafoundations;

import javafoundations.*;
import javafoundations.exceptions.*;
import java.util.Vector;


public class HeapSort<T extends Comparable<T>> {
  
  private MaxHeap<T> maxHeap;
  
  public HeapSort() {
    maxHeap = new LinkedMaxHeap<T>();
  }
  
  public Vector<T> sortInDescending(Vector<T> toSort) {
    
      for (int i = 0; i < toSort.size(); i++){
          //add elements of vector to maxHeap
          maxHeap.add(toSort.get(i));
          
        }
      
      Vector<T> result = new Vector<T>();
      //loop through maxHeap (same size as toSort)
      for (int j = 0; j < toSort.size(); j++) {
          
          //remove the max and add it to a new vector (will be in descending order)
          result.add(maxHeap.removeMax());
        }
      
      return result;
    }
  
  
}
