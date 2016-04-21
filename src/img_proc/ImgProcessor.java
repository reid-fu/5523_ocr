package img_proc;
import java.util.ArrayList;
import java.util.List;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

import ocr_main.Std;

/** Responsible for making image black and white, as well as segmenting characters */
@SuppressWarnings("unused")
public class ImgProcessor {
	public ImgDecomp processImg(Mat m){
		//call boundRect(Mat)
		//get lines based on vertical ranges of bounding rectangles, assign chars to lines 
		//get stats for spacing between chars 
		//separate chars into words based on longer spacings 
		return null;
	}
	public List<Rect> boundRects(Mat m){
		List<Rect> rects = new ArrayList<>();
		for(MatOfPoint mat_pt : contours(m))
			rects.add(Imgproc.boundingRect(mat_pt));
		return rects;
	}
	public List<MatOfPoint> contours(Mat m){
		Mat m2 = new Mat(m.rows(), m.cols(), m.type());
		Imgproc.threshold(m, m2, 0, 255, Imgproc.THRESH_BINARY_INV + Imgproc.THRESH_OTSU);
		m2.convertTo(m2, CvType.CV_8UC1);
		List<MatOfPoint> contours = new ArrayList<>();
		Imgproc.findContours(m2, contours, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_NONE);
		return contours;
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
