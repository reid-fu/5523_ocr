package util;
import java.util.List;
import org.opencv.core.Mat;
import ocr_main.Std;

public class Classifier {
	private double[] weights;
	private List<Mat[]> templates;
	public Classifier(int numFonts){
		weights = new double[numFonts];
		for(int i = 0;i < numFonts;i++)
			weights[i] = 1.0 / numFonts;
	}
	public void classify(Mat[] tests){
		for(int i = 0;i < tests.length;i++){
			int minIndex = 0;
			double minDiff = Double.POSITIVE_INFINITY;
			for(int j = 0;j < templates.get(0).length;j++){
				double diff = weight_diff(j, tests[i]);
				if(diff < minDiff){
					minDiff = diff;
					minIndex = j;
				}
			}
			System.out.println(minIndex);
		}
	}
	public double weight_diff(int index, Mat m){
		double diff = 0;
		for(int i = 0;i < weights.length;i++){
			Mat tempM = templates.get(i)[index];
			diff += weights[i]*difference(tempM, m);
		}
		return diff;
	}
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
