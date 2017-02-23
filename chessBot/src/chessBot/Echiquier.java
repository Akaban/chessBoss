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
	
	public Case[][] getEchiquier()
	{
		return area;
	}

	public void readPieces() throws AWTException, IOException
	{
		for(int i=0; i < 8 ; i++) for(int j=0; j <8; j++)
			{
			try{
			area[j][i].findPiece();
			}
			catch (java.lang.IndexOutOfBoundsException e)
			{
				e.printStackTrace();
				System.out.println("Robot will move to the problematic case");
				Case c = area[i][j];
				(new Robot()).mouseMove((int)c.getRectangle().getCenterX(),(int) c.getRectangle().getCenterY());
			}
			}
	}


}
