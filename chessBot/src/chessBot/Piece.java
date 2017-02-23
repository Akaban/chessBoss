package chessBot;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Piece {
	
	enum nomPiece { Pawn,Knight,King,Queen,Bishop,Rook };
	
	private nomPiece type;
	private playColor.color couleur;
	
	
	public static BufferedImage[] dataPiece;
	public static final nomPiece[] mapPiece = {nomPiece.Pawn,nomPiece.Knight,nomPiece.King,nomPiece.Queen,nomPiece.Bishop,nomPiece.Rook};
	public static final playColor.color[] mapColor = {playColor.color.WHITE,playColor.color.BLACK};

	
	public Piece(nomPiece type,playColor.color couleur)
	{
		this.type = type;
		this.couleur = couleur;
	}
	
	

	
	public static String toStringEnum(nomPiece type)
	{
		String ret;
		
		if (type == null) return "empty";
		
		switch (type)
		{
		case Pawn :
			ret = "pawn";
			break;
		case Knight :
			ret = "knight";
			break;
		case King :
			ret = "king";
			break;
		case Queen :
			ret = "queen";
			break;
		case Bishop:
			ret = "bishop";
			break;
		case Rook:
			ret = "rook";
			break;
		default :
			ret = "empty";
				
		}
		return ret;
		}
	
	public char toChar()
	{
		char ret;
		
		if (this.type == null) return '%';
		
		switch (this.type)
		{
		case Pawn :
			ret = 'p';
			break;
		case Knight :
			ret = 'n';
			break;
		case King :
			ret = 'k';
			break;
		case Queen :
			ret = 'q';
			break;
		case Bishop:
			ret = 'b';
			break;
		case Rook:
			ret = 'r';
			break;
		default :
			ret = '%';
				
		}
		
		if(this.couleur == playColor.color.WHITE) ret = Character.toUpperCase(ret);
		
		return ret;
	
		
		
	}
	
	public static void initImageData() throws IOException
	{
		String color;
		dataPiece = new BufferedImage[6];
		String couleur="white";
		int countPiece=0;
		for(nomPiece piece : nomPiece.values())
		{
			String path1 = Main.path + couleur + "-" + toStringEnum(piece)+".png";
			System.out.println(path1);
			dataPiece[countPiece] = robotHelper.traitementContour(ImageIO.read(new File(path1)),false);
			ImageIO.write(dataPiece[countPiece], "png", new File(Main.path + "piece"+couleur+countPiece+".png"));
			
			countPiece++;
		}
	
		
		System.out.println("finished init");

}
}
