/*******************************************************************************
 * This files was developed for CS4233: Object-Oriented Analysis & Design.
 * The course was taken at Worcester Polytechnic Institute.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package hanto.studentirsark.beta;

import static hanto.common.HantoPieceType.*;
import static hanto.common.MoveResult.*;
import static hanto.common.HantoPlayerColor.*;
import static org.junit.Assert.*;
import hanto.common.*;
import hanto.studentirsark.HantoGameFactory;

import org.junit.*;

/**
 * Test cases for Beta Hanto.
 * @version Sep 14, 2014
 */
public class BetaHantoMasterTest
{
	/**
	 * Internal class for these test cases.
	 * @version Sep 13, 2014
	 */
	class TestHantoCoordinate implements HantoCoordinate
	{
		private final int x, y;
		
		public TestHantoCoordinate(int x, int y)
		{
			this.x = x;
			this.y = y;
		}
		/*
		 * @see hanto.common.HantoCoordinate#getX()
		 */
		@Override
		public int getX()
		{
			return x;
		}

		/*
		 * @see hanto.common.HantoCoordinate#getY()
		 */
		@Override
		public int getY()
		{
			return y;
		}

	}
	
	private static HantoGameFactory factory;
	private HantoGame game;
	
	@BeforeClass
	public static void initializeClass()
	{
		factory = HantoGameFactory.getInstance();
	}
	
	@Before
	public void setup()
	{
		// By default, blue moves first.
		game = factory.makeHantoGame(HantoGameID.BETA_HANTO, BLUE);
	}
	
	@Test // 1
	public void blueMovesFirst() throws HantoException
	{
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		final HantoPiece p = game.getPieceAt(makeCoordinate(0,0));
		assertEquals(BLUE, p.getColor());
	}
	
	@Test //2
	public void redMovesFirst() throws HantoException
	{
		game = factory.makeHantoGame(HantoGameID.BETA_HANTO, RED);
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		final HantoPiece p = game.getPieceAt(makeCoordinate(0,0));
		assertEquals(RED, p.getColor());
	}
	
	@Test //3
	public void firstMoveAsSparrowIsValid() throws HantoException
	{
		final MoveResult mr = game.makeMove(SPARROW, null, makeCoordinate(0, 0));
		assertEquals(OK, mr);
	}
	
	@Test //4
	public void firstMoveAsSparrowIsRetrievable() throws HantoException
	{
		game.makeMove(SPARROW, null, makeCoordinate(0, 0));
		final HantoPiece p = game.getPieceAt(makeCoordinate(0,0));
		assertEquals(SPARROW, p.getType());
	}
	
	@Test(expected = HantoException.class)	//5
	public void firstMoveNotAtOrigin() throws HantoException
	{
		game.makeMove(SPARROW, null, makeCoordinate(1, 0));
	}
	
	@Test //6
	public void secondMoveNotAtOrigin() throws HantoException
	{
		game.makeMove(SPARROW, null, makeCoordinate(0, 0));
		final MoveResult mr2 = game.makeMove(BUTTERFLY, null, makeCoordinate(1, 0));
		assertEquals(OK, mr2);
	}
	
	@Test	// Professor Pollice 
	public void bluePlacesInitialButterflyAtOrigin() throws HantoException
	{
		final MoveResult mr = game.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		assertEquals(OK, mr);
		final HantoPiece p = game.getPieceAt(makeCoordinate(0, 0));
		assertEquals(BLUE, p.getColor());
		assertEquals(BUTTERFLY, p.getType());
	}
	
	@Test // 8
	public void playersMakeThreeMoves() throws HantoException
	{
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 1));
		final MoveResult mr3 = game.makeMove(SPARROW, null, makeCoordinate(1, 0));
		assertEquals(OK, mr3);
	}
	
	@Test // 9
	public void firstMoveRetrievableAfterMakingSecondMove() throws HantoException
	{
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		game.makeMove(SPARROW, null, makeCoordinate(1, 0));
		final HantoPiece p = game.getPieceAt(makeCoordinate(0,0));
		assertEquals(BUTTERFLY, p.getType());
		assertEquals(BLUE, p.getColor());
	}
	
	@Test // 10
	public void firstMoveRetrievableAfterMakingSecondMoveInNegativeCoordinates() throws HantoException
	{
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		game.makeMove(SPARROW, null, makeCoordinate(-1, 0));
		final HantoPiece p = game.getPieceAt(makeCoordinate(0,0));
		assertEquals(BUTTERFLY, p.getType());
		assertEquals(BLUE, p.getColor());
	}
	
	@Test(expected = HantoException.class) // 11
	public void playerPlacesPieceOnAnotherPiece() throws HantoException
	{
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		game.makeMove(SPARROW, null, makeCoordinate(1, 0));
		game.makeMove(SPARROW, null, makeCoordinate(1, 0));
	}
	
	@Test(expected = HantoException.class) //12
	public void playerPlacesMoreThanOneButterfly() throws HantoException
	{
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		game.makeMove(SPARROW, null, makeCoordinate(1, 0));
		game.makeMove(BUTTERFLY, null, makeCoordinate(-1, 0));
	}
	
	@Test(expected = HantoException.class) //13
	public void playerPlacesMoreThanFiveSparrows() throws HantoException
	{
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		game.makeMove(BUTTERFLY, null, makeCoordinate(1, 0));
		game.makeMove(SPARROW, null, makeCoordinate(-1, 0));
		game.makeMove(SPARROW, null, makeCoordinate(-2, 0));
		game.makeMove(SPARROW, null, makeCoordinate(-3, 0));
		game.makeMove(SPARROW, null, makeCoordinate(-4, 0));
		game.makeMove(SPARROW, null, makeCoordinate(-5, 0));
		game.makeMove(SPARROW, null, makeCoordinate(-6, 0));
		game.makeMove(SPARROW, null, makeCoordinate(-7, 0));
		game.makeMove(SPARROW, null, makeCoordinate(-8, 0));
		game.makeMove(SPARROW, null, makeCoordinate(-9, 0));
		game.makeMove(SPARROW, null, makeCoordinate(-10, 0));
		game.makeMove(SPARROW, null, makeCoordinate(-11, 0));
	}
	
	@Test(expected = HantoException.class) //14
	public void playerPlacesNonAdjacentPiece() throws HantoException
	{
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		game.makeMove(BUTTERFLY, null, makeCoordinate(2, 0));
	}
	
	@Test(expected = HantoException.class) // 15
	public void playerTriesToMoveButterfly() throws HantoException
	{
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		game.makeMove(BUTTERFLY, makeCoordinate(0, 0), makeCoordinate(1, 0));
	}
	
	@Test(expected = HantoException.class) // 16
	public void playerTriesToMoveSparrow() throws HantoException
	{
		game.makeMove(SPARROW, null, makeCoordinate(0, 0));
		game.makeMove(SPARROW, makeCoordinate(0, 0), makeCoordinate(1, 0));
	}
	
	@Test(expected = HantoException.class) //17
	public void redPlayerDoesNotPlaceButterflyInFirstFourTurns() throws HantoException
	{
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		game.makeMove(SPARROW, null, makeCoordinate(1, 0));
		game.makeMove(SPARROW, null, makeCoordinate(-1, 0));
		game.makeMove(SPARROW, null, makeCoordinate(-2, 0));
		game.makeMove(SPARROW, null, makeCoordinate(-3, 0));
		game.makeMove(SPARROW, null, makeCoordinate(-4, 0));
		game.makeMove(SPARROW, null, makeCoordinate(-5, 0));
		game.makeMove(SPARROW, null, makeCoordinate(-6, 0));
	}
	
	@Test(expected = HantoException.class) //18
	public void bluePlayerDoesNotPlaceButterflyInFirstFourTurns() throws HantoException
	{
		game.makeMove(SPARROW, null, makeCoordinate(0, 0));
		game.makeMove(BUTTERFLY, null, makeCoordinate(1, 0));
		game.makeMove(SPARROW, null, makeCoordinate(-1, 0));
		game.makeMove(SPARROW, null, makeCoordinate(-2, 0));
		game.makeMove(SPARROW, null, makeCoordinate(-3, 0));
		game.makeMove(SPARROW, null, makeCoordinate(-4, 0));
		game.makeMove(SPARROW, null, makeCoordinate(-5, 0));
		game.makeMove(SPARROW, null, makeCoordinate(-6, 0));
	}
	
	@Test //19
	public void sixTurnsNoWinnerProducesDraw() throws HantoException
	{
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		game.makeMove(BUTTERFLY, null, makeCoordinate(1, 0));
		game.makeMove(SPARROW, null, makeCoordinate(-1, 0));
		game.makeMove(SPARROW, null, makeCoordinate(-2, 0));
		game.makeMove(SPARROW, null, makeCoordinate(-3, 0));
		game.makeMove(SPARROW, null, makeCoordinate(-4, 0));
		game.makeMove(SPARROW, null, makeCoordinate(-5, 0));
		game.makeMove(SPARROW, null, makeCoordinate(-6, 0));
		game.makeMove(SPARROW, null, makeCoordinate(-7, 0));
		game.makeMove(SPARROW, null, makeCoordinate(-8, 0));
		game.makeMove(SPARROW, null, makeCoordinate(-9, 0));
		final MoveResult mr12 = game.makeMove(SPARROW, null, makeCoordinate(-10, 0));
		assertEquals(DRAW, mr12);
	}
	
	@Test // 20
	public void redButterflySurroundedBlueWins() throws HantoException
	{
		game = factory.makeHantoGame(HantoGameID.BETA_HANTO, RED);
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		game.makeMove(BUTTERFLY, null, makeCoordinate(1, 0));
		game.makeMove(SPARROW, null, makeCoordinate(-1, 1));
		game.makeMove(SPARROW, null, makeCoordinate(-1, 0));
		game.makeMove(SPARROW, null, makeCoordinate(0, -1));
		game.makeMove(SPARROW, null, makeCoordinate(1, -1));
		final MoveResult mr7  = game.makeMove(SPARROW, null, makeCoordinate(0, 1));
		assertEquals(BLUE_WINS, mr7);
	}
	
	@Test // 22
	public void blueButterflySurroundedRedWins() throws HantoException
	{
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		game.makeMove(BUTTERFLY, null, makeCoordinate(1, 0));
		game.makeMove(SPARROW, null, makeCoordinate(-1, 1));
		game.makeMove(SPARROW, null, makeCoordinate(-1, 0));
		game.makeMove(SPARROW, null, makeCoordinate(0, -1));
		game.makeMove(SPARROW, null, makeCoordinate(1, -1));
		final MoveResult mr7  = game.makeMove(SPARROW, null, makeCoordinate(0, 1));
		assertEquals(RED_WINS, mr7);
	}
	
	@Test //23
	public void bothPlayersSurrondedAtSameTime() throws HantoException
	{
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, -1));
		game.makeMove(SPARROW, null, makeCoordinate(0, 1));
		game.makeMove(SPARROW, null, makeCoordinate(-1, 1));
		game.makeMove(SPARROW, null, makeCoordinate(-1, 0));
		game.makeMove(SPARROW, null, makeCoordinate(-1, -1));
		game.makeMove(SPARROW, null, makeCoordinate(0, -2));
		game.makeMove(SPARROW, null, makeCoordinate(1, -2));
		game.makeMove(SPARROW, null, makeCoordinate(1, 0));
		final MoveResult mr10 = game.makeMove(SPARROW, null, makeCoordinate(1, -1));
		assertEquals(DRAW, mr10);
	}
	
	@Test //24
	public void emptyBoardPrintsEmptyString() throws HantoException
	{
		assertEquals("", game.getPrintableBoard());
	}
	
	@Test //25
	public void nonEmptyBoardPrintsPieceProperly() throws HantoException
	{
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		assertEquals("(0, 0): BLUE Butterfly\n", game.getPrintableBoard());
	}
	
	@Test // 26
	public void impossibleNegXMoveThrowsProperException() throws HantoException
	{
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		try
		{
			game.makeMove(BUTTERFLY, null, makeCoordinate(-3000, 1));
		} catch (HantoException exception)
		{
			assertEquals("Cannot have discontiguous piece", exception.getMessage());
		}
	}
	
	@Test // 27
	public void impossiblePosXMoveThrowsProperException() throws HantoException
	{
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		try
		{
			game.makeMove(BUTTERFLY, null, makeCoordinate(3000, 1));
		} catch (HantoException exception)
		{
			assertEquals("Cannot have discontiguous piece", exception.getMessage());
		}
	}
	
	@Test // 28
	public void impossiblPosYMoveThrowsProperException() throws HantoException
	{
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		try
		{
			game.makeMove(BUTTERFLY, null, makeCoordinate(0, 3000));
		} catch (HantoException exception)
		{
			assertEquals("Cannot have discontiguous piece", exception.getMessage());
		}
	}
	
	@Test // 29
	public void impossibleNegYMoveThrowsProperException() throws HantoException
	{
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		try
		{
			game.makeMove(BUTTERFLY, null, makeCoordinate(0, -3000));
		} catch (HantoException exception)
		{
			assertEquals("Cannot have discontiguous piece", exception.getMessage());
		}
	}
	
	@Test(expected = HantoException.class) //30
	public void playerCannotMakeTurnAfterWin() throws HantoException
	{
		game = factory.makeHantoGame(HantoGameID.BETA_HANTO, RED);
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		game.makeMove(BUTTERFLY, null, makeCoordinate(1, 0));
		game.makeMove(SPARROW, null, makeCoordinate(-1, 1));
		game.makeMove(SPARROW, null, makeCoordinate(-1, 0));
		game.makeMove(SPARROW, null, makeCoordinate(0, -1));
		game.makeMove(SPARROW, null, makeCoordinate(1, -1));
		final MoveResult mr7  = game.makeMove(SPARROW, null, makeCoordinate(0, 1));
		assertEquals(BLUE_WINS, mr7);
		game.makeMove(SPARROW,  null, makeCoordinate(-2, 1));
	}
	
	// Helper methods
	private HantoCoordinate makeCoordinate(int x, int y)
	{
		return new TestHantoCoordinate(x, y);
	}
}
