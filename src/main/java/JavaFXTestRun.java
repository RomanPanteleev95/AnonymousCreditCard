import constants.Constant;
import entities.Message;
import entities.banks.Bank;
import entities.blocks.DoubleBlock;
import protocols.BankToBankProtocol;
import utils.DataBaseUtils;
import entities.Customer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import utils.MailNotification;
import utils.Utils;

import javax.mail.MessagingException;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class JavaFXTestRun extends Application {

    Stage mainWindow;
    Scene menuScene;
    Scene customerOperationsScene;

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        Utils.startRefill();
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        mainWindow = primaryStage;
        menuScene = menuScene();

        mainWindow.setScene(menuScene);
        mainWindow.show();

    }

    public Scene menuScene(){
        Label menuLable = new Label(Constant.UiText.MENU);

        Button createCustomerButton = new Button(Constant.UiText.SIGN_UP);
        Button customerOperations = new Button(Constant.UiText.SIGN_IN);


        createCustomerButton.setOnAction(e -> {
            Stage registrationWindow = new Stage();
            registrationWindow.setScene(registrationScene());
            registrationWindow.show();

        });
        customerOperations.setOnAction(event -> {
            Stage signInWindow = new Stage();
            signInWindow.setScene(signIn(signInWindow));
            signInWindow.show();
        });

        StackPane menuStackPane = new StackPane();
        menuStackPane.getChildren().addAll(menuLable, createCustomerButton, customerOperations);

        StackPane.setMargin(menuLable, new Insets(5, 50, 130, 50));
        StackPane.setMargin(createCustomerButton, new Insets(40, 50, 110, 50));
        StackPane.setMargin(customerOperations, new Insets(90, 50, 60, 50));

        return new Scene(menuStackPane, 300, 150);
    }

    public Scene registrationCustomerInBanksScene(Customer customer) throws SQLException, ClassNotFoundException {
        Label depositLabel = new Label(Constant.UiText.DEPOSIT_BANK);
        ChoiceBox<String> depositBankChoiceBox = new ChoiceBox<>();
        ResultSet depositBanks = DataBaseUtils.getAllDepositBanks();
        while (depositBanks.next()){
            depositBankChoiceBox.getItems().addAll(depositBanks.getString("name"));
        }

        Label creditLabel = new Label(Constant.UiText.CREDIT_BANK);
        ChoiceBox<String> creditBankChoiceBox = new ChoiceBox<>();
        ResultSet creditBanks = DataBaseUtils.getAllCreditBanks();
        while (creditBanks.next()){
            creditBankChoiceBox.getItems().addAll(creditBanks.getString("name"));
        }

        Button registrationButton = new Button(Constant.UiText.SIGN_UP);
        Button menuButton = new Button(Constant.UiText.MENU);

        registrationButton.setOnAction(event -> {
            try {
                Bank creditBank = DataBaseUtils.getBankByName(creditBankChoiceBox.getValue());
                Bank depositBank1 = DataBaseUtils.getBankByName(depositBankChoiceBox.getValue());
                Utils.registrationCustomer(customer, creditBank, depositBank1);
                Customer updatedCustomer = DataBaseUtils.getCustomerByID(customer.getId());
                mainWindow.setScene(getPersonalArea(updatedCustomer));
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        });

        menuButton.setOnAction(event -> {
            try {
                mainWindow.setScene(getPersonalArea(customer));
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
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

    public Scene customerOperations(Customer customer) throws SQLException, ClassNotFoundException {

        Label sum = new Label("Сумма");
        TextField moneyInput = new TextField();
        Button takeCreditButton = new Button("Пополнить");
        Button menuButton = new Button("Меню");

        takeCreditButton.setOnAction(event -> {
            String message = moneyInput.getText();
            try {
                Bank creditBank = DataBaseUtils.getBankByID(customer.getCreditBankId());
                DoubleBlock destinationBankDoubleBlock = DataBaseUtils.getCustomerDoubleBlockByBankId(customer.getId(), customer.getDepositBankId());
                BankToBankProtocol bankToBankProtocol = new BankToBankProtocol(creditBank, destinationBankDoubleBlock, new Message(message));
                bankToBankProtocol.runProtocol();
                mainWindow.setScene(getPersonalArea(customer));
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        });

        menuButton.setOnAction(event -> {
            try {
                mainWindow.setScene(getPersonalArea(customer));
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        });

        StackPane layout2 = new StackPane();
        layout2.getChildren().addAll(sum, moneyInput, takeCreditButton, menuButton);

        StackPane.setMargin(sum, new Insets(80, 50, 200, 50));
        StackPane.setMargin(moneyInput, new Insets(110, 50, 170, 50));
        StackPane.setMargin(takeCreditButton, new Insets(270, 50, 10, 50));
        StackPane.setMargin(menuButton, new Insets(270, 10, 10, 230));
        return new Scene(layout2, 300, 300);
    }

    public Scene registrationScene(){
        Label nameLabel = new Label(Constant.UiText.NAME);
        TextField nameInput = new TextField();

        Label emailLabel = new Label(Constant.UiText.EMAIL);
        TextField emailInput = new TextField();

        Button registrationButton = new Button(Constant.UiText.SIGN_UP);
        registrationButton.setOnAction(event -> {
            if (!emailInput.getText().isEmpty() && emailInput.getText() != null){
                String password = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 6);
                try {
                    MailNotification.sendPassword(emailInput.getText(), password);
                    DataBaseUtils.createNewCustomer(nameInput.getText(), emailInput.getText(), password);
                } catch (SQLException | MessagingException | IOException| ClassNotFoundException e) {
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

    public Scene signIn(Stage currentWindow){
        Label nameLabel = new Label(Constant.UiText.EMAIL);
        TextField emailInput = new TextField();

        Label passwordLabel = new Label(Constant.UiText.PASSWORD);
        TextField passwordInput = new TextField();

        Button signInButton = new Button(Constant.UiText.SIGN_IN);
        signInButton.setOnAction(event -> {
            try {
                Customer currentCustomer = DataBaseUtils.getCustomerByEmail(emailInput.getText());
                if (passwordInput.getText().equals(currentCustomer.getCustomerPassword())){
                    currentWindow.close();
                    mainWindow.setScene(getPersonalArea(currentCustomer));
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        StackPane signInStackPante = new StackPane();
        signInStackPante.getChildren().addAll(nameLabel, emailInput, passwordLabel, passwordInput, signInButton);
        StackPane.setMargin(nameLabel, new Insets(17, 50, 262, 50));
        StackPane.setMargin(emailInput, new Insets(50, 50, 230, 50));
        StackPane.setMargin(passwordLabel, new Insets(70, 50, 210, 50));
        StackPane.setMargin(passwordInput, new Insets(100, 50, 180, 50));
        StackPane.setMargin(signInButton, new Insets(150, 50, 130, 50));

        return new Scene(signInStackPante, 300, 300);
    }

    public Scene getPersonalArea(Customer customer) throws SQLException, ClassNotFoundException {
        Button registrationButton = new Button(Constant.UiText.REGISTRATION_IN_BANK);
        Button replenishAccountButton = new Button(Constant.UiText.REPLENISH_ACCOUNT);
        Label nameLabel = new Label(Constant.UiText.PERSONAL_AREA + customer.getName());

        Bank creditBank = new Bank();
        if (customer.getCreditBankId() != 0) {
            creditBank = DataBaseUtils.getBankByID(customer.getCreditBankId());
            registrationButton.setDisable(true);
        }else {
            replenishAccountButton.setDisable(true);
        }
        Bank depositBank = new Bank();
        if (customer.getDepositBankId() != 0) {
            depositBank = DataBaseUtils.getBankByID(customer.getDepositBankId());
        }

        Label creditBankLable = new Label(Constant.UiText.YOUR_CREDIT_BANK + (creditBank.getName() == null ? Constant.UiText.NOT_REGISTERED : creditBank.getName()));
        Label depositBankLable = new Label(Constant.UiText.YOUR_DEPOSIT_BANK + (depositBank.getName() == null ? Constant.UiText.NOT_REGISTERED : depositBank.getName()));

        registrationButton.setOnAction(event -> {
            try {
                mainWindow.setScene(registrationCustomerInBanksScene(customer));
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });

        replenishAccountButton.setOnAction(event -> {
            try {
                mainWindow.setScene(customerOperations(customer));
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });

        StackPane signInStackPante = new StackPane();
        signInStackPante.getChildren().addAll(nameLabel, creditBankLable, depositBankLable, registrationButton, replenishAccountButton);
        StackPane.setMargin(nameLabel, new Insets(5, 130, 290, 5));
        StackPane.setMargin(creditBankLable, new Insets(20, 130, 275, 5));
        StackPane.setMargin(depositBankLable, new Insets(35, 130, 260, 5));
        StackPane.setMargin(registrationButton, new Insets(100, 50, 180, 50));
        StackPane.setMargin(replenishAccountButton, new Insets(150, 50, 130, 50));

        return new Scene(signInStackPante, 300, 300);
    }

}
