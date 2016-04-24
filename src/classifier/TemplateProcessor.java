package classifier;
import java.util.ArrayList;
import java.util.List;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import img_proc.ImgProcessor;
import ocr_main.Std;
import util.Chars74Util;

public class TemplateProcessor {
	private static int[] template_nums = new int[]{1, 17, 29};
	public static List<Mat[]> templates(){
		List<Mat[]> templates = new ArrayList<>();
		for(char c = '0';c <= '9';c++){
			loadTemplates(c, templates);
		}
		for(char c = 'A';c <= 'Z';c++){
			loadTemplates(c, templates);
		}
		for(char c = 'a';c <= 'z';c++){
			loadTemplates(c, templates);
		}
		return templates;
	}
	private static void loadTemplates(char c, List<Mat[]> templates){
		Mat[] charTemps = new Mat[template_nums.length];
		for(int i = 0;i < template_nums.length;i++){
			Mat img = Chars74Util.getStdImg(c, template_nums[i]);
			charTemps[i] = img;
		}
		templates.add(charTemps);
	}
	
	//TODO: obsolete
	public static List<Mat[]> digitTemplates(boolean output, String...fonts){
		List<Mat[]> templates = new ArrayList<>();
		for(int i = 0;i < fonts.length;i++)
			templates.add(charsInFont(output, fonts[i]));
		return templates;
	}
	public static Mat[] charsInFont(boolean output, String font){
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
