package chessBot;

import java.awt.AWTException;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import chessBot.Piece.nomPiece;

public class robotHelper {
	
	
	static final int minusScreenZoneFactor = 7;
	static final int translateScreenZoneFactor = 8;
	static final int translateCorrection = 15;
	static int countScreen =0;
	static int doing=0;
	
	public static void adjustRectangle(Rectangle rec,int sizecase)
	{
		rec.grow(-(sizecase/minusScreenZoneFactor) -3,-sizecase/minusScreenZoneFactor );
		rec.translate(sizecase/translateScreenZoneFactor, sizecase/translateScreenZoneFactor);
		rec.translate(-sizecase/translateCorrection, -sizecase/translateCorrection);
		rec.translate(-Math.round(translateCorrection/2.5f),0);
		
		
	}
	
	public static void jouerCoup(Case[] cases, Robot r){
		
		
		for(Case c : cases){
			Point loc = c.localisationPoint();
			r.mouseMove(loc.x, loc.y);
			r.delay(100);
			r.mousePress(InputEvent.BUTTON1_MASK);
			r.delay(100);
			r.mouseRelease(InputEvent.BUTTON1_MASK);
			r.delay(300);
		}
		
	}
	
	public static int distance(BufferedImage img1, BufferedImage img2){
		
		int ret=0;
		
		for(int i=0; i < img1.getWidth(); i++)
		{
			for(int j=0; j < img1.getWidth(); j++)
			{
				int x = 0;
				int y = 0;
				if( (new Color(img1.getRGB(i, j))).equals(Color.WHITE)){
					x = 1;
				}
				if( (new Color(img2.getRGB(i, j))).equals(Color.WHITE)){
					y = 1;
				}
				
				ret += Math.abs(x - y);
				
			}
		}
		
		return ret;
	}
	
	
	public static nomPiece classification(BufferedImage img){
		
		int min = 1000;
		int i_min = 0;
		
		for(int i =0; i < 7; i++)
		{
			int d = distance(img, Piece.dataPiece[i]);
			
			if (d < min){
				min = d;
				i_min = i;
			}
		}
		System.out.println(min);
		System.out.println(Piece.mapPiece[i_min].toString());
		return Piece.mapPiece[i_min];
		

	}
		
	public static BufferedImage toBufferedImageOfType(BufferedImage original, int type) {
	    if (original == null) {
	        throw new IllegalArgumentException("original == null");
	    }

	    // Don't convert if it already has correct type
	    if (original.getType() == type) {
	        return original;
	    }

	    // Create a buffered image
	    BufferedImage image = new BufferedImage(original.getWidth(), original.getHeight(), type);

	    // Draw the image onto the new buffer
	    Graphics2D g = image.createGraphics();
	    try {
	        g.setComposite(AlphaComposite.Src);
	        g.drawImage(original, 0, 0, null);
	    }
	    finally {
	        g.dispose();
	    }

	    return image;
	}
	
	public static BufferedImage deepCopy(BufferedImage bi) {
		 ColorModel cm = bi.getColorModel();
		 boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		 WritableRaster raster = bi.copyData(null);
		 return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
		}
	
	public static BufferedImage traitementContour(BufferedImage img,Color couleurFond)
	{
		//TODO Besoin de d�tection de la couleur de case
		BufferedImage ret = deepCopy(img);
		double fuzziness = 0.05;
		double maxDistance = 441 * fuzziness;
		
		for(int i=0; i < img.getWidth();i++)
		{
			for(int j=0; j < img.getHeight();j++)
			{
				Color pixel = new Color(img.getRGB(i, j));
				int rDelta = pixel.getRed() - couleurFond.getRed();
				int gDelta = pixel.getGreen() - couleurFond.getGreen();
				int bDelta = pixel.getBlue() - couleurFond.getBlue();
				
				double distance = Math.sqrt((double)(rDelta*rDelta+gDelta*gDelta+bDelta*bDelta));
				
				if (distance < maxDistance) ret.setRGB(i, j, Color.BLACK.getRGB());
				else ret.setRGB(i, j, Color.WHITE.getRGB());
				
			}
		}
		
		
		
		return ret;
	}
	
	
	public static BufferedImage ungrow(BufferedImage img,int decay)
	{
		BufferedImage ret = new BufferedImage(img.getWidth(),img.getWidth()-decay,img.getType());
		for(int i=0; i < ret.getWidth();i++)
		{
			for(int j=0; j < ret.getHeight();j++)
			{
				ret.setRGB(i, j,img.getRGB(i,j) );
			}
		}
		return ret;
	}
	
	/*public static Piece.nomPiece findPiece(Rectangle rectangle,boolean couleurCase,int decay) throws AWTException, IOException
	{
		BufferedImage lookingAt = traitementContour(new Robot().createScreenCapture(rectangle),couleurCase);
		ImageIO.write(lookingAt, "png", new File(Main.path + "looking"+".png"));
		
		double min = 10d;
		int minp=0;
		double match = 0;//metrique
			for(int p=0; p < 6; p++)
			{
				BufferedImage dataPiece = Piece.dataPiece[p]; 
				//match = matchContours(lookingAt,dataPiece);
				if(match == (-1d)) return null;
				System.out.println("Ressemblance avec la piece " + Piece.toStringEnum(Piece.mapPiece[p])+"= " + match);
				if (match < min)
				{
					min = match;
					minp = p;
				}
								
			}
			
			if (min > 0.5d) return null;
			
			System.out.println("Nearest piece is therefore: " + Piece.toStringEnum(Piece.mapPiece[minp]));
		
		
		return Piece.mapPiece[minp];
	}*/
	
	public static void whatDoISee(Echiquier echiquier) throws AWTException, IOException
	{
		Case[][] data=echiquier.getEchiquier();
		for(int i=0; i < 8;i++) for(int j=0;j < 8; j++)
		{
			int decay=0;
			if(j == 7) decay=6;
			Case c = data[j][i];
			Rectangle r = c.getRectangle();
			adjustRectangle(r,decay);
			BufferedImage img = new Robot().createScreenCapture(r);
			BufferedImage lookingAt = traitementContour(img,c.detectColor(img));
			String casename = (j+1) + "-" + (i+1);
			ImageIO.write(lookingAt, "png", new File(Main.path + "looking"+casename+".png"));
		}
		
			
	}
	
	public static int[] findEchiquierChess() throws AWTException
	{
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		BufferedImage screen = new Robot().createScreenCapture(new Rectangle(0,0,dim.width,dim.height));
		Color vert = new Color(118,150,86); //Vert Chess
		Color blanc = new Color(238,238,210); //Blanc Chess
		Color blanc_select = new Color(247,247,130); 
		Color vert_select = new Color(187,202,68); //Vert Chess
		
		int minxv = 6000;
		int maxyv = 0;
		int minxb = 6000;
		int minyb = 6000;
		
		for(int i=0; i < screen.getWidth(); i++)
		{
			for(int j=0; j < screen.getHeight(); j++)
			{
				Color pixel = new Color(screen.getRGB(i,j));
				if (pixel.equals(vert) || pixel.equals(vert_select))
				{
					if (i < minxv || j > maxyv) 
					{
						minxv = i;
						maxyv = j;
					}
				}
				else if (pixel.equals(blanc) || pixel.equals(blanc_select))
				{
					if (i < minxb || j < minyb) 
					{
						minxb = i;
						minyb = j;
					}
				}
			}
		}
		
		int[] ret ={minxv,maxyv,minxb,minyb};
		return ret ;
	}
	
	public static BufferedImage simpleScreen(Rectangle r)
	{
		BufferedImage ret = null;
		
		try {
			ret = new Robot().createScreenCapture(r);
		}
		catch(AWTException e)
		{
			e.printStackTrace();
		}
		
		return ret;
	}
	
	public static void screenshotMaker(Rectangle rectangle,String name) throws AWTException{
		BufferedImage img;
		int sizecase = (int)rectangle.getWidth();
		adjustRectangle(rectangle,sizecase);
		try {
			img = new Robot().createScreenCapture(rectangle);
			ImageIO.write(img, "png", new File("C:\\Users\\Seven\\workspace\\chessBot\\data\\screenshot"+countScreen+".png"));
			countScreen++;
		} catch (IOException e) {
			e.printStackTrace();
		}
}

}
