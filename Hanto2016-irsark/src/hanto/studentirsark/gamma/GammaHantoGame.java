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
import hanto.studentirsark.beta.BetaHantoPlayerState;
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
	private final static int MAX_WALK_MOVEMENT = 1;
	//Movement Strategies
	MovementStrategy sparrowStrat = new WalkMovement(MAX_WALK_MOVEMENT);

	/**
	 * @param firstPlayer
	 */
	public GammaHantoGame(HantoPlayerColor firstPlayer) {
		super(firstPlayer);
		playerState = new GammaHantoPlayerState();
		MAX_NUMBER_TURNS = 20;
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

	
}
