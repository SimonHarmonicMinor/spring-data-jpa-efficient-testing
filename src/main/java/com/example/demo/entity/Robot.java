package com.example.demo.entity;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.IDENTITY;

import com.sun.istack.NotNull;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "robot")
public class Robot {
  @Id
  @GeneratedValue(strategy = IDENTITY)
  @Column(name = "robot_id")
  private Long id;

  @NotNull
  private String name;

  @NotNull
  private boolean switched;

  @Enumerated(STRING)
  @NotNull
  private Type type;

  public enum Type {
    DRIVER,
    LOADER,
    VACUUM
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public boolean isSwitched() {
    return switched;
  }

  public Type getType() {
    return type;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setSwitched(boolean switched) {
    this.switched = switched;
  }

  public void setType(Type type) {
    this.type = type;
  }

  public Robot(String name, boolean switched, Type type) {
    this.name = name;
    this.switched = switched;
    this.type = type;
  }

  public Robot() {

  }
}
