package ocr_main;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import classifier.Classifier;
import classifier.DigitTemplates;
import classifier.Trainer;
import img_proc.*;
import util.*;

public class OCR_Main {
	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		List<Mat[]> templates = DigitTemplates.digitTemplates(false, "calibri", "times");
		List<Mat[]> trainSets = Trainer.digitTrainSets(false, "arial", "consolas", "verdana");
		Classifier c = new Classifier(2, templates);
		c.train(trainSets);
		Mat[] tests = DigitTemplates.charsInFont(false, "courier");
		String fileName = new SimpleDateFormat("yyyyMMddhhmm'.txt'").format(new Date());
		File fileOut = new File(fileName);
		PrintStream out;
		try {
			out = new PrintStream(fileOut);
			c.classify(tests, out);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		String[] actualAngles = {"16", "24", "3", "-20", "-8"};
		for (String actualAngle : actualAngles) {
			Mat img = Imgcodecs.imread("img/skewed"+actualAngle+".jpg", Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE);
			//double angle = ImgProcessor.compute_skew("img/skewed"+angle+".jpg", "img/final_skewed"+angle+".jpg");
			double calcdAngle = SkewProcessor.compute_skew(img);
			// TODO: debug!
			Mat unskewed = SkewProcessor.deskew(img, calcdAngle);
			Imgcodecs.imwrite("img/unskewed"+actualAngle+".jpg", unskewed);
		}
	}
	public static Mat[] testSet(boolean output){
		Mat img = Imgcodecs.imread("img/digits_test.png");
		ImgProcessor p = new ImgProcessor();
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
