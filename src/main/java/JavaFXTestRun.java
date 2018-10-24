import db.DataBaseAnalog;
import entities.Customer;
import entities.Message;
import entities.banks.Bank;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import protocols.BankToBankProtocol;
import utils.MailNotification;
import utils.Utils;

import javax.mail.MessagingException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;

public class JavaFXTestRun extends Application {

    Stage mainWindow;
    Scene menuScene;
    Scene createCustomerScene;
    Scene customerOperationsScene;

    DataBaseAnalog DATA_BASE_ANALOG = DataBaseAnalog.getDataBaseAnalog();

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
//        Utils.registrationBank();
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        mainWindow = primaryStage;
        menuScene = getMenuScene();
        createCustomerScene = createCustomer();
        customerOperationsScene = customerOperations();

        mainWindow.setScene(menuScene);
        mainWindow.show();

    }

    public Scene getMenuScene(){
        Label menuLable = new Label("Меню");

        Button createCustomerButton = new Button("Зарегистрироваться");
        Button customerOperations = new Button("Войти");


        createCustomerButton.setOnAction(e -> {
            Stage registrationWindow = new Stage();
            registrationWindow.setScene(registrationScene());
            registrationWindow.show();

        });
        customerOperations.setOnAction(event -> {
            Stage signInWindow = new Stage();
            signInWindow.setScene(getSignIn(signInWindow));
            signInWindow.show();
        });

        StackPane menuStackPane = new StackPane();
        menuStackPane.getChildren().addAll(menuLable, createCustomerButton, customerOperations);

        StackPane.setMargin(menuLable, new Insets(5, 50, 130, 50));
        StackPane.setMargin(createCustomerButton, new Insets(40, 50, 110, 50));
        StackPane.setMargin(customerOperations, new Insets(90, 50, 60, 50));

        return new Scene(menuStackPane, 300, 150);
    }

    public Scene createCustomer(){
        Label nameLabel = new Label("Имя клиента");
        TextField nameInput = new TextField();

        Label depositLabel = new Label("Депозитный банк");
        ChoiceBox<String> depositBank = new ChoiceBox<>();
        Map<String, Bank> depositBanks = DATA_BASE_ANALOG.getDepositBanks();
        for (Bank bank : depositBanks.values()) {
            depositBank.getItems().addAll(bank.getName());
        }

        Label creditLabel = new Label("Кредитный банк");
        ChoiceBox<String> creditBank = new ChoiceBox<>();
        Map<String, Bank> creditCradBanks = DATA_BASE_ANALOG.getCreditCradBanks();
        for (Bank bank : creditCradBanks.values()) {
            creditBank.getItems().addAll(bank.getName());
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
                mainWindow.setScene(menuScene);
            });
        menuButton.setOnAction(event -> {
            mainWindow.setScene(menuScene);
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
            mainWindow.setScene(menuScene);
        });

        menuButton.setOnAction(event -> {
            mainWindow.setScene(menuScene);
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

    public Scene registrationScene(){
        Label nameLabel = new Label("Имя клиента");
        TextField nameInput = new TextField();

        Label emailLabel = new Label("E-mail");
        TextField emailInput = new TextField();

        Button registrationButton = new Button("Зарегисьрироваться");
        registrationButton.setOnAction(event -> {
            if (!emailInput.getText().isEmpty() && emailInput.getText() != null){
                String password = UUID.randomUUID().toString().replaceAll("-", "");
                try {
                    MailNotification.sendPassword(emailInput.getText(), password);
                } catch (MessagingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        StackPane registrationCustomerStackPante = new StackPane();
        registrationCustomerStackPante.getChildren().addAll(nameLabel, nameInput, emailLabel, emailInput, registrationButton);
        StackPane.setMargin(nameLabel, new Insets(17, 50, 262, 50));
        StackPane.setMargin(nameInput, new Insets(50, 50, 230, 50));
        StackPane.setMargin(emailLabel, new Insets(70, 50, 210, 50));
        StackPane.setMargin(emailInput, new Insets(100, 50, 180, 50));
        StackPane.setMargin(registrationButton, new Insets(150, 50, 130, 50));


        return new Scene(registrationCustomerStackPante, 300, 300);
    }

    public Scene getSignIn(Stage currentWindow){
        Label nameLabel = new Label("Имя клиента");
        TextField nameInput = new TextField();

        Label passwordLabel = new Label("Пароль");
        TextField passwordInput = new TextField();

        Button signInButton = new Button("Войти");
        signInButton.setOnAction(event -> {
            if (passwordInput.getText().equals("admin") && nameInput.getText().equals("admin")){
                currentWindow.close();
                mainWindow.setScene(customerOperations());
            }

//            try {
//                PreparedStatement preparedStatement = Utils.getPreparedStatement(Constant.SqlQuery.SELCT_USER_PASSWORD_BY_NAME);
//                preparedStatement.setString(1, nameInput.getText());
//                String passwordFromDataBase = preparedStatement.executeQuery().getString(1);
//                if (passwordInput.getText().equals(passwordFromDataBase)){
//                    currentWindow.close();
//                    mainWindow.setScene(customerOperations());
//                }
//            } catch (ClassNotFoundException e) {
//                e.printStackTrace();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
        });

        StackPane signInStackPante = new StackPane();
        signInStackPante.getChildren().addAll(nameLabel, nameInput, passwordLabel, passwordInput, signInButton);
        StackPane.setMargin(nameLabel, new Insets(17, 50, 262, 50));
        StackPane.setMargin(nameInput, new Insets(50, 50, 230, 50));
        StackPane.setMargin(passwordLabel, new Insets(70, 50, 210, 50));
        StackPane.setMargin(passwordInput, new Insets(100, 50, 180, 50));
        StackPane.setMargin(signInButton, new Insets(150, 50, 130, 50));

        return new Scene(signInStackPante, 300, 300);
    }

    public Scene getPersonalArea(Customer customer){
        Label nameLabel = new Label("Личный кабинет: " + customer.getName());


        return null;
    }



}
