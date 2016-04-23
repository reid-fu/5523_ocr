package img_proc;
import java.util.*;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import img_proc.ImgDecomp.*;
import ocr_main.Std;
import util.*;

/** Responsible for making image black and white, as well as segmenting characters */
public class ImgProcessor {
	public ImgDecomp processImg(Mat m){
		// TODO: debug!
//		double skewAngle = ImgProcessor.compute_skew(m);
//		Mat unskewed = ImgProcessor.deskew(m, skewAngle);
		List<Rect> rects = boundRects(m);
		Map<Range,List<Rect>> lines = separateLines(rects);
		SpaceStats spaceStats = processSpaces(lines.get(lines.keySet().iterator().next()));
//		int i = 0; //FOR DEBUGGING PURPOSES
//		for(Rect r : lines.get(lines.keySet().iterator().next())){
//			Imgcodecs.imwrite("debug_img/" + i + ".png", m.submat(r));
//			i++;
//		}
		// TODO: Hough
		return this.buildDecomp(lines, spaceStats);
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
	/** each line will have chars listed left to right */
	public Map<Range,List<Rect>> separateLines(List<Rect> rects){
		Map<Range,List<Rect>> lines = new TreeMap<>(CompareUtil.rangeComparator());
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
		lines.replaceAll((k,v) -> { v.sort(CompareUtil.rectComparator()); return v; });
		return lines;
	}
	/** assumes rects is in order of left to right;
	 * combines rects if necessary (e.g. dot and body of i) */
	public SpaceStats processSpaces(List<Rect> rects){
		SpaceStats stats = new SpaceStats();
		for(int i = 0;i < rects.size()-1;i++){
			Rect r1 = rects.get(i), r2 = rects.get(i+1);
			int space = r2.x - (r1.x + r1.width);
			if(space < 0){ //characters overlap horizontally --> combine
				int left = Math.min(r1.x, r2.x), top = Math.min(r1.y, r2.y);
				int right = Math.max(r1.x + r1.width, r2.x + r2.width);
				int bottom = Math.max(r1.y + r1.height, r2.y + r2.height);
				Rect r3 = new Rect(left, top, right - left, bottom - top);
				rects.remove(i+1); rects.remove(i);
				rects.add(i, r3);
				i--;
			} else {
				stats.addSpace(space);
			}
		}
		stats.updateSepSpaces(); //TODO implement this method
		return stats;
	}
	public ImgDecomp buildDecomp(Map<Range,List<Rect>> lines, SpaceStats spaceStats){
		ImgDecomp decomp = new ImgDecomp();
		for(Range range : lines.keySet()){
			Line l = decomp.new Line();
			List<Rect> line = lines.get(range);
			Word word = decomp.new Word();
			word.chars.add(line.get(0));
			for(int i = 1;i < line.size()-1;i++){
				Rect r1 = word.lastChar(), r2 = line.get(i);
				int space = r2.x - (r1.x + r1.width);
				if(spaceStats.spaceCat(space) == SpaceStats.WORD_SEP){
					l.words.add(word);
					word = decomp.new Word();
				}
				word.chars.add(r2);
			}
		}
		return decomp;
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
	private int countNonZero(Mat m){
		int count = 0;
		for(int i = 0;i < m.rows();i++)
			for(int j = 0;j < m.cols();j++)
				if(m.get(i,j)[0] > Std.STD_THRESH)
					count++;
		return count;
	}
}
