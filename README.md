# mancala-game
Three-level(Easy, Medium and Difficult) single player mode of the board game Mancala using Greedy Adversarial Search, MiniMax Algorithm and MiniMax with Alpha-Beta Pruning. (Java)

## Mancala Instructions to Play:
Mancala is a two-player game from Africa in which players moves stones around a board trying to capture as many as possible. In the board , player 1 owns the bottom row of stones and player 2 owns the top row. There are also two special pits on the board, called Mancalas, in which each player accumulates his or her captured stones (player 1's Mancala is on the right and player 2's Mancala is on the left). 
On a player's turn, he or she chooses one of the pits on his or her side of the board (not the Mancala) and removes all of the stones from that pit. The player then places one stone in each pit, moving counterclockwise around the board, starting with the pit immediately next to the chosen pit, including his or her Mancala but NOT his or her opponents Mancala, until he or she has run out of stones. If the player's last stone ends in his or her own Mancala, the player gets another turn. If the player's last stone ends in an empty pit on his or her own side, the player captures all of the stones in the pit directly across the board from where the last stone was placed (the opponents stones are removed from the pit and placed in the player's Mancala) as well as the last stone placed (the one placed in the empty pit). The game ends when one player cannot move on his or her turn, at which time the other player captures all of the stones remaining on his or her side of the board. 

## The code
 A program to determine the next move by implementing the following algorithms:  
  Greedy 
  Minimax 
  Alpha-Beta
 
#Input
 
File input.txt that describes the current state of the game:   
< Task# > Greedy=1, MiniMax=2, Alpha-Beta=3, Competition=4   
< Your player: 1 or 2>   
< Cutting off depth>   
< Board state for player-2>    
< Board state for player-1>   
<#stones in player-2’s mancala>    
<#stones in player-1’s mancala> 

# Output  

## Greedy: 
The program outputs one file named “next_state.txt” showing the next state of the board after the greedy move in the following format.   
next_state.txt   
 Line-1 represents the board state for player-2, i.e. the upper side of the board. Each number is separated by a single white space.   
 Line-2 represents the board state for player-1, i.e. the upper side of the board. Each number is separated by a single white space.   
 Line-3 gives you the number of stones in player-2’s mancala.  Line-4 gives you the number of stones in player-1’s mancala. 
   
## MiniMax:   
The program outputs two files named “next_state.txt” showing the next state of the board after the greedy move and “traverse_log.txt” showing the traverse log of your program in the following format.   
 The format of “next_state.txt” is same as above.   
 The format of “traverse_log.txt” is as below:  
The MiniMax traverse log requires 3 columns. Each column is separated by “,” (a single comma). Three columns are node, depth and value.
  
## Alpha-Beta:  
The program outputs two files named “next_state.txt” showing the next state of the board after the greedy move and “traverse_log.txt” showing the traverse log of your program in the following format.   
 The format of “next_state.txt” is the same as shown above.   
 The format of “traverse_log.txt” will be similar to the one for MiniMax, but with two additional columns.   
Node,Depth,Value,Alpha,Beta
