package com.example.demo.service;

import com.example.demo.repository.RobotView;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class AuditService {

  public void serverPageRequested(Page<RobotView> page) {
    // no op
  }
}
