package com.emazon.ms_stock.domain.model;

import com.emazon.ms_stock.ConsUtils;

import java.util.List;

public class Category {
  private Long id;
  private String name;
  private String description;

  public static final List<String> VALID_SORT_FIELDS = List.of(ConsUtils.NAME);

  public Category() {
  }

  public Category(String name, String description) {
    this.name = name;
    this.description = description;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public static Boolean isValidSortField(String fields) {
    return VALID_SORT_FIELDS.contains(fields);
  }
}
