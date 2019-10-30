/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Problem;

/**
 *
 * @author Shreya
 */
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Scanner;

/**
 *
 * @author Shreya
 */
class Board
{
    public static int N;
    public static int player_no, opponent;
    
    int board[][];
    String pitname[][];
    int mancala[];

    
    boolean terminal=false;
    int turn;
    int pit_no;
    int depth;
    String logname;
    
    int value;
    int chosen;
    
    int pointer;//change in playMove
    int lastpit;
    
    public Board(int b[][], int m[],int N /*,int c*/)
    {
        
        this.N=N;
        chosen=-1;
        pointer=-1;
        value=-1;
        turn=-1;
        pit_no=-1;
        depth=-1;
        lastpit=-1;
        logname="";
        
        board=new int[2][N];
        board[0]=Arrays.copyOf(b[0],N);
        board[1]=Arrays.copyOf(b[1],N);
        mancala=new int[2];
        mancala=Arrays.copyOf(m,2);
        
        pitname=new String[2][N];
        for(int i=0;i<pitname[0].length;i++)
        {
            pitname[0][i]="B"+(2+i);
            pitname[1][i]="A"+(2+i);
        }
    }
    public Board(Board b)
    {
        N=b.N;
        pointer=b.pointer;
        lastpit=b.lastpit;
        value=b.value;
        turn=b.turn;
        pit_no=b.pit_no;
        depth=b.depth;
        logname=b.logname;
        chosen=b.chosen;
        
        board=new int[2][N];
        board[0]=Arrays.copyOf(b.board[0], b.N);
        board[1]=Arrays.copyOf(b.board[1], b.N);
        mancala=new int[2];
        mancala=Arrays.copyOf(b.mancala,2);
        pitname=new String[2][N];
        System.arraycopy(b.pitname[0], 0, pitname[0], 0, N);
        System.arraycopy(b.pitname[1], 0, pitname[1], 0, N);
        
    }
    
    public void utility()
    {
        value=mancala[player_no]-mancala[opponent];
    }
    public void playMove()
    {
        int length=2*N+1;
        int oppositeturn=(turn+1)%2;
        int temp[]=new int[length];
        int count=-1;
        int i=-1;
        if(turn==1)//player2
        {
            int boardtemp[]=reverse(board[turn]);
            System.arraycopy(boardtemp,0,temp,0,N);
            
            temp[N]=mancala[turn];
            
            System.arraycopy(board[oppositeturn],0,temp,N+1,N);
            
            int pit1=N-1-pit_no;
            count=temp[pit1];
            temp[pit1]=0;
            i=pit1;
        }
        else //player1
        {
            System.arraycopy(board[turn],0,temp,0,N);
            
            temp[N]=mancala[turn];
            
            int boardtemp[]=reverse(board[oppositeturn]);
            System.arraycopy(boardtemp,0,temp,N+1,N);
            
            count=temp[pit_no];
            temp[pit_no]=0;
            i=pit_no;
        }
        
        while(count>0)
        {
            temp[++i%temp.length]++;
            count--;
        }
        pointer=i%temp.length;
        lastpit=temp[pointer];
        
        if(turn==1)//player2
        {
            int boardtemp[]=new int[N];
            System.arraycopy(temp,0,boardtemp,0,N);
            board[turn]=reverse(boardtemp);
            
            mancala[turn]=temp[N];
            
            System.arraycopy(temp,N+1,board[oppositeturn],0,N);
        }
        else //player1
        {
            System.arraycopy(temp,0,board[turn],0,N);
            
            mancala[turn]=temp[N];
            
            int boardtemp[]=new int[N];
            System.arraycopy(temp,N+1,boardtemp,0,N);
            board[oppositeturn]=reverse(boardtemp);
            
        }
        
        
        utility();
    }
    
    public static int[] reverse(int a[])
    {
        int b[]=new int[a.length];
        for(int i=0;i<a.length;i++)
        {
            b[i]=a[a.length-1-i];
        }
        return b;
    }
    
}
public class mancala
{
    public static Scanner file;
    public static File output;
    public static PrintWriter traverselog;
    public static PrintWriter nextstate;        
    static int cutoff;
    static int player_no;
    static int opponent;
    static int N=-1;
    
    static int mancala[];
    static int player[][];
    
    static Board initial;
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
         try
        { 
            nextstate=new PrintWriter(new FileWriter(new File("next_state.txt")));  
            output=new File("traverse_log.txt");
            traverselog=new PrintWriter(new FileWriter(output));            
            file=new Scanner(new FileReader(args[1]));
            int task=Integer.parseInt(file.nextLine()); //Line 1 
            player_no=Integer.parseInt(file.nextLine())-1;//Line 2
            opponent=(player_no+1)%2;
            cutoff=Integer.parseInt(file.nextLine());//Line 3
            String p2s[]=file.nextLine().split(" "); //Line 4
            String p1s[]=file.nextLine().split(" "); //Line 5
            N=p2s.length;
            
            player=new int[2][p1s.length];
            for(int i=0;i<p1s.length;i++)
            {
              player[0][i]=Integer.parseInt(p1s[i]);  
              player[1][i]=Integer.parseInt(p2s[i]); //store in correct order
            }
            p1s=null;
            p2s=null;
            mancala=new int[2];
            mancala[1]=Integer.parseInt(file.nextLine());
            mancala[0]=Integer.parseInt(file.nextLine());
            
            initial=new Board(player,mancala,N);
            Board.N=N;
            Board.player_no=player_no;
            Board.opponent=opponent;
            
            initial.depth=0; //root
            initial.turn=player_no;
            initial.value=-999999999;
            initial.logname="root";
           
            
            switch(task)
            {
                case 1:
                    cutoff=1;
                case 2:
                    traverselog.println("Node,Depth,Value");
                    Minimax(initial);
                    break;
                case 3: 
                    traverselog.println("Node,Depth,Value,Alpha,Beta");
                    AlphaBeta(initial);
                    break;
                default: 
                    break;
            }
           traverselog.close();
            file.close();
            nextstate.close();
           
        } 
        catch (Exception ex) 
        {
            
        }
    }
   
    static void Minimax(Board b)
    {
        Board b1=new Board(b);
        
        Board ans=Max(b1,0,player_no);//board, length, turn
        for(int x: ans.board[1])
            nextstate.print(x+" ");
        nextstate.println();
        for(int x: ans.board[0])
            nextstate.print(x+" ");
        nextstate.println();
        nextstate.println(ans.mancala[1]);
        nextstate.println(ans.mancala[0]);
        
    }
    static Board Min(Board b,int length,int turn)
    {
        if(length==cutoff)//Check Terminal condition
        {
            b.utility();
            b.chosen=b.pit_no;
            traverselog.println(b.logname+","+b.depth+","+Infi(b.value));
            return b;
        }
        
        
        
        int min=999999999;
        int imin=-1;
        Board b3=new Board(b);
        
        Board b1=new Board(b);
        
        
        b1.value=999999999;
        b1.turn=turn;
        
        int oppositeturn=(turn+1)%2;
        
        traverselog.println(b1.logname+","+b1.depth+","+Infi(b1.value));
        
        
        //ENDGAME!!
        boolean endgameturn=true;
        for(int x: b.board[turn])
        {
            if(x!=0)
                endgameturn=false;
        }
        boolean endgameopp=true;
        for(int x: b.board[(turn+1)%2])
        {
            if(x!=0)
                endgameopp=false;
        }
        if(endgameturn&&endgameopp)
        {
            b.utility();
            traverselog.println(b.logname+","+b.depth+","+Infi(b.value));
            return b;
        }        
        else if(endgameturn)
        {
            for(int k=0;k<N;k++)
            {
                b.mancala[oppositeturn]+=b.board[oppositeturn][k];
                b.board[oppositeturn][k]=0;
            }
            b.utility();
            traverselog.println(b.logname+","+b.depth+","+Infi(b.value));
            return b;
        }
        else if(endgameopp)
        {
            for(int k=0;k<N;k++)
            {
                b.mancala[turn]+=b.board[turn][k];
                b.board[turn][k]=0;
            }
            b.utility();
            traverselog.println(b.logname+","+b.depth+","+Infi(b.value));
            return b;
        }
    
        //ENDGAME!!
        
        
        
        for(int i=0;i<N;i++)
        {
            Board b2=new Board(b1);
            if(b2.turn==b.turn)
            {
               
            }
            else
            b2.depth++;
            
            if(b2.board[b2.turn][i]!=0) //can't choose empty pit
            {
                b2.pit_no=i;
                b2.logname=b2.pitname[b2.turn][i];
                
                b2.playMove();   
                //*****************************************************
                int pointer=b2.pointer;//pit containing last stone 
                if((b2.turn==1)&&(pointer<N))//player1
                {   
                    pointer=N-1-pointer;
                }
                if(pointer==N)//last stone in player's Mancala //player gets another move
                {
                    
                    Board b2dash=new Board(b2);
                    b2dash=Min(b2,length,turn);
                    
                    if(min>b2.value)
                    {
                        b.value=b2dash.value;
                        b.chosen=i;
                        b3=new Board(b2dash);
                        min=b2dash.value;
                        imin=i;
                    }
                    
                    
                    traverselog.println(b.logname+","+b.depth+","+Infi(b.value));
                    continue;
                }
                if((pointer>-1)&&(pointer<N)&&(b2.lastpit==1))//last pit was empty and was of the player
                {
                    b2.mancala[turn]+=1+b2.board[oppositeturn][pointer];
                    b2.board[turn][pointer]=0;
                    b2.board[oppositeturn][pointer]=0;
                    b2.utility();
                }
                //check endgame condition
                boolean endgame=true;
                for(int x: b2.board[turn])
                {
                    if(x!=0)
                        endgame=false;
                }
                if(endgame)
                {
                    
                    for(int k=0;k<N;k++)
                    {
                        b2.mancala[oppositeturn]+=b2.board[oppositeturn][k];
                        b2.board[oppositeturn][k]=0;
                    }
                    b2.utility();
                    
                }
                else
                {
                endgame=true;
                for(int x: b2.board[oppositeturn])
                {
                    if(x!=0)
                        endgame=false;
                }
                if(endgame)
                {
                    
                    for(int k=0;k<N;k++)
                    {
                        b2.mancala[turn]+=b2.board[turn][k];
                        b2.board[turn][k]=0;
                    }
                    b2.utility();
                    
                }
                }
                //****************************************************
                //Initialize for MIN
                Board resultBoard=Max(b2,length+1,((b2.turn+1)%2));
                
                if(min>resultBoard.value)
                {
                    b1.value=resultBoard.value;
                    
                    b.value=resultBoard.value;
                    b.chosen=i;
                    
                    b3=new Board(b2);
                    min=resultBoard.value;
                    imin=i;
                }
                
                traverselog.println(b.logname+","+b.depth+","+Infi(b.value));
                
            }
        }
        b.value=b3.value;
        b.chosen=imin;
        return b3;

    }
    
    static Board Max(Board b,int length,int turn)
    {
        if(length==cutoff)//Check Terminal condition
        {
            b.utility();
            b.chosen=b.pit_no;
            
            traverselog.println(b.logname+","+b.depth+","+Infi(b.value));
            return b;
        }
        
        
        int max=-999999999;
        int imax=-1;
        Board b3=new Board(b);
        
        Board b1=new Board(b);
        
        
        b1.value=-999999999;
        b1.turn=turn;
        
        
        
        
        traverselog.println(b1.logname+","+b1.depth+","+Infi(b1.value));
        int oppositeturn=(turn+1)%2;
        
        //ENDGAME!!
        boolean endgameturn=true;
        for(int x: b.board[turn])
        {
            if(x!=0)
                endgameturn=false;
        }
        boolean endgameopp=true;
        for(int x: b.board[(turn+1)%2])
        {
            if(x!=0)
                endgameopp=false;
        }
        if(endgameturn&&endgameopp)
        {
            b.utility();
            traverselog.println(b.logname+","+b.depth+","+Infi(b.value));
            return b;
        }     
        else if(endgameturn)
        {
            for(int k=0;k<N;k++)
            {
                b.mancala[oppositeturn]+=b.board[oppositeturn][k];
                b.board[oppositeturn][k]=0;
            }
            b.utility();
            traverselog.println(b.logname+","+b.depth+","+Infi(b.value));
            return b;
        }
        else if(endgameopp)
        {
            for(int k=0;k<N;k++)
            {
                b.mancala[turn]+=b.board[turn][k];
                b.board[turn][k]=0;
            }
            b.utility();
            traverselog.println(b.logname+","+b.depth+","+Infi(b.value));
            return b;
        }
        //ENDGAME!!
        
        
        for(int i=0;i<N;i++)
        {
    
            Board b2=new Board(b1);
            if((b2.turn==b.turn)&&(!b2.logname.equals("root")))
            {
               
            }
            else
            b2.depth++;
            
            if(b2.board[b2.turn][i]!=0) //can't choose empty pit
            {
                b2.pit_no=i;
                b2.logname=b2.pitname[b2.turn][b2.pit_no];
                b2.playMove();   
        
                //*****************************************************
                int pointer=b2.pointer;//pit containing last stone 
                //CHANGED:
                if((b2.turn==1)&&(pointer<N))//player1
                {
                    pointer=N-1-pointer;
                }
                else if(pointer==N)//last stone in player's Mancala //player gets another move
                {
                    Board b2dash=new Board(b2);
                    b2dash=Max(b2,length,turn);
                    
                    b2.chosen=i;
                    
                    if(max<b2.value)
                    {
                        b1.value=b2dash.value;

                        b.value=b2dash.value;
                        b.chosen=i;
                        b3=new Board(b2dash);
                        max=b2dash.value;
                        imax=i;
                    }
                    
                    traverselog.println(b.logname+","+b.depth+","+Infi(b.value));
                    continue;
                    
                }
                //CHANGED:
                if((pointer>-1)&&(pointer<N)&&(b2.lastpit==1))//last pit was empty and was of the player
                {
                    b2.mancala[turn]+=1+b2.board[oppositeturn][pointer];
                    b2.board[turn][pointer]=0;
                    b2.board[oppositeturn][pointer]=0;
                    b2.utility();
                }
                //***check endgame condition
                boolean endgame=true;
                for(int x: b2.board[turn])
                {
                    if(x!=0)
                        endgame=false;
                }
                if(endgame)
                {
                    for(int k=0;k<N;k++)
                    {
                        b2.mancala[oppositeturn]+=b2.board[oppositeturn][k];
                        b2.board[oppositeturn][k]=0;
                    }
                    b2.utility();
                    
                }
                else
                {    
                endgame=true;
                for(int x: b2.board[oppositeturn])
                {
                    if(x!=0)
                        endgame=false;
                }
                if(endgame)
                {
                    for(int k=0;k<N;k++)
                    {
                        b2.mancala[turn]+=b2.board[turn][k];
                        b2.board[turn][k]=0;
                    }
                    b2.utility();
                    
                }
                }
                //***check endgame condition
                
                Board resultBoard=Min(b2,length+1,((b2.turn+1)%2));
                
                if(max<resultBoard.value)
                {
                    b1.value=resultBoard.value;
                    
                    b.value=resultBoard.value;
                    b.chosen=i;
                    
                    b3=new Board(b2);
                    max=resultBoard.value;
                    imax=i;
                    
                }
                                
                traverselog.println(b.logname+","+b.depth+","+Infi(b.value));
            }
        }
        
        //update b
        b.value=b3.value;
        b.chosen=imax;
        
        return b3; //b3 is future board
    }
    //Alpha Beta
    
    static void AlphaBeta(Board b)
    {
        Board b1=new Board(b);
        int alpha=-999999999;
        int beta=999999999;
        Board ans=ABMax(b1,0,player_no,alpha,beta);//board, length, turn
        
        //player 2
        for(int x: ans.board[1])
            nextstate.print(x+" ");
        nextstate.println();
        for(int x: ans.board[0])
            nextstate.print(x+" ");
        nextstate.println();
        nextstate.println(ans.mancala[1]);
        nextstate.println(ans.mancala[0]);
        
    }
    
    static String Infi(int a)
    {
        if(a==-999999999)
            return "-Infinity";
        else if(a==999999999)
            return "Infinity";
        else
            return ""+a;

    }
    
    public static Board ABMin(Board b,int length,int turn,int alpha,int beta)
    {
        if(length==cutoff)//Check Terminal condition
        {
            b.utility();
            b.chosen=b.pit_no;
            
            traverselog.println(b.logname+","+b.depth+","+Infi(b.value)+","+Infi(alpha)+","+Infi(beta));
            
            return b;
        }
        
        int min=999999999;
        int imin=-1;
        Board b3=new Board(b);
        
        Board b1=new Board(b);
        
        b1.value=999999999;
        b1.turn=turn;
        
        int oppositeturn=(turn+1)%2;
        
        traverselog.println(b1.logname+","+b1.depth+","+Infi(b1.value)+","+Infi(alpha)+","+Infi(beta));
       
        
        //ENDGAME!!
        boolean endgameturn=true;
        for(int x: b.board[turn])
        {
            if(x!=0)
                endgameturn=false;
        }
        boolean endgameopp=true;
        for(int x: b.board[(turn+1)%2])
        {
            if(x!=0)
                endgameopp=false;
        }
        if(endgameturn&&endgameopp)
        {
            b.utility();
            traverselog.println(b.logname+","+b.depth+","+Infi(b.value)+","+Infi(alpha)+","+Infi(beta));
            return b;
        }
        else if(endgameturn)
        {
            
            for(int k=0;k<N;k++)
            {
                b.mancala[oppositeturn]+=b.board[oppositeturn][k];
                b.board[oppositeturn][k]=0;
            }
            b.utility();
            traverselog.println(b.logname+","+b.depth+","+Infi(b.value)+","+Infi(alpha)+","+Infi(beta));
            return b;
        }
        else if(endgameopp)
        {
            for(int k=0;k<N;k++)
            {
                b.mancala[turn]+=b.board[turn][k];
                b.board[turn][k]=0;
            }
            b.utility();
            traverselog.println(b.logname+","+b.depth+","+Infi(b.value)+","+Infi(alpha)+","+Infi(beta));
            return b;
        }
        //ENDGAME!!
        
        
        for(int i=0;i<N;i++)
        {
            Board b2=new Board(b1);
            if(b2.turn==b.turn)
            {
                
            }
            else
            b2.depth++;
            
            if(b2.board[b2.turn][i]!=0) //can't choose empty pit
            {
                b2.pit_no=i;
                b2.logname=b2.pitname[b2.turn][i];
                
                b2.playMove();   
                //*****************************************************
                int pointer=b2.pointer;//pit containing last stone 
                if((b2.turn==1)&&(pointer<N))//player1
                {   
                    pointer=N-1-pointer;
                }
                if(pointer==N)//last stone in player's Mancala //player gets another move
                {
                    
                    Board b2dash=new Board(b2);
                    b2dash=ABMin(b2,length,turn,alpha,beta);
                    //b2.chosen=b2.pit_no;
                    
                    if(min>b2.value)
                    {
                        b.value=b2dash.value;
                        b.chosen=i;
                        b3=new Board(b2dash);
                        min=b2dash.value;
                        imin=i;
                    }
                
                    //******Alpha Beta
                    if(min<=alpha)
                    {
                        traverselog.println(b.logname+","+b.depth+","+Infi(b.value)+","+Infi(alpha)+","+Infi(beta));
                        break;
                    }    
                    beta=Math.min(min,beta);
                    //******Alpha Beta
                
                    
                    
        
                    traverselog.println(b.logname+","+b.depth+","+Infi(b.value)+","+Infi(alpha)+","+Infi(beta));
                    continue;
                }
                if((pointer>-1)&&(pointer<N)&&(b2.lastpit==1))//last pit was empty and was of the player
                {
                    b2.mancala[turn]+=1+b2.board[oppositeturn][pointer];
                    b2.board[turn][pointer]=0;
                    b2.board[oppositeturn][pointer]=0;
                    b2.utility();
                }
                
                //***check endgame condition
                boolean endgame=true;
                for(int x: b2.board[turn])
                {
                    if(x!=0)
                        endgame=false;
                }
                if(endgame)
                {
                    
                    for(int k=0;k<N;k++)
                    {
                        b2.mancala[oppositeturn]+=b2.board[oppositeturn][k];
                        b2.board[oppositeturn][k]=0;
                    }
                    b2.utility();
                    
                }
                else
                {    
                endgame=true;
                for(int x: b2.board[oppositeturn])
                {
                    if(x!=0)
                        endgame=false;
                }
                if(endgame)
                {
                    
                    for(int k=0;k<N;k++)
                    {
                        b2.mancala[turn]+=b2.board[turn][k];
                        b2.board[turn][k]=0;
                    }
                    b2.utility();
                    
                }
                }
                //***check endgame condition
                
                //****************************************************
                //Initialize for MIN
                Board resultBoard=ABMax(b2,length+1,((b2.turn+1)%2),alpha,beta);
                
                if(min>resultBoard.value)
                {
                    b1.value=resultBoard.value;
                    
                    b.value=resultBoard.value;
                    b.chosen=i;
                    
                    b3=new Board(b2);
                    min=resultBoard.value;
                    imin=i;
                }
                
                //******Alpha Beta
                if(min<=alpha)
                {
                    traverselog.println(b.logname+","+b.depth+","+Infi(b.value)+","+Infi(alpha)+","+Infi(beta));
                    break;
                }    
                beta=Math.min(min,beta);
                //******Alpha Beta
                
                traverselog.println(b.logname+","+b.depth+","+Infi(b.value)+","+Infi(alpha)+","+Infi(beta));
                
            }
        }
        b.value=b3.value;
        b.chosen=imin;
        return b3;

    }
    
    public static Board ABMax(Board b,int length,int turn,int alpha,int beta)
    {
        if(length==cutoff)//Check Terminal condition
        {
            b.utility();
            b.chosen=b.pit_no;
            
            
            traverselog.println(b.logname+","+b.depth+","+Infi(b.value)+","+Infi(alpha)+","+Infi(beta));
            return b;
        }
        
        int max=-999999999;
        int imax=-1;
        Board b3=new Board(b);
        
        Board b1=new Board(b);
        
        b1.value=-999999999;
        b1.turn=turn;
        
        
        
        traverselog.println(b1.logname+","+b1.depth+","+Infi(b1.value)+","+Infi(alpha)+","+Infi(beta));
        int oppositeturn=(turn+1)%2;
        
        //ENDGAME!!
        boolean endgameturn=true;
        for(int x: b.board[turn])
        {
            if(x!=0)
                endgameturn=false;
        }
        boolean endgameopp=true;
        for(int x: b.board[(turn+1)%2])
        {
            if(x!=0)
                endgameopp=false;
        }
        if(endgameturn&&endgameopp)
        {
            b.utility();
            traverselog.println(b.logname+","+b.depth+","+Infi(b.value)+","+Infi(alpha)+","+Infi(beta));
            return b;
        }
        else if(endgameturn)
        {
            
            for(int k=0;k<N;k++)
            {
                b.mancala[oppositeturn]+=b.board[oppositeturn][k];
                b.board[oppositeturn][k]=0;
            }
            b.utility();
            traverselog.println(b.logname+","+b.depth+","+Infi(b.value)+","+Infi(alpha)+","+Infi(beta));
            return b;
        }
        else if(endgameopp)
        {
            for(int k=0;k<N;k++)
            {
                b.mancala[turn]+=b.board[turn][k];
                b.board[turn][k]=0;
            }
            b.utility();
            traverselog.println(b.logname+","+b.depth+","+Infi(b.value)+","+Infi(alpha)+","+Infi(beta));
            return b;
        }
        //ENDGAME!!
        
        for(int i=0;i<N;i++)
        {
    
            Board b2=new Board(b1);
            if((b2.turn==b.turn)&&(!b2.logname.equals("root")))
            {
        
            }
            else
            b2.depth++;
            
            if(b2.board[b2.turn][i]!=0) //can't choose empty pit
            {
                b2.pit_no=i;
                b2.logname=b2.pitname[b2.turn][b2.pit_no];
                b2.playMove();   
        
                int pointer=b2.pointer;//pit containing last stone 
                //CHANGED:
                if((b2.turn==1)&&(pointer<N))//player1
                {
                    pointer=N-1-pointer;
                }
                else if(pointer==N)//last stone in player's Mancala //player gets another move
                {
                    Board b2dash=new Board(b2);
                    b2dash=ABMax(b2,length,turn,alpha,beta);
                    
                    b2.chosen=i;
                    
                    if(max<b2.value)
                    {
                        b1.value=b2dash.value;

                        b.value=b2dash.value;
                        b.chosen=i;
                        b3=new Board(b2dash);
                        //b3=new Board(resultBoard);
                        max=b2dash.value;
                        imax=i;
                    }
                //******Alpha Beta
                if(max>=beta)
                {
                    traverselog.println(b.logname+","+b.depth+","+Infi(b.value)+","+Infi(alpha)+","+Infi(beta));
                    break;
                }    
                    
                alpha=Math.max(max,alpha);
                //******Alpha Beta
                
                    
                    
                    traverselog.println(b.logname+","+b.depth+","+Infi(b.value)+","+Infi(alpha)+","+Infi(beta));
                    continue;
                    
                }
                //CHANGED:
                if((pointer>-1)&&(pointer<N)&&(b2.lastpit==1))//last pit was empty and was of the player
                {
                    
                    b2.mancala[turn]+=1+b2.board[oppositeturn][pointer];
                    b2.board[turn][pointer]=0;
                    b2.board[oppositeturn][pointer]=0;
                    b2.utility();
                }
                
                //***check endgame condition
                boolean endgame=true;
                for(int x: b2.board[turn])
                {
                    if(x!=0)
                        endgame=false;
                }
                if(endgame)
                {
                    
                    for(int k=0;k<N;k++)
                    {
                        b2.mancala[oppositeturn]+=b2.board[oppositeturn][k];
                        b2.board[oppositeturn][k]=0;
                    }
                    b2.utility();
                    
                }
                else
                {    
                endgame=true;
                for(int x: b2.board[oppositeturn])
                {
                    if(x!=0)
                        endgame=false;
                }
                if(endgame)
                {
                    
                    for(int k=0;k<N;k++)
                    {
                        b2.mancala[turn]+=b2.board[turn][k];
                        b2.board[turn][k]=0;
                    }
                    b2.utility();
                    
                }
                }
                //***check endgame condition
                               
                Board resultBoard=ABMin(b2,length+1,((b2.turn+1)%2),alpha,beta);
                
                if(max<resultBoard.value)
                {
                    b1.value=resultBoard.value;
                    
                    b.value=resultBoard.value;
                    b.chosen=i;
                    
                    b3=new Board(b2);
                    max=resultBoard.value;
                    imax=i;
                    
                }
                //******Alpha Beta
                if(max>=beta)
                {
                    traverselog.println(b.logname+","+b.depth+","+Infi(b.value)+","+Infi(alpha)+","+Infi(beta));
                    break;
                }    
                alpha=Math.max(max,alpha);
                //******Alpha Beta
                
                
                traverselog.println(b.logname+","+b.depth+","+Infi(b.value)+","+Infi(alpha)+","+Infi(beta));
                
            }
        }
        b.value=b3.value;
        b.chosen=imax;
        return b3; //b3 is future board
    }
}
