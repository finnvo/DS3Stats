import java.util.ArrayList;
import java.util.Iterator;
///////////////////////////////////////////////////////////////////////////////
//
// Title: B2_3Tree
// Files: B2_3Tree.java, InnerNode.java
//
// Author: Finn Van Order
// Email: finnvanorder@gmail.com
//
///////////////////////////////////////////////////////////////////////////////


/**
 * Generic class for a 2-3 tree
 * 
 * @author fvanorde
 *
 * @param <K>
 * @param <V>
 */
public class B2_3Tree<K extends Comparable<? super K>, V> {
  private TreeNode root;

  /**
   * Inner class for tree nodes that stores InnerNode<K, V>
   * 
   * @author fvanorde
   *
   */
  private class TreeNode {
    private InnerNode<K, V> leftNode;
    private InnerNode<K, V> tempMiddleNode;
    private InnerNode<K, V> rightNode;
    private TreeNode leftChild;
    private TreeNode middleChild;
    private TreeNode tempMiddleChild;
    private TreeNode rightChild;
    private TreeNode parent;

    /**
     * Constructor for a TreeNode
     * 
     * @param newNode - InnerNode<K, V> that will become the leftNode of the new TreeNode
     */
    private TreeNode(InnerNode<K, V> newNode) {
      this.leftNode = newNode;
      this.rightNode = null;
      this.leftChild = null;
      this.middleChild = null;
      this.tempMiddleChild = null;
      this.rightChild = null;
      this.parent = null;
    }

    /**
     * Determines whether the given TreeNode is a 2Node
     * 
     * @return - true if the node is a 2Node, else false
     */
    private boolean is2Node() {
      if (this.rightNode == null) {
        return true;
      }
      return false;
    }
  }

  /**
   * Constructor for a B2_3Tree
   */
  public B2_3Tree() {
    root = null;
  }
  
  /**
   * @return true if the tree is empty, else false
   */
  public boolean isEmpty() {
    if (root == null) {
      return true;
    }
    return false;
  }

  /**
   * Inserts the given key and value into the tree.
   * 
   * @param key   - Key to identify the node
   * @param value - value contained by the node
   * @throws IllegalKeyException
   */
  public void insert(K key, V value) throws IllegalKeyException {
    if (key == null) { // Null check
      throw new IllegalKeyException("Cannot store a null value.");
    }
    if (get(key) != null) { // Duplicate check
      return;
    }
    InnerNode<K, V> newNode = new InnerNode<K, V>(key, value);
    // Special case 1: Empty tree
    if (root == null) {
      root = new TreeNode(newNode);
      return;
    } else {
      insert(root, root.parent, newNode);
    }
  }

  /**
   * Inserts the specified InnerNode into the tree.
   * 
   * @param curNode    - current TreeNode
   * @param parentNode - parent TreeNode of curNode
   * @param newNode    - InnerNode to insert
   */
  private void insert(TreeNode curNode, TreeNode parentNode, InnerNode<K, V> newNode) {
    // If curNode has a left child, we are not at a leaf
    if (curNode.leftChild != null) {
      // newNode is to the left of curNode
      if (newNode.getKey().compareTo(curNode.leftNode.getKey()) < 0) {
        insert(curNode.leftChild, curNode, newNode);
        return;
      }
      // curNode is a 2Node or curNode is a 3Node and newNode is between curNode's left and right
      // InnerNodes
      if ((curNode.is2Node()) || (newNode.getKey().compareTo(curNode.rightNode.getKey()) < 0)) {
        insert(curNode.middleChild, curNode, newNode);
        return;
      }
      // curNode is a 3Node and newNode is to its right
      if (newNode.getKey().compareTo(curNode.rightNode.getKey()) > 0) {
        insert(curNode.rightChild, curNode, newNode);
        return;
      }
    }
    // curNode is a leaf
    // Case 1: Insert a node with only one data element
    if (curNode.is2Node()) {
      if (newNode.getKey().compareTo(curNode.leftNode.getKey()) < 0) {
        curNode.rightNode = curNode.leftNode;
        curNode.leftNode = newNode;
      } else {
        curNode.rightNode = newNode;
      }
      return;
    }
    // Case 1.5 parentNode is null
    if (parentNode == null) {
      InnerNode<K, V>[] nodeAry = sortInnerNodes(curNode.leftNode, curNode.rightNode, newNode);
      TreeNode newParent = new TreeNode(nodeAry[1]);
      newParent.leftChild = new TreeNode(nodeAry[0]);
      newParent.middleChild = new TreeNode(nodeAry[2]);
      // Update parent of new child nodes
      newParent.leftChild.parent = newParent;
      newParent.middleChild.parent = newParent;
      root = newParent;
      return;
    }
    InnerNode<K, V>[] nodeAry = sortInnerNodes(curNode.leftNode, curNode.rightNode, newNode);
    // Create new nodes
    TreeNode newLeftOrMiddleChild = new TreeNode(nodeAry[0]);
    TreeNode newMiddleOrRightChild = new TreeNode(nodeAry[2]);

    // Case 2: Insert in a node with two data elements whose parent contains only one data element.
    if (parentNode.is2Node()) {
      // curNode is left child of parent
      if (curNode.equals(parentNode.leftChild)) {
        parentNode.rightNode = parentNode.leftNode;
        parentNode.leftNode = nodeAry[1]; // Middle node value moves up to parent
        // Move middle child over to prepare for split
        parentNode.rightChild = parentNode.middleChild;
        parentNode.leftChild = newLeftOrMiddleChild;
        parentNode.middleChild = newMiddleOrRightChild;
      } // curNode is middle child of parent
      else {
        parentNode.rightNode = nodeAry[1];
        parentNode.middleChild = newLeftOrMiddleChild;
        parentNode.rightChild = newMiddleOrRightChild;
      }
      // Update parent of new child nodes
      newLeftOrMiddleChild.parent = parentNode;
      newMiddleOrRightChild.parent = parentNode;
      return;
    }
    // Case 3: Insert in a node with two data elements whose parent also contains two data elements.
    if (!parentNode.is2Node()) {
      TreeNode fourNode = createTempFourNode(curNode, newNode);
      insertFourNode(parentNode, fourNode);
    }
  }

  /**
   * Inserts a four node into the tree by splitting the four node and recursively pushing the new
   * parent of the split four node up the tree
   * 
   * @param target
   * @param fourNode
   */
  private void insertFourNode(TreeNode target, TreeNode fourNode) {
    TreeNode insertNode = splitFourNode(fourNode);
    if (target == null) {
      root = insertNode;
      return;
    } else {
      insertFourNodeHelper(target, insertNode);
    }
  }

  /**
   * Helper method for insertFourNode. Inserts node into the specified target node and recursively
   * calls insertFourNode if insertion results in a new four node.
   * 
   * @param target     - node to insert into
   * @param insertNode - split four node to insert
   */
  private void insertFourNodeHelper(TreeNode target, TreeNode insertNode) {
    TreeNode tmp = null;
    // Inserting into a 2Node -> done after this
    if (target.is2Node()) {
      if (insertNode.leftNode.getKey().compareTo(target.leftNode.getKey()) < 0) {
        target.rightNode = target.leftNode;
        target.leftNode = insertNode.leftNode;

        target.rightChild = target.middleChild;
        target.middleChild = insertNode.middleChild;
        target.leftChild = insertNode.leftChild;
      }
      if (insertNode.leftNode.getKey().compareTo(target.leftNode.getKey()) > 0) {
        target.rightNode = insertNode.leftNode;

        target.middleChild = insertNode.leftChild;
        target.rightChild = insertNode.middleChild;
      }
      insertNode.middleChild.parent = target;
      insertNode.leftChild.parent = target;
      return;
    }
    // Inserting into a 3Node -> create another temporary 4Node and continue recursive insert
    if (!target.is2Node()) {
      tmp = createTempFourNode(target, insertNode.leftNode);
      // insertNode is leftmost node
      if (insertNode.leftNode.getKey().compareTo(target.leftNode.getKey()) < 0) {
        tmp = updateNodeLinkages(tmp, insertNode.leftChild, insertNode.middleChild,
            target.middleChild, target.rightChild);
        insertFourNode(target.parent, tmp);
        return;
      } else if (insertNode.leftNode.getKey().compareTo(target.rightNode.getKey()) < 0) {
        tmp = updateNodeLinkages(tmp, target.leftChild, insertNode.leftChild,
            insertNode.middleChild, target.rightChild);
        insertFourNode(target.parent, tmp);
        return;
      } else {
        tmp = updateNodeLinkages(tmp, target.leftChild, target.middleChild, insertNode.leftChild,
            insertNode.middleChild);
        insertFourNode(target.parent, tmp);
        return;
      }
    }
  }

  /**
   * Updates linkages for a TreeNode
   * 
   * @param node    - Node whose linkages need to be updated
   * @param lChild  - TreeNode that should be attached as node's left child
   * @param m1Child - TreeNode that should be attached as node's middle child
   * @param m2Child - TreeNode that should be attached as node's temporary middle child
   * @param rChild  - TreeNode that should be attached as node's right child
   * @return TreeNode with updated child and parent pointers
   */
  private TreeNode updateNodeLinkages(TreeNode node, TreeNode lChild, TreeNode m1Child,
      TreeNode m2Child, TreeNode rChild) {
    // Update node's child node pointers
    node.leftChild = lChild;
    node.middleChild = m1Child;
    node.tempMiddleChild = m2Child;
    node.rightChild = rChild;
    // Update child nodes' parent pointers
    if (lChild != null) {
      node.leftChild.parent = node;
    }
    if (m1Child != null) {
      node.middleChild.parent = node;
    }
    if (m2Child != null) {
      node.tempMiddleChild.parent = node;
    }
    if (rChild != null) {
      node.rightChild.parent = node;
    }
    return node;
  }

  /**
   * Creates a temporary four node for use in the recursive insertFourNode method
   * 
   * @param node         - three node to turn into a four node
   * @param newInnerNode - new InnerNode to add to the four node
   * @return - four node
   */
  private TreeNode createTempFourNode(TreeNode node, InnerNode<K, V> newInnerNode) {    
    InnerNode<K, V>[] nodeAry = sortInnerNodes(node.leftNode, node.rightNode, newInnerNode);
    TreeNode temp = new TreeNode(nodeAry[0]);
    temp.leftChild = node.leftChild;
    temp.middleChild = node.middleChild;
    temp.rightChild = node.rightChild;
    temp.tempMiddleNode = nodeAry[1];
    temp.rightNode = nodeAry[2];
    return temp;
  }

  /**
   * Splits a four node into two new TreeNodes and links them to a new parent TreeNode
   * 
   * @param node
   * @return parent TreeNode of the split four node
   */
  private TreeNode splitFourNode(TreeNode node) {
    // Create parent from temp middle node
    TreeNode newParent = new TreeNode(node.tempMiddleNode);
    // Split off new left and right child nodes
    TreeNode newLeftNode = new TreeNode(node.leftNode);
    newLeftNode.parent = newParent;
    TreeNode newRightNode = new TreeNode(node.rightNode);
    newRightNode.parent = newParent;
    newParent.leftChild = newLeftNode;
    newParent.middleChild = newRightNode;
    // Copy over linkages
    if (node.leftChild != null) {
      newLeftNode.leftChild = node.leftChild;
      newLeftNode.leftChild.parent = newLeftNode;
    }
    if (node.middleChild != null) {
      newLeftNode.middleChild = node.middleChild;
      newLeftNode.middleChild.parent = newLeftNode;
    }
    if (node.tempMiddleChild != null) {
      newRightNode.leftChild = node.tempMiddleChild;
      newRightNode.leftChild.parent = newRightNode;
    }
    if (node.rightChild != null) {
      newRightNode.middleChild = node.rightChild;
      newRightNode.middleChild.parent = newRightNode;
    }
    return newParent;
  }

  /**
   * Creates a sorted array of three InnerNodes
   * 
   * @param n1 - InnerNode<K, V>
   * @param n2 - InnerNode<K, V>
   * @param n3 - InnerNode<K, V>
   * @return array of InnerNode<K, V> objects in ascending order of <K>
   */
  private InnerNode<K, V>[] sortInnerNodes(InnerNode<K, V> n1, InnerNode<K, V> n2,
      InnerNode<K, V> n3) {
    @SuppressWarnings("unchecked")
    InnerNode<K, V>[] nodeAry = (InnerNode<K, V>[]) new InnerNode[] {n1, n2, n3};
    int i = 0;
    boolean swap = true;
    while (swap) {
      swap = false;
      i++;
      for (int n = 0; n < nodeAry.length - i; n++) {
        if (nodeAry[n].getKey().compareTo(nodeAry[n + 1].getKey()) > 0) {
          InnerNode<K, V> tmp = nodeAry[n];
          nodeAry[n] = nodeAry[n + 1];
          nodeAry[n + 1] = tmp;
          swap = true;
        }
      }
    }
    return nodeAry;
  }

  /**
   * Searches the tree for a node with the specified key
   * 
   * @param key - key to search for
   * @return - value of the node with specified key, else null
   */
  public V get(K key) {
    // Empty tree
    if (root == null) {
      return null;
    }
    // Start recursive search
    InnerNode<K,V> node = getInnerNode(key, root);
    if (node != null) {
      return node.getValue();
    }
    return null;
  }
  
  /**
   * Recursive helper method for get(K key).
   * 
   * @param key     - key to search for
   * @param curNode - current node in search stack
   * @return - InnerNode with the specified key, else null
   */
  private InnerNode<K, V> getInnerNode(K key, TreeNode curNode) {
    InnerNode<K, V> value = null;
    // Check for match
    // 2Node or match in leftNode of 3Node
    if (curNode.leftNode.getKey().compareTo(key) == 0) {
      return curNode.leftNode;
    }
    // 3Node and match in rightNode
    else if ((!curNode.is2Node()) && curNode.rightNode.getKey().compareTo((key)) == 0) {
      return curNode.rightNode;
    }
    // At a leaf and key not found -> return null
    else if ((curNode.leftChild == null)) {
      return value;
    }
    // Not at leaf so keep searching
    // Search left
    else if (key.compareTo(curNode.leftNode.getKey()) < 0) {
      value = getInnerNode(key, curNode.leftChild);
    }
    // Search middle
    // If 2Node and greater than leftNode
    else if ((curNode.is2Node() && key.compareTo(curNode.leftNode.getKey()) > 0)) {
      value = getInnerNode(key, curNode.middleChild);
    }
    // threeNode and less than rightNode
    else if ((!curNode.is2Node() && (key.compareTo(curNode.rightNode.getKey()) < 0))) {
      value = getInnerNode(key, curNode.middleChild);
    }
    // Search right
    else if (((!curNode.is2Node())) && ((key.compareTo(curNode.rightNode.getKey()) > 0))) {
      value = getInnerNode(key, curNode.rightChild);
    }
    return value;
  }

  /**
   * Recursively searches tree for a TreeNode containing the specified key
   * @param key - key to search for
   * @param curNode - current TreeNode
   * @return - TreeNode with the given key, else null
   */
  private TreeNode getTreeNode(K key, TreeNode curNode) {
    TreeNode value = null;
    // Check for match
    // 2Node or match in leftNode of 3Node
    if (curNode.leftNode.getKey().compareTo(key) == 0) {
      return curNode;
    }
    // 3Node and match in rightNode
    else if ((!curNode.is2Node()) && curNode.rightNode.getKey().compareTo((key)) == 0) {
      return curNode;
    }
    // At a leaf and key not found -> return null
    else if ((curNode.leftChild == null)) {
      return null;
    }
    // Not at leaf so keep searching
    // Search left
    else if (key.compareTo(curNode.leftNode.getKey()) < 0) {
      value = getTreeNode(key, curNode.leftChild);
    }
    // Search middle
    // If 2Node and greater than leftNode
    else if ((curNode.is2Node() && key.compareTo(curNode.leftNode.getKey()) > 0)) {
      value = getTreeNode(key, curNode.middleChild);
    }
    // threeNode and less than rightNode
    else if ((!curNode.is2Node() && (key.compareTo(curNode.rightNode.getKey()) < 0))) {
      value = getTreeNode(key, curNode.middleChild);
    }
    // Search right
    else if (((!curNode.is2Node())) && ((key.compareTo(curNode.rightNode.getKey()) > 0))) {
      value = getTreeNode(key, curNode.rightChild);
    }
    return value;
  }

  /**
   * Traverses the tree in order and adds all objects to an ArrayList 
   * @return ArrayList containing all objects in the tree
   */
  public ArrayList<V> traverse() {
    ArrayList<V> list = new ArrayList<V>();
    if (root == null) {
      return list;
    }
    list = traverseHelper(list, root);
    return list;
  }
  
  /**
   * Recursive helper method for traverse
   * @param list - ArrayList to add objects to
   * @param node - Current TreeNode in the recursion stack
   * @return - ArrayList of objects
   */
  private ArrayList<V> traverseHelper(ArrayList<V> list, TreeNode node) {
    if (node.leftChild == null) {
      list.add(node.leftNode.getValue());
      if (node.rightNode != null) {
        list.add(node.rightNode.getValue());
      }
    } // End leaf
    else if (node.is2Node()) {
      traverseHelper(list,node.leftChild);
      list.add(node.leftNode.getValue());
      traverseHelper(list,node.middleChild);
    } else if (!node.is2Node()) {
      traverseHelper(list,node.leftChild);
      list.add(node.leftNode.getValue());
      traverseHelper(list,node.middleChild);
      list.add(node.rightNode.getValue());
      traverseHelper(list,node.rightChild);
    }    
    return list;
  }



  /**
   * @param args
   */
  public static void main(String[] args) {
    // TODO Auto-generated method stub
    B2_3Tree<Integer, String> tree = new B2_3Tree<Integer, String>();
    try {
      tree.insert(1, "One");
      tree.insert(40, "Fourty"); 
      tree.insert(14, "Fourteen");
      tree.insert(34, "Thirty Four");
      tree.insert(15, "Fiveteen");
      tree.insert(6, "Six");
      //tree.printHelper();
      tree.insert(7, "Seven");
      //tree.printHelper();
      tree.insert(35, "Thirty Five");
      tree.insert(8, "Eight");
      tree.insert(9, "Nine");
      tree.insert(22, "Twenty Two");
      tree.insert(32, "Thirty Two");
      tree.insert(23, "Twenty Three");
      tree.insert(36, "Thirty Six");
      tree.insert(24, "Twenty Four");
      tree.insert(10, "Ten");
      tree.insert(27, "Twenty Seven");
      tree.insert(28, "Twenty Eight");
      tree.insert(37, "Thirty Seven");
      tree.insert(29, "Twenty Nine");
      tree.insert(30, "Thirty");
      tree.insert(11, "Eleven");
      tree.insert(12, "Twelve");
      tree.insert(33, "Thirty Three");
      tree.insert(13, "Thirteen");
      tree.insert(31, "Thirty One");
      tree.insert(5, "Five");
      tree.insert(21, "Twenty One");
      tree.insert(16, "Sixteen");
      tree.insert(17, "Seventeen");
      tree.insert(38, "Thirty Eight");
      tree.insert(18, "Eighteen");
      tree.insert(19, "Nineteen");
      tree.insert(20, "Twenty");
      tree.insert(2, "Two");
      tree.insert(3, "Three");
      tree.insert(39, "Thirty Nine");
      tree.insert(4, "Four");
      tree.insert(25, "Twenty Five");
      tree.insert(26, "Twenty Six");
      tree.insert(0, "Zero");

      //tree.printHelper();
      System.out.println("\n--------------------------------------------------");
      System.out.println("Traverse test:"); 
      ArrayList<String> list = tree.traverse();
      Iterator<String> itr = list.iterator();
      while (itr.hasNext()) {
        System.out.println(itr.next()); 
      }
    } catch (IllegalKeyException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
}
