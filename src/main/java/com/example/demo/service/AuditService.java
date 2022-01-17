package com.example.demo.service;

import com.example.demo.repository.ServerView;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class AuditService {

  public void serverPageRequested(Page<ServerView> page) {
    // no op
  }
}
