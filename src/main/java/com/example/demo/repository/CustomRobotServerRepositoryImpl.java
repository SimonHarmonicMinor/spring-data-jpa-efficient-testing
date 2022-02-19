package com.example.demo.repository;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

@Repository
class CustomRobotServerRepositoryImpl implements
    CustomRobotServerRepository {

  @Override
  public Page<RobotView> findByFilter(RobotFilter filter, int page, int pageSize) {
    // stub
    return Page.empty();
  }
}
