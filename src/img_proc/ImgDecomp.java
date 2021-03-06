package img_proc;
import java.util.ArrayList;
import java.util.List;
import org.opencv.core.Rect;
import org.opencv.core.Mat;

public class ImgDecomp {
	protected List<Line> lines = new ArrayList<>();
	private Mat origImg;
	public ImgDecomp(Mat origImg){
		this.origImg = origImg;
	}
	public List<Line> lines(){
		return lines;
	}
	public List<Word> words(){
		List<Word> words = new ArrayList<>();
		for(Line line : lines)
			words.addAll(line.words);
		return words;
	}
	public List<Word> words(Line line){
		return line.words;
	}
	public List<Rect> chars(){
		List<Rect> chars = new ArrayList<>();
		for(Line line : lines)
			for(Word word : line.words)
				chars.addAll(word.chars);
		return chars;
	}
	public List<Rect> chars(Word word){
		return word.chars;
	}
	public List<Mat> charImgs(){
		List<Mat> charImgs = new ArrayList<>();
		List<Rect> chars = chars();
		for(Rect r : chars){
			Mat charImg = origImg.submat(r);
			charImg = ImgProcessor.standardizedImg(charImg);
			charImgs.add(charImg);
		}
		return charImgs;
	}
	
	public class Line {
		protected List<Word> words = new ArrayList<>();
		public String toString(){
			return words.toString();
		}
	}
	public class Word {
		protected List<Rect> chars = new ArrayList<>();
		public Rect lastChar(){
			return chars.get(chars.size()-1);
		}
		public String toString(){
			return chars.toString();
		}
	}
}
