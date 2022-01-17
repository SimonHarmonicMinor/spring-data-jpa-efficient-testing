package com.example.demo.service;

import com.example.demo.repository.ServerFilter;
import com.example.demo.repository.ServerRepository;
import com.example.demo.repository.ServerView;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class ServerViewService {
  private final ServerRepository serverRepository;
  private final AuditService auditService;

  public ServerViewService(ServerRepository serverRepository,
      AuditService auditService) {
    this.serverRepository = serverRepository;
    this.auditService = auditService;
  }

  public Page<ServerView> getServersByFilter(ServerFilter filter, int pageNumber, int pageSize) {
    final var page = serverRepository.findByFilter(filter, pageNumber, pageSize);
    auditService.serverPageRequested(page);
    return page;
  }
}
