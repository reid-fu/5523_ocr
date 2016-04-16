package util;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

public class OCR_Util {
	public static Mat[] digitTemplates(){
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
			}
		}
		return set;
	}
}
