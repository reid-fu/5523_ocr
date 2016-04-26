package img_proc;
import java.util.ArrayList;
import java.util.List;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class Preprocessor {
	public static List<Rect> boundRects(Mat m){
		List<Rect> rects = new ArrayList<>();
		for(MatOfPoint mat_pt : contours(m))
			rects.add(Imgproc.boundingRect(mat_pt));
		return rects;
	}
	public static List<MatOfPoint> contours(Mat m){
		Mat m2 = new Mat(m.rows(), m.cols(), m.type());
		if(m.channels() > 1)
			Imgproc.cvtColor(m, m, Imgproc.COLOR_RGB2GRAY);
		//Imgproc.threshold(m, m2, 0, 255, Imgproc.THRESH_BINARY + Imgproc.THRESH_OTSU);
		//m2.convertTo(m2, CvType.CV_8UC1);
		
		// TODO: remove! For debugging
		//Imgcodecs.imwrite("binary_8UC1.jpg", m2);
		Imgproc.threshold(m, m2, 0, 255, Imgproc.THRESH_BINARY_INV + Imgproc.THRESH_OTSU);
		m2.convertTo(m2, CvType.CV_8UC1);
		Mat binCopy = m2.clone();
		Imgcodecs.imwrite("binary_inv_8UC1.jpg", m2);
		
		List<MatOfPoint> contours = new ArrayList<>();
		// TODO: if it does not decrease quality, change to Imgproc.CHAIN_APPROX_SIMPLE
		Imgproc.findContours(m2, contours, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_NONE);
		// TODO: remove! For debugging
		Imgproc.drawContours(binCopy, contours, -1, new Scalar(90,255,145)); // new Scalar(0,255,0)
		Imgcodecs.imwrite("contours.jpg", binCopy);
		return contours;
	}
}
