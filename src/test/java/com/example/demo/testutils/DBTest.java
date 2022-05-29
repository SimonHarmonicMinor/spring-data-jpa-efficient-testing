package com.example.demo.testutils;

import static org.springframework.transaction.annotation.Propagation.NOT_SUPPORTED;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;
import org.springframework.transaction.annotation.Transactional;

@Retention(RetentionPolicy.RUNTIME)
@DataJpaTest
@Import({TestDBFacade.Config.class})
@Transactional(propagation = NOT_SUPPORTED)
public @interface DBTest {

  @AliasFor(annotation = DataJpaTest.class, attribute = "properties")
  String[] properties() default {};
}
