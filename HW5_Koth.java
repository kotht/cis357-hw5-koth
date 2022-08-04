package main.hw5_koth;

import javafx.application.Application;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * Driver Class used display GUI and to implement it with OOP concepts implemented in hw4.
 */
public class HW5_Koth extends Application{
    /** Member Variables */
    private String name = "The Koth Register System";
    private ProductCatalog catalog;
    private Register register;
    private String filename = "itemsTable.txt";
    ObservableMap<String,ProductSpecification> itemMap;

    // Koth Register elements
    private Label lblWelcome = new Label("Welcome to "+name+"!!!");
    private Button btnNewSale = new Button("New Sale");
    private Button btnItemData = new Button("ItemData");
    private Label lblTotalSale = new Label("Total sale for the day is: $0.00");

    // New Sale elements
    private Label lblItemId = new Label("Item ID:");
    private ChoiceBox<String> cbItems = new ChoiceBox();
    private Label lblItemName = new Label("NA");
    private Label lblItemPrice = new Label("$0.00");
    private TextField tfQuantity = new TextField();
    private Label lblItemTotal = new Label("$0.00");
    private Button btnAdd = new Button("Add");

    private TextArea taReceipt = new TextArea();

    private ListView<String> lvReceipt =  new ListView<>();

    private Label lblSaleSubTotal = new Label("$0.00");
    private Label lblSaleTaxTotal = new Label("$0.00");
    private TextField tfAmtTendered = new TextField();
    private Button btnCheckout = new Button("Checkout");
    private Label lblChangeVal = new Label("$0.00");

    private Button btnDone = new Button("Done");

    Scene promptScene, saleScene;

    @Override
    public void start(Stage stage) throws IOException {

        initializations();


        // prompt UI
        stage.setTitle("Koth Register");
        BorderPane borderPane = new BorderPane();
        HBox hb = new HBox();
        hb.getChildren().addAll(btnNewSale,btnItemData);
        hb.setAlignment(Pos.CENTER);
        hb.setSpacing(10);
        borderPane.setCenter(hb);
        borderPane.setTop(lblWelcome);
        borderPane.setBottom(lblTotalSale);

        // Set properties of prompt
        borderPane.setPadding(new Insets(20,10,30,10));
        borderPane.setAlignment(lblTotalSale, Pos.BOTTOM_CENTER);
        borderPane.setAlignment(lblWelcome,Pos.TOP_CENTER);
        btnNewSale.setMaxSize(100,35);
        btnItemData.setMaxSize(100,35);
        Font font = Font.font("Ink Free", FontWeight.BOLD, 25);
        lblWelcome.setFont(font);
        lblTotalSale.setFont(font);

        // Create prompt
        promptScene = new Scene(borderPane,400,250);

        // Handle promptScene events
        btnNewSale.setOnAction(e->{
            stage.setTitle("New Sale");
            stage.setScene(saleScene);
            resets();
            register.makeNewSale();
        });
        btnItemData.setOnAction(e->{
            System.exit(0);
        });

        // ----------------------------------------------------------------------------------------------------------

        // Scene saleScene
        // Upper half of saleScene
        GridPane gp = new GridPane();
        gp.setHgap(10);
        gp.setVgap(10);
        gp.setPadding(new Insets(1,10,15,10));

        ColumnConstraints c1 = new ColumnConstraints(0);
        c1.setPercentWidth(25);
        ColumnConstraints c2 = new ColumnConstraints(1);
        c2.setPercentWidth(75);

        gp.getColumnConstraints().addAll(c1,c2);

        GridPane.setConstraints(lblItemId,0,0);
        GridPane.setConstraints(cbItems,1,0);
        Label lblItemN = new Label("Item Name:");
        GridPane.setConstraints(lblItemN,0,1);
        GridPane.setConstraints(lblItemName,1,1);
        Label lblItemP = new Label("Item Price:");
        GridPane.setConstraints(lblItemP,0,2);
        GridPane.setConstraints(lblItemPrice,1,2);
        gp.addRow(3,new Label("Quantity:"),tfQuantity);
        Label lblItemT = new Label("Item Total:");
        GridPane.setConstraints(lblItemT,0,4);
        GridPane.setConstraints(lblItemTotal,1,4);
        GridPane.setConstraints(btnAdd,1,6);

//        lblItemName.textProperty().bind(cbItems.valueProperty());

        // Set properties of top half of saleScene
        gp.setBorder(new Border(new BorderStroke(Color.valueOf("#A9A9A9"),BorderStrokeStyle.SOLID,CornerRadii.EMPTY,BorderWidths.DEFAULT)));
        btnAdd.setMaxSize(500,500);
        gp.getChildren().addAll(lblItemId,cbItems,lblItemN,lblItemName,lblItemP,lblItemPrice,lblItemT,lblItemTotal,btnAdd);
        cbItems.prefWidth(700);


        // Lower half of saleScene
        GridPane gp1 = new GridPane();
        ColumnConstraints col1 = new ColumnConstraints(0);
        col1.setPercentWidth(35);
        ColumnConstraints col2 = new ColumnConstraints(1);
        col2.setPercentWidth(25);
        ColumnConstraints col3 = new ColumnConstraints(2);
        col3.setPercentWidth(40);

        gp1.setHalignment(lblItemId, HPos.RIGHT ); // horizontal Alignment

        gp1.getColumnConstraints().addAll(col1,col2,col3);
        gp1.setHgap(10);
        gp1.setVgap(10);

        Label lblSaleSub = new Label("Sale Sub Total:");
        GridPane.setConstraints(new Label("Sale Sub Total:"),0,0);
        GridPane.setConstraints(lblSaleSubTotal,1,0);
        Label lblSaleTx = new Label("Sale Tax Total(6%):");
        GridPane.setConstraints(lblSaleTx,0,1);
        GridPane.setConstraints(lblSaleTaxTotal,1,1);
        Label lblTa = new Label("Tendered Amount:");
        GridPane.setConstraints(lblTa,0,2);
        GridPane.setConstraints(tfAmtTendered,1,2);
        GridPane.setConstraints(btnCheckout,2,2);
        Label lblChange = new Label("Change:");
        GridPane.setConstraints(lblChange,0,3);
        GridPane.setConstraints(lblChangeVal,1,3);

        btnCheckout.setMaxSize(200,200);

        gp1.getChildren().addAll(lblSaleSub,lblSaleSubTotal,lblSaleTx,lblSaleTaxTotal,lblTa,tfAmtTendered,btnCheckout,lblChange,lblChangeVal);

        // Set properties for lower half of saleScene
        taReceipt.setEditable(false);
        taReceipt.setWrapText(false);
        taReceipt.setPrefWidth(700);
        taReceipt.setPrefHeight(500);
        Font taFont = Font.font("Courier New", FontWeight.NORMAL, null,17);
        taReceipt.setFont(taFont);


        VBox vb = new VBox();
        vb.setPadding(new Insets(10,10,15,10));
        vb.setSpacing(10);
        vb.getChildren().addAll(taReceipt,gp1); // textArea
        vb.setBorder(new Border(new BorderStroke(Color.valueOf("#A9A9A9"),BorderStrokeStyle.SOLID,CornerRadii.EMPTY,BorderWidths.DEFAULT)));

        // Create saleScene
        VBox vbox = new VBox();
        vbox.getChildren().addAll(gp,vb, btnDone);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(10,10,10,10));
        vbox.setSpacing(5);
        HBox hbox = new HBox();
        hbox.getChildren().addAll(vbox);
        hbox.setAlignment(Pos.CENTER);
        saleScene = new Scene(hbox, 400, 600);

        // Handle saleScene events
        btnDone.setOnAction(e->{
            stage.setTitle("Koth Register");
            stage.setScene(promptScene);
            lblTotalSale.setText("Total sale for the day is: $" + String.format("%.2f",register.getDailyTotal()));
        });

        // Handle item selection from choiceBox and update labels accordingly
        cbItems.setOnAction(e -> {
            if(cbItems.getValue() != null){
                ProductSpecification spec = catalog.getSpecification(cbItems.getValue());
                lblItemName.setText(spec.getName());
                lblItemPrice.setText("$" + String.format("%.2f", spec.getPrice()));
            }
        });

        tfQuantity.setOnAction(e->{
            if(tfQuantity.getText()!=null){
                try{
                    int quantity = Integer.parseInt(tfQuantity.getText());
                    lblItemTotal.setText("");

                }
                catch (NumberFormatException exc) {
                    exc.printStackTrace();
                }

            }
        });

        // Handle Add Button click
        btnAdd.setOnAction(e -> {
                try{
                    int quantity = Integer.parseInt(tfQuantity.getText());
                    if(quantity > 0) {
                        register.enterItem(cbItems.getValue(), quantity);
                        ProductSpecification spec = catalog.getSpecification(cbItems.getValue());
                        lblItemTotal.setText("$" + String.format("%.2f", (spec.getPrice() * quantity)));

                        List<SalesLineItem> sli = register.getSalesLineItems();

                        StringBuilder str = new StringBuilder();
                        str.append("--------------------------------\n");

                        sli.forEach((elem)->{
                            str.append(elem.toString());
                        });

                        taReceipt.setText(String.valueOf(str));

                        lblSaleSubTotal.setText("$" + String.format("%.2f", register.getItemTotal()));
                        lblSaleTaxTotal.setText("$" + String.format("%.2f", register.getTotal()));
                    }
                    else {
                        lblItemTotal.setText("!!! Invalid Quantity");
                    }
                }
                catch (NumberFormatException err){
                    lblItemTotal.setText("!!! Invalid Quantity");
                    err.printStackTrace();
                }
        });

        btnCheckout.setOnAction(e -> {
            try{
                float amt = Integer.parseInt(tfAmtTendered.getText());
                register.makePayment(amt);
                btnAdd.setDisable(true);

            } catch(NumberFormatException err){
                err.printStackTrace();
            }
        });

        // ----------------------------------------------------------------------------------------------------------


        // Place scene in stage and invoke show function
        stage.setScene(promptScene);
        stage.setResizable(false); // setting stage size to not resizable
        stage.show();
    }

    public void initializations(){
        catalog = new ProductCatalog(filename);
        register = new Register(catalog);
        cbItems.getItems().addAll(catalog.getKeys());
    }

    public void resets(){
        lblItemName.setText("NA");
        lblItemPrice.setText("$0.00");
        tfQuantity.clear();
        lblItemTotal.setText("$0.00");
        taReceipt.setText(null);
        cbItems.setValue(null);
        lblSaleSubTotal.setText("$0.00");
        lblSaleTaxTotal.setText("$0.00");
        tfAmtTendered.clear();
        lblChangeVal.setText("$0.00");
        btnAdd.setDisable(false);
    }

    public static void main(String[] args){
        Application.launch(args);
    }
}
