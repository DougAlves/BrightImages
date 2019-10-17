package application.models;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

public class Ocr {
	private Tesseract tesseract; 
	private double alpha1;
	private double alpha2;
	private int beta1;
	private int beta2;
	private File image;
	private PatternMatcher lowPricePattern;
	private PatternMatcher highPricePattern;
	private PatternMatcher cepPattern;
	private PatternMatcher datePattern;
	private PatternMatcher contratoPattern;
	private PatternMatcher vencimentoPattern;

	public Ocr() {
		tesseract = new Tesseract();
		tesseract.setLanguage("por");
		tesseract.setDatapath("lib/Tess4J/tessdata");
		lowPricePattern = new PatternMatcher("nn.nn");
		highPricePattern = new PatternMatcher("nnn.nn");
		cepPattern = new PatternMatcher("nnnnn—nnn");
		datePattern = new PatternMatcher("nn/nn");
		contratoPattern = new PatternMatcher("nnnnnnnnnn");
		vencimentoPattern = new PatternMatcher("nn/nn/nnnn");
	}
	
	
	public String extractText(File file) {
		try {
			return tesseract.doOCR(file);			
		}catch (TesseractException  e) {
			return "";
		}
	}
	public String extractText() {
		try {
			return tesseract.doOCR(image);			
		}catch (TesseractException  e) {
			return "";
		}
	}
	public List<String> extractInfo(String text){
		List<String> patterns = new ArrayList<String>();
		patterns.add(lowPricePattern.extractPathern(text));
		patterns.add(highPricePattern.extractPathern(text));
		patterns.add(cepPattern.extractPathern(text));
		patterns.add(datePattern.extractPathern(text));
		patterns.add(contratoPattern.extractPathern(text));
		patterns.add(vencimentoPattern.extractPathern(text));
		
		return patterns;
	}	
	
	public File prepareImage(File path) throws Exception{
		Mat img = new Mat();
        img = Imgcodecs.imread(path.getAbsolutePath(), Imgcodecs.IMREAD_GRAYSCALE);
        if (img.empty()) throw new Exception();
        System.out.println(img);
        Mat contrasted = new Mat();
        img.convertTo(contrasted, 4, alpha1, beta1);
        contrasted.convertTo(contrasted, 3, alpha2, beta2);
        Imgcodecs.imwrite("images/current.jpg", contrasted);
        return new File("images/current.jpg");
	}
	
	public void prepareImage() throws Exception{
		Mat img = new Mat();
        img = Imgcodecs.imread(image.getAbsolutePath(), Imgcodecs.IMREAD_GRAYSCALE);
        if (img.empty()) throw new Exception();
        System.out.println(img);
        Mat contrasted = new Mat();
        img.convertTo(contrasted, 4, alpha1, beta1);
        contrasted.convertTo(contrasted, 3, alpha2, beta2);
        Imgcodecs.imwrite("./images/current.jpg", contrasted);
	}
	
	public String toString() {
		return alpha1 + " " + beta1 + " " + alpha2 + " " + beta2 + " ";
	}
	public void setImage(File file) {
		image = file;
	}
	
	public File getImage() {
		return image;
	}
	
	public double getAlpha1() {
		return alpha1;
	}

	public void setAlpha1(double alpha1) {
		this.alpha1 = alpha1;
	}

	public double getAlpha2() {
		return alpha2;
	}

	public void setAlpha2(double alpha2) {
		this.alpha2 = alpha2;
	}

	public int getBeta1() {
		return beta1;
	}

	public void setBeta1(int beta1) {
		this.beta1 = beta1;
	}

	public int getBeta2() {
		return beta2;
	}

	public void setBeta2(int beta2) {
		this.beta2 = beta2;
	}
	
}
