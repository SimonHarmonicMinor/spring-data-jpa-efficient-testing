package com.example.demo.service;

import com.example.demo.repository.RobotFilter;
import com.example.demo.repository.RobotRepository;
import com.example.demo.repository.RobotView;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class RobotViewService {
  private final RobotRepository robotRepository;
  private final AuditService auditService;

  public RobotViewService(RobotRepository robotRepository,
      AuditService auditService) {
    this.robotRepository = robotRepository;
    this.auditService = auditService;
  }

  public Page<RobotView> getRobotsByFilter(RobotFilter filter, int pageNumber, int pageSize) {
    final var page = robotRepository.findByFilter(filter, pageNumber, pageSize);
    auditService.serverPageRequested(page);
    return page;
  }
}
