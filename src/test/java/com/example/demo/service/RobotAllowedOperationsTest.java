package com.example.demo.service;

import static com.example.demo.entity.Robot.Type.DRIVER;
import static com.example.demo.entity.Robot.Type.LOADER;
import static com.example.demo.entity.Robot.Type.VACUUM;
import static com.example.demo.entity.RobotTestBuilder.aRobot;
import static com.example.demo.service.OperationStatus.ALLOWED;
import static com.example.demo.service.OperationStatus.RESTRICTED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.transaction.annotation.Propagation.NOT_SUPPORTED;

import com.example.demo.testutils.TestDBFacade;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
@Import(TestDBFacade.Config.class)
@Transactional(propagation = NOT_SUPPORTED)
@Commit
class RobotAllowedOperationsTest {

  @Autowired
  private TestDBFacade db;
  @Autowired
  private RobotAllowedOperations robotAllowedOperations;

  @TestConfiguration
  static class Config {

    @Bean
    public RobotAllowedOperations serverAllowedOperations() {
      return new RobotAllowedOperations();
    }

    @Bean
    public RobotRestrictions serverRestrictions() {
      return new RobotRestrictions();
    }
  }

  @Test
  //@Disabled("Always fails due to rollback-only behavior")
  void shouldNotAllowSomeRobotsToSwitchOn() {
    innerShouldNotAllowSomeRobotsToSwitchOn(robotAllowedOperations::getRobotsSwitchOnStatus);
  }

  @Test
  void shouldNotAllowSomeRobotsToSwitchOnRequiresNew() {
    innerShouldNotAllowSomeRobotsToSwitchOn(robotAllowedOperations::getRobotsSwitchOnStatusRequiresNew);
  }

  @Test
  void shouldNotAllowSomeRobotsToSwitchOnNoRollbackFor() {
    innerShouldNotAllowSomeRobotsToSwitchOn(robotAllowedOperations::getRobotsSwitchOnStatusNoRollBackFor);
  }

  @Test
  void shouldNotAllowSomeRobotsToSwitchOnReadTransactional() {
    innerShouldNotAllowSomeRobotsToSwitchOn(robotAllowedOperations::getRobotsSwitchOnStatusReadTransactional);
  }

  private void innerShouldNotAllowSomeRobotsToSwitchOn(
      Function<Collection<Long>, Map<Long, OperationStatus>> function) {
    final var driver = db.save(
        aRobot().switched(true).type(DRIVER)
    );
    final var loader = db.save(
        aRobot().switched(false).type(LOADER)
    );
    final var vacuumTemplate = aRobot().switched(false).type(VACUUM);
    final var vacuum = db.save(vacuumTemplate);
    db.saveAll(
        vacuumTemplate.switched(true),
        vacuumTemplate.switched(true),
        vacuumTemplate.switched(true)
    );
    final var robotsIds = List.of(driver.getId(), loader.getId(), vacuum.getId());

    final var operations = function.apply(
        robotsIds
    );

    assertEquals(RESTRICTED, operations.get(driver.getId()));
    assertEquals(ALLOWED, operations.get(loader.getId()));
    assertEquals(RESTRICTED, operations.get(vacuum.getId()));
  }


}