import GUI.*;
import javafx.application.Application;
import javafx.stage.Stage;

public class Armazem extends Application {
    Stage window;

    @Override
    public void start(Stage primaryStage){
        GUI view = new View();

        window = primaryStage;
        window.setScene(view.menu());
        window.setTitle("Menu de Admin");
        window.show();

    }


    public static void main(String[] args) {
        launch(args);
    }
}
