package rougelike.menu.loginmenu;

import java.util.function.BiConsumer;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class LoginMenuView {
    private final LoginMenuModel model;
    private final Runnable showLoginMenu;
    private final BiConsumer<String, Integer> loginAction1;
    TextField address = createTextFields("127.0.0.1", 200, 150);
    TextField port = createTextFields("8888", 200, 150);
    Label failedLoginLabel = createFailedLoginLabel();

    public LoginMenuView(LoginMenuModel model, Runnable showLoginMenu, BiConsumer<String, Integer> loginAction1,
            Runnable showCommunityMenu) {
        this.model = model;
        this.showLoginMenu = showLoginMenu;
        this.loginAction1 = loginAction1;
    }

    public TextField createTextFields(String text, int prefwidth, int maxWidth) {

        TextField textField = new TextField(text);
        textField.setPrefWidth(prefwidth);
        textField.setMaxWidth(maxWidth);

        return textField;

    }

    public Label createFailedLoginLabel() {
        Label label = new Label();

        label.setStyle("-fx-font-size: 20px;");
        label.setFont(new Font("Arial", 20));
        label.setTextFill(Color.rgb(255, 0, 0, 1));

        return label;
    }

    public void showFailedLogin() {
        failedLoginLabel.setText("Login Failed");
    }

    public Label createLabel(String text, int r, int g, int b) {
        Label label = new Label(text);

        label.setStyle("-fx-font-size: 20px;");
        label.setFont(new Font("Arial", 20));
        label.setTextFill(Color.rgb(r, g, b, 1));

        return label;
    }

    @SuppressWarnings("unused")
    public Button consumerButton(String text, BiConsumer<String, Integer> action) {
        Button consumer = new Button(text);

        consumer.setOnAction(e -> {
            action.accept(address.getText(), Integer.parseInt(port.getText()));
            if (model.getLoginStatus() == false) {
                showFailedLogin();
            }
        });

        return consumer;
    }

    @SuppressWarnings("unused")
    public Button createButton(String text, Runnable action) {
        Button Button = new Button(text);

        Button.setOnAction(e -> {
            action.run();
        });

        return Button;
    }

    public Region build() {
        GridPane layout = new GridPane();

        layout.add(createLabel("Address", 255, 255, 255), 0, 0);
        layout.add(address, 0, 1);
        layout.add(createLabel("Port", 255, 255, 255), 0, 2);
        layout.add(port, 0, 3);
        layout.add(failedLoginLabel, 0, 4);
        layout.add(consumerButton("login", loginAction1), 0, 5);
        layout.add(createButton("main menu", showLoginMenu), 0, 6);
        layout.setVgap(5);
        layout.setHgap(5);

        layout.setAlignment(Pos.CENTER);

        return layout;
    }
}
