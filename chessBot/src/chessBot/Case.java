package chessBot;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Case {
	
	static final Color vert = new Color(118,150,86);
	static final Color blanc = new Color(238,238,210); 
	static final Color blanc_select = new Color(247,247,130); 
	static final Color vert_select = new Color(187,202,68); //Vert Chess

	
	private Piece piece;
	private int x; //Coordonnees dans le repere de l'echiquier.
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
		robotHelper.adjustRectangle(r, 0);
		this.adjustedRectangle = r;
	}
	
	public void findPiece() throws AWTException, IOException
	{
		Rectangle rec = adjustedRectangle;
		BufferedImage img = new Robot().createScreenCapture(rec);
		BufferedImage img2 = robotHelper.traitementContour(img, this.detectColor());
		Piece.nomPiece piece = robotHelper.classification(img2);
		this.piece = new Piece(piece, this.detectColorPiece(img));
		ImageIO.write(img, "png", new File(Main.path + "looking"+xe+"-"+ye+".png"));
	}
	
	public boolean isEmpty()
	{
		return (this.piece == null || this.piece.getType() == Piece.nomPiece.Empty);
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
	
	public static String colorToString(Color c)
	{
		if(c == null)
			return "default";
		
		if (c.equals(vert))
		{
			return "vert";
		}
		else if (c.equals(blanc))
		{
			return "blanc";
		}
		else if (c.equals(vert_select))
		{
			return "vert fluo";
		}
		else if (c.equals(blanc_select))
		{
			return "jaune";
		}
		
		return "default";
	}
	
	public Color defaultColor()
	{
		if (this.color)
		{
			return vert;
		}
		else
		{
			return blanc;
		}
	}
	
	public Color detectColor() throws IOException
	{
		Point loc = adjustedRectangle.getLocation();
		int size = (int)adjustedRectangle.getWidth();
		Rectangle r = new Rectangle(loc.x, loc.y,size/4,size/4);
		BufferedImage img = robotHelper.simpleScreen(r);
		int countBs=0;
		int countVs=0;
		
		//ImageIO.write(img, "png", new File (Main.path + "detection-" + k + "-" + x +".png"));
		
		for(int i=0; i < img.getWidth(); i++)
			for(int j=0; j< img.getHeight();j++)
			{
				Color c = new Color(img.getRGB(i, j));
				if (c.equals(vert_select))
				{
					countVs++;
				}
				else if (c.equals(blanc_select))
				{
					countBs++;
				}
			}
		
		//System.out.println("I see bs = " + countBs +" and countVs = " + countVs);
		
		if (countVs < 20 && countBs < 20)
			return defaultColor();
		if (countVs > countBs)
			return vert_select;
		else
			return blanc_select;
		
		
	}
	
	public playColor.color detectColorPiece(BufferedImage img) throws AWTException{
		
		int size = (int)adjustedRectangle.getWidth();
		int x = size/2;
		int y = (int)(size * (2/3d));
		
		Color c = new Color(img.getRGB(x, y));
		
		if (c.getRed() > 150) //la case est blanche
			return playColor.color.WHITE;
		return playColor.color.BLACK;
		
	}
	
	public Point localisationPoint()
	{
		int size = (int)adjustedRectangle.getWidth();
		int x = size/2;
		int y = size/2;
		
		Point loc = adjustedRectangle.getLocation();
		loc.translate(x, y);
		
		return loc;
		
		
	}
	


}
