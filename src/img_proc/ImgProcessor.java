package img_proc;
import java.util.ArrayList;
import java.util.List;
import org.opencv.core.Mat;
import org.opencv.core.Range;
import ocr_main.Std;

/** Responsible for making image black and white, as well as segmenting characters */
@SuppressWarnings("unused")
public class ImgProcessor {
	public ImgDecomp processImg(Mat m){
		return null;
	}
	
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
