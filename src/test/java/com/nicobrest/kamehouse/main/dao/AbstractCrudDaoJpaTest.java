package com.nicobrest.kamehouse.main.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.nicobrest.kamehouse.main.exception.KameHouseConflictException;
import com.nicobrest.kamehouse.main.exception.KameHouseNotFoundException;
import com.nicobrest.kamehouse.testmodule.model.DragonBallUser;
import com.nicobrest.kamehouse.testmodule.testutils.DragonBallUserTestUtils;

import java.util.List;

/**
 * Abstract class to group common test functionality for CRUD Jpa DAOs.
 * 
 * @author nbrest
 *
 */
public abstract class AbstractCrudDaoJpaTest<T, D> extends AbstractDaoJpaTest<T, D> {

  public static final Long INVALID_ID = 987987L;

  /**
   * Create entity test.
   */
  protected void createTest(CrudDao<T> dao, Class<T> clazz, T entity) {
    Long createdId = dao.create(entity);

    Identifiable identifiableEntity = (Identifiable) entity;
    identifiableEntity.setId(createdId);
    T createdEntity = findById(clazz, createdId);
    assertEquals(entity, createdEntity);
    testUtils.assertEqualsAllAttributes(entity, createdEntity);
  }

  /**
   * Create entity ConflictException test.
   */
  public void createConflictExceptionTest(CrudDao<T> dao, T entity) {
    thrown.expect(KameHouseConflictException.class);
    thrown.expectMessage("ConstraintViolationException: Error inserting data");
    dao.create(entity);
    Identifiable identifiableEntity = (Identifiable) entity;
    identifiableEntity.setId(null);

    dao.create(entity);
  }

  /**
   * Read entity test.
   */
  public void readTest(CrudDao<T> dao, T entity) {
    persistEntityInRepository(entity);
    Identifiable identifiableEntity = (Identifiable) entity;

    T returnedEntity = dao.read(identifiableEntity.getId());

    assertNotNull(returnedEntity);
    assertEquals(entity, returnedEntity);
    testUtils.assertEqualsAllAttributes(entity, returnedEntity);
  }

  /**
   * Read all entities test.
   */
  public void readAllTest(CrudDao<T> dao, List<T> entitiesList) {
    for (T entity : entitiesList) {
      persistEntityInRepository(entity);
    }

    List<T> returnedList = dao.readAll();

    assertEquals(entitiesList, returnedList);
    testUtils.assertEqualsAllAttributesList(entitiesList, returnedList);
  }

  /**
   * Update entity test.
   */
  public void updateTest(CrudDao<T> dao, Class<T> clazz, T entity, T updatedEntity) {
    persistEntityInRepository(entity);
    Identifiable identifiableEntity = (Identifiable) entity;
    Identifiable identifiableUpdatedEntity = (Identifiable) updatedEntity;
    identifiableUpdatedEntity.setId(identifiableEntity.getId());

    dao.update(updatedEntity);

    T returnedEntity = findById(clazz, identifiableUpdatedEntity.getId());
    assertEquals(entity, returnedEntity);
    testUtils.assertEqualsAllAttributes(updatedEntity, returnedEntity);
  }

  /**
   * Update entity NotFoundException test.
   */
  public void updateNotFoundExceptionTest(CrudDao<T> dao, Class<T> clazz, T entity) {
    thrown.expect(KameHouseNotFoundException.class);
    thrown.expectMessage(
        clazz.getSimpleName() + " with id " + INVALID_ID + " was not found in the repository.");
    Identifiable identifiableEntity = (Identifiable) entity;
    identifiableEntity.setId(INVALID_ID);

    dao.update(entity);
  }

  /**
   * Delete entity test.
   */
  public void deleteTest(CrudDao<T> dao, T entity) {
    persistEntityInRepository(entity);
    Identifiable identifiableEntity = (Identifiable) entity;

    T deletedEntity = dao.delete(identifiableEntity.getId());

    assertEquals(entity, deletedEntity);
    testUtils.assertEqualsAllAttributes(entity, deletedEntity);
  }

  /**
   * Delete entity NotFoundException test.
   */
  public void deleteNotFoundExceptionTest(CrudDao<T> dao, Class<T> clazz) {
    thrown.expect(KameHouseNotFoundException.class);
    thrown.expectMessage(
        clazz.getSimpleName() + " with id " + INVALID_ID + " was not found in the repository.");

    dao.delete(INVALID_ID);
  }
}
