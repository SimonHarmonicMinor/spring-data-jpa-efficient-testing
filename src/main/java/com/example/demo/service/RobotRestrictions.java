package com.example.demo.service;

import static java.lang.String.format;
import static org.springframework.transaction.annotation.Propagation.REQUIRES_NEW;

import com.example.demo.annotation.ReadTransactional;
import com.example.demo.exception.OperationRestrictedException;
import com.example.demo.repository.RobotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RobotRestrictions {

  @Autowired
  private RobotRepository robotRepository;

  @Transactional(readOnly = true)
  public void checkSwitchOn(Long serverId) {
    innerCheckSwitchOn(serverId);
  }

  @Transactional(readOnly = true, propagation = REQUIRES_NEW)
  public void checkSwitchOnRequiresNew(Long serverId) {
    innerCheckSwitchOn(serverId);
  }

  @Transactional(readOnly = true, noRollbackFor = Exception.class)
  public void checkSwitchOnNoRollBackFor(Long serverId) {
    innerCheckSwitchOn(serverId);
  }

  @ReadTransactional
  public void checkSwitchOnReadTransactional(Long serverId) {
    innerCheckSwitchOn(serverId);
  }

  private void innerCheckSwitchOn(Long robotId) {
    final var robot =
        robotRepository.findById(robotId)
            .orElseThrow();
    if (robot.isSwitched()) {
      throw new OperationRestrictedException(
          format("Robot %s is already switched on", robot.getName())
      );
    }
    final var count = robotRepository.countAllByTypeAndIdNot(robot.getType(), robotId);
    if (count >= 3) {
      throw new OperationRestrictedException(
          format("There is already 3 switched on robots of type %s", robot.getType())
      );
    }
  }
}
