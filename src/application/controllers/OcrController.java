package application.controllers;

import java.io.File;
import java.util.List;

import application.Main;
import application.models.Ocr;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;


public class OcrController {
	
	private Main mainStage;
    
	@FXML
    private ImageView imgPanel;
	
    @FXML
    private Button btnExtrair;
    
    @FXML
    private Button fileChoser;

	@FXML
    private Slider alpha1;

    @FXML
    private Slider beta1;
    
    @FXML
    private Slider alpha2;

    @FXML
    private Slider beta2;
    
    @FXML
    private TextField txtPreco;
    
    @FXML
    private TextField txtCEP;
    
    @FXML
    private TextField txtMA;
    
    @FXML
    private TextField txtCC;
    
    @FXML
    private Font x1;
	
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
    	try {
    		System.out.println("Oi rapasiada");
			System.out.println("até agora ta tudo tranquilo");
			String text = ocr.extractText();
			System.out.println("Oia como funciona");
			List<String> info = ocr.extractInfo(text);
			System.out.println(text);
			txtPreco.setText(info.get(0));
			txtCEP.setText(info.get(1));
			txtMA.setText(info.get(2));
			txtCC.setText(info.get(3));
			System.out.println(info);
    	} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
      
    @FXML
    void selecionarArquivo(ActionEvent event) {
    	FileChooser choser = new FileChooser();
    	File file = choser.showOpenDialog(null);
    	ocr.setImage(file);
    	try {
			ocr.prepareImage();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    @FXML
    void setAlpha1(MouseEvent event) {
        System.out.println("oi mesmo");
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
    
  
}

