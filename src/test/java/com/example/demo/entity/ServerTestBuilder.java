package com.example.demo.entity;

import static com.example.demo.entity.Server.Type.JBOSS;

import com.example.demo.entity.Server.Type;
import com.example.demo.testutils.TestBuilder;
import java.util.function.Consumer;

public class ServerTestBuilder implements TestBuilder<Server> {
  private String name = "";
  private boolean switched = false;
  private Type type = JBOSS;

  private ServerTestBuilder() {
  }

  private ServerTestBuilder(ServerTestBuilder builder) {
    this.name = builder.name;
    this.switched = builder.switched;
    this.type = builder.type;
  }

  public static ServerTestBuilder aServer() {
    return new ServerTestBuilder();
  }

  public ServerTestBuilder withName(String name) {
    return copyWith(b -> b.name = name);
  }

  public ServerTestBuilder withSwitched(boolean switched) {
    return copyWith(b -> b.switched = switched);
  }

  public ServerTestBuilder withType(Type type) {
    return copyWith(b -> b.type = type);
  }

  private ServerTestBuilder copyWith(Consumer<ServerTestBuilder> consumer) {
    final var copy = new ServerTestBuilder(this);
    consumer.accept(copy);
    return copy;
  }

  @Override
  public Server build() {
    final var server = new Server();
    server.setName(name);
    server.setSwitched(switched);
    server.setType(type);
    return server;
  }
}