/**
 * 
 */
package hanto.studentirsark.epsilon;

import static hanto.common.HantoPieceType.*;

import hanto.common.HantoCoordinate;
import hanto.common.HantoException;
import hanto.common.HantoGame;
import hanto.common.HantoPiece;
import hanto.common.HantoPieceType;
import hanto.common.HantoPlayerColor;
import hanto.studentirsark.common.BaseHantoGame;
import hanto.studentirsark.common.FlyMovement;
import hanto.studentirsark.common.HantoCoordinateImpl;
import hanto.studentirsark.common.JumpMovement;
import hanto.studentirsark.common.MovementStrategy;
import hanto.studentirsark.common.WalkMovement;

/**
 * @author irshusdock, arkessler
 *
 */
public class EpsilonHantoGame extends BaseHantoGame implements HantoGame{

	//Constants defined by problem
	private final static int MAX_BUTTERFLY_WALK_MOVEMENT = 1;
	private final static int MAX_SPARROW_FLY_MOVEMENT = 4;
	private final static int MAX_CRAB_WALK_MOVEMENT = 1;
	//Movement Strategies
	MovementStrategy butterflyMoveStrat = new WalkMovement(MAX_BUTTERFLY_WALK_MOVEMENT);
	MovementStrategy crabMoveStrat = new WalkMovement(MAX_CRAB_WALK_MOVEMENT);
	MovementStrategy sparrowMoveStrat = new FlyMovement(MAX_SPARROW_FLY_MOVEMENT);
	MovementStrategy jumpMoveStrat = new JumpMovement();
	/**
	 * Basic constructor
	 * @param firstPlayer
	 */
	public EpsilonHantoGame(HantoPlayerColor firstPlayer) {
		super(firstPlayer);
		playerState = new EpsilonHantoPlayerState();
		gameAcceptsResignations = true;
	}

	/* (non-Javadoc)
	 * @see hanto.studentirsark.common.BaseHantoGame#movePiece(hanto.common.HantoCoordinate, hanto.common.HantoCoordinate, hanto.common.HantoPiece)
	 */
	@Override
	protected void movePiece(HantoCoordinate from, HantoCoordinate to, HantoPiece piece) throws HantoException {
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
		else if (piece.getType() == HORSE)
		{
			jumpMoveStrat.checkValidMovement(gameBoard, playerColor, from, to, piece);
		}
		gameBoard.removePiece(fromCoordinateImpl);
		gameBoard.addPiece(toCoordImpl, piece);
	}
	
		
}
