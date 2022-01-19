package com.example.demo.repository;

import com.example.demo.entity.Server;
import com.example.demo.entity.Server.Type;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ServerRepository extends JpaRepository<Server, Long>, CustomServerRepository {

  long countAllByTypeAndIdNot(Type type, Long id);

  @Query("SELECT DISTINCT name FROM Server")
  Set<String> findUniqueNames();
}
