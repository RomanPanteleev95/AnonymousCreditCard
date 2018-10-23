import db.DataBaseAnalog;
import entities.Message;
import entities.banks.Bank;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import protocols.BankToBankProtocol;
import utils.Utils;

import java.sql.SQLException;
import java.util.Map;

public class JavaFXTestRun extends Application {

    Stage window;
    Scene menuScene;
    Scene createCustomerScene;
    Scene customerOperationsScene;

    DataBaseAnalog DATA_BASE_ANALOG = DataBaseAnalog.getDataBaseAnalog();

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        Utils.registrationBank();
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        window = primaryStage;
        menuScene = getMenuScene();
        createCustomerScene = createCustomer();
        customerOperationsScene = customerOperations();

        window.setScene(menuScene);
        window.show();

    }

    public Scene getMenuScene(){
        Label menuLable = new Label("Меню");

        Button createCustomerButton = new Button("Зарегистрироваться в банках");
        Button customerOperations = new Button("Пополнить счет");


        createCustomerButton.setOnAction(e -> window.setScene(createCustomerScene));
        customerOperations.setOnAction(event -> window.setScene(customerOperationsScene));

        StackPane menuStackPane = new StackPane();
        menuStackPane.getChildren().addAll(menuLable, createCustomerButton, customerOperations);

        StackPane.setMargin(menuLable, new Insets(5, 50, 380, 50));
        StackPane.setMargin(createCustomerButton, new Insets(40, 50, 360, 50));
        StackPane.setMargin(customerOperations, new Insets(90, 50, 310, 50));

        return new Scene(menuStackPane, 300, 400);
    }

    public Scene createCustomer(){
        Label nameLabel = new Label("Имя клиента");
        TextField nameInput = new TextField();

        Label depositLabel = new Label("Депозитный банк");
        ChoiceBox<String> depositBank = new ChoiceBox<>();
        Map<String, Bank> depositBanks = DATA_BASE_ANALOG.getDepositBanks();
        for (Bank bank : depositBanks.values()) {
            depositBank.getItems().addAll(bank.getId());
        }

        Label creditLabel = new Label("Кредитный банк");
        ChoiceBox<String> creditBank = new ChoiceBox<>();
        Map<String, Bank> creditCradBanks = DATA_BASE_ANALOG.getCreditCradBanks();
        for (Bank bank : creditCradBanks.values()) {
            creditBank.getItems().addAll(bank.getId());
        }

        Button createButton = new Button("Зарегисьрировать клиента");
        Button menuButton = new Button("Меню");

            createButton.setOnAction(event -> {
                String customerName = nameInput.getText();
                Utils.createCustomer(customerName);
                Utils.registrationCustomer(DATA_BASE_ANALOG.getCustomer(customerName),
                        DATA_BASE_ANALOG.getCreditCardBank(creditBank.getValue()),
                        DATA_BASE_ANALOG.getDepositBank(depositBank.getValue()));
                nameInput.clear();
                window.setScene(menuScene);
            });
        menuButton.setOnAction(event -> {
            window.setScene(menuScene);
            nameInput.clear();
        });

        StackPane createCustomerStackPane = new StackPane();
        createCustomerStackPane.getChildren().addAll(nameLabel, nameInput, depositLabel, depositBank, creditLabel, creditBank, createButton, menuButton);

        StackPane.setMargin(nameLabel, new Insets(17, 50, 262, 50));
        StackPane.setMargin(nameInput, new Insets(50, 50, 230, 50));
        StackPane.setMargin(depositLabel, new Insets(70, 50, 210, 50));
        StackPane.setMargin(depositBank, new Insets(100, 50, 180, 50));
        StackPane.setMargin(creditLabel, new Insets(120, 50, 160, 50));
        StackPane.setMargin(creditBank, new Insets(150, 50, 130, 50));
        StackPane.setMargin(createButton, new Insets(200, 50, 80, 50));
        StackPane.setMargin(menuButton, new Insets(270, 10, 10, 230));

        return new Scene(createCustomerStackPane, 300, 300);
    }

    public Scene customerOperations(){
        Label nameCustomerLabel = new Label("Имя клиента");
        Label nameDepositLabel = new Label("Название депозитного банка");
        Label nameCreditLabel = new Label("Название кредитного");
        Label sum = new Label("Сумма");
        TextField customerNameInput = new TextField();
        TextField creditBankNameInput = new TextField();
        TextField depositBankNameInput = new TextField();
        TextField moneyInput = new TextField();
        Button takeCreditButton = new Button("Пополнить");
        Button menuButton = new Button("Меню");

        takeCreditButton.setOnAction(event -> {
            String customerName = customerNameInput.getText();
            String creditBankName = creditBankNameInput.getText();
            String depositBankName = depositBankNameInput.getText();
            String message = moneyInput.getText();

            BankToBankProtocol bankToBankProtocol = new BankToBankProtocol(DATA_BASE_ANALOG.getCustomer(customerName), DATA_BASE_ANALOG.getCreditCardBank(creditBankName),
                    DATA_BASE_ANALOG.getDepositBank(depositBankName), new Message(message));
            bankToBankProtocol.runProtocol();

            customerNameInput.clear();
            depositBankNameInput.clear();
            creditBankNameInput.clear();
            window.setScene(menuScene);
        });

        menuButton.setOnAction(event -> {
            window.setScene(menuScene);
            customerNameInput.clear();
            depositBankNameInput.clear();
            creditBankNameInput.clear();
        });

        StackPane layout2 = new StackPane();
        layout2.getChildren().addAll(nameCustomerLabel, nameDepositLabel, nameCreditLabel, sum, customerNameInput,
                depositBankNameInput, creditBankNameInput, moneyInput, takeCreditButton, menuButton);
        StackPane.setMargin(nameCustomerLabel, new Insets(20, 50, 260, 50));
        StackPane.setMargin(customerNameInput, new Insets(50, 50, 230, 50));
        StackPane.setMargin(nameDepositLabel, new Insets(80, 50, 200, 50));
        StackPane.setMargin(depositBankNameInput, new Insets(110, 50, 170, 50));
        StackPane.setMargin(nameCreditLabel, new Insets(140, 50, 140, 50));
        StackPane.setMargin(creditBankNameInput, new Insets(170, 50, 110, 50));
        StackPane.setMargin(sum, new Insets(200, 50, 80, 50));
        StackPane.setMargin(moneyInput, new Insets(230, 50, 50, 50));
        StackPane.setMargin(takeCreditButton, new Insets(270, 50, 10, 50));
        StackPane.setMargin(menuButton, new Insets(270, 10, 10, 230));
        return new Scene(layout2, 300, 300);
    }

}
