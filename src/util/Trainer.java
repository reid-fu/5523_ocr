package util;
import java.util.ArrayList;
import java.util.List;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import img_proc.ImgProcessor;
import ocr_main.Std;

public class Trainer {
	public static List<Mat[]> digitTrainSets(boolean output, String...fonts){
		List<Mat[]> sets = new ArrayList<>();
		for(int i = 0;i < fonts.length;i++)
			sets.add(charsInFont(output, fonts[i]));
		return sets;
	}
	private static Mat[] charsInFont(boolean output, String font){
		Mat img1 = Imgcodecs.imread("img/digits_" + font + ".png");
		ImgProcessor p = new ImgProcessor();
		Mat calibri[] = new Mat[10];
		for(int i = 0;i < 2;i++){
			for(int j = 0;j < 5;j++){
				int rowStart = (i == 0) ? 0 : img1.rows()/2;
				int rowEnd = (i == 0) ? img1.rows()/2 - 1 : img1.rows()-1;
				int colStart = img1.cols() * j/5;
				int colEnd = img1.cols() * (j+1)/5 - 1;
				calibri[i*5 + j] = p.boundingRectChar(img1.submat(rowStart, rowEnd, colStart, colEnd));
				Imgproc.resize(calibri[i*5 + j], calibri[i*5 + j], new Size(Std.STD_WIDTH, Std.STD_HEIGHT));
				if(output)
					Imgcodecs.imwrite("debug_img/" + (i*5 + j) + font + ".png", calibri[i*5 + j]);
			}
		}
		return calibri;
	}
}
