/*******************************************************************************
 * This files was developed for CS4233: Object-Oriented Analysis & Design.
 * The course was taken at Worcester Polytechnic Institute.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package hanto.studentirsark.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static hanto.common.HantoPieceType.*;
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
	// Constants
	
	//Gamma Hanto
	private final int GAMMA_HANTO_BUTTERFLY_DISTANCE = 1;
	private final int GAMMA_HANTO_SPARROW_DISTANCE = 1;
	
	//Delta Hanto
	private final int DELTA_HANTO_BUTTERFLY_DISTANCE = 1;
	private final int DELTA_HANTO_SPARROW_DISTANCE = Integer.MAX_VALUE;
	private final int DELTA_HANTO_CRAB_DISTANCE = 3;
	
	//Epsilon Hanto
	private final int EPSILON_HANTO_BUTTERFLY_DISTANCE = 1;
	private final int EPSILON_HANTO_SPARROW_DISTANCE = 4;
	private final int EPSILON_HANTO_CRAB_DISTANCE = 1;


	private static ValidMoveGenerator instance = null;
	
	private Map<HantoPieceType, MovementStrategy> movementStrategies;
	
	/**
	 * Return the singleton instance of the class
	 * @return the singleston instance of the class
	 */
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
	
	/**
	 * Generate all valid moves a player can make
	 * @param gameType the Game ID (delta, gamma, etc)
	 * @param gameBoard the game board
	 * @param playerState the current players piece counts/player state
	 * @param playerColor the current players color
	 * @param firstMove whether or not this is the first move
	 * @param butterflyLoc the location of the current players butterfly
	 * @param turnsTaken the number of turns the current player has taken
	 * @return a list of all valid moves a player can take
	 */
	public List<HantoMoveRecord> generateValidMoves (HantoGameID gameType, GameBoard gameBoard, PlayerState playerState,
			HantoPlayerColor playerColor, boolean firstMove, HantoCoordinate butterflyLoc, int turnsTaken)
	{
		List<HantoMoveRecord> retMoves = new ArrayList<HantoMoveRecord>();
		
		//Generate all possible piece placement moves
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
		
		//Generate all possible piece movement moves only if butterfly has been placed
		if(butterflyLoc != null)
		{
			List<HantoMoveRecord> movementMoves = generateMovementMoves(gameType, gameBoard, playerState, playerColor, butterflyLoc, turnsTaken);
		
			for (HantoMoveRecord rec: movementMoves)
			{
				retMoves.add(rec);
			}
		}
		return retMoves;
	}

	/**
	 * Generate all possible movement moves from a game board for a player
	 * @param gameType the type of game it is (Beta, Gamma, etc.)
	 * @param gameBoard the current game board
	 * @param playerState the current player state
	 * @param playerColor the current player color
	 * @param butterflyLoc the location of the current players butterfly
	 * @param turnsTaken the number of turns the current player has taken
	 * @return a list of all the possible movement moves (i.e. moves that do not involve placing a piece)
	 */
	private List<HantoMoveRecord> generateMovementMoves(HantoGameID gameType, GameBoard gameBoard,
			PlayerState playerState, HantoPlayerColor playerColor, HantoCoordinate butterflyLoc, int turnsTaken) {

		//The list of all empty hexes adjacent to at least one game piece
		Set<HantoCoordinate> emptyAdjacentHexes = new HashSet<HantoCoordinate>();
		
		//The list of all coordinates that are the current players color
		List<HantoCoordinate> samePlayerPieces = new ArrayList<HantoCoordinate>();
		
		//The list of possible moves. Will be returned at the end of the function
		List<HantoMoveRecord> possibleMoves = new ArrayList<HantoMoveRecord>();
		
		//Instantiate the movement strategies
		generateMovementStrategies(gameType);
		
		//Add all locations adjacent to at least one other piece
		for(HantoCoordinate coord: gameBoard.getBoard().keySet())
		{
			emptyAdjacentHexes.addAll(new HantoCoordinateImpl(coord).getAdjacentCoordinates());
		}
		
		//Filter out locations with pieces already on them
		for(HantoCoordinate coord: gameBoard.getBoard().keySet())
		{
			emptyAdjacentHexes.remove(new HantoCoordinateImpl(coord));
		}
		
		//Generate all pieces on the game board that are the same color as the current player	
		for(HantoCoordinate coord: gameBoard.getBoard().keySet())
		{
			if (gameBoard.getPieceAt(coord).getColor() == playerColor)
			{
				samePlayerPieces.add(coord);
			}		
		}
		
		//For every piece the player can move
		for(HantoCoordinate fromCoord: samePlayerPieces)
		{
			//Try to move that piece to every possible valid movement location
			MovementStrategy currentCoordsMoveStrat = movementStrategies.get(gameBoard.getPieceAt(fromCoord).getType());
			for(HantoCoordinate toCoord: emptyAdjacentHexes)
			{
				try
				{
				currentCoordsMoveStrat.checkValidMovement(gameBoard, playerColor, fromCoord, toCoord, gameBoard.getPieceAt(fromCoord));
				possibleMoves.add(new HantoMoveRecord(gameBoard.getPieceAt(fromCoord).getType(), fromCoord, toCoord));
				}
				catch (HantoException e)
				{
					continue;
				}
			}
		}
		
		return possibleMoves;
	}

	/**
	 * Based on the gametype, generate the jump, walk, and fly strategies and associate them with piece types
	 * @param gameType the game id (beta, gamma, etc)
	 */
	private void generateMovementStrategies(HantoGameID gameType) {
		movementStrategies = new HashMap<HantoPieceType, MovementStrategy>();
		switch (gameType) {
		case BETA_HANTO:
			//pieces cannot move in beta so do not add anything to the strategies
			break;
		case GAMMA_HANTO:
			//pieces can only walk 1 in Gamma hanto
			movementStrategies.put(BUTTERFLY, new WalkMovement(GAMMA_HANTO_BUTTERFLY_DISTANCE));
			movementStrategies.put(SPARROW, new WalkMovement(GAMMA_HANTO_SPARROW_DISTANCE));
			break;
		case DELTA_HANTO:
			//pieces can walk and fly in Delta hanto
			movementStrategies.put(BUTTERFLY, new WalkMovement(DELTA_HANTO_BUTTERFLY_DISTANCE));
			movementStrategies.put(CRAB, new WalkMovement(DELTA_HANTO_CRAB_DISTANCE));
			movementStrategies.put(SPARROW, new FlyMovement(DELTA_HANTO_SPARROW_DISTANCE));
			break;
		case EPSILON_HANTO:
			movementStrategies.put(BUTTERFLY, new WalkMovement(EPSILON_HANTO_BUTTERFLY_DISTANCE));
			movementStrategies.put(CRAB, new WalkMovement(EPSILON_HANTO_CRAB_DISTANCE));
			movementStrategies.put(SPARROW, new FlyMovement(EPSILON_HANTO_SPARROW_DISTANCE));
			movementStrategies.put(HORSE, new JumpMovement());
			break;
		}
	}

	/**
	 * If this is the first move of the game, this function is used to generate all possible placements of 
	 * pieces at 0,0
	 * @param playerState contains information about the number of pieces allowed for the player
	 * @return
	 */
	private List<HantoMoveRecord> generateFirstMoves(PlayerState playerState) {
		List<HantoMoveRecord> retMoves = new ArrayList<HantoMoveRecord>();
		
		
		for(HantoPieceType pieceType : HantoPieceType.values())
		{
			try
			{
				playerState.checkPieceCountValidity(pieceType);
				playerState.undoIncrement(pieceType);
				retMoves.add(new HantoMoveRecord(pieceType, null, new HantoCoordinateImpl(0,0)));
			}
			catch (HantoException e)
			{
				continue;
			}
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
		
		for(HantoPieceType pieceType : HantoPieceType.values())
		{
			try
			{
				if((pieceType != BUTTERFLY) && (butterflyLoc == null) && (turnsTaken == 3))
				{
					continue;
				}
				playerState.checkPieceCountValidity(pieceType);
				playerState.undoIncrement(pieceType);
				for(HantoCoordinate coord: validCoordsToPlace)
				{
					retMoves.add(new HantoMoveRecord(pieceType, null, coord));
				}
			}
			catch (HantoException e)
			{
				continue;
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
