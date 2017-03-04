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

	static final playColor.color couleurDeJeu = playColor.color.BLACK;
	static final boolean capture = true; //si le bot doit capturer les screen des pieces 
	
	static final String path = "./data/";
	//static final String path = "C:\\Users\\Seven\\Documents\\git\\chessBot\\data\\";
	static int x;
	static int y;

	static Point mouseLoc1;
	static Point mouseLoc2;
	static int size;
	
	static double distance (Point p) {
	    return (Math.sqrt(p.x * p.x + p.y * p.y));
	    }

	
	public static void jeu(Echiquier e, StockfishInterface s, Robot r) throws IOException, InterruptedException, AWTException{
		
		if(couleurDeJeu == e.getTurn()){ // a lui de jouer
			
			System.out.println("a moi de jouer");
			String[] nextMove = s.nextMove(e.getFen(), 1000);
			String coup = nextMove[0];
			int score = Integer.parseInt(nextMove[1]);
			System.out.println("Score: " + score/100d);
			System.out.println("Je Joue: " + coup);
			Case[] cases = e.PGNtoPtr(coup);
			robotHelper.jouerCoup(cases, r);
			System.out.println("ok");
			e.inverseTurn();
			//mise a jour echiquier
			e.updateEchiquier(cases);
		}
		else{ // pas a lui de jouer
			// lecture
			int[][] old = Echiquier.deepCopy(e.simpleArea());
			while(couleurDeJeu != e.getTurn()){
				
				e.readPieces();
				int[][] sa1 = e.simpleArea();
				boolean b = !Echiquier.equalSimpleArea(old,e.simpleArea());
								
				if(b){
					r.delay(100);
					e.readPieces();
					System.out.println("Changement joueur");
					e.inverseTurn();
				}
				else{
					System.out.println("oklm");
				}
			}
		}
		
	}
	
	public static void main(String[] args) throws InterruptedException, AWTException, IOException
	{
		Robot r = new Robot();
		

		int[] coord = robotHelper.findEchiquierChess();

		mouseLoc1 = new Point(coord[2],coord[3]);
		mouseLoc2 = new Point(coord[0],coord[1]);
		mouseLoc2.translate(-mouseLoc1.x, -mouseLoc1.y);
		size=(int)distance(mouseLoc2);
		
		if (size == 6000)
			try {
				throw new Exception("Echiquier non trouvé !");
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				System.exit(1);
			}
		
		System.out.println(size);
		
		Echiquier e = new Echiquier(mouseLoc1.x,mouseLoc1.y,size,couleurDeJeu);
		
		Piece.initImageData(e,capture);
		
		e.readPieces();
		
		for(int j=0; j < 8 ; j++)
		{
			for(int i=0;  i< 8; i++)
			{
				Case c = e.getCase(i, j);
				System.out.print(c.getPiece().toChar());
			
			}
			System.out.print("\n");
		}
		
		System.out.println(e.getFen());
		
		StockfishInterface s = new StockfishInterface();
		
		while(true) {
			jeu(e, s, r);
			for(int j=0; j < 8 ; j++)
			{
				for(int i=0;  i< 8; i++)
				{
					Case c = e.getCase(i, j);
					System.out.print(c.getPiece().toChar());
				
				}
				System.out.print("\n");
			}
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