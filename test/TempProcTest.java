import java.util.List;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;

import classifier.TemplateProcessor;
import util.Chars74Util;

public class TempProcTest {
	public static void main(String[] args){
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		List<Mat[]> temps = TemplateProcessor.templates();
		char c = 'a';
		Mat[] char_temps = temps.get(Chars74Util.chars74_DirNum(c) - 1);
		for(int i = 0;i < char_temps.length;i++)
			Imgcodecs.imwrite("debug_img/tempProcTest" + i + ".png", char_temps[i]);
	}
}
