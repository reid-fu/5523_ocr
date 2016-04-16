package util;
import org.opencv.core.Mat;
import ocr_main.Std;

public class Classifier {
	/** assumes m1 and m2 are same size */
	public int difference(Mat m1, Mat m2){
		int diff = 0;
		for(int i = 0;i < m1.rows();i++)
			for(int j = 0;j < m1.cols();j++){
				double m1_e = m1.get(i,j)[0];
				double m2_e = m2.get(i,j)[0];
				int thresh = Std.STD_THRESH;
				if((m1_e > thresh && m2_e <= thresh) || (m1_e <= thresh && m2_e > thresh))
					diff++;
			}
		return diff;
	}
}
