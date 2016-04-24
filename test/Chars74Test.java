import org.opencv.core.Core;
import org.opencv.core.Mat;
import util.Chars74Util;

public class Chars74Test {
	public static void main(String[] args){
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		Mat img = Chars74Util.getImg('f', 15);
		System.out.println(img);
	}
}
