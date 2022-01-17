package com.example.demo.service;

import static java.lang.String.format;
import static org.springframework.transaction.annotation.Propagation.REQUIRES_NEW;

import com.example.demo.annotation.ReadTransactional;
import com.example.demo.exception.OperationRestrictedException;
import com.example.demo.repository.ServerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ServerRestrictions {

  @Autowired
  private ServerRepository serverRepository;

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

  private void innerCheckSwitchOn(Long serverId) {
    final var server =
        serverRepository.findById(serverId)
            .orElseThrow();
    if (server.isSwitched()) {
      throw new OperationRestrictedException(
          format("Server %s is already switched on", server.getName())
      );
    }
    final var count = serverRepository.countAllByTypeAndIdNot(server.getType(), serverId);
    if (count >= 3) {
      throw new OperationRestrictedException(
          format("There is already 3 switched on servers of type %s", server.getType())
      );
    }
  }
}
