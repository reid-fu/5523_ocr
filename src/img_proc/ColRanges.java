package img_proc;
import java.util.ArrayList;
import java.util.List;
import org.opencv.core.Range;

public class ColRanges {
	private List<Range> blackRanges = new ArrayList<>();
	private List<Range> whiteRanges = new ArrayList<>();
	public void addBlackRange(Range r){
		blackRanges.add(r);
	}
	public void addWhiteRange(Range r){
		whiteRanges.add(r);
	}
}
