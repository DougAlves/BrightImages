package application.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

import application.Main;
import application.Utils;
import application.models.Ocr;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;

public class OcrController {

	private Main mainStage;

	@FXML
	private Button btnLog;

	@FXML
	private ImageView imgPanel;

	@FXML
	private Button btnExtrair;

	@FXML
	private TextField txtVencimento;

	@FXML
	private Button btnWebcam;

	@FXML
	private Button fileChoser;

	@FXML
	private TextField txtPreco;

	@FXML
	private TextField txtCEP;

	@FXML
	private TextField txtMA;

	@FXML
	private TextField txtCC;

	@FXML
	private Slider alpha1;

	@FXML
	private Slider beta1;

	@FXML
	private Slider alpha2;

	@FXML
	private Slider beta2;

	@FXML
	private Font x1;

	private boolean cameraActive = false;

	private VideoCapture capture = new VideoCapture(0);
	
	private ScheduledExecutorService timer;

	private Ocr ocr = new Ocr();

	public Main getMainStage() {
		return mainStage;
	}

	public void setMainStage(Main mainStage) {
		this.mainStage = mainStage;
	}

	@FXML
	void doOcr(ActionEvent event) {
		System.out.println(ocr);
		ocr.setAlpha1(alpha1.getValue());
		ocr.setAlpha2(alpha2.getValue());
		ocr.setBeta1((int) beta1.getValue());
		ocr.setBeta2((int) beta2.getValue());

		if (cameraActive)
			ocrCamera();
		else {
			try {
				ocr.prepareImage();
				String text = ocr.extractText();
				Map<String, String> info = ocr.extractInfo(text);
				System.out.println(text);
				txtPreco.setText(info.get("nn.nn").length() > info.get("nnn.nn").length() ? info.get("nn.nn")
						: info.get("nnn.nn"));

				txtCEP.setText(info.get("nnnnn-nnn").length() > info.get("nnnnn—nnn").length() ? info.get("nnnnn-nnn")
						: info.get("nnnnn—nnn"));

				txtMA.setText(info.get("nn/nnnn"));
				txtCC.setText(info.get("nnnnnnnnnn"));
				txtVencimento.setText(info.get("nn/nn/nnnn"));
				System.out.println(info);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println("aqui");
				e.printStackTrace();
			}
		}
	}

	public void ocrCamera() {
		Mat img = grabFrame();
		stopAcquisition();
		this.cameraActive = false;
		ocr.prepareImage(img);
		String text = ocr.extractText();
		Map<String, String> info = ocr.extractInfo(text);
		System.out.println(text);
		txtPreco.setText(
				info.get("nn.nn").length() > info.get("nnn.nn").length() ? info.get("nn.nn") : info.get("nnn.nn"));
		txtPreco.setText(
				txtPreco.getText().length() >= info.get("nnn,nn").length() ? txtPreco.getText() : info.get("nnn,nn"));

		txtCEP.setText(info.get("nnnnn-nnn").length() > info.get("nnnnn—nnn").length() ? info.get("nnnnn-nnn")
				: info.get("nnnnn—nnn"));
		txtVencimento.setText(info.get("nn/nn/nnnn"));
		txtMA.setText(info.get("nn/nnnn"));
		txtCC.setText(info.get("nnnnnnnnnn"));
		System.out.println(info);

	}

	@FXML
	void selecionarArquivo(ActionEvent event) {
		FileChooser choser = new FileChooser();
		File file = choser.showOpenDialog(null);

		ocr.setImage(file);
		try {
			FileInputStream input = new FileInputStream(file.getAbsolutePath());
			Image img = new Image(input);
			imgPanel.setImage(img);
			imgPanel.fitWidthProperty();
			ocr.prepareImage();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@FXML
	void selectFromWebcam(ActionEvent event) {
		if (!this.cameraActive) {
			// start the video capture
			this.capture.open(0);

			// is the video stream available?
			if (this.capture.isOpened()) {
				this.cameraActive = true;

				// grab a frame every 33 ms (30 frames/sec)
				Runnable frameGrabber = new Runnable() {

					@Override
					public void run() {
						// effectively grab and process a single frame
						Mat frame = grabFrame();
						// convert and show the frame
						Image imageToShow = Utils.mat2Image(frame);
						updateImageView(imgPanel, imageToShow);
					}
				};

				this.timer = Executors.newSingleThreadScheduledExecutor();
				this.timer.scheduleAtFixedRate(frameGrabber, 0, 33, TimeUnit.MILLISECONDS);

				// update the button content
			} else {
				// log the error
				System.err.println("Impossible to open the camera connection...");
			}
		} else {
			// the camera is not active at this point
			this.cameraActive = false;
			// update again the button content
			// stop the timer
			this.stopAcquisition();
		}

	}

	private void stopAcquisition() {
		this.cameraActive = false;

		if (this.timer != null && !this.timer.isShutdown()) {
			try {
				// stop the timer
				this.timer.shutdown();
				this.timer.awaitTermination(33, TimeUnit.MILLISECONDS);
			} catch (InterruptedException e) {
				// log any exception
				System.err.println("Exception in stopping the frame capture, trying to release the camera now... " + e);
			}
		}

		if (this.capture.isOpened()) {
			
			// release the camera
			this.capture.release();
		}
	}

	private Mat grabFrame() {
		// init everything
		Mat frame = new Mat();

		// check if the capture is open
		if (this.capture.isOpened()) {
			try {
				// read the current frame
				this.capture.read(frame);

				// if the frame is not empty, process it
				if (!frame.empty()) {
					// Imgproc.cvtColor(frame, frame, Imgproc.COLOR_BGR2GRAY);
				}

			} catch (Exception e) {
				// log the error
				System.err.println("Exception during the image elaboration: " + e);
			}
		}

		return frame;
	}

	private void updateImageView(ImageView view, Image image) {
		Utils.onFXThread(view.imageProperty(), image);
	}

	@FXML
	void setAlpha1(MouseEvent event) {
		ocr.setAlpha1(alpha1.getValue());
	}

	@FXML
	void setAlpha2(MouseEvent event) {
		ocr.setAlpha2(alpha2.getValue());
	}

	@FXML
	void setBeta1(MouseEvent event) {
		ocr.setBeta1((int) beta1.getValue());
	}

	@FXML
	void setBeta2(MouseEvent event) {
		ocr.setBeta2((int) beta2.getValue());
	}

	@FXML
	void doLog(ActionEvent event) {
		try {

			FileWriter fileWriter = new FileWriter("./log/log.txt", true); // Set true for append mode

			PrintWriter writer = new PrintWriter(fileWriter);
			StringBuilder sb = new StringBuilder();
			sb.append(txtCC.getText() + "\t");
			sb.append(txtCEP.getText() + "\t");
			sb.append(txtMA.getText() + "\t");
			sb.append(txtVencimento.getText() + "\t");
			sb.append(txtPreco.getText());
			System.out.println(sb.toString());
			writer.println(sb.toString());
			writer.close();
			fileWriter.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

		}
	}

}
