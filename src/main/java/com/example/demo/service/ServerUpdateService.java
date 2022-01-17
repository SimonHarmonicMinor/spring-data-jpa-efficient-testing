package com.example.demo.service;

import com.example.demo.repository.ServerRepository;
import java.util.Collection;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ServerUpdateService {

  private final ServerRepository serverRepository;
  private final ServerRestrictions serverRestrictions;

  public ServerUpdateService(
      ServerRepository serverRepository,
      ServerRestrictions serverRestrictions
  ) {
    this.serverRepository = serverRepository;
    this.serverRestrictions = serverRestrictions;
  }

  @Transactional
  public void switchOnServers(Collection<Long> serverIds) {
    for (Long serverId : serverIds) {
      switchOnServer(serverId);
    }
  }

  @Transactional
  public void switchOnServer(Long serverId) {
    final var server =
        serverRepository.findById(serverId)
            .orElseThrow();
    server.setSwitched(true);
    serverRepository.saveAndFlush(server);
    serverRestrictions.checkSwitchOn(serverId);
  }
}