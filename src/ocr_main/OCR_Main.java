package ocr_main;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import util.Preprocessor;

public class OCR_Main {
	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		Mat[] train = trainSet();
	}
	public static Mat[] trainSet(){
		Mat img = Imgcodecs.imread("img/digits_train.png");
		Preprocessor p = new Preprocessor();
		Mat set[] = new Mat[10];
		for(int i = 0;i < 2;i++){
			for(int j = 0;j < 5;j++){
				int rowStart = (i == 0) ? 0 : img.rows()/2;
				int rowEnd = (i == 0) ? img.rows()/2 - 1 : img.rows()-1;
				int colStart = img.cols() * j/5;
				int colEnd = img.cols() * (j+1)/5 - 1;
				set[i*5 + j] = p.boundingRectChar(img.submat(rowStart, rowEnd, colStart, colEnd));
				Imgcodecs.imwrite("img/" + (i*5 + j) + ".png", set[i*5 + j]);
			}
		}
		return set;
	}
	public static Mat[] testSet(){
		Mat img = Imgcodecs.imread("img/digits_test.png");
		Mat set[] = new Mat[10];
		for(int i = 0;i < set.length;i++){
			int colStart = img.cols() * i/10;
			int colEnd = img.cols() * (i+1)/10 - 1;
			set[i] = img.submat(0, img.rows()-1, colStart, colEnd);
		}
		return set;
	}
}
