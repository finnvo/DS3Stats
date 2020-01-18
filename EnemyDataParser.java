import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;
import java.util.stream.Collectors;

///////////////////////////////////////////////////////////////////////////////
//
// Title: EnemyDataParser
// Files: Enemy.java, B2_3Tree.java, EnemyDataParser.java
//
// Author: Finn Van Order
// Email: finnvanorder@gmail.com
//
///////////////////////////////////////////////////////////////////////////////


/**
 * Creates an object containing a B tree of enemy objects along with a few ancillary lists for
 * fast access of frequent values
 * @author fvanorde
 *
 */
public class EnemyDataParser {

  private ArrayList<String> areaList;
  private HashSet<String> weaknessSet;
  private HashSet<String> resistancesSet;
  private HashSet<String> immunitiesSet;
  private B2_3Tree<String, Enemy> tree;

  public EnemyDataParser() {
    areaList = new ArrayList<String>();
    weaknessSet = new HashSet<String>();
    resistancesSet = new HashSet<String>();
    immunitiesSet = new HashSet<String>();
    tree = new B2_3Tree<String, Enemy>();
  }


  /**
   * @return the areaSet
   */
  public ArrayList<String> getAreaList() {
    return areaList;
  }
  
  /**
   * @return an ArrayList of enemy weaknesses
   */
  public ArrayList<String> getWeaknessList() {
    ArrayList<String> list = new ArrayList<String>();
    list.addAll(weaknessSet);
    list.sort(null);
    return list;
  }
  
  /**
   * @return an ArrayList of enemy resistances
   */
  public ArrayList<String> getResistancesList() {
    ArrayList<String> list = new ArrayList<String>();
    list.addAll(resistancesSet);
    list.sort(null);
    return list;
  }
  
  /**
   * @return an ArrayList of enemy immunities
   */
  public ArrayList<String> getImmunitiesList() {
    ArrayList<String> list = new ArrayList<String>();
    list.addAll(immunitiesSet);
    list.sort(null);
    return list;
  }

  /**
   * @return the 2-3 tree of enemy objects
   */
  public B2_3Tree<String, Enemy> getTree() {
    return tree;
  }
  
  /*
   * Sets the tree field of this parser to the given 2-3 tree
   */
  public void setTree(B2_3Tree<String, Enemy> tree) {
    this.tree = tree;
  }


  /**
   * Parses a CSV file and creates a 2-3 tree from its contents if in the valid format.
   * Valid CSV format:
   *    Header: Enemy,Area,Type,Weaknesses,Resistances,Immunities,HP,Souls
   *    Body: (Values corresponding to header row)
   * @param file
   */
  public void parseCSV(File file) {
    int ln = 0;
    // parse csv
    try {
      Scanner csvScnr = new Scanner(file);
      while (csvScnr.hasNextLine()) {
        String row = csvScnr.nextLine();
        String[] data = row.split(",");
        ln++;
        if (ln == 1) { // Strip off the header row
          continue;
        } 
        if (data.length < 8) { // Sanity check to prevent ArrayIndexOutOfBounds exceptions
          continue;
        }
        try {
          String name = data[0].trim();
          String area = data[1].trim();
          String type = data[2].trim();
          HashSet<String> weaknesses = generateSetFromString(data[3]);
          HashSet<String> resistances = generateSetFromString(data[4]);
          HashSet<String> immunities = generateSetFromString(data[5]);
          String hp = data[6].trim();
          String souls = data[7].trim();
          Enemy enemy = new Enemy(name, area, type, weaknesses, resistances, immunities, hp, souls);
          tree.insert(enemy.getKey(), enemy);
          // Keep running list of areas/weaknesses/resistances/immunities for faster access in 
          // drawing the GUI
          if (!areaList.contains(area)) {
            areaList.add(area);
          }
          weaknessSet.addAll(weaknesses);
          resistancesSet.addAll(resistances);
          immunitiesSet.addAll(immunities);
        } catch (Exception e) {
          System.out.println(e.getMessage());
        }
      }
      csvScnr.close();
    } catch (FileNotFoundException e) { // File issues handled in Main.java
    }
  }

  /**
   * Generates a set from a semi-colon delimited string
   * @param str
   * @return HashSet<String> from the semi-colon delimited string
   */
  private HashSet<String> generateSetFromString(String str) {
    HashSet<String> set = new HashSet<String>();
    if (str == null) {
      return set;
    }
    String[] data = str.split(";");
    for (int i = 0; i < data.length; i++) {
      set.add(data[i].trim());
    }
    return set;
  }
  
  /**
   * Searches the tree for an enemy with the given name. Searches all game areas for the enemy due
   * to some enemies residing in multiple maps.
   * @param key - name of the enemy to search for
   * @return - ArrayList of all enemies with the given name
   */
  public ArrayList<Enemy> search(String key) {
    ArrayList<Enemy> enemyList = new ArrayList<Enemy>();
    Iterator<String> itr = areaList.iterator();
    while (itr.hasNext()) {
      String searchKey = key.toUpperCase().concat(" - " +itr.next().toUpperCase());
      Enemy enemy = tree.get(searchKey);
      if (enemy != null) {
        enemyList.add(enemy);
      }
    }
    return enemyList;
  }
  
  /**
   * Searches the tree for an enemy with the given name. 
   * @param name - name of the enemy to search for
   * @return - enemy with the given name
   */
  public Enemy nameSearch(String name) {
    Enemy enemy = null;
    ArrayList<Enemy> enemyList = this.getTree().traverse();
    ArrayList<Enemy> temp = (ArrayList<Enemy>) enemyList.stream()
        .filter(e -> e.getName().equals(name))
        .limit(1)
        .collect((Collectors.toList()));
    enemy = temp.get(0);
    return enemy;
  }

  /**
   * @param args
   */
  public static void main(String[] args) {
    // TODO Auto-generated method stub

  }

}
