package chessBot;

import java.awt.AWTException;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;


public class robotHelper {
	
	static{ System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }
	
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
		//rec.translate(0, -Math.round(translateCorrection/4f));
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
	
	public static BufferedImage traitementContour(BufferedImage img,boolean color)
	{
		Color couleurFond;
		if (color)
			couleurFond = new Color(118,150,86); //Vert Chess
		else couleurFond = new Color(238,238,210); //Blanc Chess
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
	
	public static Mat getBiggestContour(List<MatOfPoint> contours)
	{
		MatOfPoint ret = null;
		int max = 0;
		
		for(MatOfPoint m : contours)
		{
			Mat mm = (Mat)m;
			System.out.println(mm.type());
			Mat mm_one = new Mat(mm.rows(),mm.cols(),CvType.CV_8UC1);
			mm.convertTo(mm_one, CvType.CV_8UC1);
			System.out.println(mm_one.channels());
			int nonzero=Core.countNonZero(mm_one);
			if (nonzero > max)
			{
				ret = m;
				max=nonzero;
			}
		}
		
		return ((Mat)ret);
	}
	
	public static double matchContours(BufferedImage img1,BufferedImage img2) throws IOException
	{
		img1 = toBufferedImageOfType(img1,BufferedImage.TYPE_3BYTE_BGR);
		img2 = toBufferedImageOfType(img2,BufferedImage.TYPE_3BYTE_BGR);
		
		int img1x = img1.getWidth();
		int img1y = img1.getHeight();
		int img2x = img2.getWidth();
		int img2y = img2.getHeight();
		
		Mat mat1 = new Mat(img1x,img1y,CvType.CV_8UC3);
		Mat mat1gscale = new Mat(img1x,img1y,CvType.CV_8UC1);
		
		byte[] pixels = ((DataBufferByte) img1.getRaster().getDataBuffer()).getData();
		mat1.put(0, 0, pixels);
		
		
		Mat mat2 = new Mat(img2x,img2y,CvType.CV_8UC3);
		Mat mat2gscale = new Mat(img2x,img2y,CvType.CV_8UC1);
		byte[] pixels1 = ((DataBufferByte) img2.getRaster().getDataBuffer()).getData();
		mat1.put(0, 0, pixels1);
		
		
		Mat mat2gscale1 = new Mat(img2x,img2y,CvType.CV_8UC1);
		Mat mat1gscale1 = new Mat(img1x,img1y,CvType.CV_8UC1);
		
		Imgproc.cvtColor(mat1, mat1gscale, Imgproc.COLOR_RGB2GRAY);
		Imgproc.cvtColor(mat2, mat2gscale, Imgproc.COLOR_RGB2GRAY);
		
		Imgproc.threshold(mat1gscale, mat1gscale1, 127, 255, Imgproc.THRESH_TOZERO);
		Imgproc.threshold(mat2gscale, mat2gscale1, 127, 255, Imgproc.THRESH_TOZERO);
		
		List<MatOfPoint> contours1 = new ArrayList<MatOfPoint>();
		Imgproc.findContours(mat1gscale1, contours1, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
		
		List<MatOfPoint> contours2 = new ArrayList<MatOfPoint>();
		Imgproc.findContours(mat2gscale1, contours2, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
			
		//debug only
		BufferedImage gray = new BufferedImage(img2x,img2y,BufferedImage.TYPE_BYTE_GRAY);
		byte[] data = ((DataBufferByte) gray.getRaster().getDataBuffer()).getData();
		mat1gscale1.get(0, 0, data);
		ImageIO.write(gray, "png", new File(Main.path + "current"+doing+".png"));
		Mat image_contour = Mat.zeros(img2x, img2y, CvType.CV_8UC3);
		for(int i=0; i < contours1.size(); i++) Imgproc.drawContours(image_contour, contours1,i,new Scalar (255,255,255),-1);
		Imgcodecs.imwrite(Main.path + "contours-" + doing + ".jpg",image_contour);
		doing++;	
		//debug only
		
		double match = 0d;
		if (contours1.size() == 0) return (-1d); //pas de contours trouvé donc probablement vide
		 match = Imgproc.matchShapes(getBiggestContour(contours1),getBiggestContour(contours2),Imgproc.CV_CONTOURS_MATCH_I1,0d);
		return match;
		
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
	
	public static Piece.nomPiece findPiece(Rectangle rectangle,boolean couleurCase,int decay) throws AWTException, IOException
	{
		BufferedImage lookingAt = traitementContour(new Robot().createScreenCapture(rectangle),couleurCase);
		ImageIO.write(lookingAt, "png", new File(Main.path + "looking"+".png"));
		
		double min = 10d;
		int minp=0;
		double match;
			for(int p=0; p < 6; p++)
			{
				BufferedImage dataPiece = Piece.dataPiece[p]; 
				match = matchContours(lookingAt,dataPiece);
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
	}
	
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
			BufferedImage lookingAt = traitementContour(new Robot().createScreenCapture(r),c.getColor());
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
		
		int minxv = 6000;
		int maxyv = 0;
		int minxb = 6000;
		int minyb = 6000;
		
		for(int i=0; i < screen.getWidth(); i++)
		{
			for(int j=0; j < screen.getHeight(); j++)
			{
				Color pixel = new Color(screen.getRGB(i,j));
				if (pixel.equals(vert))
				{
					if (i < minxv || j > maxyv) 
					{
						minxv = i;
						maxyv = j;
					}
				}
				else if (pixel.equals(blanc))
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
	
/*	public static void checkImage(Rectangle rectangle) throws AWTException, IOException
	{
		BufferedImage img,img2;
		int sizecase = (int)rectangle.getWidth();
		adjustRectangle(rectangle,sizecase);
		
		img = toBufferedImageOfType(new Robot().createScreenCapture(rectangle),BufferedImage.TYPE_3BYTE_BGR);
		img2 = toBufferedImageOfType(ImageIO.read(new File("C:\\Users\\Seven\\workspace\\chessBot\\data\\black-rook.png")),BufferedImage.TYPE_3BYTE_BGR);
		
		Mat mat = new Mat((int)rectangle.getWidth(),(int)rectangle.getWidth(),CvType.CV_8UC3);
		byte[] pixels = ((DataBufferByte) img.getRaster().getDataBuffer()).getData();
		mat.put(0, 0, pixels);
		Mat matgscale = mat.clone();
		
		Mat mat1 = new Mat((int)rectangle.getWidth(),(int)rectangle.getWidth(),CvType.CV_8UC3);
		byte[] pixels1 = ((DataBufferByte) img2.getRaster().getDataBuffer()).getData();
		mat1.put(0, 0, pixels1);
		Mat mat1gscale = mat1.clone();
		
		Imgproc.cvtColor(mat, matgscale, Imgproc.COLOR_RGB2GRAY);
		Imgproc.cvtColor(mat1, mat1gscale, Imgproc.COLOR_RGB2GRAY);
		Imgproc.threshold(matgscale, mat, 127, 255, Imgproc.THRESH_TOZERO);
		Imgproc.threshold(mat1gscale, mat1, 127, 255, Imgproc.THRESH_TOZERO);
		
		List<MatOfPoint> contours1 = new ArrayList<MatOfPoint>();
		Imgproc.findContours(mat, contours1, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
		
		List<MatOfPoint> contours2 = new ArrayList<MatOfPoint>();
		Imgproc.findContours(mat1, contours2, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
			
		double match = Imgproc.matchShapes((Mat)contours1.get(0),(Mat)contours2.get(0),Imgproc.CV_CONTOURS_MATCH_I1,0d);
		System.out.println(match);
		
		
	}*/
	
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
