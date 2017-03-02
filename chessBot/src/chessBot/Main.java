package chessBot;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.AWTException;
import java.awt.Point;
import java.io.File;
import java.io.IOException;


import javax.imageio.ImageIO;
import chessBot.robotHelper;


public class Main {

	static final playColor.color couleurDeJeu = playColor.color.WHITE;
	static final String path = "./data/";
	static int x;
	static int y;

	static Point mouseLoc1;
	static Point mouseLoc2;
	static int size;
	
	static double distance (Point p) {
	    return (Math.sqrt(p.x * p.x + p.y * p.y));
	    }

	public static void main(String[] args) throws InterruptedException, AWTException, IOException
	{
		int[] coord = robotHelper.findEchiquierChess();
		mouseLoc1 = new Point(coord[2],coord[3]);
		mouseLoc2 = new Point(coord[0],coord[1]);
		mouseLoc2.translate(-mouseLoc1.x, -mouseLoc1.y);
		size=(int)distance(mouseLoc2);
		
		System.out.println(size);
		
		Echiquier e = new Echiquier(mouseLoc1.x,mouseLoc1.y,size);
		
		Piece.initImageData(e);
		
		Case[][] echiquier = e.getEchiquier();
		
		e.readPieces();
		
		for(int j=0; j < 8 ; j++)
		{
			for(int i=0;  i< 8; i++)
			{
				Case c = echiquier[j][i];
				System.out.print(c.getPiece().toChar());
			
			}
			System.out.print("\n");
		}
		
		
		
		//echiquier[0][0].findPiece();
		
		//robotHelper.whatDoISee(e);
		
		/*for(int j=0; j < 8 ; j++)
		{
			for(int i=0;  i< 8; i++)
			{
				int decay=0;
				if(j == 7) decay=6;
				Case c = echiquier[j][i];
				Rectangle r = c.getRectangle();
				robotHelper.adjustRectangle(r,size/17);
				String casename = (j+1) + "-" + (i+1);
				ImageIO.write(new Robot().createScreenCapture(r), "png", new File(Main.path + "looking"+casename+".png"));	
			}
		}*/
		
		//robotHelper.whatDoISee(e);
		
	//	echiquier[0][6].findPiece();
		
		
/*		for(int j=0; j < 8 ; j++)
		{
			for(int i=0;  i< 8; i++)
			{
			System.out.print(echiquier[j][i].PieceToChar());	
			}
			System.out.print("\n");
		}*/
		
		
		//for(int i=0; i < 8 ; i++) robotHelper.screenshotMaker(echiquier[i][6].getRectangle(), "null");
		//robotHelper.screenshotMaker(echiquier[3][5].getRectangle(), "");
//		
//		Robot r = new Robot();
//		
//		r.delay(5000);
//		
//		for(int j=0; j < 8 ; j++)
//		{
//			for(int i=0;  i< 8; i++)
//			{
//				Case c = echiquier[i][j];
//				r.mouseMove((int)c.getRectangle().getCenterX(),(int) c.getRectangle().getCenterY());
//				r.delay(4000);
//			}
//			System.out.print("\n");
//		}
//		


	}



}