/**
 * 
 */
package hanto.studentirsark.delta;

import static hanto.common.HantoPieceType.BUTTERFLY;
import static hanto.common.HantoPieceType.CRAB;
import static hanto.common.HantoPieceType.SPARROW;

import hanto.common.HantoCoordinate;
import hanto.common.HantoException;
import hanto.common.HantoGame;
import hanto.common.HantoPiece;
import hanto.common.HantoPlayerColor;
import hanto.common.MoveResult;
import hanto.studentirsark.common.BaseHantoGame;
import hanto.studentirsark.common.FlyMovement;
import hanto.studentirsark.common.HantoCoordinateImpl;
import hanto.studentirsark.common.MovementStrategy;
import hanto.studentirsark.common.WalkMovement;

/**
 * @author Alexi
 *
 */
public class DeltaHantoGame extends BaseHantoGame implements HantoGame {

	//Constants defined by problem
	private final static int MAX_BUTTERFLY_WALK_MOVEMENT = 1;
	private final static int MAX_CRAB_WALK_MOVEMENT = 3;
	//Movement Strategies
	MovementStrategy butterflyMoveStrat = new WalkMovement(MAX_BUTTERFLY_WALK_MOVEMENT);
	MovementStrategy crabMoveStrat = new WalkMovement(MAX_CRAB_WALK_MOVEMENT);
	MovementStrategy sparrowMoveStrat = new FlyMovement(Integer.MAX_VALUE);
	/**
	 * @param firstPlayer
	 */
	public DeltaHantoGame(HantoPlayerColor firstPlayer) {
		super(firstPlayer);
		playerState = new DeltaHantoPlayerState();
		gameAcceptsResignations = true;
	}

	/* (non-Javadoc)
	 * @see hanto.studentirsark.common.BaseHantoGame#checkMaxTurnCount(hanto.common.MoveResult)
	 */
	@Override
	protected MoveResult checkMaxTurnCount(MoveResult surroundedCheckResult) {
		return surroundedCheckResult;
	}

	/* (non-Javadoc)
	 * @see hanto.studentirsark.common.BaseHantoGame#movePiece(hanto.common.HantoPieceType, hanto.common.HantoCoordinate, hanto.common.HantoCoordinate, hanto.studentirsark.common.HantoCoordinateImpl, hanto.studentirsark.common.HantoPieceImpl)
	 */
	@Override
	protected void movePiece(HantoCoordinate from, HantoCoordinate to,
			HantoPiece piece) throws HantoException {
		HantoCoordinateImpl fromCoordinateImpl = new HantoCoordinateImpl(from);
		HantoCoordinate toCoordImpl = new HantoCoordinateImpl(to);
		if (piece.getType() == BUTTERFLY)
		{
			butterflyMoveStrat.checkValidMovement(gameBoard, playerColor, from, to, piece);
		}
		else if (piece.getType() == SPARROW)
		{
			sparrowMoveStrat.checkValidMovement(gameBoard, playerColor, from, to, piece);
		}
		else if (piece.getType() == CRAB)
		{
			crabMoveStrat.checkValidMovement(gameBoard, playerColor, from, to, piece);
		}	
		gameBoard.removePiece(fromCoordinateImpl);
		gameBoard.addPiece(toCoordImpl, piece);
	}
	
}
