package projekat;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;

public class AboutController {
    public ImageView imageView;

    @FXML
    public void initialize(){
        File slika = new File("resources/img/vitkovici.jpg");
        if(!(slika.exists())){
            imageView.setImage(null);
        }else{
            Image img = new Image("/img/vitkovici.jpg");
            imageView.setImage(img);
        }
    }
}
