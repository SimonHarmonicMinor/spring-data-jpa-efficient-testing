package com.example.demo.service;

import static com.example.demo.service.OperationStatus.ALLOWED;
import static com.example.demo.service.OperationStatus.RESTRICTED;
import static com.example.demo.service.OperationStatus.ROBOT_IS_ABSENT;
import static java.lang.String.format;

import com.example.demo.annotation.ReadTransactional;
import com.example.demo.exception.OperationRestrictedException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RobotAllowedOperations {

  private static final Logger LOG = LoggerFactory.getLogger(RobotAllowedOperations.class);
  @Autowired
  private RobotRestrictions robotRestrictions;

  @Transactional(readOnly = true)
  public Map<Long, OperationStatus> getRobotsSwitchOnStatus(Collection<Long> robotIds) {
    return innerGetRobotsSwitchOnStatus(robotIds, robotRestrictions::checkSwitchOn);
  }

  @Transactional(readOnly = true)
  public Map<Long, OperationStatus> getRobotsSwitchOnStatusRequiresNew(
      Collection<Long> robotIds
  ) {
    return innerGetRobotsSwitchOnStatus(robotIds, robotRestrictions::checkSwitchOnRequiresNew);
  }

  @Transactional(readOnly = true)
  public Map<Long, OperationStatus> getRobotsSwitchOnStatusNoRollBackFor(
      Collection<Long> robotIds
  ) {
    return innerGetRobotsSwitchOnStatus(robotIds, robotRestrictions::checkSwitchOnNoRollBackFor);
  }

  @ReadTransactional
  public Map<Long, OperationStatus> getRobotsSwitchOnStatusReadTransactional(
      Collection<Long> robotIds
  ) {
    return innerGetRobotsSwitchOnStatus(
        robotIds,
        robotRestrictions::checkSwitchOnReadTransactional
    );
  }

  private Map<Long, OperationStatus> innerGetRobotsSwitchOnStatus(
      Collection<Long> robotIds,
      Consumer<Long> restrictionChecker
  ) {
    final var result = new HashMap<Long, OperationStatus>();
    for (Long robotId : robotIds) {
      result.put(robotId, getOperationStatus(robotId, restrictionChecker));
    }
    return result;
  }

  private OperationStatus getOperationStatus(Long robotId, Consumer<Long> restrictionChecker) {
    try {
      restrictionChecker.accept(robotId);
      return ALLOWED;
    } catch (NoSuchElementException e) {
      LOG.debug(format("Server with id %s is absent", robotId), e);
      return ROBOT_IS_ABSENT;
    } catch (OperationRestrictedException e) {
      LOG.debug(format("Server with id %s cannot be switched on", robotId), e);
      return RESTRICTED;
    }
  }
}
