import constants.Constant;
import utils.DataBaseUtils;
import entities.Customer;
import entities.Message;
import entities.banks.Bank;
import entities.banks.CreditCardBank;
import entities.banks.DepositBank;
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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;

public class JavaFXTestRun extends Application {

    Stage mainWindow;
    Scene menuScene;
    Scene customerOperationsScene;

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        Utils.registrationBank();
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        mainWindow = primaryStage;
        menuScene = getMenuScene();
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

    public Scene getRegistrationCustomerInBanksScene(Customer customer) throws SQLException, ClassNotFoundException {
        Label depositLabel = new Label("Депозитный банк");
        ChoiceBox<String> depositBankChoiceBox = new ChoiceBox<>();
        ResultSet depositBanks = DataBaseUtils.getAllDepositBanks();
        while (depositBanks.next()){
            depositBankChoiceBox.getItems().addAll(depositBanks.getString("name"));
        }

        Label creditLabel = new Label("Кредитный банк");
        ChoiceBox<String> creditBankChoiceBox = new ChoiceBox<>();
        ResultSet creditBanks = DataBaseUtils.getAllCreditBanks();
        while (creditBanks.next()){
            creditBankChoiceBox.getItems().addAll(creditBanks.getString("name"));
        }

        Button registrationButton = new Button("Зарегисьрировать клиента");
        Button menuButton = new Button("Меню");



        registrationButton.setOnAction(event -> {
            try {
                PreparedStatement creditBankStatement = Utils.getPreparedStatement(Constant.SqlQuery.SELECT_BANK_BY_NAME);
                creditBankStatement.setString(1, creditBankChoiceBox.getValue());
                ResultSet resultSetCredit = creditBankStatement.executeQuery();
                int creditBankId = 111;
                String creditBankName = "";
                String creditBankPrivateKey = "";
                if (resultSetCredit.next()){
                    creditBankId = resultSetCredit.getInt("id");
                    creditBankName = resultSetCredit.getString("name");
                    creditBankPrivateKey = resultSetCredit.getString("private_key");
                }
                CreditCardBank creditCardBank = new CreditCardBank(creditBankId, creditBankName);
                creditCardBank.setPrivateKey(creditBankPrivateKey);

                PreparedStatement depositBankStatement = Utils.getPreparedStatement(Constant.SqlQuery.SELECT_BANK_BY_NAME);
                depositBankStatement.setString(1, depositBankChoiceBox.getValue());
                ResultSet resultSetDeposit = depositBankStatement.executeQuery();
                int depositBankId = 111;
                String depositBankName = "";
                String depositBankPrivateKey = "";
                if (resultSetDeposit.next()){
                    depositBankId = resultSetCredit.getInt("id");
                    depositBankName = resultSetDeposit.getString("name");
                    depositBankPrivateKey = resultSetDeposit.getString("private_key");
                }
                DepositBank depositBank1 = new DepositBank(depositBankId, depositBankName);
                depositBank1.setPrivateKey(depositBankPrivateKey);
                Utils.registrationCustomer(customer, creditCardBank, depositBank1);
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            mainWindow.setScene(getPersonalArea(customer));
        });
        menuButton.setOnAction(event -> {
            mainWindow.setScene(getPersonalArea(customer));
        });

        StackPane createCustomerStackPane = new StackPane();
        createCustomerStackPane.getChildren().addAll(depositLabel, depositBankChoiceBox, creditLabel, creditBankChoiceBox, registrationButton, menuButton);

        StackPane.setMargin(depositLabel, new Insets(70, 50, 210, 50));
        StackPane.setMargin(depositBankChoiceBox, new Insets(100, 50, 180, 50));
        StackPane.setMargin(creditLabel, new Insets(120, 50, 160, 50));
        StackPane.setMargin(creditBankChoiceBox, new Insets(150, 50, 130, 50));
        StackPane.setMargin(registrationButton, new Insets(200, 50, 80, 50));
        StackPane.setMargin(menuButton, new Insets(270, 10, 10, 230));

        return new Scene(createCustomerStackPane, 300, 300);
    }

    public Scene customerOperations(){
//        Label nameCustomerLabel = new Label("Имя клиента");
//        Label nameDepositLabel = new Label("Название депозитного банка");
//        Label nameCreditLabel = new Label("Название кредитного");
//        Label sum = new Label("Сумма");
//        TextField customerNameInput = new TextField();
//        TextField creditBankNameInput = new TextField();
//        TextField depositBankNameInput = new TextField();
//        TextField moneyInput = new TextField();
//        Button takeCreditButton = new Button("Пополнить");
//        Button menuButton = new Button("Меню");
//
//        takeCreditButton.setOnAction(event -> {
//            String customerName = customerNameInput.getText();
//            String creditBankName = creditBankNameInput.getText();
//            String depositBankName = depositBankNameInput.getText();
//            String message = moneyInput.getText();
//
//            BankToBankProtocol bankToBankProtocol = new BankToBankProtocol(DATA_BASE_ANALOG.getCustomer(customerName), DATA_BASE_ANALOG.getCreditCardBank(creditBankName),
//                    DATA_BASE_ANALOG.getDepositBank(depositBankName), new Message(message));
//            bankToBankProtocol.runProtocol();
//
//            customerNameInput.clear();
//            depositBankNameInput.clear();
//            creditBankNameInput.clear();
//            mainWindow.setScene(menuScene);
//        });
//
//        menuButton.setOnAction(event -> {
//            mainWindow.setScene(menuScene);
//            customerNameInput.clear();
//            depositBankNameInput.clear();
//            creditBankNameInput.clear();
//        });
//
//        StackPane layout2 = new StackPane();
//        layout2.getChildren().addAll(nameCustomerLabel, nameDepositLabel, nameCreditLabel, sum, customerNameInput,
//                depositBankNameInput, creditBankNameInput, moneyInput, takeCreditButton, menuButton);
//        StackPane.setMargin(nameCustomerLabel, new Insets(20, 50, 260, 50));
//        StackPane.setMargin(customerNameInput, new Insets(50, 50, 230, 50));
//        StackPane.setMargin(nameDepositLabel, new Insets(80, 50, 200, 50));
//        StackPane.setMargin(depositBankNameInput, new Insets(110, 50, 170, 50));
//        StackPane.setMargin(nameCreditLabel, new Insets(140, 50, 140, 50));
//        StackPane.setMargin(creditBankNameInput, new Insets(170, 50, 110, 50));
//        StackPane.setMargin(sum, new Insets(200, 50, 80, 50));
//        StackPane.setMargin(moneyInput, new Insets(230, 50, 50, 50));
//        StackPane.setMargin(takeCreditButton, new Insets(270, 50, 10, 50));
//        StackPane.setMargin(menuButton, new Insets(270, 10, 10, 230));
//        return new Scene(layout2, 300, 300);
        return null;
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

                try {
                    PreparedStatement preparedStatement = Utils.getPreparedStatement(Constant.SqlQuery.INSERT_NEW_CUSTOMER);
                    preparedStatement.setString(1, nameInput.getText());
                    preparedStatement.setString(2, password);
                    preparedStatement.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
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
//            if (passwordInput.getText().equals("admin") && nameInput.getText().equals("admin")){
//                currentWindow.close();
//                mainWindow.setScene(customerOperations());
//            }

            try {
                PreparedStatement preparedStatement = Utils.getPreparedStatement(Constant.SqlQuery.SELCT_USER_PASSWORD_BY_NAME);
                preparedStatement.setString(1, nameInput.getText());
                ResultSet rs =  preparedStatement.executeQuery();
                String passwordFromDataBase = "";
                if (rs.next()) {
                    passwordFromDataBase = rs.getString("customer_password");
                }

                if (passwordInput.getText().equals(passwordFromDataBase)){
                    currentWindow.close();
                    Customer customer = new Customer(rs.getString("name"));
                    mainWindow.setScene(getPersonalArea(customer));
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
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
        String creditBankAccount = "";
        if (customer.getBillIdInCreditCardBank() == null){
            creditBankAccount = "не зарегистрирован";
        }else{
            creditBankAccount = customer.getBillIdInCreditCardBank();
        }

        String depositBankAccount = "";
        if (customer.getBillIdInCreditCardBank() == null){
            depositBankAccount = "не зарегистрирован";
        }else{
            depositBankAccount = customer.getBillIdInCreditCardBank();
        }


        Label creditBank = new Label("Счет в кредитном банк: " + creditBankAccount);
        Label depositBank = new Label("Счет в кредитном банк: " + depositBankAccount);

        Button registrationButton = new Button("Зарегистрироваться в банках");
        Button replenishAccountButton = new Button("Пополнить счет");
        registrationButton.setOnAction(event -> {
            try {
                mainWindow.setScene(getRegistrationCustomerInBanksScene(customer));
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });

        StackPane signInStackPante = new StackPane();
        signInStackPante.getChildren().addAll(nameLabel, creditBank, depositBank, registrationButton, replenishAccountButton);
        StackPane.setMargin(nameLabel, new Insets(5, 130, 290, 5));
        StackPane.setMargin(creditBank, new Insets(20, 130, 275, 5));
        StackPane.setMargin(depositBank, new Insets(35, 130, 260, 5));
        StackPane.setMargin(registrationButton, new Insets(100, 50, 180, 50));
        StackPane.setMargin(replenishAccountButton, new Insets(150, 50, 130, 50));

        return new Scene(signInStackPante, 300, 300);
    }

}
