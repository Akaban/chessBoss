package chessBot;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Robot;
import java.io.IOException;
import java.util.Arrays;

public class Echiquier {

	private Case[][] area;
	private boolean white_kingside;
	private boolean white_queenside;
	private boolean black_kingside;
	private boolean black_queenside;
	private int nbCoups;
	
	private playColor.color turn; 
	
	public Echiquier(Echiquier e){
		this.area = deepCopy(this.area);
	}

	public Echiquier(int startx,int starty,int size) {

	this.turn = playColor.color.WHITE;
	this.white_kingside = true;
	this.white_queenside = true;
	this.black_kingside = true;
	this.black_queenside = true;
	this.nbCoups = 1;
	int sizecase = Math.round(size/8f);
	this.area = new Case[8][8];
	
	int firsty = starty;
	boolean color = true;
	
	for(int i=0; i < 8; i++)
	{
		starty = firsty;
		for(int j=0 ; j < 8; j++)
		{
			color=!color;
			area[j][i] = new Case(startx,starty,j,i,sizecase,color);
			starty = starty + sizecase;
			
		}
		color = !color;
		startx = startx + sizecase;
	}
	

	}
	
	public int[][] simpleArea()
	{
		int[][] ret = new int[8][8];
		
		for(int i=0; i < 8; i++)
		{
			for(int j=0 ; j < 8; j++)
			{
				ret[i][j] = Piece.toIntEnum(area[i][j].getPiece().getType());
				
			}
		}
		
		return ret;
	}
	
	public playColor.color getTurn(){
		return this.turn;
	}
	
	public String getFen()
	{
		String ret="";
		int countEmpty=0;
		
		for(int i=0; i < 8 ; i++)
		{
			for(int j=0; j <8; j++)
		{
			Case c = area[i][j];
			
			if (c.isEmpty())
			{
				countEmpty++;
			}
			else
			{
				if (countEmpty > 0)
				{
					ret += countEmpty;
					countEmpty=0;
				}
				ret += c.getPiece().toChar();
			}
			
		}
			if (countEmpty > 0)
			{
				ret += countEmpty;
				countEmpty=0;
			}
			if (i < 7)ret += "/";
		}
		
		ret+= " w KQkq - 0 1";
		
		return ret;
		
	}
	
	public void updateEchiquier(Case[] cases){
		Case c1 = cases[0];
		Case c2 = cases[1];
		c2.setPiece(c1.getPiece());
		c1.setEmpty();
	}
	
	public void inverseTurn(){
		if (this.turn == playColor.color.WHITE)
			this.turn = playColor.color.BLACK;
		else
			this.turn = playColor.color.WHITE;
	}
	
	public Case[][] getEchiquier()
	{
		return area;
	}

	public void readPieces() throws AWTException, IOException
	{
		for(int i=0; i < 8 ; i++) for(int j=0; j <8; j++)
			{
			area[j][i].findPiece();
				
			}
	}
	
	public Case[] PGNtoPtr(String pgn)
	{
		String pgn1 = pgn.substring(0, 2);
		String pgn2 = pgn.substring(2,4);
		
		char pgn1_col = pgn1.charAt(0);
		char pgn2_col = pgn2.charAt(0);
		int pgn1_row = Character.getNumericValue(pgn1.charAt(1));
		int pgn2_row = Character.getNumericValue(pgn2.charAt(1));
		
		int pgn1_coli = ((int) pgn1_col) - 97;
		int pgn2_coli = ((int) pgn2_col) - 97;
		
		Case[] ret = {area[8-pgn1_row][pgn1_coli], area[8-pgn2_row][pgn2_coli]};
		
		return ret;
	}
	
	public boolean isEquals(Echiquier echiquier){
		
		for(int i=0; i < 8;i++)
			for(int j=0;j < 8;j++)
			{
				
				if(this.area[i][j].getPiece().getType() != echiquier.area[i][j].getPiece().getType())
					return false;
			}
		
		return true;
	}
	
	
	public static Case[][] deepCopy(Case[][] original) {
	    if (original == null) {
	        return null;
	    }

	    final Case[][] result = new Case[original.length][];
	    for (int i = 0; i < original.length; i++) {
	        result[i] = Arrays.copyOf(original[i], original[i].length);
	        // For Java versions prior to Java 6 use the next:
	        // System.arraycopy(original[i], 0, result[i], 0, original[i].length);
	    }
	    return result;
	    
	}
	
	public static int[][] deepCopy(int[][] original) {
	    if (original == null) {
	        return null;
	    }

	    final int[][] result = new int[original.length][];
	    for (int i = 0; i < original.length; i++) {
	        result[i] = Arrays.copyOf(original[i], original[i].length);
	        // For Java versions prior to Java 6 use the next:
	        // System.arraycopy(original[i], 0, result[i], 0, original[i].length);
	    }
	    return result;
	    
	}
	
	public static boolean equalSimpleArea(int[][] a1,int[][] a2)
	{
		for (int i=0; i < 8; i++)
			for(int j=0; j < 8; j++)
			{
				if(a1[i][j] != a2[i][j])
					return false;
			}
		
		return true;
	}
	
	//Faire des fonctions de détections de roque et savoir également détecter quand le mec a joué (un changement).
	


}
