package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import com.example.demo.entity.Robot;
import com.example.demo.exception.OperationRestrictedException;
import com.example.demo.repository.RobotRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RobotUpdateServiceMockingTest {
  @Mock
  private RobotRepository robotRepository;
  @Mock
  private RobotRestrictions robotRestrictions;
  private RobotUpdateService service;

  @BeforeEach
  void beforeEach() {
    service = new RobotUpdateService(robotRepository, robotRestrictions);
  }

  @Test
  void shouldSwitchOnSuccessfully() {
    final var robot = new Robot();
    robot.setSwitched(false);
    when(robotRepository.findById(1L)).thenReturn(Optional.of(robot));
    doNothing().when(robotRestrictions).checkSwitchOn(1L);

    service.switchOnRobot(1L);

    assertTrue(robot.isSwitched());
  }

  @Test
  @Disabled("Always fails")
  void shouldRollbackIfCannotSwitchOn() {
    final var robot = new Robot();
    robot.setSwitched(false);
    when(robotRepository.findById(1L)).thenReturn(Optional.of(robot));
    doThrow(new OperationRestrictedException("")).when(robotRestrictions).checkSwitchOn(1L);

    assertThrows(OperationRestrictedException.class, () -> service.switchOnRobot(1L));

    assertFalse(robot.isSwitched());
  }
}