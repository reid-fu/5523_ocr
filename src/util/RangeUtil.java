package util;
import java.util.Set;
import org.opencv.core.Range;

public class RangeUtil {
	/** @return first Range in set that overlaps with r */
	public static Range overlapRangeInSet(Range r, Set<Range> set){
		for(Range r2 : set)
			if((r.start >= r2.start && r.start < r2.end) || (r2.start >= r.start && r2.start < r.end))
				return r2;
		return null;
	}
	/** @return smallest Range that contains both r1 and r2 */
	public static Range rangeUnion(Range r1, Range r2){
		int start = (r1.start < r2.start) ? r1.start : r2.start;
		int end = (r1.end > r2.end) ? r1.end : r2.end;
		return new Range(start, end);
	}
}
