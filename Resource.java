package com.google.interviews.catan;

/**
 * 
 */
public abstract class Resource {
    public int prob;
    public int combos;

    public abstract String getName();

  @Override
      public boolean equals(Object other) {
      return this.getClass().equals(other.getClass());
  }
}
