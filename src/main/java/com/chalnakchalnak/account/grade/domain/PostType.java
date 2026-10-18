package com.chalnakchalnak.account.grade.domain;

public enum PostType {
  PRODUCT("일반상품"),
  AUCTION("경매");

  private final String label;

  PostType(String label) {
    this.label = label;
  }

  public String getLabel() {
    return label;
  }
}
