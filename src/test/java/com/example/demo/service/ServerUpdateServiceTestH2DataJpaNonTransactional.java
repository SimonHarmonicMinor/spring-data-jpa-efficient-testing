package com.example.demo.service;

import static com.example.demo.entity.ServerTestBuilder.aServer;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.transaction.annotation.Propagation.NOT_SUPPORTED;

import com.example.demo.entity.Server;
import com.example.demo.exception.OperationRestrictedException;
import com.example.demo.repository.ServerRepository;
import com.example.demo.testutils.TestDBFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
@Import(TestDBFacade.Config.class)
@Transactional(propagation = NOT_SUPPORTED)
class ServerUpdateServiceTestH2DataJpaNonTransactional {

  @Autowired
  private ServerUpdateService service;
  @Autowired
  private TestDBFacade db;
  @MockBean
  private ServerRestrictions serverRestrictions;

  @BeforeEach
  void beforeEach() {
    db.cleanDatabase();
  }

  @TestConfiguration
  static class Config {

    @Bean
    public ServerUpdateService service(
        ServerRepository serverRepository,
        ServerRestrictions serverRestrictions
    ) {
      return new ServerUpdateService(serverRepository, serverRestrictions);
    }
  }

  @Test
  void shouldSwitchOnSuccessfully() {
    final var id = db.save(aServer().withSwitched(false)).getId();
    doNothing().when(serverRestrictions).checkSwitchOn(id);

    service.switchOnServer(id);

    final var savedServer = db.find(id, Server.class);
    assertTrue(savedServer.isSwitched());
  }

  @Test
  void shouldRollbackIfCannotSwitchOn() {
    final var id = db.save(aServer().withSwitched(false)).getId();
    doThrow(new OperationRestrictedException("")).when(serverRestrictions).checkSwitchOn(id);

    assertThrows(OperationRestrictedException.class, () -> service.switchOnServer(id));

    final var savedServer = db.find(id, Server.class);
    assertFalse(savedServer.isSwitched());
  }
}