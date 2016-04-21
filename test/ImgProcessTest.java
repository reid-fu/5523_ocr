import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import img_proc.*;

public class ImgProcessTest {
	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		ImgProcessor processor = new ImgProcessor();
		Mat m = Imgcodecs.imread("img/digits_arial.png");
		ImgDecomp decomp = processor.processImg(m);
		System.out.println(decomp);
	}
}
