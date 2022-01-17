package com.example.demo.repository;

import org.springframework.data.domain.Page;

public interface CustomServerRepository {
  Page<ServerView> findByFilter(ServerFilter filter, int page, int pageSize);
}
