package util;
import java.util.Comparator;
import org.opencv.core.Range;
import org.opencv.core.Rect;

public class CompareUtil {
	public static Comparator<Range> rangeComparator(){
		return new Comparator<Range>(){
			@Override
			public int compare(Range arg0, Range arg1) {
				return arg0.start - arg1.start;
			}
		};
	}
	public static Comparator<Rect> rectComparator(){
		return new Comparator<Rect>(){
			@Override
			public int compare(Rect arg0, Rect arg1) {
				return arg0.x - arg1.x;
			}
		};
	}
}
