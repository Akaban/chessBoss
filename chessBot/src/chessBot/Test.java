package chessBot;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

public class Test {
	
	static final Color vert = new Color(118,150,86);
	static final Color blanc = new Color(238,238,210); 
	static final Color blanc_select = new Color(247,247,130); 
	static final Color vert_select = new Color(187,202,68); //Vert Ches
	
	public static Color detectColor(BufferedImage img,ArrayList<Color> colorlist) throws IOException
	{

		int countBs=0;
		int countVs=0;
		

		
		//ImageIO.write(img, "png", new File (Main.path + "detection-" + k + "-" + x +".png"));
		
		for(int i=0; i < img.getWidth(); i++)
			for(int j=0; j< img.getHeight();j++)
			{
				Color c = new Color(img.getRGB(i, j));
				if (c.equals(vert_select))
				{
					countVs+=1;
					System.out.println("v");
				}
				else if (c.equals(blanc_select))
				{
					countBs+=1;
					System.out.println("b");
				}
				else
					colorlist.add(c);
			}
		
		System.out.println("I see bs = " + countBs +" and countVs = " + countVs);
		
		if (countVs < 20 && countBs < 20)
			return vert;
		if (countVs > countBs)
			return vert_select;
		else
			return blanc_select;
		
		
	}
	
	public static void main(String[] args) throws IOException, AWTException
	{
		
		Piece.initImageData(null,false);
		
		BufferedImage img = ImageIO.read(new File(Main.path + "looking3-1" + ".png"));
		BufferedImage img2 = robotHelper.traitementContour(img,blanc_select);
		Piece.nomPiece piece = robotHelper.classification(img2);
		
		System.out.println(vert.equals(blanc));
		
		ArrayList<Color> cl = new ArrayList<Color>();
		
		detectColor(img,cl);
		
		 Map<Color,Integer> charCounter=new HashMap<Color,Integer>();
		 
		 for(Color c : cl)
		 {
			 if(charCounter.containsKey(c))
			 {
				 charCounter.put(c, charCounter.get(c) + 1);
			 }
			 else
			 {
				 charCounter.put(c, 0);
			 }
		 }
		 
		 System.out.println(charCounter);
		
		
		
	}

}
