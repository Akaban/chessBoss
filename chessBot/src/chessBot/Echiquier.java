package chessBot;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.io.IOException;

public class Echiquier {

	private Case[][] area;

	public Echiquier(int startx,int starty,int size) {

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
		
		return ret;
		
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


}
