package com.example.demo.service;

import com.example.demo.repository.RobotRepositoryRobot;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RobotUpdateService {

  private final RobotRepositoryRobot robotRepository;
  private final RobotRestrictions robotRestrictions;

  public RobotUpdateService(
      RobotRepositoryRobot robotRepository,
      RobotRestrictions robotRestrictions
  ) {
    this.robotRepository = robotRepository;
    this.robotRestrictions = robotRestrictions;
  }

  @Transactional
  public void switchOnRobot(Long robotId) {
    reallyLongOperation();
    final var robot =
        robotRepository.findById(robotId)
            .orElseThrow();
    robot.setSwitched(true);
    robotRepository.saveAndFlush(robot);
    robotRestrictions.checkSwitchOn(robotId);
  }

  private void reallyLongOperation() {

  }
}