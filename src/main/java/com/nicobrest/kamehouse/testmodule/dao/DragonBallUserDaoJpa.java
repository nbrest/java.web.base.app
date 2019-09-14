package com.nicobrest.kamehouse.testmodule.dao;

import com.nicobrest.kamehouse.main.dao.AbstractDaoJpa;
import com.nicobrest.kamehouse.main.exception.KameHouseNotFoundException;
import com.nicobrest.kamehouse.main.exception.KameHouseServerErrorException;
import com.nicobrest.kamehouse.testmodule.model.DragonBallUser;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

/**
 * JPA DAO for the DragonBallUser test entities.
 *
 * @author nbrest
 */
public class DragonBallUserDaoJpa extends AbstractDaoJpa implements DragonBallUserDao {

  private static final String DBUSER_WITH_ID = "DragonBallUser with id ";
  private static final String NOT_FOUND_IN_REPOSITORY = " was not found in the repository.";
  private static final String GET_DRAGONBALLUSER = "Get DragonBallUser: {}";

  @Override
  @CacheEvict(value = { "getAllDragonBallUsersCache", "getDragonBallUserCache",
      "getDragonBallUserByUsernameCache", "getDragonBallUserByEmailCache" }, allEntries = true)
  public Long createDragonBallUser(DragonBallUser dragonBallUser) {
    logger.trace("Create DragonBallUser: {}", dragonBallUser);
    createEntityInRepository(dragonBallUser);
    return dragonBallUser.getId();
  }

  @Override
  @Cacheable(value = "getDragonBallUserCache")
  public DragonBallUser getDragonBallUser(Long id) {
    logger.trace(GET_DRAGONBALLUSER, id);
    EntityManager em = getEntityManager();
    DragonBallUser dragonBallUser = null;
    try {
      em.getTransaction().begin();
      Query query = em.createQuery("SELECT dbu from DragonBallUser dbu where dbu.id=:pId");
      query.setParameter("pId", id);
      dragonBallUser = (DragonBallUser) query.getSingleResult();
      em.getTransaction().commit();
    } catch (PersistenceException pe) {
      handlePersistentException(pe);
    } finally {
      em.close();
    }
    return dragonBallUser;
  }

  @Override
  @Cacheable(value = "getDragonBallUserByUsernameCache")
  public DragonBallUser getDragonBallUser(String username) {
    logger.trace(GET_DRAGONBALLUSER, username);
    EntityManager em = getEntityManager();
    DragonBallUser dragonBallUser = null;
    try {
      em.getTransaction().begin();
      Query query = em.createQuery(
          "SELECT dbu from DragonBallUser dbu where dbu.username=:pUsername");
      query.setParameter("pUsername", username);
      dragonBallUser = (DragonBallUser) query.getSingleResult();
      em.getTransaction().commit();
    } catch (PersistenceException pe) {
      handlePersistentException(pe);
    } finally {
      em.close();
    }
    return dragonBallUser;
  }

  @Override
  @Cacheable(value = "getDragonBallUserByEmailCache")
  public DragonBallUser getDragonBallUserByEmail(String email) {
    logger.trace(GET_DRAGONBALLUSER, email);
    EntityManager em = getEntityManager();
    DragonBallUser dragonBallUser = null;
    try {
      em.getTransaction().begin();
      Query query = em.createQuery("SELECT dbu from DragonBallUser dbu where dbu.email=:pEmail");
      query.setParameter("pEmail", email);
      dragonBallUser = (DragonBallUser) query.getSingleResult();
      em.getTransaction().commit();
    } catch (PersistenceException pe) {
      // Iterate through the causes of the PersistenceException to identify and
      // return the correct exception.
      Throwable cause = pe;
      while (cause != null) {
        if (cause instanceof javax.persistence.NoResultException) {
          throw new KameHouseNotFoundException("DragonBallUser with email " + email
              + NOT_FOUND_IN_REPOSITORY);
        }
        cause = cause.getCause();
      }
      throw new KameHouseServerErrorException("PersistenceException in getDragonBallUserByEmail",
          pe);
    } finally {
      em.close();
    }
    return dragonBallUser;
  }

  @Override
  @CacheEvict(value = { "getAllDragonBallUsersCache", "getDragonBallUserCache",
      "getDragonBallUserByUsernameCache", "getDragonBallUserByEmailCache" }, allEntries = true)
  public void updateDragonBallUser(DragonBallUser dragonBallUser) {
    logger.trace("Update DragonBallUser: {}", dragonBallUser);
    EntityManager em = getEntityManager();
    try {
      em.getTransaction().begin();
      DragonBallUser updatedDbUser = em.find(DragonBallUser.class, dragonBallUser.getId());
      if (updatedDbUser != null) {
        updatedDbUser.setAge(dragonBallUser.getAge());
        updatedDbUser.setEmail(dragonBallUser.getEmail());
        updatedDbUser.setPowerLevel(dragonBallUser.getPowerLevel());
        updatedDbUser.setStamina(dragonBallUser.getStamina());
        updatedDbUser.setUsername(dragonBallUser.getUsername());
      }
      em.getTransaction().commit();
      if (updatedDbUser == null) {
        throw new KameHouseNotFoundException(DBUSER_WITH_ID + dragonBallUser.getId()
            + NOT_FOUND_IN_REPOSITORY);
      }
    } catch (PersistenceException pe) {
      handlePersistentException(pe);
    } finally {
      em.close();
    }
  }

  @Override
  @CacheEvict(value = { "getAllDragonBallUsersCache", "getDragonBallUserCache",
      "getDragonBallUserByUsernameCache", "getDragonBallUserByEmailCache" }, allEntries = true)
  public DragonBallUser deleteDragonBallUser(Long id) {
    // find(): returns the entity from the EntityManager if its already in
    // memory. Otherwise it goes to the database to find it.
    // getReference(): Returns a proxy to the real entity. Useful if you need to
    // access the primary key used to look up the entity but not the other data
    // of the object.
    logger.trace("Delete DragonBallUser: {}", id);
    return deleteEntityFromRepository(id, DragonBallUser.class);
  }

  @Override
  @Cacheable(value = "getAllDragonBallUsersCache")
  public List<DragonBallUser> getAllDragonBallUsers() {
    logger.trace("Get all DragonBallUsers");
    return getAllEntitiesFromRepository(DragonBallUser.class);
  }
}
