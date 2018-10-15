import db.DataBaseAnalog;
import entities.Customer;
import entities.Intermediary;
import entities.Message;
import entities.banks.CreditCardBank;
import entities.banks.DepositBank;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import protocols.BankToBankProtocol;
import utils.Utils;

import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class JavaFXTestRun extends Application {

    Stage window;
    Scene menuScene;
    Scene showLogs;
    Scene createCustomerScene;
    Scene createDepositBankScene;
    Scene createCreditBankScene;
    Scene registrationDepositBankScene;
    Scene registrationCreditBankScene;
    Scene registrationCustomerScene;
    Scene customerOperationsScene;

    DataBaseAnalog dataBaseAnalog = DataBaseAnalog.getDataBaseAnalog();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        window = primaryStage;
        menuScene = getMenuScene();
        showLogs = showLogs();
        createCustomerScene = createCustomer();
        createDepositBankScene = createDepositBank();
        createCreditBankScene = createCrediBank();
        registrationDepositBankScene = registrationDepositBank();
        registrationCreditBankScene = registrationCreditBankScene();
        registrationCustomerScene = registrationCustomer();
        customerOperationsScene = customerOperations();

        window.setScene(menuScene);
        window.show();

    }

    public Scene getMenuScene(){
        Label menuLable = new Label("Menu");

        Button createCustomerButton = new Button("Create customer");
        Button createDepositBankButton = new Button("Create deposit bank");
        Button createCreditCardBankButton = new Button("Create credit card bank");
        Button registrationDepositBankButton = new Button("Registration deposit bank");
        Button registrationCreditCardBankButton = new Button("Registration credit card bank");
        Button registrationCustomerButton = new Button("Registration customer");
        Button customerOperations = new Button("Customer operations");
        Button showLogs = new Button("Show logs");


        createCustomerButton.setOnAction(e -> window.setScene(createCustomerScene));
        createDepositBankButton.setOnAction(event -> window.setScene(createDepositBankScene));
        createCreditCardBankButton.setOnAction(event -> window.setScene(createCreditBankScene));
        registrationDepositBankButton.setOnAction(event -> window.setScene(registrationDepositBankScene));
        registrationCreditCardBankButton.setOnAction(event -> window.setScene(registrationCreditBankScene));
        registrationCustomerButton.setOnAction(event -> window.setScene(registrationCustomerScene));
        customerOperations.setOnAction(event -> window.setScene(customerOperationsScene));
        showLogs.setOnAction(event -> window.setScene(this.showLogs));

        StackPane menuStackPane = new StackPane();
        menuStackPane.getChildren().addAll(menuLable, createCustomerButton, createDepositBankButton, createCreditCardBankButton,
                registrationDepositBankButton, registrationCreditCardBankButton, registrationCustomerButton, customerOperations, showLogs);

        StackPane.setMargin(menuLable, new Insets(5, 50, 380, 50));
        StackPane.setMargin(createCustomerButton, new Insets(40, 50, 360, 50));
        StackPane.setMargin(createDepositBankButton, new Insets(80, 50, 320, 50));
        StackPane.setMargin(createCreditCardBankButton, new Insets(120, 50, 280, 50));
        StackPane.setMargin(registrationDepositBankButton, new Insets(160, 50, 240, 50));
        StackPane.setMargin(registrationCreditCardBankButton, new Insets(200, 50, 200, 50));
        StackPane.setMargin(registrationCustomerButton, new Insets(240, 50, 160, 50));
        StackPane.setMargin(customerOperations, new Insets(280, 50, 120, 50));
        StackPane.setMargin(showLogs, new Insets(380, 10, 20, 210));

        return new Scene(menuStackPane, 300, 400);
    }

    public Scene showLogs(){
        Label allCustomers = new Label("All customers");
        TextArea outputArea = new TextArea();
        Button showCustomers = new Button("Show customers");
        showCustomers.setMinWidth(120);
        Button showDepositBanks = new Button("Show deposit banks");
        showDepositBanks.setMinWidth(120);
        Button showCreditBanks = new Button("Show credit banks");
        showCreditBanks.setMinWidth(120);
        Button showCustomerBill = new Button("Show customer's bill");
        showCustomerBill.setMinWidth(120);
        Button menuButton = new Button("Menu");

        showCustomers.setOnAction(event -> {
            outputArea.clear();
            Map<String, Customer> customersMap = dataBaseAnalog.getCustomers();
            for (String s : customersMap.keySet()){
                Customer customer = customersMap.get(s);
                outputArea.appendText(s + " - ");
                if (customer.getBillIdInDepositBank() == null){
                    outputArea.appendText("not registered \n");
                }else {
                    outputArea.appendText("shared_key_with_deposit_bank(" + customer.getSecretKeySharedWithDepositBank() + "),"
                    + "Bill_id_in_deposit_bank(" + customer.getBillIdInDepositBank() + ")," + "Bill_id_in_credit_card_bank(" + customer.getBillIdInCreditCardBank() + ");\n");
                }
            }
        });

        menuButton.setOnAction(event -> {
            window.setScene(menuScene);
            outputArea.clear();
        });

        showDepositBanks.setOnAction(event -> {
            outputArea.clear();
            Set<String> depositBanksId = dataBaseAnalog.getDepositBanks().keySet();
            for (String s : depositBanksId){
                outputArea.appendText(s + "\n");
            }
        });

        showCreditBanks.setOnAction(event -> {
            outputArea.clear();
            Set<String> creditBanksId = dataBaseAnalog.getCreditCradBanks().keySet();
            for (String s : creditBanksId){
                outputArea.appendText(s + "\n");
            }
        });

        showCustomerBill.setOnAction(event -> {
            outputArea.clear();

            {
                Stage bankWindow = new Stage();
                TextArea depositBankInput = new TextArea();
                Button setDepositBankNameButton = new Button("Select");
                StackPane tmpLayout = new StackPane();

                setDepositBankNameButton.setOnAction(innerEvent -> {
                    String depositBankName = depositBankInput.getText();
                    DepositBank depositBank = (DepositBank) dataBaseAnalog.getDepositBank(depositBankName);
                    if (!dataBaseAnalog.getDepositBanks().containsKey(depositBankName)){
                        outputArea.appendText("Deposit bank is absent!");
                        bankWindow.close();
                        return;
                    }
                    Map<String, String> customersMoney = depositBank.getCustomerMoney();
                    if (customersMoney.size() == 0){
                        outputArea.appendText("Bank hasn't accounts");
                        bankWindow.close();
                        return;
                    }
                    for (String s : customersMoney.keySet()) {
                        outputArea.appendText("Customer " + s + " | Money: " + customersMoney.get(s));
                    }
                    bankWindow.close();
                });

                tmpLayout.getChildren().addAll(depositBankInput, setDepositBankNameButton);
                StackPane.setMargin(depositBankInput, new Insets(5, 5, 50, 5));
                StackPane.setMargin(setDepositBankNameButton, new Insets(70, 50, 20, 50));
                Scene tpmScene = new Scene(tmpLayout, 200, 100);
                bankWindow.setScene(tpmScene);
                bankWindow.show();
            }

        });


        StackPane layout1 = new StackPane();
        layout1.getChildren().addAll(allCustomers, outputArea, showCustomers, menuButton, showDepositBanks, showCreditBanks, showCustomerBill);

        StackPane.setMargin(allCustomers, new Insets(5, 50, 280, 50));
        StackPane.setMargin(outputArea, new Insets(10, 10, 150, 150));
        StackPane.setMargin(showCustomers, new Insets(10, 460, 550, 10));
        StackPane.setMargin(showDepositBanks, new Insets(50, 460, 510, 10));
        StackPane.setMargin(showCreditBanks, new Insets(90, 460, 470, 10));
        StackPane.setMargin(showCustomerBill, new Insets(130, 460, 430, 10));
        StackPane.setMargin(menuButton, new Insets(550, 10, 10, 540));

        return new Scene(layout1, 600, 600);
    }

    public Scene createCustomer(){
        Label nameLabel = new Label("Customer name");
        TextArea nameInput = new TextArea();
        Button createButton = new Button("Create and go back");
        Button menuButton = new Button("Menu");

        createButton.setOnAction(event -> {
            String customerName = nameInput.getText();
            Utils.createCustomer(customerName);
            nameInput.clear();
            window.setScene(menuScene);
        });
        menuButton.setOnAction(event -> {
            window.setScene(menuScene);
            nameInput.clear();
        });

        StackPane createCustomerStackPane = new StackPane();
        createCustomerStackPane.getChildren().addAll(nameLabel, nameInput, createButton, menuButton);

        StackPane.setMargin(nameLabel, new Insets(17, 50, 262, 50));
        StackPane.setMargin(nameInput, new Insets(50, 50, 230, 50));
        StackPane.setMargin(createButton, new Insets(80, 50, 120, 50));
        StackPane.setMargin(menuButton, new Insets(270, 10, 10, 230));

        return new Scene(createCustomerStackPane, 300, 300);
    }

    public Scene createDepositBank(){
        Label nameLabel = new Label("Deposit bank name");
        TextArea nameInput = new TextArea();
        Button createDepositBunkButton = new Button("Create and go back");
        Button menuButton = new Button("Menu");
        createDepositBunkButton.setOnAction(event -> {
            String depositBankName = nameInput.getText();
            Utils.createDepositBank(depositBankName);
            nameInput.clear();
            window.setScene(menuScene);
        });
        menuButton.setOnAction(event -> {
            window.setScene(menuScene);
            nameInput.clear();
        });

        StackPane layout2 = new StackPane();
        layout2.getChildren().addAll(nameLabel, nameInput, createDepositBunkButton, menuButton);
        StackPane.setMargin(nameLabel, new Insets(17, 50, 262, 50));
        StackPane.setMargin(nameInput, new Insets(50, 50, 200, 50));
        StackPane.setMargin(createDepositBunkButton, new Insets(80, 50, 120, 50));
        StackPane.setMargin(menuButton, new Insets(270, 10, 10, 230));
        return new Scene(layout2, 300, 300);
    }

    public Scene createCrediBank(){
        Label nameLabel = new Label("Credit bank name");
        TextArea nameInput = new TextArea();
        Button createCreditBankButton = new Button("Create and go back");
        Button menuButton = new Button("Menu");
        createCreditBankButton.setOnAction(event -> {
            String creditBankName = nameInput.getText();
            Utils.createCreditCardBank(creditBankName);
            nameInput.clear();
            window.setScene(menuScene);
        });
        menuButton.setOnAction(event -> {
            window.setScene(menuScene);
            nameInput.clear();
        });

        StackPane layout2 = new StackPane();
        layout2.getChildren().addAll(nameLabel, nameInput, createCreditBankButton, menuButton);
        StackPane.setMargin(nameLabel, new Insets(17, 50, 262, 50));
        StackPane.setMargin(nameInput, new Insets(50, 50, 200, 50));
        StackPane.setMargin(createCreditBankButton, new Insets(80, 50, 120, 50));
        StackPane.setMargin(menuButton, new Insets(270, 10, 10, 230));
        return new Scene(layout2, 300, 300);
    }

    public Scene registrationDepositBank(){
        Label nameLabel = new Label("Deposit bank name");
        TextArea nameInput = new TextArea();
        Button button2 = new Button("Create and go back");
        Button menuButton = new Button("Menu");
        button2.setOnAction(event -> {
            String depositBankName = nameInput.getText();
            Utils.registrationBank(dataBaseAnalog.getDepositBank(depositBankName));
            nameInput.clear();
            window.setScene(menuScene);
        });
        menuButton.setOnAction(event -> {
            window.setScene(menuScene);
            nameInput.clear();
        });

        StackPane layout2 = new StackPane();
        layout2.getChildren().addAll(nameLabel, nameInput, button2, menuButton);
        StackPane.setMargin(nameLabel, new Insets(17, 50, 262, 50));
        StackPane.setMargin(nameInput, new Insets(50, 50, 200, 50));
        StackPane.setMargin(button2, new Insets(80, 50, 120, 50));
        StackPane.setMargin(menuButton, new Insets(270, 10, 10, 230));
        return new Scene(layout2, 300, 300);
    }

    public Scene registrationCreditBankScene(){
        Label nameLabel = new Label("Credit bank name");
        TextArea nameInput = new TextArea();
        Button button2 = new Button("Create and go back");
        Button menuButton = new Button("Menu");
        button2.setOnAction(event -> {
            String creditBankName = nameInput.getText();
            Utils.registrationBank(dataBaseAnalog.getCreditCardBank(creditBankName));
            nameInput.clear();
            window.setScene(menuScene);
        });
        menuButton.setOnAction(event -> {
            window.setScene(menuScene);
            nameInput.clear();
        });

        StackPane layout2 = new StackPane();
        layout2.getChildren().addAll(nameLabel, nameInput, button2, menuButton);
        StackPane.setMargin(nameLabel, new Insets(17, 50, 262, 50));
        StackPane.setMargin(nameInput, new Insets(50, 50, 200, 50));
        StackPane.setMargin(button2, new Insets(80, 50, 120, 50));
        StackPane.setMargin(menuButton, new Insets(270, 10, 10, 230));
        return new Scene(layout2, 300, 300);
    }

    public Scene registrationCustomer(){
        Label nameCustomerLabel = new Label("Customer name");
        Label nameDepositLabel = new Label("Deposit bank name");
        Label nameCreditLabel = new Label("Credit bank name");
        TextArea customerNameInput = new TextArea();
        TextArea depositBankNameInput = new TextArea();
        TextArea creditBankNameInput = new TextArea();
        Button registrationCustomerButton = new Button("Registration customer and go back");
        Button menuButton = new Button("Menu");

        registrationCustomerButton.setOnAction(event -> {
            String customerName = customerNameInput.getText();
            String depositBankName = depositBankNameInput.getText();
            String creditBankName = creditBankNameInput.getText();
            Utils.registrationCustomer(dataBaseAnalog.getCustomer(customerName), dataBaseAnalog.getCreditCardBank(creditBankName), dataBaseAnalog.getDepositBank(depositBankName));
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
        layout2.getChildren().addAll(nameCustomerLabel, nameDepositLabel, nameCreditLabel, customerNameInput, depositBankNameInput,
                creditBankNameInput, registrationCustomerButton, menuButton);
        StackPane.setMargin(nameCustomerLabel, new Insets(30, 50, 280, 50));
        StackPane.setMargin(nameDepositLabel, new Insets(80, 50, 200, 50));
        StackPane.setMargin(nameCreditLabel, new Insets(130, 50, 130, 50));
        StackPane.setMargin(customerNameInput, new Insets(50, 50, 230, 50));
        StackPane.setMargin(depositBankNameInput, new Insets(100, 50, 160, 50));
        StackPane.setMargin(creditBankNameInput, new Insets(170, 50, 100, 50));
        StackPane.setMargin(registrationCustomerButton, new Insets(250, 50, 80, 50));
        StackPane.setMargin(menuButton, new Insets(270, 10, 10, 230));
        return new Scene(layout2, 300, 300);


    }

    public Scene customerOperations(){
        Label nameCustomerLabel = new Label("Customer name");
        Label nameDepositLabel = new Label("Deposit bank name");
        Label nameCreditLabel = new Label("Credit bank name");
        Label sum = new Label("Sum");
        TextArea customerNameInput = new TextArea();
        TextArea creditBankNameInput = new TextArea();
        TextArea depositBankNameInput = new TextArea();
        TextArea moneyInput = new TextArea();
        Button takeCreditButton = new Button("Take credit");
        Button menuButton = new Button("Menu");

        takeCreditButton.setOnAction(event -> {
            String customerName = customerNameInput.getText();
            String creditBankName = creditBankNameInput.getText();
            String depositBankName = depositBankNameInput.getText();
            String message = moneyInput.getText();

            BankToBankProtocol bankToBankProtocol = new BankToBankProtocol(dataBaseAnalog.getCustomer(customerName), dataBaseAnalog.getCreditCardBank(creditBankName),
                    dataBaseAnalog.getDepositBank(depositBankName), new Message(message));
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
