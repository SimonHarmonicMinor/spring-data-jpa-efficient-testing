package com.example.demo.service;

import static com.example.demo.entity.RobotTestBuilder.aRobot;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

import com.example.demo.entity.Robot;
import com.example.demo.exception.OperationRestrictedException;
import com.example.demo.testutils.TestDBFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

@SpringBootTest
@AutoConfigureTestDatabase
@AutoConfigureTestEntityManager
@Import(TestDBFacade.Config.class)
class RobotUpdateServiceTestH2TestDataBuilder {
  @Autowired
  private RobotUpdateService service;
  @Autowired
  private TestDBFacade db;
  @MockBean
  private RobotRestrictions robotRestrictions;

  @BeforeEach
  void beforeEach() {
    db.cleanDatabase();
  }

  @Test
  void shouldSwitchOnSuccessfully() {
    final var id = db.save(aRobot().withSwitched(false)).getId();
    doNothing().when(robotRestrictions).checkSwitchOn(id);

    service.switchOnRobot(id);

    final var savedServer  = db.find(id, Robot.class);
    assertTrue(savedServer.isSwitched());
  }

  @Test
  void shouldRollbackIfCannotSwitchOn() {
    final var id = db.save(aRobot().withSwitched(false)).getId();
    doThrow(new OperationRestrictedException("")).when(robotRestrictions).checkSwitchOn(id);

    assertThrows(OperationRestrictedException.class, () -> service.switchOnRobot(id));

    final var savedRobot  = db.find(id, Robot.class);
    assertFalse(savedRobot.isSwitched());
  }
}