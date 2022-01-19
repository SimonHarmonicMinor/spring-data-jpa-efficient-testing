package com.example.demo.service;

import static com.example.demo.entity.Server.Type.JBOSS;
import static com.example.demo.entity.ServerTestBuilder.aServer;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

import com.example.demo.entity.Server;
import com.example.demo.exception.OperationRestrictedException;
import com.example.demo.repository.ServerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
@AutoConfigureTestDatabase
class ServerUpdateServiceTestH2 {
  @Autowired
  private ServerUpdateService service;
  @Autowired
  private ServerRepository serverRepository;
  @MockBean
  private ServerRestrictions serverRestrictions;

  @BeforeEach
  void beforeEach() {
    serverRepository.deleteAll();
  }

  @Test
  void shouldSwitchOnSuccessfully() {
    final var server = new Server();
    server.setSwitched(false);
    server.setType(JBOSS);
    server.setName("some_name");
    serverRepository.save(server);
    doNothing().when(serverRestrictions).checkSwitchOn(server.getId());

    service.switchOnServer(server.getId());

    final var savedServer  = serverRepository.findById(server.getId()).orElseThrow();
    assertTrue(savedServer.isSwitched());
  }

  @Test
  void shouldRollbackIfCannotSwitchOn() {
    final var server = new Server();
    server.setSwitched(false);
    server.setType(JBOSS);
    server.setName("some_name");
    serverRepository.save(server);
    doThrow(new OperationRestrictedException("")).when(serverRestrictions).checkSwitchOn(server.getId());

    assertThrows(OperationRestrictedException.class, () -> service.switchOnServer(server.getId()));

    final var savedServer  = serverRepository.findById(server.getId()).orElseThrow();
    assertFalse(savedServer.isSwitched());
  }
}