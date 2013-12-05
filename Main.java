public class Main {
    public static void main(String[] args){
        int time=30;
        String init_position="W"
                + "EEEEEEEE"
                + "XXEEEEEE"
                + "XEXEEEEE"
                + "OEEOXEEE"
                + "EEEXOEEE"
                + "EEEEEEEE"
                + "EEEEEEEE"
                + "EEEEEEEE";
        /*String init_position="W
			EXXEEEEE
			OOOOXEEE
			EXXOXEEE
			XEOXXXEE
			EOOOOEXE
			EOEOEXEX
			OEEEEEEE
			EEEEEEEE";*/
        if(args.length>0){
            init_position=args[0];
            time=Integer.parseInt(args[1]);                
        }
        OthelloPosition2 op=new OthelloPosition2(init_position);
        op.initialize();
        //op.illustrate();
        //System.out.println(op.isValid(19));
        //op.match_bracket(11, true,10);
        //System.out.println(op.getSquares().toString());
        //op.makeMove(11, true);
        //op.illustrate();
        //System.out.println("is_legal: "+op.isLegalMove(65, false));
        //int lmoves[]=op.getLegalMoves(op.playerToMove,op.board);
        //for(int i=0;i<lmoves.length;i++) System.out.println("Move: "+lmoves[i]/10+","+lmoves[i]%10);
        
        boolean flag=op.playerToMove;
        /*for(int i=0;i<200;i++){
            System.out.println("Player: "+flag);
            op.play(flag);
            flag = !flag;            
            op.illustrate();
            System.out.println("\n");
        }*/
        
        op.play(flag);
        //op.illustrate();
    }
    
}

