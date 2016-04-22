/*******************************************************************************
 * This files was developed for CS4233: Object-Oriented Analysis & Design.
 * The course was taken at Worcester Polytechnic Institute.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package hanto.studentirsark.delta;


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
public class DeltaHantoMasterTest {
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
		game = factory.makeHantoGame(HantoGameID.DELTA_HANTO, BLUE);
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
		game = factory.makeHantoGame(HantoGameID.DELTA_HANTO, RED);
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
	
	@Test //5
	public void firstMoveAsCrabIsRetrievable() throws HantoException
	{
		game.makeMove(CRAB, null, makeCoordinate(0, 0));
		final HantoPiece p = game.getPieceAt(makeCoordinate(0,0));
		assertEquals(CRAB, p.getType());
	}
	
	@Test(expected = HantoException.class)	//6
	public void firstMoveNotAtOrigin() throws HantoException
	{
		game.makeMove(SPARROW, null, makeCoordinate(1, 0));
	}
	
	@Test //7
	public void secondMoveNotAtOrigin() throws HantoException
	{
		game.makeMove(SPARROW, null, makeCoordinate(0, 0));
		final MoveResult mr2 = game.makeMove(BUTTERFLY, null, makeCoordinate(1, 0));
		assertEquals(OK, mr2);
	}
	
	@Test //8
	public void secondMoveHasProperColor() throws HantoException
	{
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 1));
		HantoPiece originalMove =game.getPieceAt(makeCoordinate(0, 1));
		assertEquals(RED, originalMove.getColor());
	}
	@Test // 9
	public void playersMakeThreeMoves() throws HantoException
	{
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 1));
		final MoveResult mr3 = game.makeMove(SPARROW, null, makeCoordinate(0, -1));
		assertEquals(OK, mr3);
	}
	
	@Test // 10
	public void firstMoveRetrievableAfterMakingSecondMove() throws HantoException
	{
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		game.makeMove(SPARROW, null, makeCoordinate(1, 0));
		final HantoPiece p = game.getPieceAt(makeCoordinate(0,0));
		assertEquals(BUTTERFLY, p.getType());
		assertEquals(BLUE, p.getColor());
	}
	
	@Test // 11
	public void firstMoveRetrievableAfterMakingSecondMoveInNegativeCoordinates() throws HantoException
	{
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		game.makeMove(SPARROW, null, makeCoordinate(-1, 0));
		final HantoPiece p = game.getPieceAt(makeCoordinate(0,0));
		assertEquals(BUTTERFLY, p.getType());
		assertEquals(BLUE, p.getColor());
	}
	
	@Test(expected = HantoException.class) // 12
	public void playerPlacesPieceOnAnotherPiece() throws HantoException
	{
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		game.makeMove(SPARROW, null, makeCoordinate(1, 0));
		game.makeMove(SPARROW, null, makeCoordinate(1, 0));
	}
	
	@Test(expected = HantoException.class) //13
	public void playerPlacesMoreThanOneButterfly() throws HantoException
	{
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		game.makeMove(SPARROW, null, makeCoordinate(1, 0));
		game.makeMove(BUTTERFLY, null, makeCoordinate(-1, 0));
	}
	
	@Test //14
	public void playerPlacesMoreThanFourSparrows() throws HantoException
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
		try
		{
			game.makeMove(SPARROW, null, makeCoordinate(-5, 0));
			//The test should not reach this point
			assertTrue(false);
		} catch (HantoException exc)
		{
			assertEquals(exc.getMessage(), "Too many of single type placed");
		}
	}
	
	@Test //15
	public void playerPlacesMoreThanFourCrabs() throws HantoException
	{
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		game.makeMove(BUTTERFLY, null, makeCoordinate(1, 0));
		game.makeMove(CRAB, null, makeCoordinate(-1, 0));
		game.makeMove(CRAB, null, makeCoordinate(2, 0));
		game.makeMove(CRAB, null, makeCoordinate(-2, 0));
		game.makeMove(CRAB, null, makeCoordinate(3, 0));
		game.makeMove(CRAB, null, makeCoordinate(-3, 0));
		game.makeMove(CRAB, null, makeCoordinate(4, 0));
		game.makeMove(CRAB, null, makeCoordinate(-4, 0));
		game.makeMove(CRAB, null, makeCoordinate(5, 0));
		try
		{
			game.makeMove(CRAB, null, makeCoordinate(-5, 0));
			//The test should not reach this point
			assertTrue(false);
		} catch (HantoException exc)
		{
			assertEquals(exc.getMessage(), "Too many of single type placed");
		}
	}
	
	@Test //16
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
	
	@Test //17
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
	
	@Test //18
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
	
	@Test //19
	public void playerTriesToWalkButterflyThroughSingleHexOpening() throws HantoException
	{
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		game.makeMove(BUTTERFLY, null, makeCoordinate(1, -1));
		game.makeMove(SPARROW, null, makeCoordinate(-1, 0));
		game.makeMove(SPARROW, null, makeCoordinate(2, -2));
		game.makeMove(SPARROW, null, makeCoordinate(-1, 1));
		game.makeMove(SPARROW, null, makeCoordinate(2, -1));
		try
		{
			game.makeMove(BUTTERFLY, makeCoordinate(0, 0), makeCoordinate(0, -1));
			//The test should not reach this point
			assertTrue(false);
		} catch (HantoException exc)
		{
			assertEquals("Cannot find valid walking path to destination", exc.getMessage());
		}
		
	}
	
	@Test //20
	public void playerTriesToWalkCrabThroughSingleHexOpening() throws HantoException
	{
		game.makeMove(CRAB, null, makeCoordinate(0, 0));
		game.makeMove(CRAB, null, makeCoordinate(1, -1));
		game.makeMove(BUTTERFLY, null, makeCoordinate(-1, 0));
		game.makeMove(SPARROW, null, makeCoordinate(2, -2));
		game.makeMove(SPARROW, null, makeCoordinate(-1, 1));
		game.makeMove(SPARROW, null, makeCoordinate(2, -1));
		try
		{
			game.makeMove(CRAB, makeCoordinate(0, 0), makeCoordinate(0, -1));
			//The test should not reach this point
			assertTrue(false);
		} catch (HantoException exc)
		{
			assertEquals("Cannot find valid walking path to destination", exc.getMessage());
		}
		
	}
	
	@Test //21
	public void playerTriesToFlySparrowThroughSingleHexOpening() throws HantoException
	{
		game.makeMove(SPARROW, null, makeCoordinate(0, 0));
		game.makeMove(CRAB, null, makeCoordinate(-1, 0));
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 1));
		game.makeMove(SPARROW, null, makeCoordinate(-2, 0));
		assertEquals(game.makeMove(SPARROW, makeCoordinate(0, 0), makeCoordinate(-1, 1)), OK);
			
		
	}
	@Test //22
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
	
	@Test //23
	public void playerTriesToWalkButterflyOneHex() throws HantoException
	{
		game.makeMove(SPARROW, null, makeCoordinate(0, 0));
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, -1));
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 1));
		game.makeMove(SPARROW, null, makeCoordinate(0, -2));
		assertEquals(game.makeMove(BUTTERFLY, makeCoordinate(0, 1), makeCoordinate(1,0)), OK);	
	}
	
	@Test (expected = HantoException.class) //24
	public void playerTriesToWalkButterflyTwoHexes() throws HantoException
	{
		game.makeMove(SPARROW, null, makeCoordinate(0, 0));
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, -1));
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 1));
		game.makeMove(SPARROW, null, makeCoordinate(0, -2));
		assertEquals(game.makeMove(BUTTERFLY, makeCoordinate(0, 1), makeCoordinate(1,-1)), OK);	
	}
	
	@Test //25
	public void playerTriesToWalkCrabOneHex() throws HantoException
	{
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, -1));
		game.makeMove(CRAB, null, makeCoordinate(0, 1));
		game.makeMove(SPARROW, null, makeCoordinate(0, -2));
		assertEquals(game.makeMove(CRAB, makeCoordinate(0, 1), makeCoordinate(1,0)), OK);	
	}
	
	@Test //26
	public void playerTriesToWalkCrabTwoHexes() throws HantoException
	{
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, -1));
		game.makeMove(CRAB, null, makeCoordinate(0, 1));
		game.makeMove(SPARROW, null, makeCoordinate(0, -2));
		assertEquals(game.makeMove(CRAB, makeCoordinate(0, 1), makeCoordinate(1,-1)), OK);	
	}
	
	@Test //27
	public void playerTriesToWalkCrabThreeHexes() throws HantoException
	{
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, -1));
		game.makeMove(CRAB, null, makeCoordinate(0, 1));
		game.makeMove(SPARROW, null, makeCoordinate(0, -2));
		assertEquals(game.makeMove(CRAB, makeCoordinate(0, 1), makeCoordinate(1,-2)), OK);	
	}
	
	@Test (expected = HantoException.class)//28
	public void playerTriesToWalkCrabFourHexesAndFails() throws HantoException
	{
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, -1));
		game.makeMove(CRAB, null, makeCoordinate(0, 1));
		game.makeMove(SPARROW, null, makeCoordinate(0, -2));
		assertEquals(game.makeMove(CRAB, makeCoordinate(0, 1), makeCoordinate(0,-2)), OK);	
	}
	
	@Test (expected = HantoException.class)//29
	public void playerTriesToWalkButterflyOneHexViolatingContiguity() throws HantoException
	{
		game.makeMove(SPARROW, null, makeCoordinate(0, 0));
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, -1));
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 1));
		game.makeMove(SPARROW, null, makeCoordinate(0, -2));
		assertEquals(game.makeMove(BUTTERFLY, makeCoordinate(0, 1), makeCoordinate(0, 2)), OK);	
	}
	
	@Test (expected = HantoException.class)//30
	public void playerTriesToWalkCrabThreeHexesViolatingContinuity() throws HantoException
	{
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, -1));
		game.makeMove(CRAB, null, makeCoordinate(0, 1));
		game.makeMove(SPARROW, null, makeCoordinate(0, -2));
		assertEquals(game.makeMove(CRAB, makeCoordinate(0, 1), makeCoordinate(0, 2)), OK);	
	}
	
	@Test //31
	public void playerFliesTrappedSparrowContiguityMaintained() throws HantoException
	{
		game.makeMove(SPARROW, null, makeCoordinate(0, 0));
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 1));
		game.makeMove(BUTTERFLY, null, makeCoordinate(-1, 0));
		game.makeMove(SPARROW, null, makeCoordinate(0, 2));
		game.makeMove(SPARROW, null, makeCoordinate(0, -1));
		game.makeMove(SPARROW, null, makeCoordinate(0, 3));
		game.makeMove(SPARROW, null, makeCoordinate(1, -1));
		game.makeMove(SPARROW, null, makeCoordinate(0, 4));
		game.makeMove(SPARROW, makeCoordinate(0,0), makeCoordinate(-1, 1));
	}
	
	@Test  (expected = HantoException.class)//32
	public void playerFliesTrappedSparrowContiguityViolated() throws HantoException
	{
		game.makeMove(SPARROW, null, makeCoordinate(0, 0));
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 1));
		game.makeMove(BUTTERFLY, null, makeCoordinate(-1, 0));
		game.makeMove(SPARROW, null, makeCoordinate(0, 2));
		game.makeMove(SPARROW, null, makeCoordinate(0, -1));
		game.makeMove(SPARROW, null, makeCoordinate(0, 3));
		game.makeMove(SPARROW, null, makeCoordinate(1, -1));
		game.makeMove(SPARROW, null, makeCoordinate(0, 4));
		game.makeMove(SPARROW, makeCoordinate(0,0), makeCoordinate(0, -2));
	}
	
	@Test //33
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
			assertEquals("Wrong piece type in makeMove", exc.getMessage());
		}
	}
	
	@Test //34
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
	
	@Test(expected = HantoException.class) //35
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
	
	@Test(expected = HantoException.class) //36
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
	
	@Test //37
	public void blueButterflySurrounded () throws HantoException
	{
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		game.makeMove(SPARROW, null, makeCoordinate(0, 1));
		game.makeMove(SPARROW, null, makeCoordinate(-1, 0));
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 2));
		game.makeMove(SPARROW, null, makeCoordinate(0, -1));
		game.makeMove(BUTTERFLY, makeCoordinate(0, 2), makeCoordinate(-1, 2));
		game.makeMove(CRAB, null, makeCoordinate(1, -1));
		game.makeMove(BUTTERFLY, makeCoordinate(-1, 2), makeCoordinate(0,2));
		game.makeMove(SPARROW, makeCoordinate(-1, 0), makeCoordinate(-1, 1));
		game.makeMove(BUTTERFLY, makeCoordinate(0, 2), makeCoordinate(-1, 2));
		game.makeMove(SPARROW, null, makeCoordinate(-1, 0));
		game.makeMove(BUTTERFLY, makeCoordinate(-1, 2), makeCoordinate(0,2));
		game.makeMove(CRAB, makeCoordinate(1, -1), makeCoordinate(1, 0));
		game.makeMove(BUTTERFLY, makeCoordinate(0, 2), makeCoordinate(-1, 2));
		MoveResult mr = game.makeMove(SPARROW, null, makeCoordinate(1, -1));
		assertEquals(mr, RED_WINS);
	}
	
	@Test //38
	public void redButterflySurrounded () throws HantoException
	{
		game = factory.makeHantoGame(HantoGameID.DELTA_HANTO, RED);
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		game.makeMove(SPARROW, null, makeCoordinate(0, 1));
		game.makeMove(SPARROW, null, makeCoordinate(-1, 0));
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 2));
		game.makeMove(SPARROW, null, makeCoordinate(0, -1));
		game.makeMove(BUTTERFLY, makeCoordinate(0, 2), makeCoordinate(-1, 2));
		game.makeMove(SPARROW, null, makeCoordinate(1, -1));
		game.makeMove(BUTTERFLY, makeCoordinate(-1, 2), makeCoordinate(0,2));
		game.makeMove(SPARROW, makeCoordinate(-1, 0), makeCoordinate(-1, 1));
		game.makeMove(BUTTERFLY, makeCoordinate(0, 2), makeCoordinate(-1, 2));
		game.makeMove(SPARROW, null, makeCoordinate(-1, 0));
		game.makeMove(BUTTERFLY, makeCoordinate(-1, 2), makeCoordinate(0,2));
		game.makeMove(SPARROW, makeCoordinate(1, -1), makeCoordinate(1, 0));
		game.makeMove(BUTTERFLY, makeCoordinate(0, 2), makeCoordinate(-1, 2));
		MoveResult mr = game.makeMove(CRAB, null, makeCoordinate(1, -1));
		assertEquals(mr, BLUE_WINS);
	}
	
	@Test //39
	public void bothButterfliesSurrounded () throws HantoException
	{
		game.makeMove(BUTTERFLY, null, makeCoordinate(0,0));
		game.makeMove(BUTTERFLY, null, makeCoordinate(0,1));
		game.makeMove(SPARROW, null, makeCoordinate(-1, 0));
		game.makeMove(SPARROW, null, makeCoordinate(-1, 2));
		game.makeMove(SPARROW, null, makeCoordinate(0, -1));
		game.makeMove(SPARROW, null, makeCoordinate(0, 2));
		game.makeMove(SPARROW, null, makeCoordinate(1, -1));
		game.makeMove(SPARROW, null, makeCoordinate(1, 1));
		game.makeMove(SPARROW, null, makeCoordinate(-2, 1));
		game.makeMove(SPARROW, null, makeCoordinate(2, 0));
		game.makeMove(SPARROW, makeCoordinate(-2,1), makeCoordinate(-1, 1));
		assertEquals(game.makeMove(SPARROW, makeCoordinate(2,0), makeCoordinate(1, 0)), DRAW);
		
	}
	
	@Test //40
	public void blueResigns() throws HantoException
	{
		MoveResult mr = game.makeMove(null, null, null);
		assertEquals(mr, RED_WINS);
	}
	
	@Test //41
	public void redResigns() throws HantoException
	{
		game.makeMove(BUTTERFLY, null, makeCoordinate(0,0));
		MoveResult mr = game.makeMove(null, null, null);
		assertEquals(mr, BLUE_WINS);
	}
	
	@Test (expected = HantoException.class)//42
	public void tryToMoveNotValidPiece() throws HantoException
	{
		game.makeMove(DOVE, null, makeCoordinate(0,0));
	}
	
	// Helper methods
	private HantoCoordinate makeCoordinate(int x, int y)
	{
		return new TestHantoCoordinate(x, y);
	}
}
