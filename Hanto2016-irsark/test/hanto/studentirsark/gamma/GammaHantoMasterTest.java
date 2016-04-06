/*******************************************************************************
 * This files was developed for CS4233: Object-Oriented Analysis & Design.
 * The course was taken at Worcester Polytechnic Institute.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package hanto.studentirsark.gamma;


import static hanto.common.HantoPieceType.*;
import static hanto.common.MoveResult.*;
import static hanto.common.HantoPlayerColor.*;
import static org.junit.Assert.*;
import hanto.common.*;
import hanto.studentirsark.HantoGameFactory;

import org.junit.*;

/**
 * @authors Ian Shusdock, Alexi Kessler
 *
 */
public class GammaHantoMasterTest {
	/**
	 * Internal class for these test cases.
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
		game = factory.makeHantoGame(HantoGameID.GAMMA_HANTO, BLUE);
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
	
	@Test //7
	public void secondMoveHasProperColor() throws HantoException
	{
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 1));
		HantoPiece originalMove =game.getPieceAt(makeCoordinate(0, 1));
		assertEquals(RED, originalMove.getColor());
	}
	@Test // 8
	public void playersMakeThreeMoves() throws HantoException
	{
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 1));
		final MoveResult mr3 = game.makeMove(SPARROW, null, makeCoordinate(0, -1));
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
	
	@Test //13
	public void playerPlacesMoreThanFiveSparrows() throws HantoException
	{
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		game.makeMove(BUTTERFLY, null, makeCoordinate(1, 0));
		game.makeMove(SPARROW, null, makeCoordinate(-1, 0));
		game.makeMove(SPARROW, null, makeCoordinate(2, 0));
		game.makeMove(SPARROW, null, makeCoordinate(-2, 0));
		game.makeMove(SPARROW, null, makeCoordinate(3, 0));
		game.makeMove(SPARROW, null, makeCoordinate(-3, 0));
		game.makeMove(SPARROW, null, makeCoordinate(4, 0));
		game.makeMove(SPARROW, null, makeCoordinate(-4, 0));
		game.makeMove(SPARROW, null, makeCoordinate(5, 0));
		game.makeMove(SPARROW, null, makeCoordinate(-5, 0));
		game.makeMove(SPARROW, null, makeCoordinate(6, 0));
		try
		{
			game.makeMove(SPARROW, null, makeCoordinate(-6, 0));
			//The test should not reach this point
			assertTrue(false);
		} catch (HantoException exc)
		{
			assertEquals(exc.getMessage(), "Too many of single type placed");
		}
	}
	
	@Test //14
	public void playerPlacesPieceAdjacentToOpponentPiece() throws HantoException
	{
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		game.makeMove(BUTTERFLY, null, makeCoordinate(1, 0));
		try
		{
			game.makeMove(SPARROW, null, makeCoordinate(1, -1));
			//The test should not reach this point
			assertTrue(false);
		} catch (HantoException exc)
		{
			assertEquals(exc.getMessage(), "Cannot place piece next to opponent piece");
		}
	}
	
	@Test //15
	public void playerPlacesNonAdjacentPiece() throws HantoException
	{
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		game.makeMove(BUTTERFLY, null, makeCoordinate(1, 0));
		try
		{
			game.makeMove(SPARROW, null, makeCoordinate(4, -2));
			//The test should not reach this point
			assertTrue(false);
		} catch (HantoException exc)
		{
			assertEquals(exc.getMessage(), "Must place piece next to piece of same color");
		}
	}
	
	@Test //16
	public void playerTriesToMoveNonExistingPiece() throws HantoException
	{
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		try
		{
			game.makeMove(BUTTERFLY, makeCoordinate(0, 1), makeCoordinate(1, 0));
			//The test should not reach this point
			assertTrue(false);
		} catch (HantoException exc)
		{
			assertEquals("Trying to move a non-existant piece", exc.getMessage());
		}
	}
	
	@Test //17
	public void playerTriesToMovePieceThroughSingleHexOpening() throws HantoException
	{
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		game.makeMove(BUTTERFLY, null, makeCoordinate(1, -1));
		game.makeMove(SPARROW, null, makeCoordinate(-1, 0));
		game.makeMove(SPARROW, null, makeCoordinate(2, -2));
		game.makeMove(SPARROW, null, makeCoordinate(0, 1));
		game.makeMove(SPARROW, null, makeCoordinate(2, -1));
		try
		{
			game.makeMove(SPARROW, makeCoordinate(0, 0), makeCoordinate(-1, 1));
			//The test should not reach this point
			assertTrue(false);
		} catch (HantoException exc)
		{
			assertEquals("Cannot move through single hex opening", exc.getMessage());
		}
		
	}
	
	@Test //18
	public void playerTriesToMovePieceOntoAnotherPiece() throws HantoException
	{
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		game.makeMove(BUTTERFLY, null, makeCoordinate(1, -1));
		try
		{
			game.makeMove(BUTTERFLY, makeCoordinate(0, 0), makeCoordinate(1, -1));
			//The test should not reach this point
			assertTrue(false);
		} catch (HantoException exc)
		{
			assertEquals("Trying to place piece on existing piece", exc.getMessage());
		}
	}
	
	@Test //19
	public void playerTriesToMovePieceViolatingContiguity() throws HantoException
	{
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		game.makeMove(BUTTERFLY, null, makeCoordinate(1, -1));
		try
		{
			game.makeMove(BUTTERFLY, makeCoordinate(0, 0), makeCoordinate(-1, 1));
			//The test should not reach this point
			assertTrue(false);
		} catch (HantoException exc)
		{
			assertEquals("Cannot have discontiguous piece", exc.getMessage());
		}
	}
	
	@Test //20
	public void playerTriesToMovePieceWithValidContiguity() throws HantoException
	{
		System.out.println("Starting test of valid move");
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		game.makeMove(BUTTERFLY, null, makeCoordinate(1, -1));
		game.makeMove(SPARROW, null, makeCoordinate(-1, 0));
		game.makeMove(SPARROW, null, makeCoordinate(2, -2));
		game.makeMove(SPARROW, null, makeCoordinate(0, 1));
		game.makeMove(SPARROW, null, makeCoordinate(2, -1));
		System.out.println("Making move");
		game.makeMove(SPARROW, makeCoordinate(0, 1), makeCoordinate(-1, 1));
	}
	
	@Test //21
	public void playerTriesToMovePieceOfWrongType() throws HantoException
	{
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		game.makeMove(BUTTERFLY, null, makeCoordinate(1, -1));
		try
		{
			game.makeMove(SPARROW, makeCoordinate(0, 0), makeCoordinate(1, 0));
			//The test should not reach this point
			assertTrue(false);
		} catch (HantoException exc)
		{
			assertEquals("Wrong piece type is makeMove", exc.getMessage());
		}
	}
	
	@Test //22
	public void playerTriesToMoveOpponentPiece() throws HantoException
	{
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		game.makeMove(BUTTERFLY, null, makeCoordinate(1, -1));
		try
		{
			game.makeMove(SPARROW, makeCoordinate(1, -1), makeCoordinate(1, 0));
			//The test should not reach this point
			assertTrue(false);
		} catch (HantoException exc)
		{
			assertEquals("Can't move opponent's piece", exc.getMessage());
		}
	}
	
	@Test(expected = HantoException.class) //23
	public void redPlayerDoesNotPlaceButterflyInFirstFourTurns() throws HantoException
	{
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		game.makeMove(SPARROW, null, makeCoordinate(1, 0));
		game.makeMove(SPARROW, null, makeCoordinate(-1, 0));
		game.makeMove(SPARROW, null, makeCoordinate(2, 0));
		game.makeMove(SPARROW, null, makeCoordinate(-2, 0));
		game.makeMove(SPARROW, null, makeCoordinate(3, 0));
		game.makeMove(SPARROW, null, makeCoordinate(-3, 0));
		game.makeMove(SPARROW, null, makeCoordinate(4, 0));
	}
	
	@Test(expected = HantoException.class) //24
	public void bluePlayerDoesNotPlaceButterflyInFirstFourTurns() throws HantoException
	{
		game.makeMove(SPARROW, null, makeCoordinate(0, 0));
		game.makeMove(BUTTERFLY, null, makeCoordinate(1, 0));
		game.makeMove(SPARROW, null, makeCoordinate(-1, 0));
		game.makeMove(SPARROW, null, makeCoordinate(2, 0));
		game.makeMove(SPARROW, null, makeCoordinate(-2, 0));
		game.makeMove(SPARROW, null, makeCoordinate(3, 0));
		game.makeMove(SPARROW, null, makeCoordinate(-3, 0));
		game.makeMove(SPARROW, null, makeCoordinate(4, 0));
	}
	
	@Test //25
	public void gameEndsInADrawAfterTwentyTurns() throws HantoException
	{
		assertTrue(false);
	}
	
	// Helper methods
	private HantoCoordinate makeCoordinate(int x, int y)
	{
		return new TestHantoCoordinate(x, y);
	}
}
