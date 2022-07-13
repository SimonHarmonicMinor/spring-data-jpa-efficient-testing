package com.example.demo.service;

import com.example.demo.repository.RobotRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RobotUpdateService {

  private final RobotRepository robotRepository;
  private final RobotRestrictions robotRestrictions;

  public RobotUpdateService(
      RobotRepository robotRepository,
      RobotRestrictions robotRestrictions
  ) {
    this.robotRepository = robotRepository;
    this.robotRestrictions = robotRestrictions;
  }

  @Transactional
  public void switchOnRobot(Long robotId) {
    final var robot =
        robotRepository.findById(robotId)
            .orElseThrow();
    robot.setSwitched(true);
    robotRepository.flush();
    robotRestrictions.checkSwitchOn(robotId);
  }

  private void reallyLongOperation() {

  }
}