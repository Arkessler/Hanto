package hanto.studentirsark.tournament;

import static hanto.common.HantoPlayerColor.BLUE;
import static hanto.common.HantoPlayerColor.RED;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import hanto.common.HantoCoordinate;
import hanto.common.HantoException;
import hanto.common.HantoGame;
import hanto.common.HantoGameID;
import hanto.studentirsark.HantoGameFactory;
import hanto.tournament.HantoMoveRecord;

import static hanto.common.HantoGameID.*;
import static hanto.common.MoveResult.*;
import static hanto.common.HantoPieceType.*;

public class TournamentPlayerTest {

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
	
	// Helper methods
	private HantoCoordinate makeCoordinate(int x, int y)
	{
		return new TestHantoCoordinate(x, y);
	}
	
	private static HantoGameFactory factory;
	private HantoPlayer player;
	private HantoGame game;
	
	@BeforeClass
	public static void initializeClass()
	{
		factory = HantoGameFactory.getInstance();
	}
	
	@Before
	public void setup()
	{
		player = new HantoPlayer();
		game = factory.makeHantoGame(EPSILON_HANTO, BLUE);
	}
	
	@Test //1
	public void playerCanStartEpsilonGame()
	{
		player.startGame(EPSILON_HANTO, BLUE, true);
	}
	
	@Test //2
	public void playerCanMakeValidFirstMove() throws HantoException
	{
		player.startGame(EPSILON_HANTO, BLUE, true);
		HantoMoveRecord mr = player.makeMove(new HantoMoveRecord(null, null, null));
		assertEquals(game.makeMove(mr.getPiece(), mr.getFrom(), mr.getTo()), OK);
	}
	
	@Test //3
	public void playerCanMakeValidSecondMove() throws HantoException
	{
		player.startGame(EPSILON_HANTO, RED, false);
		game.makeMove(BUTTERFLY, null, makeCoordinate(0,0));
		HantoMoveRecord mr = player.makeMove(new HantoMoveRecord(BUTTERFLY, null, makeCoordinate(0,0)));
		assertEquals(game.makeMove(mr.getPiece(), mr.getFrom(), mr.getTo()), OK);
	}
	
	@Test //3
	public void playerCanMake2ValidMoves() throws HantoException
	{
		player.startGame(EPSILON_HANTO, BLUE, true);
		HantoMoveRecord mr = player.makeMove(new HantoMoveRecord(null, null, null));
		assertEquals(game.makeMove(mr.getPiece(), mr.getFrom(), mr.getTo()), OK);
		game.makeMove(BUTTERFLY, null, makeCoordinate(1,0));
		mr = player.makeMove(new HantoMoveRecord(BUTTERFLY, null, makeCoordinate(1,0)));
		assertEquals(game.makeMove(mr.getPiece(), mr.getFrom(), mr.getTo()), OK);
	}
	
}
