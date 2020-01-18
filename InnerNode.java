///////////////////////////////////////////////////////////////////////////////
//
// Title:           InnerNode
// Files:           InnerNode.java, B2_3Tree.java
//
// Author:          Finn Van Order
// Email:           fvanorder@gmail.com
//
///////////////////////////////////////////////////////////////////////////////


/**
 * Class representing an inner node used in the B2_3Tree class
 * @author fvanorde
 *
 * @param <K>
 * @param <V>
 */
public class InnerNode<K extends Comparable<? super K>, V> {
  private K key;
  private V value;
  
  public InnerNode(K key, V value) {
    this.key = key;
    this.value = value;
  }
  
  

  /**
   * @return the value
   */
  public V getValue() {
    return value;
  }



  /**
   * @param value the value to set
   */
  public void setValue(V value) {
    this.value = value;
  }



  /**
   * @return the key
   */
  public K getKey() {
    return key;
  }



  /**
   * @param args
   */
  public static void main(String[] args) {
    // TODO Auto-generated method stub

  }


}
