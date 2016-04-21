package img_proc;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class SkewProcessor {
	public static double skewAngle(Mat m){
		return 0;
	}
	public static Mat deskewedImg(Mat m){
		return null;
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
}
