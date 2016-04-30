/**
 * 
 */
package hanto.studentirsark.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import hanto.common.HantoCoordinate;
import hanto.common.HantoException;
import hanto.common.HantoGameID;
import hanto.common.HantoPieceType;
import hanto.common.HantoPlayerColor;
import hanto.tournament.HantoMoveRecord;


/**
 * @author arkessler, irshusdock
 *
 */
public class ValidMoveGenerator {
	private static ValidMoveGenerator instance = null;
	
	public static ValidMoveGenerator getInstance()
	{
		if (instance == null)
		{
			instance = new ValidMoveGenerator();
		}
		return instance;
	}
	
	private ValidMoveGenerator()
	{
		
	}
	
	public List<HantoMoveRecord> generateValidMoves (HantoGameID gameType, GameBoard gameBoard, PlayerState playerState,
			HantoPlayerColor playerColor, boolean firstMove, HantoCoordinate butterflyLoc, int turnsTaken)
	{
		List<HantoMoveRecord> retMoves = new ArrayList<HantoMoveRecord>();
		
		List<HantoMoveRecord> placementMoves;
		if (firstMove)
		{
			placementMoves = generateFirstMoves(playerState);
		}
		else
		{
			placementMoves = generatePlacementMoves(gameType, gameBoard, playerState, playerColor, butterflyLoc, turnsTaken);
		}
		
		
		
		for (HantoMoveRecord rec: placementMoves)
		{
			retMoves.add(rec);
		}
		return retMoves;
	}

	/**
	 * If this is the first move of the game, this function is used to generate all possible placements of 
	 * pieces at 0,0
	 * @param playerState contains information about the number of pieces allowed for the player
	 * @return
	 */
	private List<HantoMoveRecord> generateFirstMoves(PlayerState playerState) {
		List<HantoMoveRecord> retMoves = new ArrayList<HantoMoveRecord>();
		try
		{
			playerState.checkPieceCountValidity(HantoPieceType.BUTTERFLY);
			HantoMoveRecord newRec = new HantoMoveRecord(HantoPieceType.BUTTERFLY, null, new HantoCoordinateImpl(0, 0));
			retMoves.add(newRec);	
		} catch (HantoException exc)
		{
			
		}
		try
		{
			playerState.checkPieceCountValidity(HantoPieceType.SPARROW);
			HantoMoveRecord newRec = new HantoMoveRecord(HantoPieceType.SPARROW, null, new HantoCoordinateImpl(0, 0));
			retMoves.add(newRec);
		} catch (HantoException exc)
		{
			
		}
		try
		{
			playerState.checkPieceCountValidity(HantoPieceType.CRAB);
			HantoMoveRecord newRec = new HantoMoveRecord(HantoPieceType.CRAB, null, new HantoCoordinateImpl(0, 0));
			retMoves.add(newRec);
		} catch (HantoException exc)
		{
			
		}
		try
		{
			playerState.checkPieceCountValidity(HantoPieceType.HORSE);
			HantoMoveRecord newRec = new HantoMoveRecord(HantoPieceType.HORSE, null, new HantoCoordinateImpl(0, 0));
			retMoves.add(newRec);
		} catch (HantoException exc)
		{
			
		}
		
		return retMoves;
	}

	/**
	 * generate all possible piece placements
	 * @param gameType the version of the game being used
	 * @param gameBoard the game board
	 * @param playerState information about the number of pieces placed for each player
	 * @param playerColor the color of the current player
	 * @return
	 */
	private List<HantoMoveRecord> generatePlacementMoves(HantoGameID gameType, GameBoard gameBoard, PlayerState playerState,
			HantoPlayerColor playerColor, HantoCoordinate butterflyLoc, int turnsTaken) {
		List<HantoMoveRecord> retMoves = new ArrayList<HantoMoveRecord>();
		
		List<HantoCoordinate> validCoordsToPlace = new ArrayList<HantoCoordinate>();
		
		Set<HantoCoordinate> allPlacedPieces = gameBoard.getBoard().keySet();
		List<HantoCoordinate> samePlayerPieces = new ArrayList<HantoCoordinate>();
		
		//Get the coordinates that correspond to this player's pieces
		for (HantoCoordinate coord: allPlacedPieces)
		{
			if (gameBoard.getPieceAt(coord)!=null)
			{
				if (turnsTaken != 0)
				{
					if (gameBoard.getPieceAt(coord).getColor() == playerColor)
					{
						samePlayerPieces.add(coord);
					}
				}
				else
				{
					samePlayerPieces.add(coord);
				}
			}
		}
		
		//Find the valid coords for placement within the adjacent coords
		for (HantoCoordinate coord: samePlayerPieces)
		{
			HantoCoordinateImpl coordImpl = new HantoCoordinateImpl(coord);
			List<HantoCoordinate> adjacentCoords = coordImpl.getAdjacentCoordinates();
			for (HantoCoordinate adjCoord: adjacentCoords)
			{
				if (gameBoard.getPieceAt(adjCoord) == null)
				{
					if (turnsTaken != 0)
					{
						if (noOpponentPiecesAdjacent(gameBoard, adjCoord, playerColor))
						{
							validCoordsToPlace.add(adjCoord);
						}
					}
					else
					{
						validCoordsToPlace.add(adjCoord);
					}
				}
			}
		}
		
		//Generate MoveRecords for butterflies
		try
		{
			playerState.checkPieceCountValidity(HantoPieceType.BUTTERFLY);
			for(HantoCoordinate coord: validCoordsToPlace)
			{
				HantoMoveRecord newRec = new HantoMoveRecord(HantoPieceType.BUTTERFLY, null, coord);
				retMoves.add(newRec);
			}
		} catch (HantoException exc)
		{
			
		}
		
		//Check if butterfly needs to be placed
		if ((butterflyLoc == null) && (turnsTaken == 3))
		{
				//If a butterfly needs to be placed, don't generate any other moves
		}
		else	//Otherwise generate placements for the other valid piece types
		{
			try
			{
				playerState.checkPieceCountValidity(HantoPieceType.SPARROW);
				for(HantoCoordinate coord: validCoordsToPlace)
				{
					HantoMoveRecord newRec = new HantoMoveRecord(HantoPieceType.SPARROW, null, coord);
					retMoves.add(newRec);
				}
			} catch (HantoException exc)
			{
				
			}
			try
			{
				playerState.checkPieceCountValidity(HantoPieceType.CRAB);
				for(HantoCoordinate coord: validCoordsToPlace)
				{
					HantoMoveRecord newRec = new HantoMoveRecord(HantoPieceType.CRAB, null, coord);
					retMoves.add(newRec);
				}
			} catch (HantoException exc)
			{
				
			}
			try
			{
				playerState.checkPieceCountValidity(HantoPieceType.HORSE);
				for(HantoCoordinate coord: validCoordsToPlace)
				{
					HantoMoveRecord newRec = new HantoMoveRecord(HantoPieceType.HORSE, null, coord);
					retMoves.add(newRec);
				}
			} catch (HantoException exc)
			{
				
			}
		}
		
		return retMoves;
	}

	/**
	 * Checks if there are any opponent pieces adjacent to checkCoord
	 * @param playerColor the current player's color
	 * @param gameBoard the gameBoard that is being checked
	 * @param checkCoord the coordinate being checked
	 * @return
	 */
	private boolean noOpponentPiecesAdjacent(GameBoard gameBoard, HantoCoordinate checkCoord, HantoPlayerColor playerColor) {
		boolean retVal = true;
		HantoCoordinateImpl checkCoordImpl = new HantoCoordinateImpl(checkCoord);
		List<HantoCoordinate> adjacentPieces = checkCoordImpl.getAdjacentCoordinates();
		for (HantoCoordinate coord: adjacentPieces)
		{
			if (gameBoard.getPieceAt(coord) != null)
			{
				if (gameBoard.getPieceAt(coord).getColor() != playerColor)
				{
					retVal = false;
				}
			}
		}
		return retVal;
	}
}
