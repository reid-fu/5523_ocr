package util;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import img_proc.Preprocessor;
import ocr_main.Std;

public class Chars74Util {
	public static Mat getImg(char c, int number){
		String folder = "img/" + (Character.isLowerCase(c) ? c + "_low" : c) + "/";
		int dir_num = chars74_DirNum(c);
		String file = folder + "img" + String.format("%03d", dir_num) + "-" + String.format("%05d", number) + ".png";
		Mat img = Imgcodecs.imread(file);
		return img;
	}
	public static Mat getStdImg(char c, int number){
		Mat img = getImg(c, number);
		Preprocessor preproc = new Preprocessor();
		Rect crop = preproc.boundRects(img).get(0);
		Mat crop_img = img.submat(crop);
		Imgproc.resize(crop_img, crop_img, new Size(Std.STD_WIDTH, Std.STD_HEIGHT));
		return crop_img;
	}
	public static int chars74_DirNum(char c){
		return Character.isDigit(c) ? c - '0' + 1 :
			(Character.isUpperCase(c) ? c - 'A' + 11 : c - 'a' + 37);
	}
}
