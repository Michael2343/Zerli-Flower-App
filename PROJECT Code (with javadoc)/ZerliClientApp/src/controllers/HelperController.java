package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class HelperController {
	private Stage myStage;
    @FXML
    private ImageView helpContent;

    @FXML
    private Label helpTitle;

    @FXML
    void exitHelperPressed(MouseEvent event) {
    	myStage.close();
    }
    
    public void SetHelper(String title, String imgName,Stage stage)
    {
    	myStage = stage;
    	helpTitle.setText(title);
    	Image image = new Image("/png/" + imgName);
    	helpContent.setImage(image);
    }

}
