/**
 * 
 */
package hanto.studentirsark.common;

import hanto.common.HantoCoordinate;
import hanto.common.HantoException;
import hanto.common.HantoPiece;
import hanto.common.HantoPlayerColor;

/**
 * @author Alexi
 *
 */
public class JumpMovement implements MovementStrategy {

	/* (non-Javadoc)
	 * @see hanto.studentirsark.common.MovementStrategy#checkValidMovement(hanto.studentirsark.common.GameBoard, hanto.common.HantoPlayerColor, hanto.common.HantoCoordinate, hanto.common.HantoCoordinate, hanto.common.HantoPiece)
	 */
	@Override
	public void checkValidMovement(GameBoard gameBoard, HantoPlayerColor playerColor, HantoCoordinate from,
			HantoCoordinate to, HantoPiece piece) throws HantoException {
		HantoCoordinateImpl fromCoordImpl = new HantoCoordinateImpl(from);
		HantoCoordinateImpl toCoordImpl = new HantoCoordinateImpl(to);
		checkLinearity(fromCoordImpl, toCoordImpl);
		checkNotAdjacent(fromCoordImpl, toCoordImpl);
		checkForEmptyHexInPath(fromCoordImpl, toCoordImpl, gameBoard);
	}

	/**
	 * @param from
	 * @param to
	 */
	private void checkLinearity(HantoCoordinateImpl from, HantoCoordinateImpl to) throws HantoException{
		if ( !(from.getX() == to.getX()) && !(from.getY() == to.getY()) && !(from.getZCoordinate() == to.getZCoordinate()))
		{
			throw new HantoException("Jumps must be done in a straight line");
		}
	}
	
	private void checkForEmptyHexInPath(HantoCoordinateImpl from, HantoCoordinateImpl to, GameBoard gameBoard) throws HantoException
	{
		int fromX = from.getX();
		int fromY = from.getY();
		int fromZ = from.getZCoordinate();
		int toX = to.getX();
		int toY = to.getY();
		int toZ = to.getZCoordinate();
		
		int incrementX = 0;
		int incrementY = 0;
		
		if (fromX == toX)
		{
			if ((fromY - toY) > 0) 
			{
				incrementY = -1;
			}
			else 
			{
				incrementY = 1;
			}
		}
		else if (fromY == toY)
		{
			if ((fromX - toX) > 0) 
			{
				incrementX = -1;
			}
			else 
			{
				incrementX = 1;
			}
		}
		else if (fromZ == toZ)
		{
			if ((fromY - toY) > 0)  
			{
				incrementX = 1;
				incrementY = -1;
			}
			else 
			{
				incrementX = -1;
				incrementY = 1;
			}
		}
		
		HantoCoordinateImpl currCoord = new HantoCoordinateImpl(from);
		while (!(currCoord.equals(to)))
		{
			currCoord = new HantoCoordinateImpl(currCoord.getX()+incrementX, currCoord.getY()+incrementY);
			if ((gameBoard.getPieceAt(currCoord) == null) && (!(currCoord.equals(to))))
			{
				System.out.println("VIOLATED VIOLENTLY at x:"+currCoord.getX()+" y:"+currCoord.getY());
				throw new HantoException("Trying to jump over empty hex");
			}
		}
	}

	private void checkNotAdjacent(HantoCoordinateImpl from, HantoCoordinateImpl to) throws HantoException
	{
		if (from.getAdjacentCoordinates().contains(to))
		{
			throw new HantoException("Cannot jump to adjacent hex");
		}
	}
}
	