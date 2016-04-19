package ocr_main;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import util.*;

public class OCR_Main {
	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		Mat[] templates = DigitTemplates.digitTemplates(false,true).get(0);
		Mat[] tests = testSet(false);
		classify(templates, tests);
	}
	public static Mat[] testSet(boolean output){
		Mat img = Imgcodecs.imread("img/digits_test.png");
		Preprocessor p = new Preprocessor();
		Mat set[] = new Mat[10];
		for(int i = 0;i < set.length;i++){
			int colStart = img.cols() * i/10;
			int colEnd = img.cols() * (i+1)/10 - 1;
			set[i] = p.boundingRectChar(img.submat(0, img.rows()-1, colStart, colEnd));
			Imgproc.resize(set[i], set[i], new Size(Std.STD_WIDTH, Std.STD_HEIGHT));
			if(output)
				Imgcodecs.imwrite("img/" + i + "test.png", set[i]);
		}
		return set;
	}
	public static void classify(Mat[] templates, Mat[] tests){
		Classifier c = new Classifier(1);
		for(int i = 0;i < tests.length;i++){
			int minIndex = 0, minDiff = Integer.MAX_VALUE;
			for(int j = 0;j < templates.length;j++){
				int diff = c.difference(templates[j], tests[i]);
				if(diff < minDiff){
					minDiff = diff;
					minIndex = j;
				}
			}
			System.out.println(minIndex);
		}
	}
}
