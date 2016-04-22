/**
 * 
 */
package hanto.studentirsark.gamma;

import static hanto.common.MoveResult.*;
import java.util.List;
import hanto.common.HantoCoordinate;
import hanto.common.HantoException;
import hanto.common.HantoGame;
import hanto.common.HantoPiece;
import hanto.common.HantoPieceType;
import hanto.common.HantoPlayerColor;
import hanto.common.MoveResult;
import hanto.studentirsark.common.BaseHantoGame;
import hanto.studentirsark.common.HantoCoordinateImpl;
import hanto.studentirsark.common.HantoPieceImpl;
import hanto.studentirsark.common.MovementStrategy;
import hanto.studentirsark.common.WalkMovement;

/**
 * @author Irshusdock, Arkessler
 *
 */
public class GammaHantoGame extends BaseHantoGame implements HantoGame {

	//Constants defined by problem
	private final static int MAX_NUMBER_BUTTERFLIES = 1;
	private final static int MAX_NUMBER_SPARROWS = 5;
	private final static int MAX_NUMBER_TURNS = 20;
	private final static int MAX_WALK_MOVEMENT = 1;
	//Movement Strategies
	MovementStrategy sparrowStrat = new WalkMovement(MAX_WALK_MOVEMENT);

	/**
	 * @param firstPlayer
	 */
	public GammaHantoGame(HantoPlayerColor firstPlayer) {
		super(firstPlayer);
	}

	
	/**
	 * @param pieceType
	 * @param from
	 * @param to
	 * @param toCoordImpl
	 * @param pieceImpl
	 * @throws HantoException
	 */
	@Override
	protected void movePiece(HantoCoordinate from, HantoCoordinate to, 
			HantoPiece piece) throws HantoException {
		
		HantoCoordinate toCoordImpl = new HantoCoordinateImpl(to);
		HantoCoordinateImpl fromCoordinateImpl = new HantoCoordinateImpl(from);
		if (!(fromCoordinateImpl.getAdjacentCoordinates().contains(toCoordImpl)))
		{
			throw new HantoException("Cannot move more than one spot at once");
		}
		sparrowStrat.checkValidMovement(gameBoard, playerColor, from, to, piece);
		gameBoard.removePiece(fromCoordinateImpl);
		gameBoard.addPiece(toCoordImpl, piece);
	}

	/**
	 * @param pieceType
	 * @param to
	 * @param toCoordImpl
	 * @param pieceImpl
	 * @throws HantoException
	 */
	@Override
	protected void placePiece(HantoCoordinate to, HantoPiece piece) throws HantoException {
		//Ignore adjacency rules for the first two turns
		if (blueTurnsTaken + redTurnsTaken > 2)
		{
			checkCoordinatePieceAdjacency(to);
		}
		HantoCoordinate toCoordImpl = new HantoCoordinateImpl(to);
		gameBoard.addPiece(toCoordImpl, piece);
	}

	/**
	 * @param pieceType is the type of piece being placed
	 * @throws HantoException in the case of an invalid number of one piece type being placed
	 */
	@Override
	protected void checkPieceCountValidity(HantoPieceType pieceType) throws HantoException {
		if ((playerColor == HantoPlayerColor.BLUE) && (pieceType == HantoPieceType.BUTTERFLY) && (blueButterfliesPlaced < MAX_NUMBER_BUTTERFLIES))
		{
			blueButterfliesPlaced++;
		}
		else if ((playerColor == HantoPlayerColor.BLUE) && (pieceType == HantoPieceType.SPARROW) && (blueSparrowsPlaced < MAX_NUMBER_SPARROWS))
		{
			blueSparrowsPlaced++;
		}
		else if ((playerColor == HantoPlayerColor.RED) && (pieceType == HantoPieceType.BUTTERFLY) && (redButterfliesPlaced < MAX_NUMBER_BUTTERFLIES))
		{
			redButterfliesPlaced++;
		}
		else if ((playerColor == HantoPlayerColor.RED) && (pieceType == HantoPieceType.SPARROW) && (redSparrowsPlaced < MAX_NUMBER_SPARROWS))
		{
			redSparrowsPlaced++;
		}
		else
		{
			gameOver = true;
			throw new HantoException("Too many of single type placed");
		}
	}
	
	
	/* (non-Javadoc)
	 * @see hanto.studentirsark.common.BaseHantoGame#checkMaxTurnCount(hanto.common.MoveResult)
	 */
	@Override
	protected MoveResult checkMaxTurnCount(MoveResult surroundedCheckResult) {
		if ((redTurnsTaken >= MAX_NUMBER_TURNS) && (blueTurnsTaken >= MAX_NUMBER_TURNS))
		{
			gameOver = true;
			if (surroundedCheckResult == OK)
			{
				return DRAW;
			}
		}
		return surroundedCheckResult;
	}

	
}
