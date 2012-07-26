package bubbleblastsolver.mechanics;

public class GameState
{
  /** Standard size of a game field */
  public static final int DEFAULT_W = 5;

  /** Standard size of a game field */
  public static final int DEFAULT_H = 6;

  /** Size of the game field */
  public final int w, h;

  /**
   * Content of the game field. Empty entries are set to zero, otherwise the
   * number of required blasts are stored.
   */
  public final byte blobs[];

  /** Number of allowed moves left */
  public final int moves_left;

  /**
   * Construct a new, empty game state.
   * 
   * @param w
   *          Width of the game field
   * @param h
   *          height of the game field
   */
  GameState(int w, int h, int moves_left)
  {
    if (w < 1 || h < 1)
      throw new IllegalArgumentException();
    this.w = w;
    this.h = h;
    this.moves_left = moves_left;
    blobs = new byte[w * h];
  }

  /**
   * Construct a copy of the given game state.
   * 
   * @param w
   *          Width of the game field
   * @param h
   *          height of the game field
   * @param blobs
   *          Content of the game field
   */
  public GameState(int w, int h, byte blobs[], int moves_left)
  {
    if (w < 1 || h < 1)
      throw new IllegalArgumentException();
    this.w = w;
    this.h = h;
    this.blobs = blobs.clone();
    for (byte b : blobs)
      if (b < 0)
        throw new IllegalArgumentException();
    this.moves_left = moves_left;
    if (blobs.length != w * h)
      throw new IllegalArgumentException();
  }

  /**
   * Construct a new game state with standard size (5x6) and fill it with the
   * given content.
   * 
   * @param blobs
   *          Content of the new game state
   * @param moves_left
   *          Allowed number of moves
   */
  public GameState(byte blobs[], int moves_left)
  {
    this.w = DEFAULT_W;
    this.h = DEFAULT_H;
    this.blobs = blobs.clone();
    for (byte b : blobs)
      if (b < 0)
        throw new IllegalArgumentException();
    this.moves_left = moves_left;
    if (blobs.length != w * h)
      throw new IllegalArgumentException();

  }

  /**
   * Construct a new game state by playing the given move on the given parent
   * game state.
   * 
   * @param original
   * @param x
   *          X coordinate to play at
   * @param y
   *          Y coordinate to play at
   * @throws IllegalMoveException
   */
  GameState(GameState original, int x, int y) throws IllegalMoveException
  {
    this.w = original.w;
    this.h = original.h;
    this.blobs = original.blobs.clone();
    this.moves_left = original.moves_left - 1;
    if (this.moves_left < 0)
      throw new IllegalMoveException();
    make_move(x, y);
  }

  /**
   * Returns true if this is a winning state.
   * 
   * @return
   */
  public boolean winning_state()
  {
    for (byte b : blobs)
      if (b != 0)
        return false;
    return true;
  }

  /**
   * Returns true if this state is a 'lost' state.
   * 
   * @return
   */
  public boolean lost_state()
  {
    if (moves_left == 0 && !winning_state())
      return true;
    return false;
  }

  /**
   * Create a new game state after the given move.
   * 
   * @param x
   *          X coordinate to play at
   * @param y
   *          Y coordinate to play at
   * @return
   * @throws IllegalMoveException
   *           if the move is invalid
   */
  public GameState move(int x, int y) throws IllegalMoveException
  {
    // quick exit
    if (0 == moves_left)
      throw new IllegalMoveException();

    return new GameState(this, x, y);
  }

  /**
   * Generate 4 new particles originating at (x,y).
   * 
   * @param x
   * @param y
   * @param particles
   *          List of particles
   * @param first
   *          first free particle in the list
   * @return the number of new particles, 4
   */
  private int gen_particles(int x, int y, int[] particles, int first)
  {
    for (int i = 0; i < 4; ++i)
    {
      particles[3 * first + 3 * i + 0] = x;
      particles[3 * first + 3 * i + 1] = y;
      particles[3 * first + 3 * i + 2] = i;
    }

    return 4;
  }

  /**
   * Make a move at (x,y).
   * 
   * @param x
   *          X coordinate to play at
   * @param y
   *          Y coordinate to play at
   */
  private void make_move(int x, int y) throws IllegalMoveException
  {
    if (x < 0 || x >= w || y < 0 || y >= h || blobs[y * w + x] <= 0)
      throw new IllegalMoveException();

    if (0 == --blobs[y * w + x])
    {
      // Simulate the 'explosion' using 'particles'
      // Every particle has a current field and a direction, and moves
      // with one field per tick.
      // Particle Layout:  [x, y, state]
      // state: 0-3 == move, 4 == did collide, 5 == dead
      int[] particles = new int[w * h * 3 * 4];
      int num_particles = 0;
      num_particles += gen_particles(x, y, particles, num_particles);

      while (num_particles > 0)
      {
        // 'Tick':
        // - all particles move, check for collision
        // - kill all elements, add new particles
        // - remove dead particles

        // (1) move, check for collisions
        for (int i = 0; i < num_particles; ++i)
        {
          int state = particles[3 * i + 2];
          // move, check if off-field
          switch (state)
          {
          case 0:
            if ((--particles[3 * i + 0]) < 0)
              particles[3 * i + 2] = 5;
            break;
          case 1:
            if ((++particles[3 * i + 0]) >= w)
              particles[3 * i + 2] = 5;
            break;
          case 2:
            if ((--particles[3 * i + 1]) < 0)
              particles[3 * i + 2] = 5;
            break;
          case 3:
            if ((++particles[3 * i + 1]) >= h)
              particles[3 * i + 2] = 5;
            break;
          default:
            throw new InternalError();
          }
          // check if did collide
          if (particles[3 * i + 2] != 5)
          {
            int cx = particles[3 * i + 0];
            int cy = particles[3 * i + 1];
            if (blobs[cy * w + cx] > 0)
              particles[3 * i + 2] = 4; // 'did collide'
          }
        }

        // Kill blobs 
        for (int i = 0; i < num_particles; ++i)
        {
          if (4 == particles[3 * i + 2])
          {
            particles[3 * i + 2] = 5;
            int cx = particles[3 * i + 0];
            int cy = particles[3 * i + 1];
            if (blobs[cy * w + cx] > 1)
              blobs[cy * w + cx]--;
            else if (1 == blobs[cy * w + cx])
            {
              // this blob explodes
              blobs[cy * w + cx]--;
              num_particles += gen_particles(cx, cy, particles, num_particles);
            }
          }
        }

        // Kill particles
        for (int i = 0; i < num_particles; ++i)
        {
          if (particles[3 * i + 2] >= 4)
          {
            // remove particle
            for (int j = 0; j < 3; ++j)
              particles[3 * i + j] = particles[3 * (num_particles - 1) + j];
            num_particles--;
            i--; // Re-check this particle
          }
        }
      }
    }
  }
}
