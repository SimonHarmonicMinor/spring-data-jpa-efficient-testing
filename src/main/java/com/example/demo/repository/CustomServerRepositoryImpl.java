package com.example.demo.repository;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

@Repository
class CustomServerRepositoryImpl implements
    CustomServerRepository {

  @Override
  public Page<ServerView> findByFilter(ServerFilter filter, int page, int pageSize) {
    // stub
    return Page.empty();
  }
}
