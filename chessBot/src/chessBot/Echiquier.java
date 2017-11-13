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
	private boolean canCastle;
	private int nbCoups;
	
	private playColor.color turn;
	private playColor.color botColor;
	
	public Echiquier(Echiquier e){
		this.area = deepCopy(this.area);
	}

	public Echiquier(int startx,int starty,int size,playColor.color botColor) {

	this.turn = Main.couleurDeJeu;
	this.white_kingside = true;
	this.white_queenside = true;
	this.black_kingside = true;
	this.black_queenside = true;
	this.canCastle = true;
	this.nbCoups = 1;
	this.botColor = botColor;
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
	
	public boolean botIsWhite()
	{
		return this.botColor == playColor.color.WHITE;
	}
	
	public void augmenterNbCoup()
	{
		this.nbCoups++;
	}
	
	public int getNbCoup()
	{
		return this.nbCoups;
	}
	
	public Case getCase(int i,int j)
	{
		if (botColor == playColor.color.WHITE)
			return area[j][i];
		else
			return area[7-j][7-i];
	}
	
	public int[][] simpleArea()
	{
		int[][] ret = new int[8][8];
		
		for(int j=0; j < 8; j++)
		{
			for(int i=0 ; i < 8; i++)
			{
				Piece p = getCase(i,j).getPiece();
				if(p.getColor() == Main.couleurEnnemi && !p.isEmpty())
				{
				ret[j][i] = Piece.toIntEnum(p.getType());
				}
				else {
					ret[j][i] = 8;
				}
				
			}
		}
		
		return ret;
	}
	
	public playColor.color getTurn(){
		return this.turn;
	}
	
	public void printEchiquier()
	{
		for(int j=0; j < 8 ; j++)
		{
			for(int i=0;  i< 8; i++)
			{
				Case c = getCase(i, j);
				System.out.print(c.getPiece().toChar());
			
			}
			System.out.print("\n");
		}
	}
	
	public String getFen()
	{
		String ret="";
		int countEmpty=0;
		
		for(int i=0; i < 8 ; i++)
		{
			for(int j=0; j <8; j++)
		{
			Case c = getCase(j,i);
			
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
		
		if (this.botIsWhite())
			ret+=" w ";
		else
			ret+=" b ";
		if(white_kingside || white_queenside || black_kingside || black_queenside)
		{
			if(white_kingside)
				ret+="K";
			if(white_queenside)
				ret+="Q";
			if(black_kingside)
				ret+="k";
			if(black_queenside)
				ret+="q";
		}
		else
			ret+="-";
		ret+= " - 0 1";
		
		return ret;
		
	}
	
	public void zeroCastle()
	{
		white_kingside = false;
		white_queenside = false;
		black_kingside = false;
		black_queenside = false;
	}
	
	public void disableCastle(playColor.color player)
	{
		if(player == playColor.color.WHITE)
		{
			white_kingside = false;
			white_queenside = false;
		}
		else
		{
			black_kingside = false;
			black_queenside = false;
		}
	}
	
	public void updateEchiquier(Case[] cases){
		Case c1 = cases[0];
		Case c2 = cases[1];

		c2.setPiece(c1.getPiece());
		c1.setEmpty();
		
		if(c2.hasKing() && this.canCastle )
		{
			System.out.println("disabling castle");
			disableCastle(this.botColor);
			this.canCastle = false;
		}
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
			getCase(i,j).findPiece();
				
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
		
		Case[] ret = {getCase(pgn1_coli,8-pgn1_row), getCase(pgn2_coli,8-pgn2_row)};
		
		return ret;
	}
	
	public boolean isEquals(Echiquier echiquier){
		
		for(int i=0; i < 8;i++)
			for(int j=0;j < 8;j++)
			{
				
				if(getCase(i,j).getPiece().getType() != echiquier.getCase(i,j).getPiece().getType())
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
	
	public static boolean equalSimpleArea(int[][] a1,int[][] a2,Echiquier e)
	{
		for (int i=0; i < 8; i++)
			for(int j=0; j < 8; j++)
			{
				if(a1[i][j] != a2[i][j])
				{
					Main.caseptr = e.getCase(i,j);
					return false;
				}
					
			}
		
		return true;
	}
	
	public static Case indexEchiquier(Echiquier e,Case c)
	{
		return e.getCase(c.getYe(),c.getXe());
		
	}
	
	public static int equalSimpleAreaInt(int[][] a1,int[][] a2,Echiquier e)
	{
		int cpt=0;
		for (int i=0; i < 8; i++)
			for(int j=0; j < 8; j++)
			{
				if(a1[i][j] != a2[i][j])
				{
					cpt++;
					Main.caseptr = e.getCase(j, i);
				}
					
			}
		
		return cpt;
	}

}
