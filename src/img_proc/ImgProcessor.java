package img_proc;
import java.util.*;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

import img_proc.ImgDecomp.*;
import ocr_main.Std;
import util.*;

/** Responsible for making image black and white, as well as segmenting characters */
public class ImgProcessor {
	public static ImgDecomp processImg(Mat m){
		// TODO: debug!
//		double skewAngle = ImgProcessor.compute_skew(m);
//		Mat unskewed = ImgProcessor.deskew(m, skewAngle);
		//Preprocessor preproc = new Preprocessor();
		List<Rect> rects = Preprocessor.boundRects(m);
		Map<Range,List<Rect>> lines = separateLines(rects);
		SpaceStats spaceStats = processSpaces(lines.get(lines.keySet().iterator().next()));
//		int i = 0; //FOR DEBUGGING PURPOSES
//		for(Rect r : lines.get(lines.keySet().iterator().next())){
//			Imgcodecs.imwrite("debug_img/" + i + ".png", m.submat(r));
//			i++;
//		}
		// TODO: Hough
		return buildDecomp(m, lines, spaceStats);
	}
	/** each line will have chars listed left to right */
	public static Map<Range,List<Rect>> separateLines(List<Rect> rects){
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
	public static SpaceStats processSpaces(List<Rect> rects){
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
		stats.updateSepSpaces();
		return stats;
	}
	public static ImgDecomp buildDecomp(Mat origImg, Map<Range,List<Rect>> lines, SpaceStats spaceStats){
		ImgDecomp decomp = new ImgDecomp(origImg);
		for(Range range : lines.keySet()){
			Line l = decomp.new Line();
			List<Rect> line = lines.get(range);
			Word word = decomp.new Word();
			word.chars.add(line.get(0));
			for(int i = 1;i < line.size();i++){
				Rect r1 = word.lastChar(), r2 = line.get(i);
				int space = r2.x - (r1.x + r1.width);
				if(spaceStats.spaceCat(space) == SpaceStats.WORD_SEP){
					l.words.add(word);
					word = decomp.new Word();
				}
				word.chars.add(r2);
				if(i == line.size()-1)
					l.words.add(word);
			}
		decomp.lines.add(l);
		}
		return decomp;
	}
	public static Mat standardizedImg(Mat img){
		Mat stdImg = new Mat(Std.STD_WIDTH, Std.STD_HEIGHT, img.type());
		Imgproc.resize(img, stdImg, new Size(Std.STD_WIDTH, Std.STD_HEIGHT));
		return stdImg;
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
