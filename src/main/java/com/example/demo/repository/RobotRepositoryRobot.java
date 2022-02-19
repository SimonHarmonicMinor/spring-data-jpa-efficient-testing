package com.example.demo.repository;

import com.example.demo.entity.Robot;
import com.example.demo.entity.Robot.Type;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RobotRepositoryRobot extends JpaRepository<Robot, Long>,
    CustomRobotServerRepository {

  long countAllByTypeAndIdNot(Type type, Long id);

  @Query("SELECT DISTINCT name FROM Robot")
  Set<String> findUniqueNames();
}
