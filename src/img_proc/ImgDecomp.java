package img_proc;
import java.util.ArrayList;
import java.util.List;
import org.opencv.core.Mat;

public class ImgDecomp {
	protected List<Line> lines = new ArrayList<>();;
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
	public List<Mat> chars(){
		List<Mat> chars = new ArrayList<>();
		for(Line line : lines)
			for(Word word : line.words)
				chars.addAll(word.chars);
		return chars;
	}
	public List<Mat> chars(Word word){
		return word.chars;
	}
	public class Line {
		protected List<Word> words = new ArrayList<>();
	}
	public class Word {
		protected List<Mat> chars = new ArrayList<>();
	}
}
