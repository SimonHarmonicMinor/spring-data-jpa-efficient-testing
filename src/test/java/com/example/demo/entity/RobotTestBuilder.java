package com.example.demo.entity;

import static com.example.demo.entity.Robot.Type.DRIVER;

import com.example.demo.entity.Robot.Type;
import com.example.demo.testutils.TestBuilder;
import java.util.function.Consumer;

public class RobotTestBuilder implements TestBuilder<Robot> {
  private String name = "";
  private boolean switched = false;
  private Type type = DRIVER;

  private RobotTestBuilder() {
  }

  private RobotTestBuilder(RobotTestBuilder builder) {
    this.name = builder.name;
    this.switched = builder.switched;
    this.type = builder.type;
  }

  public static RobotTestBuilder aRobot() {
    return new RobotTestBuilder();
  }

  public RobotTestBuilder withName(String name) {
    return copyWith(b -> b.name = name);
  }

  public RobotTestBuilder withSwitched(boolean switched) {
    return copyWith(b -> b.switched = switched);
  }

  public RobotTestBuilder withType(Type type) {
    return copyWith(b -> b.type = type);
  }

  private RobotTestBuilder copyWith(Consumer<RobotTestBuilder> consumer) {
    final var copy = new RobotTestBuilder(this);
    consumer.accept(copy);
    return copy;
  }

  @Override
  public Robot build() {
    final var server = new Robot();
    server.setName(name);
    server.setSwitched(switched);
    server.setType(type);
    return server;
  }
}