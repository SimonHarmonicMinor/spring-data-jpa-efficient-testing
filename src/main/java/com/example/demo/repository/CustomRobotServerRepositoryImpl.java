package com.example.demo.repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import org.hibernate.SessionFactory;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.service.ServiceRegistry;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

@Repository
class CustomRobotServerRepositoryImpl implements
    CustomRobotServerRepository {

  @PersistenceContext
  private EntityManager em;

  private EntityManagerFactory emf;

  @Override
  public Page<RobotView> findByFilter(RobotFilter filter, int page, int pageSize) {
    // stub
    SessionFactoryImplementor sessionFactory = (SessionFactoryImplementor) emf.unwrap(SessionFactory.class);
    ServiceRegistry serviceRegistry = sessionFactory.getServiceRegistry();
    emf.createEntityManager()
        .createQuery("")
        .getSingleResult();
    return Page.empty();
  }
}
