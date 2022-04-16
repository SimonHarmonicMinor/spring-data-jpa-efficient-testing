package com.example.demo.repository;

import com.example.demo.entity.Robot;
import com.example.demo.entity.Robot.Type;
import java.util.List;
import java.util.Set;
import javax.persistence.QueryHint;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

public interface RobotRepository extends JpaRepository<Robot, Long>,
    CustomRobotServerRepository, JpaSpecificationExecutor<Robot> {

  long countAllByTypeAndIdNot(Type type, Long id);

  @Query("SELECT DISTINCT name FROM Robot")
  Set<String> findUniqueNames();

  @Override
  @QueryHints(
      @QueryHint(name = "hint_name", value = "hint_value")
  )
  List<Robot> findAll(Specification<Robot> spec);
}
