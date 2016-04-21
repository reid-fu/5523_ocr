package ocr_main;
import java.util.List;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import util.*;

public class OCR_Main {
	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		List<Mat[]> templates = DigitTemplates.digitTemplates(false, "calibri", "times");
		List<Mat[]> trainSets = Trainer.digitTrainSets(false, "arial", "consolas", "verdana");
		Classifier c = new Classifier(2, templates);
		c.train(trainSets);
		Mat[] tests = DigitTemplates.charsInFont(false, "courier");
		c.classify(tests);
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
				Imgcodecs.imwrite("debug_img/" + i + "test.png", set[i]);
		}
		return set;
	}
}
