package classifier;
import java.io.PrintStream;
import java.util.List;
import org.opencv.core.Mat;
import ocr_main.Std;

public class Classifier {
	private double[][] weights;
	private double learnRate = .05;
	private List<Mat[]> templates;
	/** templates should have one row for each possible character;
	 * each row should contain templates for associated character */
	public Classifier(List<Mat[]> templates){
		this.templates = templates;
		weights = new double[templates.size()][templates.get(0).length];
		for(int i = 0;i < weights.length;i++)
			for(int j = 0;j < weights[0].length;j++)
				weights[i][j] = 1.0 / weights[0].length;
	}
	/** compares similarity of templates to training samples, weights more similar template more;
	 * assumes that each Mat[] is in ascending order */
	public void train(List<Mat[]> trainSets){
		for(int i = 0;i < trainSets.size();i++){
			Mat[] trainSet = trainSets.get(i);
			for(int j = 0;j < trainSet.length;j++){
				int minDiffIndex = indexOfSimilarTemp(templates.get(i), trainSet[j]);
				updateWeights(i, minDiffIndex);
			}
		}
	}
	/** @return index of most similar template */
	private int indexOfSimilarTemp(Mat[] charTemps, Mat trainPt){
		int minDiffIndex = 0;
		int minDiff = difference(charTemps[0], trainPt);
		for(int i = 1;i < charTemps.length;i++){
			int diff = difference(charTemps[i], trainPt);
			if(diff < minDiff){
				minDiffIndex = i;
				minDiff = diff;
			}
		}
		return minDiffIndex;
	}
	private void updateWeights(int rowNum, int minDiffIndex){
		weights[rowNum][minDiffIndex] += (weights[0].length - 1)*learnRate;
		for(int i = 0;i < weights[0].length;i++)
			if(i != minDiffIndex)
				weights[rowNum][i] -= learnRate;
	}
	public char[] classify(List<Mat> tests){
		return classify(tests, System.out);
	}
	public char[] classify(List<Mat> tests, PrintStream out){
		char[] ret = new char[tests.size()];
		for(int i = 0;i < tests.size();i++){
			int minIndex = 0;
			double minDiff = weight_diff(0, tests.get(i));
			for(int j = 1;j < templates.size();j++){
				double diff = weight_diff(j, tests.get(i));
				if(diff < minDiff){
					minDiff = diff;
					minIndex = j;
				}
			}
			char clazz = minIndexToChar(minIndex);
			out.println(clazz);
			ret[i] = clazz;
		}
		return ret;
	}
	private char minIndexToChar(int minIndex){
		return (minIndex < 11) ? (char)(minIndex + '0') :
			((minIndex < 37) ? (char)(minIndex - 11 + 'A') : (char)(minIndex - 37 + 'a'));
	}
	public double weight_diff(int index, Mat m){
		double sumDiff = 0;
		for(int t = 0;t < weights[0].length;t++){
			Mat tempM = templates.get(index)[t];
			int diff = difference(tempM, m);
			if (diff == 0) {
				return 0; // exact match!
			}
			sumDiff += weights[index][t]*diff;
		}
		return sumDiff;
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
