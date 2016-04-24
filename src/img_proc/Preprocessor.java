package img_proc;
import java.util.ArrayList;
import java.util.List;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

public class Preprocessor {
	public List<Rect> boundRects(Mat m){
		List<Rect> rects = new ArrayList<>();
		for(MatOfPoint mat_pt : contours(m))
			rects.add(Imgproc.boundingRect(mat_pt));
		return rects;
	}
	public List<MatOfPoint> contours(Mat m){
		Mat m2 = new Mat(m.rows(), m.cols(), m.type());
		if(m.channels() > 1)
			Imgproc.cvtColor(m, m, Imgproc.COLOR_RGB2GRAY);
		Imgproc.threshold(m, m2, 0, 255, Imgproc.THRESH_BINARY_INV + Imgproc.THRESH_OTSU);
		m2.convertTo(m2, CvType.CV_8UC1);
		List<MatOfPoint> contours = new ArrayList<>();
		Imgproc.findContours(m2, contours, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_NONE);
		return contours;
	}
}
