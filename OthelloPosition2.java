import java.util.*;
import java.lang.*;

/** This class is used to represent game positions. It uses a 2-dimensional char array for the board and a Boolean
 *  to keep track of which player has the move.
 *
 *  @author Henrik Bj&ouml;rklund
 *  @ModifyAuthor Y.S. Horawalavithana
 */
public class OthelloPosition2 {

    /** For a normal Othello game, BOARD_SIZE is 8. */
    protected static final int BOARD_SIZE = 8;
    
    protected static final int UP=-10;
    protected static final int DOWN=10;
    protected static final int LEFT=-1;
    protected static final int RIGHT=1;
    
    protected static final int UP_RIGHT=-9;
    protected static final int DOWN_RIGHT=11;
    protected static final int DOWN_LEFT=9;
    protected static final int UP_LEFT=-11;
    
    protected static final int[] DIRECTIONS = {UP, UP_RIGHT, RIGHT, DOWN_RIGHT, DOWN, DOWN_LEFT, LEFT, UP_LEFT};

    /** True if the first player (white) has the move. */
    protected boolean playerToMove;
    
    protected int squares[]=new int[BOARD_SIZE*BOARD_SIZE]; 
    
    /*protected int SQUARE_WEIGHTS[] = {
    0,   0,   0,   0,   0,   0,   0,   0,   0,   0,
    0, 120, -20,  20,   5,   5,  20, -20, 120,   0,
    0, -20, -40,  -5,  -5,  -5,  -5, -40, -20,   0,
    0,  20,  -5,  15,   5,   5,  15,  -5,  20,   0,
    0,   5,  -5,   10,   3,   3,   10,  -5,   5,   0,
    0,   5,  -5,   10,   3,   3,   10,  -5,   5,   0,
    0,  20,  -5,  15,   5,   5,  15,  -5,  20,   0,
    0, -20, -40,  -5,  -5,  -5,  -5, -40, -20,   0,
    0, 120, -20,  20,   5,   5,  20, -20, 120,   0,
    0,   0,   0,   0,   0,   0,   0,   0,   0,   0,
};*/

protected int SQUARE_WEIGHTS[] = {
    0,   0,   0,   0,   0,   0,   0,   0,   0,   0,
    0, 2, -2,  1,   1,   1,  1, -2, 2,   0,
    0, -2, -4,  0,  0,  0,  0, -4, -2,   0,
    0,  1,  0,  0,   0,   0,  0,  0,  1,   0,
    0,  1,  0,   0,   3,   3,   0,  0,   1,   0,
    0,  1,  0,   0,   3,   3,   0,  0,   1,   0,
    0,  1,  0,  0,   0,   0,  0,  0,  1,   0,
    0, -2, -4,  0,  0,  0,  0, -4, -2,   0,
    0, 2, -2,  1,   1,   1,  1, -2, 2,   0,
    0,   0,   0,   0,   0,   0,   0,   0,   0,   0,
};
    
    int minW,maxW;
    
    int max=Integer.MIN_VALUE;
    
    int depth=10;
    
    int bestmove;
    
    int upperbound, lowerbound;
    
    int weightd=40,weightc=60;
    
    int score=0;

	

    /** The representation of the board. For convenience, the array actually has two columns and two rows more
	that the actual game board. The 'middle' is used for the board. The first index is for rows, and the second
	for columns. This means that for a standard 8x8 game board, <code>board[1][1]</code> represents the
	upper left corner, <code>board[1][8]</code> the upper right corner, <code>board[8][1]</code> the lower
	left corner, and <code>board[8][8]</code> the lower left corner. In the array, the charachters 'E', 'W',
	and 'B' are used to represent empty, white, and black board squares, respectively.
    */
    protected char [][] board;
    
    /** Creates a new position and sets all squares to empy. */
    public OthelloPosition2(){
	board = new char[BOARD_SIZE+2][BOARD_SIZE+2];
	for(int i = 0 ; i < BOARD_SIZE+2 ; i++)
	    for(int j = 0 ; j< BOARD_SIZE+2 ; j++)
		board[i][j] = 'E';
	
        //playerToMove = true;
    }

    public OthelloPosition2(String s){
	if(s.length() != 65){
	    board = new char[BOARD_SIZE+2][BOARD_SIZE+2];
	    for(int i = 0 ; i < BOARD_SIZE+2 ; i++)
		for(int j = 0 ; j< BOARD_SIZE+2 ; j++)
		    board[i][j] = 'E';
	}else{
	    board = new char[BOARD_SIZE+2][BOARD_SIZE+2];
	    if(s.charAt(0)=='W'){
		playerToMove = true;
	    }else{
		playerToMove = false;
	    }
	    for(int i = 1 ; i <= 64 ; i++){
		char c;
		if(s.charAt(i)=='E'){
		    c = 'E';
		}else if(s.charAt(i)=='O'){
		    c = 'W';
		}else{
		    c = 'B';
		}
		int column = ((i-1)%8)+1;
		int row = (i-1)/8 + 1;
		board[row][column] = c;
                //System.out.println(c);
	    }
	}


    }
    
    public char getPlayer(boolean player){        
        if(player) return 'W';
        return 'B';
    }

    /** Initializes the position by placing four markers in the middle of the board. */
    public void initialize(){
	//board[BOARD_SIZE/2][BOARD_SIZE/2] = board[BOARD_SIZE/2 +1][BOARD_SIZE/2 +1] = 'W';
	//board[BOARD_SIZE/2][BOARD_SIZE/2 +1] = board[BOARD_SIZE/2 +1][BOARD_SIZE/2] = 'B';
	//playerToMove = true;
        
        initSquares();
        
        minW=getMinWeight(SQUARE_WEIGHTS);
        maxW=getMaxWeight(SQUARE_WEIGHTS);
    }

    /* getMoves and helper functions */

    /** Returns a linked list of <code>OthelloAction</code> representing all possible moves in the position.
	If the list is empty, there are no legal moves for the player who has the move. 
    */
	
    /*public LinkedList getMoves(){

	

    }*/

    
    
    /* toMove */

    /** Returns true if the first player (white) has the move, otherwise false. */
    public boolean toMove(){
	return playerToMove;
    }
    
    public void initSquares(){         
        int j=0,index=0;
        for(int i=11;i<89;i++){
            j=i%10;
            if(j>=1 && j<=8){
                squares[index]=i;
                //System.out.println("Init_Squares: "+squares[index]);
                index++;
            }
            
        }        
        
    }
    
    public int[] getSquares(){
        return squares;
    }
    
    //Check the current move is valid square in the board
    public boolean isValid(int move){        
        for(int i=0;i<squares.length;i++){
            if(squares[i]==move) return true;
        }        
        return false;
    }
    
    //Check the current move is legal on given player
    public boolean isLegalMove(int move, boolean player,char[][] board){
        boolean legalFlag=false;
        if(board[(move/10)][(move%10)] == getPlayer(player) || board[(move/10)][(move%10)] == getPlayer(!player)){
            return legalFlag;
        }
        for(int i=0;i<DIRECTIONS.length;i++){            
                int bracket=match_bracket(move, player, DIRECTIONS[i]);
                if(bracket!=0) legalFlag |= true;                  
        }
        return legalFlag;
    }
    
    //Match Brackets for given player in given direction
    public int match_bracket(int square, boolean player,int direction){
        int bracket=square+direction;
        if(isValid(bracket)){
            if(board[(bracket/10)][(bracket%10)]==getPlayer(player)) return 0;
            while(board[(bracket/10)][(bracket%10)] == getPlayer(!player)){
                bracket += direction; 
                //System.out.println("Bracket Pos: "+bracket);
            }
            if(board[(bracket/10)][(bracket%10)] == getPlayer(player) || board[(bracket/10)][(bracket%10)] == getPlayer(!player)){
                //System.out.println("Bracket: "+bracket);
                return bracket;            
            }else{
                return 0;
            }
        }
        return 0;
    }
    
    public void play(boolean player){
        //int bestmove=getBestMove(player); 
        int lmoves[]=getLegalMoves(player,board);
        
        if(lmoves.length>0){
        	/*for(int i=0;i<lmoves.length;i++){
        		int val=strategy(lmoves[i]);
        	}*/
            	int bestmove=alphabeta(player, board, Integer.MIN_VALUE, Integer.MAX_VALUE, this.depth);
            	board=makeMove(bestmove,player,board);
            //System.out.println("SCORE: "+getScore(player,board)+" BEST_MOVE: "+(bestmove/10)+","+(bestmove%10));
            System.out.println("("+(bestmove/10)+","+(bestmove%10)+")");
        }else{
            System.out.println("pass");
        }
        
    }
    
    
   
   public int evaluate(boolean player,char[][] board){
   	return weightd*(getScore(player, board))+weightc*(getTotalWeight(player,board));
   }
    
    public int alphabeta(boolean player,char[][] board,int upperbound,int lowerbound,int depth){
        if(depth<1){
            //System.out.print(" Session score: "+(upperbound-lowerbound)+"\n");
            return evaluate(player,board);
        }
        
        
        int val=0;
        
        int lmoves[]=getLegalMoves(player,board);        
         
            
        for(int i=0;i<lmoves.length;i++){            
            if(this.depth==depth){
                board=clone().board;
            }
            char[][] b=makeMove(lmoves[i], player, board);
            int score=getScore(player, b);
            if(score>=upperbound && player){
                this.upperbound=upperbound=score;
                //System.out.println("UPPERBOUND: "+upperbound);
            }
            if(score<lowerbound && !player){
                this.lowerbound=lowerbound=score;
                //System.out.println("LOWERBOUND: "+lowerbound);
            }
            
            val=-alphabeta(!player, b, upperbound, lowerbound, depth-1);
            
           
            
            if(this.depth==depth){
                val=weightd*(this.upperbound-this.lowerbound)+weightc*(SQUARE_WEIGHTS[lmoves[i]]);
                //System.out.println(" Move: "+lmoves[i]/10+","+lmoves[i]%10+" Best_Score: "+val);
                if(val>=max){
                    max=val;
                    bestmove=lmoves[i];
                    //this.upperbound=Integer.MIN_VALUE;
                    //this.lowerbound=Integer.MAX_VALUE;
                }
            }
            
            
        }
        
        return bestmove;
    }
    
   
   
   
    
   public int getTotalWeight(boolean player,char[][] board){ 
   	int total=0;  	
   	for(int i=0;i<squares.length;i++){
   		if(board[squares[i]/10][squares[i]%10] == getPlayer(player)) total+=SQUARE_WEIGHTS[squares[i]];
   		if(board[squares[i]/10][squares[i]%10] == getPlayer(!player)) total-=SQUARE_WEIGHTS[squares[i]];
   	}
   	return total;
   }
    
   public static int getMinWeight(int... args) {
        int m = Integer.MAX_VALUE;
        for (int a : args) {
            m = Math.min(m, a);
        }
        return m;
    }

    public static int getMaxWeight(int... args) {
        int m = Integer.MIN_VALUE;
        for (int a : args) {
            m = Math.max(m, a);
        }
        return m;
    }
    
    /*public int getBestMove(boolean player){
        int bestmove=0;
        int w1=1,w2=1;
        int lmoves[]=getLegalMoves(player,board);
        int pupperbound=Integer.MIN_VALUE;
        for(int i=0;i<lmoves.length;i++){   
            
            char[][] board=clone().board;
            board=makeMove(lmoves[i], player, board);
            int pscore=getScore(player, board);
            //System.out.println("-----Player Legal Move: "+lmoves[i]+" Scored: "+pscore);
            
            int olmoves[]=getLegalMoves(!player,board);
            int olowerbound=Integer.MAX_VALUE;
            for(int j=0;j<olmoves.length;j++){
                
                char[][] oboard=makeMove(olmoves[j], !player, board);
                int oscore=getScore(!player, oboard);
                //System.out.println("Opponent Legal Move: "+olmoves[j]+" Scored: "+oscore);
                if(oscore<=olowerbound){
                    olowerbound=oscore;
                    //System.out.println("Opponent worst move"+olowerbound);
                }
            }
            
            int pwin=pscore-olowerbound;
            
            int pborder=0;
            if(isBorderMove(lmoves[i])) pborder=1;
            int pwinweight=((pwin*w1)+(pborder*w2))/(w1+w2);
            
            if(pwinweight>=pupperbound){
                pupperbound=pwin;
                //System.out.println("I am going to win.."+pupperbound);
                bestmove=lmoves[i];
            }
            
            
        }
        //System.out.println("#######################Best Move"+bestmove);
        //bestmove=lmoves[0];
        return bestmove;
    }
    
    public boolean isBorderMove(int move){
        if((move/10)==1 || (move/10)==8 || (move%10)==0 || (move%10)==0) return true;
        return false;
        
    }*/
    
    public int getScore(boolean player, char[][] board){
        int play=0,opponent=0;
        for(int i=11;i<89;i++){
            if(board[(i/10)][(i%10)]==getPlayer(player)) play++;
            if(board[(i/10)][(i%10)]==getPlayer(!player)) opponent++;
        }
        //System.out.println("Play: "+play+" Opponent: "+opponent+" Score: "+(play-opponent));
        return play-opponent;
    }
    
    public char[][] makeMove(int move,boolean player,char[][] board){
        board[(move/10)][(move%10)]=getPlayer(player);
        //System.out.println("Move at "+(move/10)+","+(move%10)+" "+board[(move/10)][(move%10)]);
        for(int i=0;i<DIRECTIONS.length;i++){
            //System.out.println("Flip Direction: "+DIRECTIONS[i]);
            makeFlips(move,player,DIRECTIONS[i],board);
            //System.out.println("-------------------------------");
        }
        return board;
    }
    
    public void makeFlips(int move, boolean player,int direction,char[][] board){
        int bracket=match_bracket(move, player,direction);
        //System.out.println("Flip Bracket: "+bracket);
        if(bracket==0) return;
        int square = move + direction;
        while(square != bracket){
            board[(square/10)][(square%10)]=getPlayer(player);
            square += direction;
        }
               
        
    }
    
    public int[] getLegalMoves(boolean player,char[][] board){
        ArrayList<Integer> lmoves=new ArrayList<Integer>();
        for(int i=0;i<squares.length;i++){
            //System.out.println("Square: "+squares[i]);
            if(isLegalMove(squares[i], player,board)){
                lmoves.add(squares[i]);
                //System.out.println("It's Legal: "+squares[i]);
            }
            //System.out.println("-------------------------");
            
        }
        return convertIntegers(lmoves);
    }
    
    public int[] convertIntegers(List<Integer> integers){
        int[] ret = new int[integers.size()];
        Iterator<Integer> iterator = integers.iterator();
        for (int i = 0; i < ret.length; i++)
        {
            ret[i] = iterator.next().intValue();
        }
        return ret;
}
    

    /* makeMove and helper functions */
    
    /** Returns the position resulting from making the move <code>action</code> in the current position. 
        Observe that this also changes the player to move next.
    */
    /*public OthelloPosition2 makeMove(OthelloAction action) throws IllegalMoveException{
       
	
	
    }*/

    

    /** Returns a new <code>OthelloPosition2</code>, identical to the current one. */
    protected OthelloPosition2 clone(){
	OthelloPosition2 newPosition = new OthelloPosition2();
	newPosition.playerToMove = playerToMove;
	for(int i = 0 ; i < BOARD_SIZE+2 ; i++)
	    for(int j = 0 ; j < BOARD_SIZE+2 ; j++)
		newPosition.board[i][j] = board[i][j];
	return newPosition;
    }

    /* illustrate and other output functions */

    /** Draws an ASCII representation of the position. White squares are marked by '0' while 
	black squares are marked by 'X'.
    */
    public void illustrate(){
	System.out.print("   ");
	for(int i=1 ; i <= BOARD_SIZE ; i++)
	    System.out.print("| " + i + " ");
	System.out.println("|");
	printHorizontalBorder();
	for(int i=1 ; i <= BOARD_SIZE ; i++){
	    System.out.print(" " + i + " ");
	    for(int j=1 ; j <= BOARD_SIZE ; j++){
		if(board[i][j] == 'W'){
		    System.out.print("| 0 ");
		}else if(board[i][j] == 'B'){
		    System.out.print("| X ");
		}else{
		    System.out.print("|   ");
		}
	    }
	    System.out.println("| " + i + " ");
	    printHorizontalBorder();
	}
	System.out.print("   ");
	for(int i=1 ; i <= BOARD_SIZE ; i++)
	    System.out.print("| " + i + " ");
	System.out.println("|\n");
    }

    private void printHorizontalBorder(){
	System.out.print("---");
	for(int i = 1 ; i <= BOARD_SIZE ; i++){
	    System.out.print("|---");
	}
	System.out.println("|---");
    }

    public String toString(){
	String s = "";
	char c,d;
	if(playerToMove){
	    s += "W";
	}else{
	    s += "B";
	}
	for(int i = 1 ; i <= 8 ; i++){
	    for(int j = 1 ; j <= 8 ; j++){
		d = board[i][j];
		if(d == 'W'){
		    c = 'O';
		}else if(d == 'B'){
		    c = 'X';
		}else{
		    c = 'E';
		}
		s += c;
	    }
	}
	return s;
    }

}
