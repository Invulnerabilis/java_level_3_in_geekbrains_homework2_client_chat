package ru.vk.client_chat;

/*
Домашнее задание. Java. Уровень 3. Урок 2. "Базы данных".

1. На сервер добавить в сетевой чат возможность подключения, авторизацию через базу данных SQLite.
Плюс, возможность изменить User Name (можно добавить и смену пароля).

2. На клиент добавить поле для смены пароля.

3. Добавить регистрацию новых пользователей.
И в качестве регистрации иметь возможность добавлять новых пользователей на сервер, изменять им User Name, пароль.

Примечание!
Смена username и password пока доступно только через ввод команд: /change_username и /change_password

Внимание!
Это только "Client Chat" проекта сетевого чата второго урока.
"Server Client" сетевого чата находится в репозитории java_level_3_in_geekbrains_g ветка homework2
*/

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.vk.client_chat.controllers.ChatController;
import ru.vk.client_chat.controllers.SignController;
import ru.vk.client_chat.models.Network;

import java.io.IOException;

public class StartClient extends Application {

    private Network network;
    private Stage primaryStage;
    private Stage authStage;
    private ChatController chatController;
    private SignController signController;

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;

        network = new Network();
        network.setStartClient(this);
        network.connect();

        openAuthDialog();
        createChatDialog();
    }

    private void openAuthDialog() throws IOException {
        FXMLLoader authLoader = new FXMLLoader(StartClient.class.getResource("auth-view.fxml"));
        authStage = new Stage();
        Scene scene = new Scene(authLoader.load(), 600, 400);

        authStage.setScene(scene);
        authStage.initModality(Modality.WINDOW_MODAL);
        authStage.initOwner(primaryStage);
        authStage.setTitle("Chatroom");
        authStage.setY(140);
        authStage.setX(650);
        authStage.show();

        SignController signController = authLoader.getController();

        signController.setNetwork(network);
        signController.setStartClient(this);
    }

    private void createChatDialog() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartClient.class.getResource("chat-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);

        primaryStage.setScene(scene);
        primaryStage.setY(140);
        primaryStage.setX(650);

        chatController = fxmlLoader.getController();
        chatController.setNetwork(network);
        chatController.setStartClient(this);

    }

    public void openChatDialog() {
        authStage.close();
        primaryStage.show();
        primaryStage.setTitle(network.getUsername());

        network.waitMessage(chatController);
        chatController.setUsernameTitle(network.getUsername());
    }

    public void showErrorAlert(String title, String errorMessage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(errorMessage);
        alert.show();
    }

    public void showInfoAlert(String title, String infoMessage) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(infoMessage);
        alert.show();
    }

    public static void main(String[] args) {
        launch();
    }
}