import java.util.HashSet;

///////////////////////////////////////////////////////////////////////////////
//
// Title: Enemy
// Files: Enemy.java
//
// Author: Finn Van Order
// Email: finnvanorder@gmail.com
//
///////////////////////////////////////////////////////////////////////////////


/**
 * Represents an enemy from Dark Souls 3
 * 
 * @author fvanorde
 *
 */

public class Enemy implements Comparable<Object> {

  private String key;
  private String name;
  private String area;
  private String type;
  private HashSet<String> weaknesses;
  private HashSet<String> resistances;
  private HashSet<String> immunities;
  private String HP;
  private String souls;
  private HashSet<String> drops;


  /**
   * Constructor of an enemy object with all fields.
   * 
   * @param key         - String key of the enemy object (full name + area)
   * @param name-       String name of the enemy
   * @param type        - type of enemy (e.g. Hollow, Abyssal, Boss, etc)
   * @param weaknesses  - String HashSet of the sorts of damage this enemy is weak to
   * @param resistances - String HashSet of the sorts of damage this enemy is resistant to
   * @param immunities  - String HashSet of the sorts of damage this enemy is immune to
   * @param HP          - Number of hit points this enemy has in NG (approx)
   * @param souls       - Number of souls this enemy will give you in NG (approx)
   * @param drops       - String HashSet of the items dropped by this enemy
   * @param tips        - ArrayList of types for fighting this enemy
   */
  public Enemy(String name, String area, String type, HashSet<String> weaknesses,
      HashSet<String> resistances, HashSet<String> immunities, String HP, String souls) {
    this.key = name.concat(" - " + area).toUpperCase();
    this.name = name;
    this.area = area;
    this.type = type;
    this.weaknesses = weaknesses;
    this.resistances = resistances;
    this.immunities = immunities;
    this.HP = HP;
    this.souls = souls;
    this.drops = null;
  }

  /**
   * Constructor of an enemy object with just the two required parameters. All other fields will be
   * set with default values: weaknesses, resistances, immunities; and drops will be initialized
   * with an empty HashSet; HP and souls will be initialized to -1, and tips will be initialized
   * with an empty ArrayList.
   * 
   * @param key   - String key of the enemy object (full name + area)
   * @param name- String name of the enemy
   */
  public Enemy(String name, String area) {
    this.key = name.concat(" - " + area).toUpperCase();
    this.name = name;
    this.area = area;
    this.type = "Generic Enemy";
    this.weaknesses = new HashSet<String>();
    this.resistances = new HashSet<String>();
    this.immunities = new HashSet<String>();
    this.HP = "Unknown";
    this.souls = "Unknown";
    this.drops = new HashSet<String>();
  }

  /**
   * Get the key of the enemy
   * 
   * @return the key
   */
  public String getKey() {
    return key;
  }

  /**
   * Get the name of the enemy
   * 
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * Set the name of the enemy.
   * 
   * @param name - the name to set
   */
  public void setName(String name) {
    if (name == null) {
      return;
    }
    this.name = name;
  }

  /**
   * 
   * @return
   */
  public String getArea() {
    return this.area;
  }

  public void setArea(String area) {
    if (area == null) {
      return;
    }
    this.area = area;
  }

  /**
   * Get the type of the enemy
   * 
   * @return the type
   */
  public String getType() {
    return type;
  }

  /**
   * Set the type of the enemy
   * 
   * @param type the type to set
   */
  public void setType(String type) {
    if (type == null) {
      return;
    }
    this.type = type;
  }

  /**
   * Get the types of damage this enemy is weak to. An enemy is weak to a sort of damage if their
   * damage modifier for a particular type of damage is greater than 1.
   * 
   * @return the weaknesses
   */
  public HashSet<String> getWeaknesses() {
    return weaknesses;
  }

  /**
   * Set the types of damage that this enemy is weak to. "Weakness" is defined by having a negative
   * value for absorption for a particular type of damage.
   * 
   * @param weaknesses the weaknesses to set
   */
  public void setWeaknesses(HashSet<String> weaknesses) {
    if (weaknesses == null) {
      return;
    }
    this.weaknesses.addAll(weaknesses);
  }

  /**
   * Get the types of damage that this enemy is resistant to. An enemy is resistant to a sort of
   * damage if their damage modifier for a particular type of damage is less than 0.5.
   * 
   * @return the resistances
   */
  public HashSet<String> getResistances() {
    return resistances;
  }

  /**
   * Set the types of damage that this enemy is resistant to. "Resistant" is defined by having a
   * value greater than 50 for absorption for a particular type of damage.
   * 
   * @param resistances the resistances to set
   */
  public void setResistances(HashSet<String> resistances) {
    if (resistances == null) {
      return;
    }
    this.resistances.addAll(resistances);
  }

  /**
   * Get the types of damage that this enemy is immune to. An enemy is immune to a sort of damage if
   * their damage modifier for a particular type of damage is 0.0.
   * 
   * @return the immunities
   */
  public HashSet<String> getImmunities() {
    return immunities;
  }

  /**
   * Set the types of damage that this enemy is immune to. "Immune" is defined by having a value
   * greater than or equal to 100 for absorption for a particular type of damage.
   * 
   * @param immunities the immunities to set
   */
  public void setImmunities(HashSet<String> immunities) {
    if (immunities == null) {
      return;
    }
    this.immunities.addAll(immunities);
  }

  /**
   * Returns the approximate number of hit points this enemy has in new game (NG).
   * 
   * @return the number of hit points that this enemy has
   */
  public String getHP() {
    return HP;
  }

  /**
   * Sets the approximate number of hit points this enemy has in new game (NG).
   * 
   * @param HP - the number of hit points that this enemy has
   */
  public void setHP(String HP) {
    if (HP == null) {
      return;
    }
    this.HP = HP;
  }

  /**
   * Returns the approximate number of souls this enemy will give the PC in new game (NG).
   * 
   * @return the number of souls the PC gets for defeating this enemy in NG
   */
  public String getSouls() {
    return souls;
  }

  /**
   * Sets the approximate number of souls this enemy will give the PC in new game (NG).
   * 
   * @param souls - the number of souls this enemy will give in NG
   */
  public void setSouls(String souls) {
    if (souls == null) {
      return;
    }
    this.souls = souls;
  }

  /**
   * Gets a list of potential items that this enemy will drop.
   * 
   * @return a string HashSet of the potential items that this enemy will drop
   */
  public HashSet<String> getDrops() {
    return drops;
  }

  /**
   * Sets the items that this enemy has a chance of dropping.
   * 
   * @param drops - String HashSet of the potential items that this enemy will drop
   */
  public void setDrops(HashSet<String> drops) {
    if (drops == null) {
      return;
    }
    this.drops.addAll(drops);
  }

  @Override
  public String toString() {
    return this.key;
  }

  @Override
  public int compareTo(Object enemy) {
    return this.key.compareTo(enemy.toString());
  }

  @Override
  public boolean equals(Object enemy) {
    return this.key.equalsIgnoreCase(enemy.toString());
  }

  /**
   * Checks whether the enemy is the same (i.e. has the same name) as the passed in enemy
   * @param enemyName - name of the enemy
   * @return true if the enemies have the same name, else false
   */
  public boolean isSameEnemy(String enemyName) {
    if (this.getName().equalsIgnoreCase(enemyName)) {
      return true;
    }
    return false;
  }
  
  /**
   * Checks whether this enemy is the same type as the passed in enemy
   * @param type - enemy type
   * @return true if the enemies have the same type, else false
   */
  public boolean isType(String type) {
    if (this.getType().equalsIgnoreCase(type)) {
      return true;
    }
    return false;
  }

  


  /**
   * 
   * 
   * @param args
   */
  public static void main(String[] args) {
    // TODO Auto-generated method stub

  }



}
