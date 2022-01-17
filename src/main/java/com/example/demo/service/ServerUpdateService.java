package com.example.demo.service;

import com.example.demo.repository.ServerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ServerUpdateService {
  @Autowired
  private ServerRepository serverRepository;
  @Autowired
  private ServerRestrictions serverRestrictions;

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
