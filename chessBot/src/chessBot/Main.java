package chessBot;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.awt.AWTException;
import java.awt.Point;
import java.io.File;
import java.io.IOException;


import javax.imageio.ImageIO;
import chessBot.robotHelper;


public class Main {

	static playColor.color white = playColor.color.WHITE;
	static playColor.color black = playColor.color.BLACK;
	static playColor.color couleurDeJeu = null; //null = autodetect
	static playColor.color couleurEnnemi;
	static final boolean capture = true; //si le bot doit capturer les screen des pieces
	static int relance = 0;
	
	//static final String path = "./data/";
	//static final String path = "C:\\Users\\Seven\\Documents\\git\\chessBot\\data\\";
	static final String path = "C:\\Users\\Seven\\git\\botance\\chessBot\\data\\";
	static int x;
	static int y;

	static Point mouseLoc1;
	static Point mouseLoc2;
	static int size;
		
	static Case caseptr;
	
	static final int botDelay = 150; //le temps que le bot met à reflechir
	
	static double distance (Point p) {
	    return (Math.sqrt(p.x * p.x + p.y * p.y));
	    }
	
	
	public static void jeu(Echiquier e, StockfishInterface s, Robot r) throws IOException, InterruptedException, AWTException{
		
		if(couleurDeJeu == e.getTurn()){ // a lui de jouer
			//e.zeroCastle();
			//System.out.println("a moi de jouer");
			String coup = null;
			try {
			coup = s.nextMove(e.getFen(), botDelay);
			}
			catch(ArrayIndexOutOfBoundsException exception) //stockfish a planté
			{
				//on le relance sans les roque
				if(relance == 0)
				{
				System.out.println("Stockish a planté. On le relance sans roque");
				r.delay(3000);
				e.zeroCastle();
				s.startOver();
				e.readPieces();
				relance++;
				return;
				}
				else if (relance < -10)
				{
					r.delay(3000);
					System.out.println(e.getFen());
					s.startOver();
					e.readPieces();
					relance++;
					return;
				}
				else
				{
					exception.printStackTrace();
					System.exit(1);
				}
			}
			System.out.println("Je Joue: " + coup);
			Case[] cases = e.PGNtoPtr(coup);
			robotHelper.jouerCoup(cases, r);
			e.inverseTurn();
			//mise a jour echiquier
			e.updateEchiquier(cases);
			//r.delay(150);
		}
		else{ // pas a lui de jouer
			// lecture
			int[][] old = Echiquier.deepCopy(e.simpleArea());
			while(couleurDeJeu != e.getTurn()){
				
				e.readPieces();
				int[][] sa1 = e.simpleArea();
				int bint = Echiquier.equalSimpleAreaInt(old,e.simpleArea(),e);
				
				//r.delay(500);
								
				if(bint > 0){ //un mouvement = 2 différences
					//r.delay(100);
					//e.readPieces();
					//bint = Echiquier.equalSimpleAreaInt(old,e.simpleArea(),e);
					//if(bint < 1)
					//{
					//	System.out.println("En fait non je me suis trompé, ma bonne tante");
					//	r.delay(1000);
					//	break;
					//}
					//e.printEchiquier();
					//System.out.println("Changement joueur");
					e.inverseTurn();
				}
				else{
					System.out.println("oklm " + bint);
					if(bint == 1)
					{
						System.out.println("Une difference qui est sur la case "
								+ caseptr.getCoordString()+" j'y vois un " + caseptr.getPiece().toString());
					}
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
				throw new Exception("Echiquier non trouvÃ© !");
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				System.exit(1);
			}
		
		System.out.println(size);
		
		Echiquier e = new Echiquier(mouseLoc1.x,mouseLoc1.y,size,couleurDeJeu);
		
		if(couleurDeJeu == null) //autodetect
		{
		Case casedetect = e.getEchiquier()[7][0];
		Rectangle rec = casedetect.getAdjustedRectangle();
		BufferedImage img = new Robot().createScreenCapture(rec);
		couleurDeJeu = casedetect.detectColorPiece(img);
		e = new Echiquier(mouseLoc1.x,mouseLoc1.y,size,couleurDeJeu);
		}
		
		couleurEnnemi = playColor.inverseColor(couleurDeJeu);
		
		System.out.println(couleurEnnemi.toString());

		
		
		Piece.initImageData(e,capture);
		
		e.readPieces();
		
		System.out.println(e.getFen());
		
		StockfishInterface s = new StockfishInterface();
		
		
		while(true) {
			jeu(e, s, r);
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