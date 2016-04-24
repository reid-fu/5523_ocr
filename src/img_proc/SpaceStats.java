package img_proc;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("unused")
public class SpaceStats {
	public static final int CHAR_SEP = 0;
	public static final int WORD_SEP = 1;
	
	private List<Integer> spaceCounts = new ArrayList<>();
	private int charSepSpace = 0;
	private int wordSepSpace = 0;
	private int threshSepSpace = 0;
	public void addSpace(int space){
		spaceCounts.add(space);
	}
	/** assumes word separation space is much larger than character separation space;
	 * uses 1D k means to find means of separation spaces */
	public void updateSepSpaces(){
		Collections.sort(spaceCounts);
		charSepSpace = spaceCounts.get(0);
		wordSepSpace = spaceCounts.get(spaceCounts.size()-1);
		int prevCharSep = -1, prevWordSep = -1;
		List<Integer> charSepCluster = new ArrayList<>();
		List<Integer> wordSepCluster = new ArrayList<>();
		
		boolean done = false;
		while(charSepSpace != prevCharSep || wordSepSpace != prevWordSep){
			this.assignCluster(charSepCluster, wordSepCluster);
			prevCharSep = charSepSpace;
			prevWordSep = wordSepSpace;
			charSepSpace = this.averageOf(charSepCluster);
			wordSepSpace = this.averageOf(wordSepCluster);
		}
		threshSepSpace = (charSepSpace + wordSepSpace) / 2;
	}
	/** uses 5 arguments: centroids, cluster containers, and data;
	 * 3 of these arguments are instance variables */
	private void assignCluster(List<Integer> charSepCluster, List<Integer> wordSepCluster){
		for(int i = 0;i < spaceCounts.size();i++){
			int el = spaceCounts.get(i);
			if(Math.abs(charSepSpace - el) < Math.abs(wordSepSpace - el)){
				charSepCluster.add(el);
			} else {
				wordSepCluster.add(el);
			}
		}
	}
	private int averageOf(List<Integer> list){
		int sum = 0;
		for(Integer i : list)
			sum += i;
		return sum / list.size();
	}
	public int spaceCat(int space){
		return (space > threshSepSpace) ? WORD_SEP : CHAR_SEP;
	}
}
