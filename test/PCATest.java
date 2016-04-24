import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import util.Chars74Util;

public class PCATest {
	public static void main(String[] args){
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
//		Preprocessor preproc = new Preprocessor();
		Mat img = Chars74Util.getImg('0', 1);
		Imgproc.resize(img, img, new Size(28,28));
//		Rect crop = preproc.boundRects(img).get(0);
//		Mat cropImg = img.submat(crop);
		Mat cov = new Mat(img.rows(), img.cols(), img.type());
		Core.gemm(img.t(), img.t(), 1, Mat.zeros(img.rows(), img.cols(), img.type()), 0, cov);
		Mat eigenvalues = new Mat(img.rows(), img.cols(), img.type());
		Mat eigenvectors = new Mat(img.rows(), img.cols(), img.type());
		Core.eigen(cov, eigenvalues, eigenvectors);
	}
}
