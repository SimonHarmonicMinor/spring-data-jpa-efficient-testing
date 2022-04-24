package com.example.demo.service;

import static com.example.demo.entity.RobotTestBuilder.aRobot;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

import com.example.demo.entity.Robot;
import com.example.demo.exception.OperationRestrictedException;
import com.example.demo.repository.RobotRepository;
import com.example.demo.testutils.TestDBFacade;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(TestDBFacade.Config.class)
class RobotUpdateServiceTestH2DataJpa {

  @Autowired
  private RobotUpdateService service;
  @Autowired
  private TestDBFacade db;
  @MockBean
  private RobotRestrictions robotRestrictions;

  @TestConfiguration
  static class Config {

    @Bean
    public RobotUpdateService service(
        RobotRepository robotRepository,
        RobotRestrictions robotRestrictions
    ) {
      return new RobotUpdateService(robotRepository, robotRestrictions);
    }
  }

  @Test
  void shouldSwitchOnSuccessfully() {
    final var id = db.save(aRobot().switched(false)).getId();
    doNothing().when(robotRestrictions).checkSwitchOn(id);

    service.switchOnRobot(id);

    final var savedRobot = db.find(id, Robot.class);
    assertTrue(savedRobot.isSwitched());
  }

  @Test
  // @Disabled("Always fails due to default transactional propagation")
  void shouldRollbackIfCannotSwitchOn() {
    final var id = db.save(aRobot().switched(false)).getId();
    doThrow(new OperationRestrictedException("")).when(robotRestrictions).checkSwitchOn(id);

    assertThrows(OperationRestrictedException.class, () -> service.switchOnRobot(id));

    final var savedRobot = db.find(id, Robot.class);
    assertFalse(savedRobot.isSwitched());
  }
}