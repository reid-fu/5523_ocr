import java.util.ArrayList;
import java.util.List;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class ContourTest {
	public static void main(String[] args){
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		Mat img = Imgcodecs.imread("img/0/img001-00001.png", Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE);
		Imgproc.threshold(img, img, 0, 255, Imgproc.THRESH_BINARY_INV + Imgproc.THRESH_OTSU);
		img.convertTo(img, CvType.CV_8UC1);
		List<MatOfPoint> contours = new ArrayList<>();
		Imgproc.findContours(img, contours, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_NONE);
		for(int i = 0;i < contours.size();i++){
			Rect rect = Imgproc.boundingRect(contours.get(i));
			System.out.println(rect);
		}
	}
}
