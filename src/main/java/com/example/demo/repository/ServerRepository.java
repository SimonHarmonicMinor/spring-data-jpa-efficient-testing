package com.example.demo.repository;

import com.example.demo.entity.Server;
import com.example.demo.entity.Server.Type;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServerRepository extends JpaRepository<Server, Long> {

  long countAllByTypeAndIdNot(Type type, Long id);
}
