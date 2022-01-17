package com.example.demo.service;

import static com.example.demo.service.OperationStatus.ALLOWED;
import static com.example.demo.service.OperationStatus.RESTRICTED;
import static com.example.demo.service.OperationStatus.SERVER_IS_ABSENT;
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
public class ServerAllowedOperations {

  private static final Logger LOG = LoggerFactory.getLogger(ServerAllowedOperations.class);
  @Autowired
  private ServerRestrictions serverRestrictions;

  @Transactional(readOnly = true)
  public Map<Long, OperationStatus> getServersSwitchOnStatus(Collection<Long> serverIds) {
    return innerGetServersSwitchOnStatus(serverIds, serverRestrictions::checkSwitchOn);
  }

  @Transactional(readOnly = true)
  public Map<Long, OperationStatus> getServersSwitchOnStatusRequiresNew(
      Collection<Long> serverIds
  ) {
    return innerGetServersSwitchOnStatus(serverIds, serverRestrictions::checkSwitchOnRequiresNew);
  }

  @Transactional(readOnly = true)
  public Map<Long, OperationStatus> getServersSwitchOnStatusNoRollBackFor(
      Collection<Long> serverIds
  ) {
    return innerGetServersSwitchOnStatus(serverIds, serverRestrictions::checkSwitchOnNoRollBackFor);
  }

  @ReadTransactional
  public Map<Long, OperationStatus> getServersSwitchOnStatusReadTransactional(
      Collection<Long> serverIds
  ) {
    return innerGetServersSwitchOnStatus(
        serverIds,
        serverRestrictions::checkSwitchOnReadTransactional
    );
  }

  private Map<Long, OperationStatus> innerGetServersSwitchOnStatus(
      Collection<Long> serverIds,
      Consumer<Long> restrictionChecker
  ) {
    final var result = new HashMap<Long, OperationStatus>();
    for (Long serverId : serverIds) {
      result.put(serverId, getOperationStatus(serverId, restrictionChecker));
    }
    return result;
  }

  private OperationStatus getOperationStatus(Long serverId, Consumer<Long> restrictionChecker) {
    try {
      restrictionChecker.accept(serverId);
      return ALLOWED;
    } catch (NoSuchElementException e) {
      LOG.debug(format("Server with id %s is absent", serverId), e);
      return SERVER_IS_ABSENT;
    } catch (OperationRestrictedException e) {
      LOG.debug(format("Server with id %s cannot be switched on", serverId), e);
      return RESTRICTED;
    }
  }
}
