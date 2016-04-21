package img_proc;
import java.util.*;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import ocr_main.Std;
import util.RangeUtil;

/** Responsible for making image black and white, as well as segmenting characters */
@SuppressWarnings("unused")
public class ImgProcessor {
	public ImgDecomp processImg(Mat m){
		// TODO: debug!
//		double skewAngle = ImgProcessor.compute_skew(m);
//		Mat unskewed = ImgProcessor.deskew(m, skewAngle);
		
		List<Rect> rects = boundRects(m);
		Map<Range,List<Rect>> lines = separateLines(rects);
		int[] spaceBetween = this.spaceBetween(lines.get(lines.keySet().iterator().next()));
		// TODO: Hough
		return this.buildDecomp(lines, spaceBetween);
	}
	
	public List<Rect> boundRects(Mat m){
		List<Rect> rects = new ArrayList<>();
		for(MatOfPoint mat_pt : contours(m))
			rects.add(Imgproc.boundingRect(mat_pt));
		return rects;
	}
	public List<MatOfPoint> contours(Mat m){
		Mat m2 = new Mat(m.rows(), m.cols(), m.type());
		if(m.channels() > 1)
			Imgproc.cvtColor(m, m, Imgproc.COLOR_RGB2GRAY);
		Imgproc.threshold(m, m2, 0, 255, Imgproc.THRESH_BINARY_INV + Imgproc.THRESH_OTSU);
		m2.convertTo(m2, CvType.CV_8UC1);
		List<MatOfPoint> contours = new ArrayList<>();
		Imgproc.findContours(m2, contours, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_NONE);
		return contours;
	}
	
	public Map<Range,List<Rect>> separateLines(List<Rect> rects){
		Map<Range,List<Rect>> lines = new TreeMap<>(new Comparator<Range>(){
			@Override
			public int compare(Range arg0, Range arg1) {
				return arg0.start - arg1.start;
			}
		});
		for(Rect rect : rects){
			Range r = new Range(rect.y, rect.y + rect.height);
			Range r2 = RangeUtil.overlapRangeInSet(r, lines.keySet());
			if(r2 == null){
				List<Rect> list = new ArrayList<Rect>();
				list.add(rect);
				lines.put(r, list);
			} else {
				Range ru = RangeUtil.rangeUnion(r, r2);
				List<Rect> list = lines.remove(r2);
				list.add(rect);
				lines.put(ru, list);
			}
		}
		return lines;
	}
	public int[] spaceBetween(List<Rect> rects){
		int[] spaceBetween = new int[]{-1, -1};
//		Map
//		for()
		return spaceBetween;
	}
	public ImgDecomp buildDecomp(Map<Range,List<Rect>> lines, int[] spaceBetween){
		return null;
	}
	
	//TODO obsolete
	/** @return bounding rectangle matrix of character */
	public Mat boundingRectChar(Mat m){
		Range rowRange = rowRangeBlack(m);
		Range colRange = colRangeBlack(m);
		return m.submat(rowRange, colRange);
	}
	public Range rowRangeBlack(Mat m){
		int row = 0, rowStart = 0, rowEnd = 0;
		while(row < m.rows() && m.cols() - countNonZero(m.row(row)) == 0)
			row++;
		if(row == m.rows()){
			return null;
		} else {
			rowStart = row;
		}
		while(row < m.rows() && m.cols() - countNonZero(m.row(row)) != 0)
			row++;
		rowEnd = (row == m.rows()) ? row : row+1;
		return new Range(rowStart, rowEnd);
	}
	public Range colRangeBlack(Mat m){
		int col = 0, colStart = 0, colEnd = 0;
		while(col < m.cols() && m.rows() - countNonZero(m.col(col))==0)
			col++;
		if(col == m.cols()){
			return null;
		} else {
			colStart = col;
		}
		while(col < m.cols() && m.rows() - countNonZero(m.col(col))!=0)
			col++;
		colEnd = (col == m.cols()) ? col : col+1;
		return new Range(colStart, colEnd);
	}
	/* HELPER METHODS */
	private List<Range> blackRowRanges(Mat m){
		List<Range> ranges = new ArrayList<>();
		int rowStart = -1;
		for(int i = 0;i < m.rows();i++)
			if(rowStart != -1 && m.cols()-countNonZero(m.row(i)) == 0){
				ranges.add(new Range(rowStart,i));
				rowStart = -1;
			} else if(rowStart == -1 && m.cols()-countNonZero(m.row(i)) != 0) {
				rowStart = i;
			}
		return ranges;
	}
	private List<Range> blackColRanges(Mat m){
		return null;
	}
	private int countNonZero(Mat m){
		int count = 0;
		for(int i = 0;i < m.rows();i++)
			for(int j = 0;j < m.cols();j++)
				if(m.get(i,j)[0] > Std.STD_THRESH)
					count++;
		return count;
	}
}
