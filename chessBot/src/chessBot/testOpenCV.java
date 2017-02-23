package chessBot;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class testOpenCV {
	
	static{ System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }
	
	public static void main(String[] args)
	{
		Mat image = Imgcodecs.imread(Main.path + "current1.png");
		if(image.empty() == true) {
		    System.out.println("Error: no image found!");
		}

		List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		Mat image32S = new Mat(image.rows(),image.cols(),CvType.CV_8UC1);
		Imgproc.cvtColor(image, image32S, Imgproc.COLOR_RGB2GRAY);

		Imgproc.findContours(image32S, contours, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);

		// Draw all the contours such that they are filled in.
		Mat contourImg = new Mat(image32S.size(), image32S.type());
		for (int i = 0; i < contours.size(); i++) {
		    Imgproc.drawContours(contourImg, contours, i, new Scalar(255, 255, 255), -1);
		}

		Imgcodecs.imwrite(Main.path+"debug_image.jpg", contourImg); // DEBUG
	}

}
