package projekat;

import at.mukprojects.giphy4j.Giphy;
import at.mukprojects.giphy4j.entity.search.SearchFeed;
import at.mukprojects.giphy4j.exception.GiphyException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;

import java.util.ResourceBundle;

public class PretragaController {
    public TextField fldPretraga;
    public ScrollPane scrollPane;
    public AnchorPane anchorPane;
    public TilePane tilePane;
    private String adresa;
    private SearchFeed feed;

    @FXML
    public void initialize(){
        scrollPane.setFitToWidth(true);
        tilePane.prefWidthProperty().bind(scrollPane.widthProperty());
        scrollPane.prefHeightProperty().bind(anchorPane.heightProperty());
    }

    public void searchAction(ActionEvent actionEvent){
        Giphy giphy = new Giphy("dc6zaTOxFJmzC");
        String text = fldPretraga.getText();

        if(text==null || text.trim().isEmpty()){
            return;
        }

        if(!(tilePane.getChildren().isEmpty())){
            tilePane.getChildren().clear();
        }

        try {
            feed = giphy.search(text, 25, 0);

            new Thread(() -> {

                for(int j=0;j<feed.getDataList().size();j++){

                    Button dugme = new Button();

                    dugme.setPrefHeight(128);
                    dugme.setPrefWidth(128);
                    Image gif = new Image("/img/loading.gif");
                    ImageView loading = new ImageView();
                    loading.setFitHeight(128);
                    loading.setFitWidth(128);
                    loading.setImage(gif);
                    Platform.runLater(()->{
                        dugme.setGraphic(loading);
                        tilePane.getChildren().add(dugme);
                    });

                    String link = feed.getDataList().get(j).getImages().getOriginalStill().getUrl();

                    int i;
                    for(i=0;i<link.length();i++){
                        if(link.charAt(i)=='?'){
                            break;
                        }
                    }

                    link = link.substring(0,i);

                    i=0;
                    while(link.charAt(i)!='.'){
                        i++;
                    }
                    link = "https://i" + link.substring(i);

                    Image img = new Image(link);
                    ImageView slicica = new ImageView();
                    slicica.setFitHeight(128);
                    slicica.setFitWidth(128);
                    slicica.setImage(img);

                    Platform.runLater(() -> {
                        dugme.setGraphic(slicica);
                        tilePane.getChildren().remove(dugme);
                        tilePane.getChildren().add(dugme);
                    });
                    String finalLink = link;
                    dugme.setOnAction(actionEvent1 -> {
                        adresa = finalLink;
                    });
                }
            }).start();

        } catch (GiphyException e) {
            e.printStackTrace();
        }

    }

    public void okAction(ActionEvent actionEvent){
        ResourceBundle bundle = ResourceBundle.getBundle("Translation");
        if(adresa==null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(bundle.getString("nijednaSlikaNijeIzabrana"));
            alert.setHeaderText(null);
            alert.setContentText(bundle.getString("unesitePretragu"));

            alert.showAndWait();
        }else{
            Node n = (Node) actionEvent.getSource();
            Stage stage = (Stage) n.getScene().getWindow();
            stage.close();
        }
    }

    public void cancelAction(ActionEvent actionEvent){
        adresa=null;
        Node n = (Node) actionEvent.getSource();
        Stage stage = (Stage) n.getScene().getWindow();
        stage.close();
    }

    public String getAdresa(){
        return adresa;
    }
}
