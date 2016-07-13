package ar.com.nicobrest.mobileinspections.dao;

import ar.com.nicobrest.mobileinspections.exception.DragonBallUserAlreadyExistsException;
import ar.com.nicobrest.mobileinspections.exception.DragonBallUserNotFoundException;
import ar.com.nicobrest.mobileinspections.model.DragonBallUser;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.Resource;

/**
 *        In-Memory DAO for the test endpoint dragonball.
 *         
 * @author nbrest
 */
public class DragonBallUserDaoInMemory implements DragonBallUserDao {

  private static Map<String, DragonBallUser> dragonBallUsers;
  private static Map<Long, String> dragonBallUsernamesById;

  @Autowired
  private DragonBallUser gohanDragonBallUser;

  // @AutoWired + @Qualifier("gotenDragonBallUser")
  @Resource(name = "gotenDragonBallUser")
  private DragonBallUser gotenDragonBallUser;

  /**
   * Static inner class that generates Ids.
   * 
   * @author nbrest 
   */
  private static class IdGenerator { 
    
    private static final AtomicInteger sequence = new AtomicInteger(1);

    private IdGenerator() {}

    /**      
     *      Return next number in the sequence.
     *      
     * @author nbrest
     * @return Long
     */
    public static Long getId() {
      return Long.valueOf(sequence.getAndIncrement());
    } 
  }
  
  /**
   *      Constructors.   
   * 
   * @author nbrest
   */
  public DragonBallUserDaoInMemory() {

    initRepository();
  }

  /**
   *      Getters and setters.
   *          
   * @author nbrest
   * @param gohanDragonBallUser DragonBallUser
   */
  public void setGohanDragonBallUser(DragonBallUser gohanDragonBallUser) {

    this.gohanDragonBallUser = gohanDragonBallUser;
  }

  /**
   *      Getters and setters.
   *          
   * @author nbrest
   * @return DragonBallUser
   */
  public DragonBallUser getGohanDragonBallUser() {

    return this.gohanDragonBallUser;
  }

  /**
   *      Getters and setters.
   *          
   * @author nbrest
   * @param gotenDragonBallUser DragonBallUser
   */
  public void setGotenDragonBallUser(DragonBallUser gotenDragonBallUser) {

    this.gotenDragonBallUser = gotenDragonBallUser;
  }

  /**
   *      Getters and setters.
   *         
   * @author nbrest
   * @return DragonBallUser
   */
  public DragonBallUser getGotenDragonBallUser() {

    return this.gotenDragonBallUser;
  }

  /**
   *      Initialize In-Memory repository.
   *         
   * @author nbrest
   */
  private static void initRepository() {

    dragonBallUsers = new HashMap<String, DragonBallUser>();
    dragonBallUsernamesById = new HashMap<Long, String>();
    
    DragonBallUser user1 = new DragonBallUser(IdGenerator.getId(), "goku", "goku@dbz.com", 
        49, 30, 1000);
    dragonBallUsers.put(user1.getUsername(), user1);
    dragonBallUsernamesById.put(user1.getId(), user1.getUsername());
    
    DragonBallUser user2 = new DragonBallUser();
    user2.setId(IdGenerator.getId());
    user2.setAge(29);
    user2.setEmail("gohan@dbz.com");
    user2.setUsername("gohan");
    user2.setPowerLevel(20);
    user2.setStamina(1000);
    dragonBallUsers.put(user2.getUsername(), user2);
    dragonBallUsernamesById.put(user2.getId(), user2.getUsername());

    DragonBallUser user3 = new DragonBallUser(IdGenerator.getId(), "goten", "goten@dbz.com", 
        19, 10, 1000);
    dragonBallUsers.put(user3.getUsername(), user3);
    dragonBallUsernamesById.put(user3.getId(), user3.getUsername());
  }
  
  /**
   *      Adds a new DragonBallUser to the repository.
   *           
   * @author nbrest
   * @throws DragonBallUserAlreadyExistsException User defined exception
   */
  public Long createDragonBallUser(DragonBallUser dragonBallUser) 
      throws DragonBallUserAlreadyExistsException {

    if (dragonBallUsers.get(dragonBallUser.getUsername()) != null) {
      throw new DragonBallUserAlreadyExistsException("DragonBallUser with username " 
          + dragonBallUser.getUsername() + " already exists in the repository.");
    }
    dragonBallUser.setId(IdGenerator.getId());
    dragonBallUsers.put(dragonBallUser.getUsername(), dragonBallUser);
    dragonBallUsernamesById.put(dragonBallUser.getId(), dragonBallUser.getUsername());
    return dragonBallUser.getId();
  }

  /**
   *      Returns a single instance of a DragonBallUser.
   *           
   * @author nbrest
   * @throws DragonBallUserNotFoundException User defined exception
   */
  public DragonBallUser getDragonBallUser(String username) throws DragonBallUserNotFoundException {

    DragonBallUser user = dragonBallUsers.get(username);

    if (user == null) {
      throw new DragonBallUserNotFoundException("DragonBallUser with username " 
          + username + " was not found in the repository.");
    }
    return user;
  }

  /**
   *      Updates an existing DragonBallUser in the repository.
   *      
   * @author nbrest
   */
  public void updateDragonBallUser(DragonBallUser dragonBallUser) 
      throws DragonBallUserNotFoundException, DragonBallUserAlreadyExistsException {

    // Check that the user being updated exists in the repo
    if (dragonBallUsernamesById.get(dragonBallUser.getId()) == null) {
      throw new DragonBallUserNotFoundException("DragonBallUser with id " 
          + dragonBallUser.getId() + " was not found in the repository.");
    }
    
    //If the username changes, check that the new username doesn´t already exist in the repo
    if (!dragonBallUser.getUsername().equals(dragonBallUsernamesById.get(dragonBallUser.getId()))) {
      if (dragonBallUsers.get(dragonBallUser.getUsername()) != null) {
        throw new DragonBallUserAlreadyExistsException("DragonBallUser with username " 
            + dragonBallUser.getUsername() + " already exists in the repository.");
      }
    }

    // Remove old entry for the updated user
    dragonBallUsers.remove(dragonBallUsernamesById.get(dragonBallUser.getId()));
    dragonBallUsernamesById.remove(dragonBallUser.getId());
    
    // Insert the new entry for the updated user
    dragonBallUsers.put(dragonBallUser.getUsername(), dragonBallUser);
    dragonBallUsernamesById.put(dragonBallUser.getId(), dragonBallUser.getUsername());
  }

  /**
   *      Deletes a DragonBallUser from the repository.
   *      
   * @author nbrest
   * @throws DragonBallUserNotFoundException User defined exception
   */
  public DragonBallUser deleteDragonBallUser(Long id) 
      throws DragonBallUserNotFoundException {

    String username = dragonBallUsernamesById.remove(id);
    if (username == null) {
      throw new DragonBallUserNotFoundException("DragonBallUser with id " 
          + id + " was not found in the repository.");
    }
    DragonBallUser removedUser = dragonBallUsers.remove(username); 
    
    return removedUser;
  }

  /**
   *      Returns all the DragonBallUsers in the repository.
   *         
   * @author nbrest
   */
  public List<DragonBallUser> getAllDragonBallUsers() {

    List<DragonBallUser> usersList = new ArrayList<DragonBallUser>(dragonBallUsers.values());

    return usersList;
  }
}
