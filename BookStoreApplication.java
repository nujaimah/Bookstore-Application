/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package book.store.application;

import static book.store.application.Record.getInstance;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.util.InputMismatchException;

/**
 *
 * @author monir
 */
public class BookStoreApplication extends Application {
    Stage window;
    Scene scene1, scene2, scene3, scene4, scene5;
    static boolean answer;
    TextField BookName, BookPrice, Username, Password, Points;
    TableView<Book> tableOfBooks, tableOfCustomerBooks;
    TableView<Customer> tableOfCustomers;
    ArrayList<Integer> selectedBooks = new ArrayList<>();
    double TotalCost = 0;
    Record booksFile = getInstance("books");
    Record customersFile = getInstance("customers");
    int numberOfBooks = 0;
    int numberOfCustomers = 0;
    Label messageLabel5,messageLabel6,messageLabel2;
    Customer currentCustomer = new Customer();
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        Stage window = primaryStage;
        window.setTitle("Bookstore App");
        
        window.setOnCloseRequest(e -> {
            e.consume();
            closeProgram();
        });
        
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10,10,10,10));
        grid.setVgap(8);
        grid.setHgap(10);
        
        Label messageLabel = new Label("Welcome to the BookStore App");
        GridPane.setConstraints(messageLabel, 0, 0);
        
        Label nameLabel = new Label("Username:");
        GridPane.setConstraints(nameLabel, 0, 1);
        
        TextField usernameInput = new TextField();
        GridPane.setConstraints(usernameInput, 1, 1);
        usernameInput.setPromptText("Username");
        
        Label passLabel = new Label("Password:");
        GridPane.setConstraints(passLabel, 0, 2);
        
        TextField passInput = new TextField();
        GridPane.setConstraints(passInput, 1, 2);
        passInput.setPromptText("Password");
        
        Button loginButton = new Button("Login");
        loginButton.setOnAction(e -> {
            if(validateOwner(usernameInput,passInput) == true){
                window.setScene(scene1);
            }
            if(validateCustomer(usernameInput, passInput) == true){
                messageLabel2.setText("Welcome " + currentCustomer.getUsername() + ". You have " + currentCustomer.getPoints() + " points. Your status is " + currentCustomer.getStatusName());
                window.setScene(scene4);
            }
        });
        GridPane.setConstraints(loginButton, 1, 3);
        
        grid.getChildren().addAll(nameLabel,usernameInput,passLabel,passInput,loginButton, messageLabel);
        
        Scene scene = new Scene(grid, 450, 200);
        window.setScene(scene);
        
        Button button1 = new Button("Books");
        Button button2 = new Button("Customers");
        Button button3 = new Button("Logout");
        
        button1.setOnAction(e -> window.setScene(scene2));
        button2.setOnAction(e -> {
            customersFile.deleteFile();
            customersFile = getInstance("customers");
            ObservableList<Customer> allCustomers;
            allCustomers = tableOfCustomers.getItems();
            int i;
            for(i=0;i<numberOfCustomers; i++){
                String name = allCustomers.get(i).getUsername();
                String pass = allCustomers.get(i).getPassword();
                double points = allCustomers.get(i).getPoints();
                customersFile.write(name + " " + pass + " " + points + "\n");
            }
            numberOfCustomers = 0;
            try {
                tableOfCustomers.setItems(getCustomers());
            } catch (FileNotFoundException ex) {
                Logger.getLogger(BookStoreApplication.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            window.setScene(scene3);
        });
        button3.setOnAction(e -> {
            booksFile.deleteFile();
            booksFile = getInstance("books");
            ObservableList<Book> allBooks;
            allBooks = tableOfBooks.getItems();
            int i;
            for(i=0;i<numberOfBooks; i++){
                String name = allBooks.get(i).getName();
                double price = allBooks.get(i).getPrice();
                booksFile.write(name + " " + price + "\n");
            }
            numberOfBooks = 0;
            try {
                tableOfCustomerBooks.setItems(getBooks());
            } catch (FileNotFoundException ex) {
                Logger.getLogger(BookStoreApplication.class.getName()).log(Level.SEVERE, null, ex);
            }
           window.setScene(scene);
        });
        
        VBox layout1 = new VBox(20);
        layout1.getChildren().addAll(button1,button2,button3);
        layout1.setAlignment(Pos.CENTER);
        scene1 = new Scene(layout1, 400, 400);
        
        ObservableList<Book> Books = FXCollections.observableArrayList();
        
        
        TableColumn<Book, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setMinWidth(200);
        nameColumn.setCellValueFactory(new PropertyValueFactory("name"));
        
        TableColumn<Book, Double> priceColumn = new TableColumn<>("Price");
        priceColumn.setMinWidth(100);
        priceColumn.setCellValueFactory(new PropertyValueFactory("price"));
        
        tableOfBooks = new TableView<>();
        tableOfBooks.setItems(getBooks());
        tableOfBooks.getColumns().addAll(nameColumn,priceColumn);
        
        BookName = new TextField();
        BookName.setPromptText("Book Name");
        BookName.setMinWidth(100);
        
        BookPrice = new TextField();
        BookPrice.setPromptText("BookPrice");
        BookPrice.setMinWidth(100);
        
        Button addButton = new Button("Add");
        addButton.setOnAction(e -> addButtonClicked());
        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(e -> deleteButtonClicked());
        
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> {
            window.setScene(scene1);
        });
        
        HBox hBox = new HBox();
        hBox.setPadding(new Insets(10,10,10,10));
        hBox.setSpacing(10);
        hBox.getChildren().addAll(BookName, BookPrice, addButton, deleteButton, backButton);
        
        
        
        VBox BooksLayout = new VBox(10);
        BooksLayout.getChildren().addAll(tableOfBooks,hBox);
        BooksLayout.setAlignment(Pos.CENTER);
        
        scene2 = new Scene(BooksLayout, 600, 600);
        
        
      ObservableList<Book> CustomerBooks = FXCollections.observableArrayList();
        
        
        TableColumn<Book, String> booknameColumn = new TableColumn<>("Name");
        booknameColumn.setMinWidth(200);
        booknameColumn.setCellValueFactory(new PropertyValueFactory("name"));
        
        TableColumn<Book, Double> bookpriceColumn = new TableColumn<>("Price");
        bookpriceColumn.setMinWidth(100);
        bookpriceColumn.setCellValueFactory(new PropertyValueFactory("price"));
        
        TableColumn selectCol = new TableColumn("Select");
        selectCol.setMinWidth(50);
        selectCol.setCellValueFactory(new PropertyValueFactory("select"));
        
        tableOfCustomerBooks = new TableView<>();
        numberOfBooks = 0;
        tableOfCustomerBooks.setItems(getBooks());
        tableOfCustomerBooks.getColumns().addAll(booknameColumn,bookpriceColumn, selectCol);
      
        
        Button buyButton = new Button("Buy");
        buyButton.setOnAction(e -> {
            TotalCost = BuyButtonClicked(tableOfCustomerBooks);
            currentCustomer.setPoints(currentCustomer.getPoints() + TotalCost * 10);
            currentCustomer.checkStatus();
            messageLabel5.setText("Total Cost: " + TotalCost);
            messageLabel6.setText("Points: " + currentCustomer.getPoints() + ", Status: " + currentCustomer.getStatusName());
            window.setScene(scene5);
        });
        Button RedeemBuyButton = new Button("Redeem points and Buy");
        RedeemBuyButton.setOnAction(e -> {
            TotalCost = RedeemBuyButtonClicked(tableOfCustomerBooks);
            messageLabel5.setText("Total Cost: " + TotalCost);
            messageLabel6.setText("Points: " + currentCustomer.getPoints() + ", Status: " + currentCustomer.getStatusName());
            window.setScene(scene5);
            });
        
        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(e -> {
            window.setScene(scene);
        });
        
        messageLabel2 = new Label("");
        
        HBox hBox2 = new HBox();
        hBox2.setPadding(new Insets(10,10,10,10));
        hBox2.setSpacing(10);
        hBox2.getChildren().addAll(buyButton,RedeemBuyButton,logoutButton);
        
        
        VBox CustomerLayout = new VBox(10);
        CustomerLayout.getChildren().addAll(messageLabel2,tableOfCustomerBooks,hBox2);
        CustomerLayout.setAlignment(Pos.CENTER);
        
        scene4 = new Scene(CustomerLayout, 600, 600);
        
        messageLabel5 = new Label("Total Cost: " + TotalCost);
        messageLabel6 = new Label("Points: P, Status: S");
        
        Button logoutsButton = new Button("Logout");
        logoutsButton.setOnAction(e -> {
            window.setScene(scene);
        });

        VBox BuyScreenlayout1 = new VBox(20);
        BuyScreenlayout1.getChildren().addAll(messageLabel5,messageLabel6,logoutsButton);
        BuyScreenlayout1.setAlignment(Pos.CENTER);
        scene5 = new Scene(BuyScreenlayout1, 400, 400);
        
        ObservableList<Customer> Customers = FXCollections.observableArrayList();
        
        
        TableColumn<Customer, String> usernameColumn = new TableColumn<>("Username");
        usernameColumn.setMinWidth(200);
        usernameColumn.setCellValueFactory(new PropertyValueFactory("username"));
        
        TableColumn<Customer, String> passwordColumn = new TableColumn<>("Password");
        passwordColumn.setMinWidth(200);
        passwordColumn.setCellValueFactory(new PropertyValueFactory("password"));
        
        TableColumn<Customer, Double> pointsColumn = new TableColumn<>("Points");
        pointsColumn.setMinWidth(100);
        pointsColumn.setCellValueFactory(new PropertyValueFactory("points"));
        
        tableOfCustomers = new TableView<>();
        tableOfCustomers.setItems(getCustomers());
        tableOfCustomers.getColumns().addAll(usernameColumn,passwordColumn,pointsColumn);
        
        Username = new TextField();
        Username.setPromptText("Username");
        Username.setMinWidth(100);
        
        Password = new TextField();
        Password.setPromptText("Password");
        Password.setMinWidth(100);
         
        Button addCustomerButton = new Button("Add");
        addCustomerButton.setOnAction(e -> addCustomerButtonClicked());
        Button deleteCustomerButton = new Button("Delete");
        deleteCustomerButton.setOnAction(e -> deleteCustomerButtonClicked());
        
        Button backCustomerButton = new Button("Back");
        backCustomerButton.setOnAction(e -> {
            window.setScene(scene1);
        });
        
        HBox hBox1 = new HBox();
        hBox1.setPadding(new Insets(10,10,10,10));
        hBox1.setSpacing(10);
        hBox1.getChildren().addAll(Username, Password, addCustomerButton, deleteCustomerButton,backCustomerButton );
        
        
        
        VBox CustomersLayout = new VBox(10);
        CustomersLayout.getChildren().addAll(tableOfCustomers,hBox1);
        CustomersLayout.setAlignment(Pos.CENTER);
        
        scene3 = new Scene(CustomersLayout, 600, 600);
        
        window.show();
    }
    
    private double BuyButtonClicked(TableView<Book> table){
        double TotalPrice = 0;
        ObservableList<Book> allBooks;
        allBooks = table.getItems();
        int i;
        for(i=0;i<numberOfBooks; i++){
            if(allBooks.get(i).getSelect().isSelected()){
                allBooks.get(i).getSelect().setSelected(false);
                TotalPrice += allBooks.get(i).getPrice();
            };
        }
        return TotalPrice;
    }
    
    private double RedeemBuyButtonClicked(TableView<Book> table){
        double TotalPrice = 0;
        ObservableList<Book> allBooks;
        allBooks = table.getItems();
        int i;
        for(i=0;i<numberOfBooks; i++){
            if(allBooks.get(i).getSelect().isSelected()){
                allBooks.get(i).getSelect().setSelected(false);
                TotalPrice += allBooks.get(i).getPrice();
            };
        }
        if(TotalPrice > (currentCustomer.getPoints() / 100)){
            TotalPrice = TotalPrice - (currentCustomer.getPoints() / 100);
            currentCustomer.setPoints(TotalPrice*10);
            currentCustomer.checkStatus();
        }else{
            currentCustomer.setPoints((currentCustomer.getPoints()) - (TotalPrice * 100));
            currentCustomer.checkStatus();
            TotalPrice = 0;
        }
        
        return TotalPrice;
    }
    
    public ObservableList<Book> getBooks() throws FileNotFoundException{
            ObservableList<Book> Books = FXCollections.observableArrayList();
            Scanner scanner = new Scanner(new FileReader("books.txt"));
            try {
                while (scanner.hasNext()) {
                    String token = scanner.next();
                    while(!scanner.hasNextDouble()){
                        token += " " + scanner.next();
                    }
                    double number = scanner.nextDouble();
                    Books.add(new Book(token, number));
                    numberOfBooks++;
                }
            } finally {
            scanner.close();
            }
            return Books;
    }
    
    public ObservableList<Customer> getCustomers() throws FileNotFoundException {
            ObservableList<Customer> Customers = FXCollections.observableArrayList();
            Scanner scanner = new Scanner(new FileReader("customers.txt"));
            try {
                while (scanner.hasNext()) {
                    String token = scanner.next();
                    String token1 = scanner.next();
                    double number = scanner.nextDouble();
                    Customers.add(new Customer(token, token1,number));
                    numberOfCustomers++;
                }
            } finally {
            scanner.close();
            }
            return Customers;
    }
    
    public void addButtonClicked(){
        Book book = new Book();
        book.setName(BookName.getText());
        book.setPrice(Double.parseDouble(BookPrice.getText()));
        tableOfBooks.getItems().add(book);
        numberOfBooks++;
        BookName.clear();
        BookPrice.clear();
    }
    
    public void addCustomerButtonClicked(){
        Customer customer = new Customer();
        customer.setUsername(Username.getText());
        customer.setPassword(Password.getText());
        customer.setPoints(0);
        tableOfCustomers.getItems().add(customer);
        numberOfCustomers++;
        Username.clear();
        Password.clear();
    }
    
    public void deleteButtonClicked(){
       ObservableList<Book> bookSelected, allBooks;
       allBooks = tableOfBooks.getItems();
       bookSelected = tableOfBooks.getSelectionModel().getSelectedItems();
       bookSelected.forEach(allBooks::remove);
       numberOfBooks--;
    }
    
    public void deleteCustomerButtonClicked(){
       ObservableList<Customer> customerSelected, allCustomers;
       allCustomers = tableOfCustomers.getItems();
       customerSelected = tableOfCustomers.getSelectionModel().getSelectedItems();
       customerSelected.forEach(allCustomers::remove);
       numberOfCustomers--;
    }
    
    //This method checks if the login information is valid (admin, admin)
    private boolean validateOwner(TextField userinput, TextField passinput){
        if(userinput.getText().equals("admin") && passinput.getText().equals("admin")){
            return true;
        }else{
            return false;
        }
        
    }
    
    
    private boolean validateCustomer(TextField userinput, TextField passinput){
        int i;
        ObservableList<Customer> allCustomers;
        allCustomers = tableOfCustomers.getItems();
        for(i=0;i<numberOfCustomers; i++){
            String name = allCustomers.get(i).getUsername();
            String pass = allCustomers.get(i).getPassword();
            double points = allCustomers.get(i).getPoints();
            if(userinput.getText().equals(name) && passinput.getText().equals(pass)){
                currentCustomer = allCustomers.get(i);
                return true;
            }
        }
        return false;
    }
    
    private void closeProgram(){
        booksFile.deleteFile();
        booksFile = getInstance("books");
        ObservableList<Book> allBooks;
        allBooks = tableOfBooks.getItems();
        int i;
        for(i=0;i<numberOfBooks; i++){
            String name = allBooks.get(i).getName();
            double price = allBooks.get(i).getPrice();
            booksFile.write(name + " " + price + "\n");
        }
        customersFile.deleteFile();
        customersFile = getInstance("customers");
        ObservableList<Customer> allCustomers;
        allCustomers = tableOfCustomers.getItems();
        for(i=0;i<numberOfCustomers; i++){
            String name = allCustomers.get(i).getUsername();
            String pass = allCustomers.get(i).getPassword();
            double points = allCustomers.get(i).getPoints();
            customersFile.write(name + " " + pass + " " + points + "\n");
        }
        Platform.exit();
        
        
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
