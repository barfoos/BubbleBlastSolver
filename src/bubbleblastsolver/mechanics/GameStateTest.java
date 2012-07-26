package bubbleblastsolver.mechanics;

import static org.junit.Assert.*;

import org.junit.Test;

public class GameStateTest
{
  void makeMoveExpectException(GameState gs, int x, int y)
  {
    try
    {
      gs.move(x, y);
      fail("Expected Exception");
    }
    catch (IllegalMoveException e)
    {
      // OK
    }
  }

  @Test
  public void testGameStateIntIntInt()
  {
    GameState gs = new GameState(1, 1, 0);
    assertEquals(true, gs.winning_state());
    assertEquals(false, gs.lost_state());
    makeMoveExpectException(gs, 0, 0);

    GameState gs2 = new GameState(1, 1, 1);
    assertEquals(true, gs2.winning_state());
    assertEquals(false, gs2.lost_state());
    makeMoveExpectException(gs2, 0, 0);
  }

  @Test
  public void testWinningLost_state()
  {
    {
      GameState gs = new GameState(1, 1, 0);
      assertEquals(true, gs.winning_state());
      assertEquals(false, gs.lost_state());
      gs.blobs[0] = 1;
      assertEquals(false, gs.winning_state());
      assertEquals(true, gs.lost_state());
    }
    {
      GameState gs = new GameState(1, 1, 1);
      assertEquals(true, gs.winning_state());
      assertEquals(false, gs.lost_state());
      gs.blobs[0] = 1;
      assertEquals(false, gs.winning_state());
      assertEquals(false, gs.lost_state());
    }
  }

  @Test
  public void testMove() throws IllegalMoveException
  {
    {
      GameState gs = new GameState(1, 1, 0);
      assertEquals(true, gs.winning_state());
      assertEquals(false, gs.lost_state());
      makeMoveExpectException(gs, 0, 0);
      gs.blobs[0] = 1;
      makeMoveExpectException(gs, 0, 0);
    }
    {
      GameState gs = new GameState(1, 1, 1);
      assertEquals(true, gs.winning_state());
      assertEquals(false, gs.lost_state());
      makeMoveExpectException(gs, 0, 0);
      gs.blobs[0] = 1;
      GameState gs2 = gs.move(0, 0);
      assertEquals(true, gs2.winning_state());
      assertEquals(false, gs2.lost_state());
      assertEquals(0, gs2.blobs[0]);
    }
    // [1 2]
    {
      GameState gs = new GameState(2, 1, 1);
      gs.blobs[0] = 1;
      gs.blobs[1] = 2;

      GameState gs2 = gs.move(0, 0);
      assertEquals(0, gs2.blobs[0]);
      assertEquals(1, gs2.blobs[1]);
      assertEquals(false, gs2.winning_state());
      assertEquals(true, gs2.lost_state());

      GameState gs3 = gs.move(1, 0);
      assertEquals(1, gs3.blobs[0]);
      assertEquals(1, gs3.blobs[1]);
      assertEquals(false, gs3.winning_state());
      assertEquals(true, gs3.lost_state());
    }
  }
}
