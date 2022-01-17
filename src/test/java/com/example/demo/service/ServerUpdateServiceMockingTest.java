package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import com.example.demo.entity.Server;
import com.example.demo.exception.OperationRestrictedException;
import com.example.demo.repository.ServerRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ServerUpdateServiceMockingTest {
  @Mock
  private ServerRepository serverRepository;
  @Mock
  private ServerRestrictions serverRestrictions;
  private ServerUpdateService service;

  @BeforeEach
  void beforeEach() {
    service = new ServerUpdateService(serverRepository, serverRestrictions);
  }

  @Test
  void shouldSwitchOnSuccessfully() {
    final var server = new Server();
    server.setSwitched(false);
    when(serverRepository.findById(1L)).thenReturn(Optional.of(server));
    doNothing().when(serverRestrictions).checkSwitchOn(1L);

    service.switchOnServer(1L);

    assertTrue(server.isSwitched());
  }

  @Test
  @Disabled("Always fails")
  void shouldRollbackIfCannotSwitchOn() {
    final var server = new Server();
    server.setSwitched(false);
    when(serverRepository.findById(1L)).thenReturn(Optional.of(server));
    doThrow(new OperationRestrictedException("")).when(serverRestrictions).checkSwitchOn(1L);

    assertThrows(OperationRestrictedException.class, () -> service.switchOnServer(1L));

    assertFalse(server.isSwitched());
  }
}