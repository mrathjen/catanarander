package com.mrathjen.catanarander;

/**
 *
 */
public class Coordinate {

  public final int radius;
  // Radians here are expressed as a multiple of pi/12
  public final int radians;

  public Coordinate(int radius, int radians) {
    this.radius = radius;
    this.radians = radians;
  }

  @Override
  public boolean equals(Object other) {
    if (!(other instanceof Coordinate)) {
      return false;
    }
    Coordinate coord = (Coordinate) other;
    return radius == coord.radius && radians == coord.radians;
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + radius;
    result = 31 * result + radians;
    return result;
  }

  @Override
  public String toString() {
    return "(" + radius + ", " + radians + ")";
  }
}
