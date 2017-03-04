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
	static final boolean capture = false; //si le bot doit capturer les screen des pieces 
	
	//static final String path = "./data/";
	//static final String path = "C:\\Users\\Seven\\Documents\\git\\chessBot\\data\\";
	static final String path = "C:\\Users\\Seven\\git\\botance\\chessBot\\data\\";
	static int x;
	static int y;

	static Point mouseLoc1;
	static Point mouseLoc2;
	static int size;
		
	static Case caseptr;
	
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
			r.delay(1000);
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
				boolean b = !Echiquier.equalSimpleArea(old,e.simpleArea(),e);
				int bint = Echiquier.equalSimpleAreaInt(old,e.simpleArea(),e);
								
				if(bint > 1){ //un mouvement = 2 différences
					System.out.println("Changement joueur");
			//		System.out.println("Il semblerait que la case ("+caseptr.getCoordString()+" aie changé");
			//		System.out.println("Il y avait auparavant un " + Piece.mapPiece[old[caseptr.getXe()][caseptr.getYe()]].toString());
			//		System.out.println("Et maintenant un: " + caseptr.getPiece().toChar());
					r.delay(100);
					e.readPieces();
					e.printEchiquier();
					System.out.println("Changement joueur");
					e.inverseTurn();
				}
				else{
					if (bint ==1)
					{
						while(true){ //on stoppe le bot et on affiche le message d'erreur en boucle
							//fen bug 1r1q2k1/2R5/1p1p2pQ/1N2n3/2P5/2Nb2P1/7P/4K3 b - - 0 34
							// le bot confonds la dame en d8 avec un pion
						System.out.println("Il n'y a qu'une différence = impossible\n la classification a confondu des pieces.");
						r.delay(2000);
						}
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
		
		Piece.initImageData(e,capture);
		
		//e.readPieces();
		
		System.out.println(e.getFen());
		
		StockfishInterface s = new StockfishInterface();
		
		Case c = e.getCase(3, 0);
		
		c.findPiece();
		
		r.mouseMove((int)c.getRectangle().getCenterX(),(int) c.getRectangle().getCenterY());
		
		while(true) {
		//	jeu(e, s, r);
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