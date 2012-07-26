package bubbleblastsolver.mechanics;

import java.util.Arrays;

/**
 * This class contains the logic for solving a given BubbleBlast-Level. This is
 * a very stupid solver which will do a depth-first traversal of the tree of
 * possible game states. It runs reasonably fast if less than 6 moves are
 * allowed.
 */
public class Solver
{
  /**
   * Solve the given game state. The resulting moves are returned as an
   * int-array, in reverse order: The last move comes first, then the second
   * last move etc. The last move in the returned array is the first move
   * played. Each move contains the 0-based x and y coordinates of the exploding
   * bubble. If no solution can be found, null is returned.
   * 
   * @param state
   *          Game state to solve
   * @return null if no solution is found, otherwise the moves in reverse order.
   * @throws IllegalMoveException
   *           Only on internal errors
   */
  public static int[] solve(GameState state) throws IllegalMoveException
  {
    if (state.winning_state())
      return new int[0];
    if (state.lost_state())
      return null;

    // try all moves
    for (int y = 0; y < state.h; ++y)
    {
      for (int x = 0; x < state.w; ++x)
      {
        if (state.blobs[y * state.w + x] > 0)
        {
          int sub[] = solve(state.move(x, y));
          if (null != sub)
          {
            int res[] = Arrays.copyOf(sub, sub.length + 2);
            res[sub.length] = x;
            res[sub.length + 1] = y;
            return res;
          }
        }
      }
    }
    return null;
  }
}
