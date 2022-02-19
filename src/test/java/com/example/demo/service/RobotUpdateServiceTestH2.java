package com.example.demo.service;

import static com.example.demo.entity.Robot.Type.DRIVER;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

import com.example.demo.entity.Robot;
import com.example.demo.exception.OperationRestrictedException;
import com.example.demo.repository.RobotRepositoryRobot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
@AutoConfigureTestDatabase
class RobotUpdateServiceTestH2 {
  @Autowired
  private RobotUpdateService service;
  @Autowired
  private RobotRepositoryRobot robotRepository;
  @MockBean
  private RobotRestrictions robotRestrictions;

  @BeforeEach
  void beforeEach() {
    robotRepository.deleteAll();
  }

  @Test
  void shouldSwitchOnSuccessfully() {
    final var robot = new Robot();
    robot.setSwitched(false);
    robot.setType(DRIVER);
    robot.setName("some_name");
    robotRepository.save(robot);
    doNothing().when(robotRestrictions).checkSwitchOn(robot.getId());

    service.switchOnRobot(robot.getId());

    final var savedRobot  = robotRepository.findById(robot.getId()).orElseThrow();
    assertTrue(savedRobot.isSwitched());
  }

  @Test
  void shouldRollbackIfCannotSwitchOn() {
    final var robot = new Robot();
    robot.setSwitched(false);
    robot.setType(DRIVER);
    robot.setName("some_name");
    robotRepository.save(robot);
    doThrow(new OperationRestrictedException("")).when(robotRestrictions).checkSwitchOn(robot.getId());

    assertThrows(OperationRestrictedException.class, () -> service.switchOnRobot(robot.getId()));

    final var savedRobot  = robotRepository.findById(robot.getId()).orElseThrow();
    assertFalse(savedRobot.isSwitched());
  }
}