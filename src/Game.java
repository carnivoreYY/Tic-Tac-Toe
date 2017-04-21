import java.util.Random;


public class Game {
	private int type;
	private Board board;
	private Random random;
	
	public Game(){
		initializeGame();
		displayBoard();
		chooseAlgorithmType();
		makeFirstMove();
		playGame();
		checkStatus();
	}
	
	private void checkStatus() {
		if (board.isWinning(Player.COMPUTER)) {
	        System.out.println("Computer has won");
	    } else if (board.isWinning(Player.USER)) {
	        System.out.println("Player has won");
	    } else {
	        System.out.println("It's a draw!");
	    }
	}

	private void playGame() {
		
		while ( board.isRunning() ) {
			
	        System.out.println("User move: ");
	        Cell userCell = new Cell(board.getScanner().nextInt(), board.getScanner().nextInt());

	        board.move(userCell, Player.USER); 
	        board.displayBoard();
	        
	        if (!board.isRunning()) {
	            break;
	        }
	        if(type == 1) {
	        	board.callMinimax(0, Player.COMPUTER, Integer.MIN_VALUE, Integer.MAX_VALUE);
		        
		        for (Cell cell : board.getRootValues()) {
		            System.out.println("Cell values: " + cell + " minimaxValue: " + cell.getMinimaxValue());
		        }
		        
		        board.move(board.getBestMoveMinimax(), Player.COMPUTER);
	        }else if(type == 2) {
	        	board.callHeuristic();
	        	for (Cell cell : board.getRootValues()) {
		            System.out.println("Cell values: " + cell + " heuristicValue: " + cell.getHeuristicValue());
		        }
	        	board.move(board.getBestMoveHeuristic(), Player.COMPUTER);
	        }
	        
	        board.displayBoard();
	    }
	}
	private void chooseAlgorithmType() {
		
		System.out.println("Which algorithm? 1 - minimax ; 2 - heuristic");
		type = board.getScanner().nextInt();
		
	}
	private void makeFirstMove() {
		
		System.out.println("Who starts? 1 - Computer ; 2 - User"); 
	    int choice = board.getScanner().nextInt();
	    
	    if(choice == 1){
	        Cell cell = new Cell(random.nextInt(Constants.BOARD_SIZE), random.nextInt(Constants.BOARD_SIZE));
	        board.move(cell, Player.COMPUTER);
	        board.displayBoard();
	    }
	}

	private void displayBoard() {
		 board.displayBoard();
	}

	private void initializeGame() {
		this.board = new Board();
		this.board.setupBoard();
	    this.random = new Random();
	}
}
