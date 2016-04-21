package img_proc;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import ocr_main.Std;

/** Responsible for making image black and white, as well as segmenting characters */
@SuppressWarnings("unused")
public class ImgProcessor {
	public ImgDecomp processImg(Mat m){
		// TODO: debug!
		double skewAngle = ImgProcessor.compute_skew(m);
		Mat unskewed = ImgProcessor.deskew(m, skewAngle);
		
		//call boundRect(Mat)
		List<Rect> rects = boundRects(m);
		//get lines based on vertical ranges of bounding rectangles, assign chars to lines
		boolean[] black = new boolean[m.rows()];
		for (Rect rect : rects) {
			// set black[i] to true for each i in bounding box vertically
		}
		//get stats for spacing between chars 
		//separate chars into words based on longer spacings
		// TODO: Hough
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
	
	/*
	void compute_skew(const char* filename)
	{
	  // Load in grayscale.
	  cv::Mat img = cv::imread(filename, 0);
	  // Binarize
	  cv::threshold(img, img, 225, 255, cv::THRESH_BINARY);
	  // Invert colors
	  cv::bitwise_not(img, img);
	  cv::Mat element = cv::getStructuringElement(cv::MORPH_RECT, cv::Size(5, 3));
	  cv::erode(img, img, element);
	  std::vector<cv::Point> points;
	  cv::Mat_<uchar>::iterator it = img.begin<uchar>();
	  cv::Mat_<uchar>::iterator end = img.end<uchar>();
	  for (; it != end; ++it)
	    if (*it)
	      points.push_back(it.pos());
	  cv::RotatedRect box = cv::minAreaRect(cv::Mat(points));
	  double angle = box.angle;
	  if (angle < -45.)
	    angle += 90.;
	  cv::Point2f vertices[4];
	  box.points(vertices);
	  for(int i = 0; i < 4; ++i)
	    cv::line(img, vertices[i], vertices[(i + 1) % 4], cv::Scalar(255, 0, 0), 1, CV_AA);
	 
	  std::cout << "File " << filename << ": " << angle << std::endl;
	  cv::imshow("Result", img);
	  cv::waitKey(0);
	}
	*/
	/*
	public static void compute_skew(String fileIn, String fileOut)
	{
		Mat realImg = Imgcodecs.imread(fileIn);
		// Load in grayscale.
		Mat img = Imgcodecs.imread(fileIn, Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE);
		// Binarize
		Imgproc.threshold(img, img, 225, 255, Imgproc.THRESH_BINARY_INV);
		// Invert colors
		//cv::bitwise_not(img, img);
		Mat element = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(5, 3));
		Imgproc.erode(img, img, element);
		
		//img.get(row, col, data)
		//Iterator<uchar> it = img..begin();
		//Mat_<uchar>::iterator end = img.end<uchar>();
		//for (; it != end; ++it)
		//  if (*it)
		//    points.push_back(it.pos());
		
		List<Point> points = new ArrayList<>();
		for (int row = 0; row < img.rows(); row++) {
		    for (int col = 0; col < img.cols(); col++) {
		    	if (img.get(row, col)[0] < 127) {
		    		points.add(new Point(row, col));
		    	}
		    }
		}
		//System.out.println(realImg.checkVector(2, CvType.CV_32F));
		MatOfPoint2f matPts;
		//matPts = new MatOfPoint2f(img);
		matPts = new MatOfPoint2f();
		

		Mat canny_output = new Mat(), hierarchy = new Mat();
		List<MatOfPoint> contours = new ArrayList<>();
		/// Detect edges using canny
		double thresh = 70.0;
		Imgproc.Canny( img, canny_output, thresh, thresh*2);
		/// Find contours
		Imgproc.findContours( canny_output, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE, new Point(0, 0) );

		for (MatOfPoint contour : contours) {
			matPts = new MatOfPoint2f(contour.toArray());
			//matPts = new MatOfPoint2f(points.toArray(new Point[0]));

			
			RotatedRect box = Imgproc.minAreaRect(matPts);
			double angle = box.angle;
			if (angle < -45.)
				angle += 90.;
			//Point2f vertices = new Point2f[4];
			Point[] vertices = new Point[4];
			box.points(vertices);
			for(int i = 0; i < 4; ++i)
				Imgproc.line(img, vertices[i], vertices[(i + 1) % 4], new Scalar(255, 0, 0), 1, Core.LINE_AA, 0);
			 
			System.out.println("File "+fileIn+": "+angle);
		}
		matPts = new MatOfPoint2f(contours.get(0).toArray());
		//matPts = new MatOfPoint2f(points.toArray(new Point[0]));

		
		RotatedRect box = Imgproc.minAreaRect(matPts);
		double angle = box.angle;
		if (angle < -45.)
			angle += 90.;
		//Point2f vertices = new Point2f[4];
		Point[] vertices = new Point[4];
		box.points(vertices);
		for(int i = 0; i < 4; ++i)
			Imgproc.line(img, vertices[i], vertices[(i + 1) % 4], new Scalar(255, 0, 0), 1, Core.LINE_AA, 0);
		 
		System.out.println("File "+fileIn+": "+angle);
		//cv::imshow("Result", img);
		//cv::waitKey(0);
		Imgcodecs.imwrite(fileOut, img);
	}
	*/
	/*
	void compute_skew(const char* filename)
	{
	   // Load in grayscale.
	   cv::Mat src = cv::imread(filename, 0);
	   cv::Size size = src.size();
	   cv::bitwise_not(src, src);
	   std::vector<cv::Vec4i> lines;
	   cv::HoughLinesP(src, lines, 1, CV_PI/180, 100, size.width / 2.f, 20);
	    cv::Mat disp_lines(size, CV_8UC1, cv::Scalar(0, 0, 0));
	    double angle = 0.;
	    unsigned nb_lines = lines.size();
	    for (unsigned i = 0; i < nb_lines; ++i)
	    {
	        cv::line(disp_lines, cv::Point(lines[i][0], lines[i][1]),
	                 cv::Point(lines[i][2], lines[i][3]), cv::Scalar(255, 0 ,0));
	        angle += atan2((double)lines[i][3] - lines[i][1],
	                       (double)lines[i][2] - lines[i][0]);
	    }
	    angle /= nb_lines; // mean angle, in radians.
	 
	    std::cout << "File " << filename << ": " << angle * 180 / CV_PI << std::endl;
	 
	    cv::imshow(filename, disp_lines);
	    cv::waitKey(0);
	    cv::destroyWindow(filename);
	}
	*/
	public static double compute_skew(String fileIn, String fileOut)
	{
		//Mat realImg = Imgcodecs.imread(fileIn);
		// Load in grayscale.
		Mat img = Imgcodecs.imread(fileIn, Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE);
		Size size = img.size();
		// Binarize
		Imgproc.threshold(img, img, 225, 255, Imgproc.THRESH_BINARY_INV);
		// Invert colors
		//cv::bitwise_not(img, img);
		
		Mat lines = new Mat();
		Imgproc.HoughLinesP(img, lines, 1, Math.PI/180, 100, size.width / 2.f, 20);
		
		Mat disp_lines = new Mat(size, CvType.CV_8UC1, new Scalar(0, 0, 0));
	    double angle = 0.;
	    int nb_lines = lines.rows();
	    for(int i = 0; i<lines.height(); i++){
	        for(int j = 0; j<lines.width();j++){
	            angle += Math.atan2(lines.get(i, j)[3] - lines.get(i, j)[1], lines.get(i, j)[2] - lines.get(i, j)[0]);
	        }
	    }
	    //angle /= nb_lines; // mean angle, in radians.
	    angle /= lines.size().area();
	    //angle = angle * 180 / Math.PI;
		 
		System.out.println("File "+fileIn+": "+angle*180/Math.PI);
		//cv::imshow("Result", img);
		//cv::waitKey(0);
		Imgcodecs.imwrite(fileOut, img);
		
		return angle;
	}
	
	public static double compute_skew(Mat img)
	{
		// TODO: check that img is in greyscale
		Size size = img.size();
		// Binarize
		Imgproc.threshold(img, img, 225, 255, Imgproc.THRESH_BINARY_INV);
		// Invert colors
		//cv::bitwise_not(img, img);
		
		Mat lines = new Mat();
		Imgproc.HoughLinesP(img, lines, 1, Math.PI/180, 100, size.width / 2.f, 20);
		
		Mat disp_lines = new Mat(size, CvType.CV_8UC1, new Scalar(0, 0, 0));
	    double angle = 0.;
	    int nb_lines = lines.rows();
	    for(int i = 0; i<lines.height(); i++){
	        for(int j = 0; j<lines.width();j++){
	            angle += Math.atan2(lines.get(i, j)[3] - lines.get(i, j)[1], lines.get(i, j)[2] - lines.get(i, j)[0]);
	        }
	    }
	    angle /= lines.size().area();
		
		return angle;
	}
	
	/**
	 * http://stackoverflow.com/questions/25346712/opencv-rotation-deskewing-in-android-c-to-java-conversion
	 */
	public static Mat deskew(Mat src, double angle) {
	    Point center = new Point(src.width()/2, src.height()/2);
	    Mat rotImage = Imgproc.getRotationMatrix2D(center, angle, 1.0);
	    //1.0 means 100 % scale
	    Size size = new Size(src.width(), src.height());
	    Imgproc.warpAffine(src, src, rotImage, size, Imgproc.INTER_LINEAR + Imgproc.CV_WARP_FILL_OUTLIERS);
	    return src;
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
