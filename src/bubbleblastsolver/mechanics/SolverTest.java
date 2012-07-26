package bubbleblastsolver.mechanics;

import static org.junit.Assert.*;

import org.junit.Test;


public class SolverTest
{
  static final byte b = 4;
  static final byte y = 3;
  static final byte g = 2;
  static final byte r = 1;

  static byte level_1_10[] = new byte[] {//
  g, g, g, g, g, //
      g, r, g, r, g, //
      g, 0, r, 0, g,//
      g, 0, r, 0, g,//
      g, r, y, r, g,//
      g, g, g, g, g,//
  };

  public void assertEqualsA(int[] expected, int[] result)
  {
    if (null == expected)
    {
      assertEquals(null, result);
      return;
    }
    if (null == result)
      fail("non-null expected");
    assertEquals(expected.length, result.length);
    for (int i = 0; i < expected.length; ++i)
      assertEquals(expected[i], result[i]);
  }

  @Test
  public void testSolve() throws IllegalMoveException
  {
    {
      GameState gs = new GameState(1, 1, 0);
      int[] res = Solver.solve(gs);
      assertEqualsA(new int[0], res);
    }
    {
      GameState gs = new GameState(1, 1, 0);
      gs.blobs[0] = 1;
      int[] res = Solver.solve(gs);
      assertEqualsA(null, res);
    }
    {
      GameState gs = new GameState(1, 1, 1);
      int[] res = Solver.solve(gs);
      assertEqualsA(new int[0], res);
    }
    {
      GameState gs = new GameState(1, 1, 1);
      gs.blobs[0] = 1;
      int[] res = Solver.solve(gs);
      assertEqualsA(new int[] { 0, 0 }, res);
    }
    {
      GameState gs = new GameState(2, 1, 1);
      gs.blobs[0] = 1;
      gs.blobs[1] = 1;
      int[] res = Solver.solve(gs);
      assertEqualsA(new int[] { 0, 0 }, res);
    }
    {
      GameState gs = new GameState(2, 1, 2);
      gs.blobs[0] = 1;
      gs.blobs[1] = 2;
      int[] res = Solver.solve(gs);
      assertEqualsA(new int[] { 1, 0, 0, 0 }, res);
    }
    {
      // a more complicated case (level 1-10)
      GameState gs = new GameState(5, 6, level_1_10, 1);
      int[] res = Solver.solve(gs);
      assertEqualsA(new int[] { 2, 2 }, res);
    }
  }
}
