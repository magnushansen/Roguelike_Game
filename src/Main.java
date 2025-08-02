import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import rougelike.Controller;
import rougelike.Global;

public class Main extends Application {
    public static void main(String[] args) throws Exception {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {

        Region root = new Controller().getView();

        Scene scene = new Scene(root, Global.WINDOW_HEIGHT, Global.WINDOW_WIDTH);
        stage.setScene(scene);
        stage.show();
    }
}
