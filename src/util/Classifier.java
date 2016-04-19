package util;
import java.util.List;
import org.opencv.core.Mat;
import ocr_main.Std;

public class Classifier {
	private double[][] weights;
	private double learningRate = .05;
	private List<Mat[]> templates;
	public Classifier(int numFonts, List<Mat[]> templates){
		this.templates = templates;
		weights = new double[numFonts][10];
		for(int i = 0;i < numFonts;i++)
			for(int j = 0;j < weights[0].length;j++)
				weights[i][j] = 1.0 / numFonts;
	}
	/** compares similarity of templates to training samples, weights more similar template more;
	 * assumes that each Mat[] is in ascending order */
	public void train(List<Mat[]> trains){
		//TODO currently only works for two template sets
		for(int i = 0;i < trains.size();i++){
			Mat[] train_set = trains.get(i);
			for(int j = 0;j < train_set.length;j++){
				int diff1 = difference(templates.get(0)[j], train_set[j]);
				int diff2 = difference(templates.get(1)[j], train_set[j]);
				if(diff1 < diff2){
					weights[0][j] += learningRate;
					weights[1][j] -= learningRate;
				} else if(diff1 > diff2){
					weights[1][j] += learningRate;
					weights[0][j] -= learningRate;
				}
			}
		}
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
			diff += weights[i][index]*difference(tempM, m);
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
