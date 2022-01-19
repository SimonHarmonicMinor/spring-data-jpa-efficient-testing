package com.example.demo.repository;

import static com.example.demo.entity.ServerTestBuilder.aServer;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.demo.testutils.TestDBFacade;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(TestDBFacade.class)
class ServerRepositoryTest {
  @Autowired
  private ServerRepository serverRepository;
  @Autowired
  private TestDBFacade db;

  @Test
  void shouldReturnUniqueNames() {
    db.saveAll(
        aServer().withName("s1"),
        aServer().withName("s1"),
        aServer().withName("s2")
    );

    final var names = serverRepository.findUniqueNames();

    assertEquals(Set.of("s1", "s2"), names);
  }
}