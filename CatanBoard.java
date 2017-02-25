package com.mrathjen.catanarander;

import java.security.SecureRandom;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/** */
public class CatanBoard {
  public static int HEX_SIDES = 6;
  public static int MIN_INTERSECTION_PROB = 8;
  public static int MAX_INTERSECTION_PROB = 12;

  public void generateBoard() {
    List<Resource> resources = initResourcePool();
    Map<Coordinate, Resource> board = new HashMap<>();
    Random rand = new SecureRandom();

    do {
      board.clear();

      Collections.shuffle(resources, rand);
      assert (resources.size() == COORDS.size());

      for (int i = 0; i < resources.size(); i++) {
        board.put(COORDS.get(i), resources.get(i));
      }
    } while (!isValidResources(board));

    do {
      Collections.shuffle(PROBS, rand);
      assert (board.size() - 1 == PROBS.size());

      int i = 0;
      for (Resource r : board.values()) {
        if (r instanceof DesertResource) {
          continue;
        }
        r.prob = PROBS.get(i++);
        r.combos = COMBOS.get(r.prob);
      }
    } while (!isValidProbs(board, resources));

    printBoard(board);
  }

  private boolean isValidProbs(Map<Coordinate, Resource> board, List<Resource> resources) {
    for (int i = 2; i < 24; i += 4) {
      Resource r = board.get(new Coordinate(1, i));
      if (r != null) {
        Resource n1 = board.get(buildCoordinate(0, 0));
        Resource n2 = board.get(buildCoordinate(1, (i - 4) % 24));
        Resource n3 = board.get(buildCoordinate(1, (i + 4) % 24));
        Resource n4 = board.get(buildCoordinate(2, (i - 2) % 24));
        Resource n5 = board.get(buildCoordinate(2, (i) % 24));
        Resource n6 = board.get(buildCoordinate(2, (i + 2) % 24));

        // Check intersection totals
        if (isNotInRange(r.combos + n1.combos + n2.combos)
            || isNotInRange(r.combos + n2.combos + n4.combos)
            || isNotInRange(r.combos + n4.combos + n5.combos)
            || isNotInRange(r.combos + n5.combos + n6.combos)
            || isNotInRange(r.combos + n6.combos + n3.combos)
            || isNotInRange(r.combos + n3.combos + n1.combos)) {
          return false;
        }

        // Check that the same probs aren't touching
        if (r.prob == n1.prob
            || r.prob == n2.prob
            || r.prob == n3.prob
            || r.prob == n4.prob
            || r.prob == n5.prob
            || r.prob == n6.prob
            || n4.prob == n5.prob
            || n5.prob == n6.prob) {
          return false;
        }
      }
    }

    // Check that no resource has the same probability
    for (int i = 0; i < resources.size() - 1; i++) {
      for (int j = i + 1; j < resources.size(); j++) {
        Resource r1 = resources.get(i);
        Resource r2 = resources.get(j);
        if (isSameResource(r1, r2)) {
          if (r1.prob == r2.prob) {
            return false;
          }
        }
      }
    }

    return true;
  }

  private boolean isNotInRange(int prob) {
    return prob < MIN_INTERSECTION_PROB || prob > MAX_INTERSECTION_PROB;
  }

  private boolean isValidResources(Map<Coordinate, Resource> board) {
    for (int i = 2; i < 24; i += 4) {
      Resource r = board.get(new Coordinate(1, i));
      if (r != null) {
        Resource n1 = board.get(buildCoordinate(0, 0));
        Resource n2 = board.get(buildCoordinate(1, (i - 4) % 24));
        Resource n3 = board.get(buildCoordinate(1, (i + 4) % 24));
        Resource n4 = board.get(buildCoordinate(2, (i - 2) % 24));
        Resource n5 = board.get(buildCoordinate(2, (i) % 24));
        Resource n6 = board.get(buildCoordinate(2, (i + 2) % 24));

        // Check direct neighbors, and compare outer layer otherwise they will be missed
        if (r.equals(n1)
            || r.equals(n2)
            || r.equals(n3)
            || r.equals(n4)
            || r.equals(n5)
            || r.equals(n6)
            || n4.equals(n5)
            || n5.equals(n6)) {
          return false;
        }
      }
    }
    return true;
  }

  public Coordinate buildCoordinate(int radius, int radians) {
    return new Coordinate(radius, (radians < 0) ? radians += 24 : radians);
  }

  /*
   *                   D0
   *              O3        B5
   *          W8       L6       S4
   *              O3        B5
   *          W8       L6       S4
   *              O3        B5
   *          W8       L6       S4
   *              O3        B5
   *                   D0
   */
  private void printBoard(Map<Coordinate, Resource> board) {
    System.out.println("\t\t\t" + board.get(buildCoordinate(2, 6)).getName());
    System.out.println("\t\t" + board.get(buildCoordinate(2, 8)).getName() + "\t\t"
        + board.get(buildCoordinate(2, 4)).getName());
    System.out.println("\t" + board.get(buildCoordinate(2, 10)).getName() + "\t\t"
        + board.get(buildCoordinate(1, 6)).getName() + "\t\t"
        + board.get(buildCoordinate(2, 2)).getName());
    System.out.println("\t\t" + board.get(buildCoordinate(1, 10)).getName() + "\t\t"
        + board.get(buildCoordinate(1, 2)).getName());
    System.out.println("\t" + board.get(buildCoordinate(2, 12)).getName() + "\t\t"
        + board.get(buildCoordinate(0, 0)).getName() + "\t\t"
        + board.get(buildCoordinate(2, 0)).getName());
    System.out.println("\t\t" + board.get(buildCoordinate(1, 14)).getName() + "\t\t"
        + board.get(buildCoordinate(1, 22)).getName());
    System.out.println("\t" + board.get(buildCoordinate(2, 14)).getName() + "\t\t"
        + board.get(buildCoordinate(1, 18)).getName() + "\t\t"
        + board.get(buildCoordinate(2, 22)).getName());
    System.out.println("\t\t" + board.get(buildCoordinate(2, 16)).getName() + "\t\t"
        + board.get(buildCoordinate(2, 20)).getName());
    System.out.println("\t\t\t" + board.get(buildCoordinate(2, 18)).getName());
  }

  private boolean isSameResource(Resource r1, Resource r2) {
    return r1.getClass().equals(r2.getClass());
  }

  private List<Resource> initResourcePool() {
    List<Resource> resources = new LinkedList<>();

    for (int i = 0; i < 1; i++) {
      resources.add(new DesertResource());
    }
    for (int i = 0; i < 3; i++) {
      resources.add(new OreResource());
    }
    for (int i = 0; i < 3; i++) {
      resources.add(new BrickResource());
    }
    for (int i = 0; i < 4; i++) {
      resources.add(new WheatResource());
    }
    for (int i = 0; i < 4; i++) {
      resources.add(new SheepResource());
    }
    for (int i = 0; i < 4; i++) {
      resources.add(new LumberResource());
    }

    return resources;
  }

  private static final List<Coordinate> COORDS =
      new LinkedList<Coordinate>() {
        {
          add(new Coordinate(0, 0));
          add(new Coordinate(1, 2));
          add(new Coordinate(1, 6));
          add(new Coordinate(1, 10));
          add(new Coordinate(1, 14));
          add(new Coordinate(1, 18));
          add(new Coordinate(1, 22));
          add(new Coordinate(2, 0));
          add(new Coordinate(2, 2));
          add(new Coordinate(2, 4));
          add(new Coordinate(2, 6));
          add(new Coordinate(2, 8));
          add(new Coordinate(2, 10));
          add(new Coordinate(2, 12));
          add(new Coordinate(2, 14));
          add(new Coordinate(2, 16));
          add(new Coordinate(2, 18));
          add(new Coordinate(2, 20));
          add(new Coordinate(2, 22));
        }
      };

  private static final List<Integer> PROBS =
      new LinkedList<Integer>() {
        {
          add(2);
          add(3);
          add(3);
          add(4);
          add(4);
          add(5);
          add(5);
          add(6);
          add(6);
          add(8);
          add(8);
          add(9);
          add(9);
          add(10);
          add(10);
          add(11);
          add(11);
          add(12);
        }
      };

  private static final Map<Integer, Integer> COMBOS =
      new HashMap<Integer, Integer>() {
        {
          put(2, 1);
          put(3, 2);
          put(4, 3);
          put(5, 4);
          put(6, 5);
          put(8, 5);
          put(9, 4);
          put(10, 3);
          put(11, 2);
          put(12, 1);
        }
      };
}
