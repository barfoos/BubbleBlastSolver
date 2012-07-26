package bubbleblastsolver;

import bubbleblastsolver.mechanics.GameState;
import bubbleblastsolver.mechanics.IllegalMoveException;
import bubbleblastsolver.mechanics.Solver;

public class SolveLevels
{
  /** A blue bubble needs to burst 4 times */
  static final byte b = 4;
  /** A yellow bubble needs to burst 3 times */
  static final byte y = 3;
  /** A green bubble needs to burst 2 times */
  static final byte g = 2;
  /** A red bubble needs to burst 1 time */
  static final byte r = 1;

  static final byte level_1_10[] = new byte[] {//
  g, g, g, g, g, //
      g, r, g, r, g, //
      g, 0, r, 0, g,//
      g, 0, r, 0, g,//
      g, r, y, r, g,//
      g, g, g, g, g,//
  };

  static final byte level_1_98[] = new byte[] {//
  y, b, b, g, 0, //
      y, r, y, 0, r, //
      g, g, y, g, g, //
      b, b, r, b, g,//
      r, g, y, r, b,//
      r, b, y, b, g //
  };

  static void solve(byte[] level, int num_moves) throws IllegalMoveException
  {
    int[] res = Solver.solve(new GameState(level, num_moves));
    if (null == res)
      System.out.println("=> Didnt solve.");
    else
    {
      System.out.println("=> Moves (0-based): ");
      for (int i = 0; i < res.length / 2; ++i)
      {
        int x = res[res.length - 2 * i - 2];
        int y = res[res.length - 2 * i - 1];
        System.out.println("" + i + ": " + x + "," + y);
      }
    }
  }

  /**
   * Example of how to solve levels.
   * 
   * @param args
   * @throws IllegalMoveException
   */
  public static void main(String[] args) throws IllegalMoveException
  {
    System.out.println("Solving level 1-10");
    solve(level_1_10, 1);

    System.out.println("Solving level 1-98");
    solve(level_1_98, 4);
  }
}
