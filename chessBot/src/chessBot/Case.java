package chessBot;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.io.IOException;

public class Case {
	
	private Piece piece;
	private int x; //Coordonnées dans le repère de l'échiquier.
	private int y;
	private int xe;
	private int ye;
	private boolean color; //1 si vert sinon 0
	private Rectangle rectangle;
	private Rectangle adjustedRectangle;

	
	public Case(Piece piece,int x,int y)
	{
		this.piece = piece;
		this.x = x;
		this.y= y;
		this.rectangle = null;
	}
	
	public Case(int x,int y,int xe,int ye, int sizecase,boolean color)
	{
		this.piece = null;
		this.x = x;
		this.y= y;
		this.xe=xe;
		this.ye=ye;
		this.color=color;
		this.rectangle = new Rectangle(x,y,sizecase,sizecase);
		Rectangle r = new Rectangle(x,y,sizecase,sizecase);
		robotHelper.adjustRectangle(r, Main.size/17);
		this.adjustedRectangle = r;
	}
	
	public void findPiece() throws AWTException, IOException
	{
		Rectangle rec = adjustedRectangle;
		Piece.nomPiece piece=robotHelper.findPiece(rec,this.color,0);
		this.piece = new Piece(piece,playColor.color.WHITE); // pas de distinction blanc noir encore
	}
	
	public boolean isEmpty()
	{
		return (this.piece == null);
	}
	
	public char PieceToChar()
	{
		char print;
		if (isEmpty()) print = '%';
		else print = piece.toChar();
		return print;
		
	}
	
	public Piece getPiece()
	{
		return this.piece;
	}
	
	public void setRectangle(Rectangle rectangle)
	{
		this.rectangle = rectangle;
	}
	
	public Rectangle getRectangle()
	{
		return this.rectangle;
	}
	
	public Rectangle getAdjustedRectangle()
	{
		return this.adjustedRectangle;
	}
	
	public boolean getColor()
	{
		return this.color;
	}

}
