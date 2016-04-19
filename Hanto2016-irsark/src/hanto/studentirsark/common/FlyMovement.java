/**
 * 
 */
package hanto.studentirsark.common;

import java.util.Map;

import hanto.common.HantoCoordinate;
import hanto.common.HantoException;
import hanto.common.HantoPiece;
import hanto.common.HantoPieceType;
import hanto.common.HantoPlayerColor;

/**
 * @author Alexi
 *
 */
public class FlyMovement implements MovementStrategy {

	/* (non-Javadoc)
	 * @see hanto.studentirsark.common.MovementStrategy#isValidMovement(hanto.studentirsark.common.BaseHantoGame, java.util.Map, hanto.common.HantoPlayerColor, hanto.common.HantoPieceType, hanto.common.HantoCoordinate, hanto.common.HantoCoordinate, hanto.studentirsark.common.HantoCoordinateImpl, hanto.studentirsark.common.HantoPieceImpl)
	 */
	@Override
	public void checkValidMovement(BaseHantoGame game, Map<HantoCoordinate, HantoPiece> board,
			HantoPlayerColor playerColor, HantoPieceType pieceType, HantoCoordinate from, HantoCoordinate to,
			HantoCoordinateImpl toCoordImpl, HantoPieceImpl pieceImpl) throws HantoException {
		return;
	}

}
