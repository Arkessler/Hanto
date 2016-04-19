/**
 * 
 */
package hanto.studentirsark.delta;

import java.util.List;

import static hanto.common.HantoPieceType.*;
import hanto.common.HantoCoordinate;
import hanto.common.HantoException;
import hanto.common.HantoGame;
import hanto.common.HantoPiece;
import hanto.common.HantoPieceType;
import hanto.common.HantoPlayerColor;
import hanto.common.MoveResult;
import hanto.studentirsark.common.BaseHantoGame;
import hanto.studentirsark.common.FlyMovement;
import hanto.studentirsark.common.HantoCoordinateImpl;
import hanto.studentirsark.common.HantoPieceImpl;
import hanto.studentirsark.common.MovementStrategy;
import hanto.studentirsark.common.WalkMovement;

/**
 * @author Alexi
 *
 */
public class DeltaHantoGame extends BaseHantoGame implements HantoGame {

	//Constants defined by problem
	private final static int MAX_NUMBER_BUTTERFLIES = 1;
	private final static int MAX_NUMBER_SPARROWS = 4;
	private final static int MAX_NUMBER_CRABS = 4;
	private final static int MAX_BUTTERFLY_WALK_MOVEMENT = 1;
	private final static int MAX_CRAB_WALK_MOVEMENT = 3;
	//Movement Strategies
	MovementStrategy butterflyMoveStrat = new WalkMovement(MAX_BUTTERFLY_WALK_MOVEMENT);
	MovementStrategy crabMoveStrat = new WalkMovement(MAX_CRAB_WALK_MOVEMENT);
	MovementStrategy sparrowMoveStrat = new FlyMovement();
	/**
	 * @param firstPlayer
	 */
	public DeltaHantoGame(HantoPlayerColor firstPlayer) {
		super(firstPlayer);
	}

	/* (non-Javadoc)
	 * @see hanto.studentirsark.common.BaseHantoGame#checkMaxTurnCount(hanto.common.MoveResult)
	 */
	@Override
	protected MoveResult checkMaxTurnCount(MoveResult surroundedCheckResult) {
		return surroundedCheckResult;
	}

	/**
	 * @param pieceType
	 * @param to
	 * @param toCoordImpl
	 * @param pieceImpl
	 * @throws HantoException
	 */
	@Override
	protected void placePiece(HantoPieceType pieceType, HantoCoordinate to, HantoCoordinateImpl toCoordImpl,
			HantoPieceImpl pieceImpl) throws HantoException {
		checkPieceCountValidity(pieceType);
		//Ignore adjacency rules for the first two turns
		if (blueTurnsTaken + redTurnsTaken > 2)
		{
			checkCoordinatePieceAdjacency(to);
		}
		board.put(toCoordImpl, pieceImpl);
	}
	
	

	/* (non-Javadoc)
	 * @see hanto.studentirsark.common.BaseHantoGame#movePiece(hanto.common.HantoPieceType, hanto.common.HantoCoordinate, hanto.common.HantoCoordinate, hanto.studentirsark.common.HantoCoordinateImpl, hanto.studentirsark.common.HantoPieceImpl)
	 */
	@Override
	protected void movePiece(HantoPieceType pieceType, HantoCoordinate from, HantoCoordinate to,
			HantoCoordinateImpl toCoordImpl, HantoPieceImpl pieceImpl) throws HantoException {
		HantoCoordinateImpl fromCoordinateImpl = new HantoCoordinateImpl(from);
		
		if (pieceType == BUTTERFLY)
		{
			butterflyMoveStrat.checkValidMovement(this, board, playerColor, pieceType, from, to, toCoordImpl, pieceImpl);
			board.remove(fromCoordinateImpl);
			board.put(toCoordImpl, pieceImpl);
		}
		else if (pieceType == SPARROW)
		{
			sparrowMoveStrat.checkValidMovement(this, board, playerColor, pieceType, from, to, toCoordImpl, pieceImpl);
			board.remove(fromCoordinateImpl);
			board.put(toCoordImpl, pieceImpl);
		}
		else if (pieceType == CRAB)
		{
			crabMoveStrat.checkValidMovement(this, board, playerColor, pieceType, from, to, toCoordImpl, pieceImpl);
			board.remove(fromCoordinateImpl);
			board.put(toCoordImpl, pieceImpl);
		}
		else
		{
			throw new HantoException("Not a valid move");
		}
	
	}

	/* (non-Javadoc)
	 * @see hanto.studentirsark.common.BaseHantoGame#checkPieceCountValidity(hanto.common.HantoPieceType)
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
		else if ((playerColor == HantoPlayerColor.BLUE) && (pieceType == HantoPieceType.CRAB) && (blueCrabsPlaced < MAX_NUMBER_CRABS))
		{
			blueCrabsPlaced++;
		}
		else if ((playerColor == HantoPlayerColor.RED) && (pieceType == HantoPieceType.BUTTERFLY) && (redButterfliesPlaced < MAX_NUMBER_BUTTERFLIES))
		{
			redButterfliesPlaced++;
		}
		else if ((playerColor == HantoPlayerColor.RED) && (pieceType == HantoPieceType.SPARROW) && (redSparrowsPlaced < MAX_NUMBER_SPARROWS))
		{
			redSparrowsPlaced++;
		}
		else if ((playerColor == HantoPlayerColor.RED) && (pieceType == HantoPieceType.CRAB) && (redCrabsPlaced < MAX_NUMBER_CRABS))
		{
			redCrabsPlaced++;
		}
		else
		{
			gameOver = true;
			throw new HantoException("Too many of single type placed");
		}		
	}

}
