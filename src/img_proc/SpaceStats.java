package img_proc;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
public class SpaceStats {
	public static final int CHAR_SEP = 0;
	public static final int WORD_SEP = 1;
	
	private Map<Integer,Integer> spaceCounts = new HashMap<>();
	private int charSepSpace;
	private int charSepTolerance;
	private int wordSepSpace;
	private int wordSepTolerance;
	public void addSpace(int space){
		if(spaceCounts.containsKey(space)){
			spaceCounts.put(space, spaceCounts.remove(space) + 1);
		} else {
			spaceCounts.put(space, 1);
		}
	}
	public void updateSepSpaces(){
		
	}
	public int spaceCat(int space){
		return -1;
	}
}
