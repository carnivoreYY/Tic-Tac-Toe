import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Board {

	private List<Cell> emptyCells;
    private Scanner scanner;
    private Player[][] board;
    private List<Cell> rootValues;

    public Board() {
    	initializeBoard();
    }

    private void initializeBoard() {
    	this.rootValues = new ArrayList<>();
		this.scanner = new Scanner(System.in);
		this.board = new Player[Constants.BOARD_SIZE][Constants.BOARD_SIZE];
	}

	public boolean isRunning() {
		
		if( isWinning(Player.COMPUTER) ) return false;
		if( isWinning(Player.USER)) return false;
		if( getEmptyCells().isEmpty() )return false;
		
		return true;
    }

    public boolean isWinning(Player player) {
    	
        if ( board[0][0] == player && board[1][1] == player && board[2][2] == player ) {
            return true;
        }
        
        if( board[0][2] == player && board[1][1] == player && board[2][0] == player ){
        	return true;
        }
        
        for (int i = 0; i < Constants.BOARD_SIZE; i++) {
           
        	// checking the rows
        	if ( board[i][0] == player && board[i][1] == player && board[i][2] == player ) {
                return true;
            }
        	
        	// checking the columns
        	if( board[0][i] == player && board[1][i] == player && board[2][i] == player ){
        		return true;
        	}
        }
        
        return false;
    }
    
    public int getHeuristicScore() {
    	int score = 0;
    	// Evaluate score for each of the 8 lines (3 rows, 3 columns, 2 diagonals)
        score += evaluateLine(0, 0, 0, 1, 0, 2);  // row 0
        score += evaluateLine(1, 0, 1, 1, 1, 2);  // row 1
        score += evaluateLine(2, 0, 2, 1, 2, 2);  // row 2
        score += evaluateLine(0, 0, 1, 0, 2, 0);  // column 0
        score += evaluateLine(0, 1, 1, 1, 2, 1);  // column 1
        score += evaluateLine(0, 2, 1, 2, 2, 2);  // column 2
        score += evaluateLine(0, 0, 1, 1, 2, 2);  // diagonal
        score += evaluateLine(0, 2, 1, 1, 2, 0);  // alternate diagonal
    	return score; 
    }
    
    private int evaluateLine(int row1, int col1, int row2, int col2, int row3, int col3) {
    	int score = 0;
    	
    	// First cell
        if ( board[row1][col1] == Player.COMPUTER ) {
           score = 1;
        } else if ( board[row1][col1] == Player.USER ) {
           score = -1;
        }
        
        // Second cell
        if ( board[row2][col2] == Player.COMPUTER ) {
           if (score == 1) {   // board1 is COMPUTER
              score = 10;
           } else if (score == -1) {  // board1 is USER
              return 0;
           } else {  // board1 is empty
              score = 1;
           }
        } else if ( board[row2][col2] == Player.USER ) {
           if (score == -1) { // board1 is USER
              score = -10;
           } else if (score == 1) { // board1 is COMPUTER
              return 0;
           } else {  // board1 is empty
              score = -1;
           }
        }
        
        // Third cell
        if ( board[row3][col3] == Player.COMPUTER ) {
           if (score > 0) {  // board1 and/or board2 is COMPUTER
              score *= 10;
           } else if (score < 0) {  // board1 and/or board2 is USER
              return 0;
           } else {  // board1 and board2 are empty
              score = 1;
           }
        } else if ( board[row3][col3] == Player.USER ) {
           if (score < 0) {  // board1 and/or board2 is USER
              score *= 10;
           } else if (score > 0) {  // board1 and/or board2 is COMPUTER
              return 0;
           } else {  // board1 and board2 are empty
              score = -1;
           }
        }
        
    	return score;
    }
    
    public List<Cell> getEmptyCells() {
        
    	emptyCells = new ArrayList<>();
        
        for (int i = 0; i < Constants.BOARD_SIZE; ++i) {
            for (int j = 0; j < Constants.BOARD_SIZE; ++j) {
                if (board[i][j] == Player.NONE) {
                    emptyCells.add(new Cell(i, j));
                }
            }
        }
        return emptyCells;
    }

    public void move(Cell point, Player player) {
    	if(board[point.getX()][point.getY()] == Player.NONE){
    		board[point.getX()][point.getY()] = player;
    	}else{
    		System.out.println("This cell is not available and you lose your turn");
    	}
    }
    
    public Cell getBestMoveHeuristic() {
        int max = Integer.MIN_VALUE;
        int best = Integer.MIN_VALUE;

        for (int i = 0; i < rootValues.size(); ++i) { 
            if (max < rootValues.get(i).getHeuristicValue()) {
                max = rootValues.get(i).getHeuristicValue();
                best = i;
            }
        }

        return rootValues.get(best);
    }
    public Cell getBestMoveMinimax() {
    	
        int max = Integer.MIN_VALUE;
        int best = Integer.MIN_VALUE;

        for (int i = 0; i < rootValues.size(); ++i) { 
            if (max < rootValues.get(i).getMinimaxValue()) {
                max = rootValues.get(i).getMinimaxValue();
                best = i;
            }
        }

        return rootValues.get(best);
    }

    public void makeUserInput() {
        System.out.println("User's move: ");
        int x = scanner.nextInt();
        int y = scanner.nextInt();
        Cell point = new Cell(x, y);
        move(point, Player.USER); 
    }

    public void displayBoard() {

    	System.out.println();
    	
        for (int i = 0; i < Constants.BOARD_SIZE; ++i) {
            for (int j = 0; j < Constants.BOARD_SIZE; ++j) {
                System.out.print(board[i][j] + " ");
            }
            
            System.out.println();
        }
    }

    public int returnMin(List<Integer> list) {
        int min = Integer.MAX_VALUE;
        int index = Integer.MIN_VALUE;
        
        for (int i = 0; i < list.size(); ++i) {
            if (list.get(i) < min) {
                min = list.get(i);
                index = i;
            }
        }
        return list.get(index);
    }

    public int returnMax(List<Integer> list) {
        int max = Integer.MIN_VALUE;
        int index = Integer.MIN_VALUE;
        for (int i = 0; i < list.size(); ++i) {
            if (list.get(i) > max) {
                max = list.get(i);
                index = i;
            }
        }
        return list.get(index);
    }
 
    public void callMinimax(int depth, Player player, int alpha, int beta){
        rootValues.clear();
        minimax(depth, player, alpha, beta);
    }
    
    public void callHeuristic() {
    	rootValues.clear();
    	heuristic();
    }
    
    public void heuristic() {
    	List<Cell> availableCells = getEmptyCells();
    	if (availableCells.isEmpty()) return;
    	 for (int i = 0; i < availableCells.size(); i++) {
    		 Cell point = availableCells.get(i);
    		 move(point, Player.COMPUTER);
    		 
    		 int currentScore = getHeuristicScore();
    		 point.setHeuristicValue(currentScore);
    		 rootValues.add(point);
    		 
    		 board[point.getX()][point.getY()] = Player.NONE; //Reset this point 
    	 }
    }

    public int minimax(int depth, Player player, int alpha, int beta) {

        if (isWinning(Player.COMPUTER)) return +1;
        if (isWinning(Player.USER)) return -1;

        List<Cell> availableCells = getEmptyCells();
        
        if (availableCells.isEmpty()) return 0; 

        List<Integer> scores = new ArrayList<>(); 

        for (int i = 0; i < availableCells.size(); i++) {
            
        	Cell point = availableCells.get(i);  

            if (player == Player.COMPUTER) { //X's turn select the maximum value
                move(point, Player.COMPUTER); 
                int currentScore = minimax(depth + 1, Player.USER, alpha, beta);
                scores.add(currentScore);
 
                if (depth == 0) {
                	point.setMinimaxValue(currentScore);
                    rootValues.add(point);
                }
                
                board[point.getX()][point.getY()] = Player.NONE; //Reset this point
                
                if(currentScore > alpha) alpha = currentScore;
                if(alpha > beta) return alpha; // beta cut-off
                
            } else if (player == Player.USER) { //O's turn select the minimum value
                move(point, Player.USER);
                int currentScore = minimax(depth + 1, Player.COMPUTER, alpha, beta);
                scores.add(currentScore);
 
                board[point.getX()][point.getY()] = Player.NONE; //Reset this point
                
                if(currentScore < beta) beta = currentScore;
                if(alpha > beta) return beta; // alpha cut-off
            }
            
        }
        
        if( player == Player.COMPUTER ){
        	return returnMax(scores);
        }
        
        return returnMin(scores);
    }

	public List<Cell> getAvailablePoints() {
		return emptyCells;
	}

	public void setAvailablePoints(List<Cell> availablePoints) {
		this.emptyCells = availablePoints;
	}
	
	public void setupBoard() {
		for(int i=0;i<Constants.BOARD_SIZE;i++){
			for(int j=0;j<Constants.BOARD_SIZE;j++){
				board[i][j] = Player.NONE;
			}
		}
	}
    
    
	public Scanner getScanner() {
		return scanner;
	}

	public void setScanner(Scanner scanner) {
		this.scanner = scanner;
	}

	public Player[][] getBoard() {
		return board;
	}

	public void setBoard(Player[][] board) {
		this.board = board;
	}
	
	public List<Cell> getRootValues(){
		return this.rootValues;
	}
}
