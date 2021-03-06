import constants.Constant;
import entities.Location;
import entities.Message;
import entities.banks.Bank;
import entities.blocks.DoubleBlock;
import protocols.BankToBankProtocol;
import protocols.CustomerToBankProtocol;
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
import validators.ValidatorsUtils;

import javax.mail.MessagingException;
import javax.management.BadAttributeValueExpException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class Main extends Application {

    Stage mainWindow;
    Scene menuScene;

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        mainWindow = primaryStage;
        menuScene = menuScene();

        mainWindow.setScene(menuScene);
        mainWindow.setMinHeight(300);
        mainWindow.setMaxHeight(300);
        mainWindow.setMinWidth(300);
        mainWindow.setMaxWidth(300);
        mainWindow.show();

    }

    public Scene menuScene(){
        Label menuLable = new Label(Constant.UiText.MENU);

        Button createCustomerButton = new Button(Constant.UiText.SIGN_UP);
        Button customerOperations = new Button(Constant.UiText.SIGN_IN);


        createCustomerButton.setOnAction(e -> {
            Stage registrationWindow = new Stage();
            registrationWindow.setScene(registrationScene(registrationWindow));
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

    public Scene registrationScene(Stage registrationWindow){
        Label nameLabel = new Label(Constant.UiText.NAME);
        TextField nameInput = new TextField();
        nameInput.setPromptText("Введите Ваше имя");

        Label emailLabel = new Label(Constant.UiText.EMAIL);
        TextField emailInput = new TextField();
        emailInput.setPromptText("Введите Ваш e-mail");

        Button registrationButton = new Button(Constant.UiText.SIGN_UP);
        registrationButton.setOnAction(event -> {
            if (!emailInput.getText().isEmpty() && emailInput.getText() != null){
                try {
                    Customer currentCustomer = DataBaseUtils.getCustomerByEmail(emailInput.getText());
                    if (currentCustomer.getId() != 0){
                        alreadyExistErrorStage();
                    } else {
                        String password = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 6);
                        MailNotification.sendPassword(emailInput.getText(), password);
                        DataBaseUtils.createNewCustomer(nameInput.getText(), emailInput.getText(), Utils.getHash(password));
                        successRegistrationScene(registrationWindow);
                    }
                } catch (SQLException | IOException| ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (MessagingException e) {
                    errorStageEmail();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
            }else {
                errorStage();
            }
        });

        StackPane registrationCustomerStackPante = new StackPane();
        registrationCustomerStackPante.getChildren().addAll(nameLabel, nameInput, emailLabel, emailInput, registrationButton);
        StackPane.setMargin(nameLabel, new Insets(17, 50, 262, 50));
        StackPane.setMargin(nameInput, new Insets(50, 50, 230, 50));
        StackPane.setMargin(emailLabel, new Insets(70, 50, 210, 50));
        StackPane.setMargin(emailInput, new Insets(100, 50, 180, 50));
        StackPane.setMargin(registrationButton, new Insets(200, 50, 80, 50));

        return new Scene(registrationCustomerStackPante, 300, 300);
    }

    public Scene signIn(Stage currentWindow){
        Label nameLabel = new Label(Constant.UiText.EMAIL);
        TextField emailInput = new TextField();
        emailInput.setPromptText("Ваш e-mail");

        Label passwordLabel = new Label(Constant.UiText.PASSWORD);
        PasswordField passwordInput = new PasswordField();
        passwordInput.setPromptText("Ваш пароль");

        Button signInButton = new Button(Constant.UiText.SIGN_IN);
        signInButton.setOnAction(event -> {
            try {
                if (emailInput.getText().isEmpty() || emailInput.getText() == null || passwordInput.getText().isEmpty() || passwordInput.getText() == null){
                    errorStage();
                } else {
                    Customer currentCustomer = DataBaseUtils.getCustomerByEmail(emailInput.getText());
                    String inputPassword = passwordInput.getText();
                    if ( Utils.getHash(inputPassword).equals(currentCustomer.getCustomerPassword())) {
                        currentWindow.close();
                        mainWindow.setScene(personalArea(currentCustomer));
                        mainWindow.setMinHeight(450);
                        mainWindow.setMaxHeight(450);
                        mainWindow.setMinWidth(300);
                        mainWindow.setMaxWidth(300);
                    }
                    else{
                        errorStageEmailOrPassword();
                    }
                }
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        });

        StackPane signInStackPante = new StackPane();
        signInStackPante.getChildren().addAll(nameLabel, emailInput, passwordLabel, passwordInput, signInButton);
        StackPane.setMargin(nameLabel, new Insets(17, 50, 262, 50));
        StackPane.setMargin(emailInput, new Insets(50, 50, 230, 50));
        StackPane.setMargin(passwordLabel, new Insets(70, 50, 210, 50));
        StackPane.setMargin(passwordInput, new Insets(100, 50, 180, 50));
//        StackPane.setMargin(privateKeyLabel, new Insets(120, 50, 150, 50));
//        StackPane.setMargin(privateKeyInput, new Insets(150, 50, 130, 50));
        StackPane.setMargin(signInButton, new Insets(200, 50, 80, 50));

        return new Scene(signInStackPante, 300, 300);
    }

    public Scene personalArea(Customer customer) throws SQLException, ClassNotFoundException {
        Button registrationButton = new Button(Constant.UiText.REGISTRATION_IN_BANK);
        Button replenishAccountButton = new Button(Constant.UiText.REPLENISH_ACCOUNT);
        Button payForPurchase = new Button(Constant.UiText.PAY_FOR_PURCHASE);
        Label nameLabel = new Label(Constant.UiText.PERSONAL_AREA + customer.getName());

        Bank creditBank = new Bank();
        if (customer.getCreditBankId() != 0) {
            creditBank = DataBaseUtils.getBankByID(customer.getCreditBankId());
            registrationButton.setDisable(true);
        }else {
            replenishAccountButton.setDisable(true);
            payForPurchase.setDisable(true);
        }
        Bank depositBank = new Bank();
        if (customer.getDepositBankId() != 0) {
            depositBank = DataBaseUtils.getBankByID(customer.getDepositBankId());
        }

        Label creditBankLable = new Label(Constant.UiText.YOUR_CREDIT_BANK + (creditBank.getName() == null ? Constant.UiText.NOT_REGISTERED : creditBank.getName()));
        Label depositBankLable = new Label(Constant.UiText.YOUR_DEPOSIT_BANK + (depositBank.getName() == null ? Constant.UiText.NOT_REGISTERED : depositBank.getName()));
        Label carrentBalance = new Label(Constant.UiText.CURRENT_BALANCE + String.format("%.2f", DataBaseUtils.getCuurentBalance(customer)) + " рублей");

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

        payForPurchase.setOnAction(event -> {
            try {
                mainWindow.setScene(payFroPurchaseScene(customer));
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });

        StackPane signInStackPante = new StackPane();
        signInStackPante.getChildren().addAll(nameLabel, creditBankLable, depositBankLable, carrentBalance, registrationButton, replenishAccountButton, payForPurchase);
        StackPane.setMargin(nameLabel, new Insets(5, 0, 290, 5));
        StackPane.setMargin(creditBankLable, new Insets(20, 0, 275, 5));
        StackPane.setMargin(depositBankLable, new Insets(35, 0, 260, 5));
        StackPane.setMargin(carrentBalance, new Insets(50, 0, 245, 5));
        StackPane.setMargin(registrationButton, new Insets(100, 50, 180, 50));
        StackPane.setMargin(replenishAccountButton, new Insets(150, 50, 130, 50));
        StackPane.setMargin(payForPurchase, new Insets(200, 50, 80, 50));

        return new Scene(signInStackPante, 300, 450);
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

        Label nameLabel = new Label("Придумайте код для совершения операций (6 символов)");
        PasswordField codeInput = new PasswordField();

        registrationButton.setOnAction(event -> {
            try {
                if (depositBankChoiceBox.getValue().isEmpty() || depositBankChoiceBox.getValue() == null || creditBankChoiceBox.getValue().isEmpty()
                        || creditBankChoiceBox.getValue() == null || codeInput.getText().isEmpty() || codeInput.getText() == null){
                    errorStage();
                }
                Bank creditBank = DataBaseUtils.getBankByName(creditBankChoiceBox.getValue());
                Bank depositBank1 = DataBaseUtils.getBankByName(depositBankChoiceBox.getValue());
                Utils.registrationCustomer(customer, creditBank, depositBank1, codeInput.getText());
                Customer updatedCustomer = DataBaseUtils.getCustomerByID(customer.getId());
                mainWindow.setScene(personalArea(updatedCustomer));
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            } catch (BadAttributeValueExpException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        menuButton.setOnAction(event -> {
            try {
                mainWindow.setScene(personalArea(customer));
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        });

        StackPane createCustomerStackPane = new StackPane();
        createCustomerStackPane.getChildren().addAll(depositLabel, depositBankChoiceBox, creditLabel, creditBankChoiceBox, registrationButton, menuButton, nameLabel, codeInput);
        StackPane.setMargin(depositLabel, new Insets(70, 50, 360, 50));
        StackPane.setMargin(depositBankChoiceBox, new Insets(100, 50, 330, 50));
        StackPane.setMargin(creditLabel, new Insets(120, 50, 310, 50));
        StackPane.setMargin(creditBankChoiceBox, new Insets(150, 50, 280, 50));
        StackPane.setMargin(nameLabel, new Insets(180, 0, 250, 0));
        StackPane.setMargin(codeInput, new Insets(200, 50, 230, 50));
        StackPane.setMargin(registrationButton, new Insets(250, 50, 180, 50));
        StackPane.setMargin(menuButton, new Insets(350, 0, 80, 0));

        return new Scene(createCustomerStackPane, 300, 450);
    }

    public Scene customerOperations(Customer customer) throws SQLException, ClassNotFoundException {

        Label sum = new Label("Сумма (руб.)");
        TextField moneyInput = new TextField();
        Button takeCreditButton = new Button("Пополнить");
        Button menuButton = new Button("Меню");

        takeCreditButton.setOnAction(event -> {
            String message = moneyInput.getText();
            if (message.isEmpty() || message == null){
                errorStage();
            }else {
                try {
                    Bank creditBank = DataBaseUtils.getBankByID(customer.getCreditBankId());
                    DoubleBlock destinationBankDoubleBlock = DataBaseUtils.getCustomerDoubleBlockByBankId(customer.getId(), customer.getDepositBankId());
                    BankToBankProtocol bankToBankProtocol = new BankToBankProtocol(creditBank, destinationBankDoubleBlock, new Message(message));
                    bankToBankProtocol.runProtocol();
                    mainWindow.setScene(personalArea(customer));
                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

        menuButton.setOnAction(event -> {
            try {
                mainWindow.setScene(personalArea(customer));
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        });

        StackPane layout2 = new StackPane();
        layout2.getChildren().addAll(sum, moneyInput, takeCreditButton, menuButton);

        StackPane.setMargin(sum, new Insets(80, 50, 350, 50));
        StackPane.setMargin(moneyInput, new Insets(110, 50, 320, 50));
        StackPane.setMargin(takeCreditButton, new Insets(270, 0, 160, 0));
        StackPane.setMargin(menuButton, new Insets(270, 10, 160, 180));
        return new Scene(layout2, 300, 450);
    }

    public Scene payFroPurchaseScene(Customer customer) throws SQLException, ClassNotFoundException {
        Label sum = new Label("Сумма покупки (руб.)");
        TextField moneyInput = new TextField();
        Button payButton = new Button("Оплатить");
        Button menuButton = new Button("Меню");
        ChoiceBox<String> locationChoiceBox = new ChoiceBox<>();
        List<Location> locations = DataBaseUtils.getAllLocation();
        for (Location location : locations){
            locationChoiceBox.getItems().addAll(location.getLocationName());
        }

        Label nameLabel = new Label("Введите код для совершения операций (6 символов)");
        PasswordField codeInput = new PasswordField();

        payButton.setOnAction(event -> {
            String message = moneyInput.getText();
            try {
                if (message.isEmpty() || message == null || locationChoiceBox.getValue().isEmpty() || locationChoiceBox.getValue() == null){
                    errorStage();
                } else {
                    if (!ValidatorsUtils.checkMoneyFormat(message)) {
                        wrongMoneyFormatErrorStage();
                    } else {
                        if (Float.parseFloat(message) > DataBaseUtils.getCuurentBalance(customer)) {
                            smallBalance();
                        } else {
                            Bank depositBank = DataBaseUtils.getBankByID(customer.getDepositBankId());
                            Location currentLocation = DataBaseUtils.getLocationByName(locationChoiceBox.getValue());
                            CustomerToBankProtocol customerToBankProtocol = new CustomerToBankProtocol(currentLocation, depositBank, customer, new Message(message));
                            customerToBankProtocol.runProtocol(codeInput.getText());
                            DoubleBlock locationDoubleBlock = DataBaseUtils.getLocationDoubleBlockByBankId(currentLocation.getLocationId(), currentLocation.getDepositBankId());
                            BankToBankProtocol bankToBankProtocol = new BankToBankProtocol(depositBank, locationDoubleBlock, new Message(message));
                            bankToBankProtocol.runProtocol();
                            mainWindow.setScene(personalArea(customer));
                        }
                    }
                }
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NumberFormatException e){
                    errorStagePK();
                }
        });

        menuButton.setOnAction(event -> {
            try {
                mainWindow.setScene(personalArea(customer));
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        });

        StackPane layout2 = new StackPane();
        layout2.getChildren().addAll(locationChoiceBox, sum, moneyInput, nameLabel, codeInput, payButton, menuButton);

        StackPane.setMargin(locationChoiceBox, new Insets(50, 50, 380, 50));
        StackPane.setMargin(sum, new Insets(80, 50, 350, 50));
        StackPane.setMargin(moneyInput, new Insets(110, 50, 320, 50));
        StackPane.setMargin(nameLabel, new Insets(140, 0, 290, 0));
        StackPane.setMargin(codeInput, new Insets(160, 50, 270, 50));
        StackPane.setMargin(payButton, new Insets(270, 0, 160, 0));
        StackPane.setMargin(menuButton, new Insets(270, 10, 160, 180));
        return new Scene(layout2, 300, 450);
    }

    public void errorStage(){
        Stage errorWindow = new Stage();
        Label errorLable = new Label("Заполните все поля!");
        Button setDepositBankNameButton = new Button("OK");

        setDepositBankNameButton.setOnAction(event1 -> {
            errorWindow.close();
        });

        StackPane errorStackPane = new StackPane();
        errorStackPane.getChildren().addAll(errorLable, setDepositBankNameButton);
        StackPane.setMargin(errorLable, new Insets(5, 10, 90, 10));
        StackPane.setMargin(setDepositBankNameButton, new Insets(45, 20, 45, 20));
        errorWindow.setScene(new Scene(errorStackPane, 300, 100));
        errorWindow.show();

    }

    public void errorStageEmail(){
        Stage errorWindow = new Stage();
        Label errorLable = new Label("Неправильно введенный e-mail!");
        Button setDepositBankNameButton = new Button("OK");

        setDepositBankNameButton.setOnAction(event1 -> {
            errorWindow.close();
        });

        StackPane errorStackPane = new StackPane();
        errorStackPane.getChildren().addAll(errorLable, setDepositBankNameButton);
        StackPane.setMargin(errorLable, new Insets(5, 10, 90, 10));
        StackPane.setMargin(setDepositBankNameButton, new Insets(45, 20, 45, 20));
        errorWindow.setScene(new Scene(errorStackPane, 300, 100));
        errorWindow.show();

    }

    public void errorStageEmailOrPassword(){
        Stage errorWindow = new Stage();
        Label errorLable = new Label("Неправильно введенный e-mail или пароль!");
        Button setDepositBankNameButton = new Button("OK");

        setDepositBankNameButton.setOnAction(event1 -> {
            errorWindow.close();
        });

        StackPane errorStackPane = new StackPane();
        errorStackPane.getChildren().addAll(errorLable, setDepositBankNameButton);
        StackPane.setMargin(errorLable, new Insets(5, 10, 90, 10));
        StackPane.setMargin(setDepositBankNameButton, new Insets(45, 20, 45, 20));
        errorWindow.setScene(new Scene(errorStackPane, 300, 100));
        errorWindow.show();

    }

    public void alreadyExistErrorStage(){
        Stage errorWindow = new Stage();
        Label errorLable = new Label("Данный email уже зарегистрирован");
        Button setDepositBankNameButton = new Button("OK");

        setDepositBankNameButton.setOnAction(event1 -> {
            errorWindow.close();
        });

        StackPane errorStackPane = new StackPane();
        errorStackPane.getChildren().addAll(errorLable, setDepositBankNameButton);
        StackPane.setMargin(errorLable, new Insets(5, 10, 40, 10));
        StackPane.setMargin(setDepositBankNameButton, new Insets(15, 50, 10, 50));
        errorWindow.setScene(new Scene(errorStackPane, 300, 50));
        errorWindow.show();
    }

    public void wrongPrivateKeyErrorStage(){
        Stage errorWindow = new Stage();
        Label errorLable = new Label("Неверный формат личного ключа");
        Button setDepositBankNameButton = new Button("OK");

        setDepositBankNameButton.setOnAction(event1 -> {
            errorWindow.close();
        });

        StackPane errorStackPane = new StackPane();
        errorStackPane.getChildren().addAll(errorLable, setDepositBankNameButton);
        StackPane.setMargin(errorLable, new Insets(5, 10, 40, 10));
        StackPane.setMargin(setDepositBankNameButton, new Insets(15, 50, 10, 50));
        errorWindow.setScene(new Scene(errorStackPane, 300, 50));
        errorWindow.show();
    }

    public void wrongMoneyFormatErrorStage(){
        Stage errorWindow = new Stage();
        Label errorLable = new Label("Неверный денежный формат");
        Button setDepositBankNameButton = new Button("OK");

        setDepositBankNameButton.setOnAction(event1 -> {
            errorWindow.close();
        });

        StackPane errorStackPane = new StackPane();
        errorStackPane.getChildren().addAll(errorLable, setDepositBankNameButton);
        StackPane.setMargin(errorLable, new Insets(5, 10, 40, 10));
        StackPane.setMargin(setDepositBankNameButton, new Insets(15, 50, 10, 50));
        errorWindow.setScene(new Scene(errorStackPane, 300, 50));
        errorWindow.show();
    }

    public void smallBalance(){
        Stage errorWindow = new Stage();
        Label errorLable = new Label("На вашем ссчету недостаточно средств!");
        Button setDepositBankNameButton = new Button("OK");

        setDepositBankNameButton.setOnAction(event1 -> {
            errorWindow.close();
        });

        StackPane errorStackPane = new StackPane();
        errorStackPane.getChildren().addAll(errorLable, setDepositBankNameButton);
        StackPane.setMargin(errorLable, new Insets(5, 10, 40, 10));
        StackPane.setMargin(setDepositBankNameButton, new Insets(15, 50, 10, 50));
        errorWindow.setScene(new Scene(errorStackPane, 300, 50));
        errorWindow.show();
    }

    public void successRegistrationScene(Stage registrationWindow){
        Stage errorWindow = new Stage();
        Label errorLable = new Label("Письмо с паролем отправлено на почту!");
        Button setDepositBankNameButton = new Button("OK");

        setDepositBankNameButton.setOnAction(event1 -> {
            errorWindow.close();
            registrationWindow.close();
        });

        StackPane errorStackPane = new StackPane();
        errorStackPane.getChildren().addAll(errorLable, setDepositBankNameButton);
        StackPane.setMargin(errorLable, new Insets(10, 10, 85, 10));
        StackPane.setMargin(setDepositBankNameButton, new Insets(45, 20, 45, 20));
        errorWindow.setScene(new Scene(errorStackPane, 250, 100));
        errorWindow.show();
    }

    public void errorStagePK(){
        Stage errorWindow = new Stage();
        Label errorLable = new Label("Неправильно введенный код");
        Button setDepositBankNameButton = new Button("OK");

        setDepositBankNameButton.setOnAction(event1 -> {
            errorWindow.close();
        });

        StackPane errorStackPane = new StackPane();
        errorStackPane.getChildren().addAll(errorLable, setDepositBankNameButton);
        StackPane.setMargin(errorLable, new Insets(5, 10, 90, 10));
        StackPane.setMargin(setDepositBankNameButton, new Insets(45, 20, 45, 20));
        errorWindow.setScene(new Scene(errorStackPane, 300, 100));
        errorWindow.show();

    }
}
