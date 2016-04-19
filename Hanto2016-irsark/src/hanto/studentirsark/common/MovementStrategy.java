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
public interface MovementStrategy {
		
	public void checkValidMovement(BaseHantoGame game, Map<HantoCoordinate, HantoPiece> board, HantoPlayerColor playerColor, 
			HantoPieceType pieceType, HantoCoordinate from, HantoCoordinate to,
			HantoCoordinateImpl toCoordImpl, HantoPieceImpl pieceImpl) throws HantoException;
}
