package Controllers;

import Model.Inventory;
import Model.Part;
import Model.Product;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;



/** RUNTIME ERROR: Runtime error happens if there is no part selected when the modify button is used. */

/** This is the FXML class for the MainScreenController class. */
public class MainScreenController implements Initializable {

    Inventory inv;

    /** Location where the part string will be input. */
    @FXML
    private TextField partSearchBox;
    /** Location where product String will be input. */
    @FXML
    private TextField productSearchBox;
    /** Table where all Products in Inventory will be shown. */
    @FXML
    private TableView<Part> partsTable;
    @FXML
    private TableView<Product> productsTable;
    private ObservableList<Part> partInventory = FXCollections.observableArrayList();
    private ObservableList<Product> productInventory = FXCollections.observableArrayList();
    private ObservableList<Part> partsInventorySearch = FXCollections.observableArrayList();
    private ObservableList<Product> productInventorySearch = FXCollections.observableArrayList();

    public MainScreenController(Inventory inv) {
        this.inv = inv;
    }

    /**
     * Initializes controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        generatePartsTable();
        generateProductsTable();
    }

    /**
     * Price column generated, assigns all the Parts in Inventory.
     */
    private void generatePartsTable() {
        partInventory.setAll(inv.getAllParts());

        TableColumn<Part, Double> costCol = formatPrice();
        partsTable.getColumns().addAll(costCol);

        partsTable.setItems(partInventory);
        partsTable.refresh();
    }

    /**
     * Price column generated, assigns all the Products in the Inventory.
     */
    private void generateProductsTable() {
        productInventory.setAll(inv.getAllProducts());

        TableColumn<Product, Double> costCol = formatPrice();
        productsTable.getColumns().addAll(costCol);

        productsTable.setItems(productInventory);
        productsTable.refresh();
    }
    @FXML
    private void exitProgram(ActionEvent event
    ) {
        Platform.exit();
    }
    @FXML
    private void exitProgramButton(MouseEvent event
    ) {
        Platform.exit();
    }

    /**
     * Search is done here. Uses the Inventory lookupPart(partName)  method.
     */
    @FXML
    private void searchForPart(MouseEvent event) {
        if (!partSearchBox.getText().trim().isEmpty()) {
            partsTable.setItems(inv.lookUpPart(partSearchBox.getText().trim()));
            partsTable.refresh();
        }
    }

    /**
     * Previous search method. Does not use the Inventory lookupProduct(partName) method.
     */
    @FXML
    private void searchForProduct(MouseEvent event
    ) {
        if (!productSearchBox.getText().trim().isEmpty()) {
            productInventorySearch.clear();
            for (Product p : inv.getAllProducts()) {
                if (p.getName().contains(productSearchBox.getText().trim())) {
                    productInventorySearch.add(p);
                }
            }
            productsTable.setItems(productInventorySearch);
            productsTable.refresh();
        }
    }

    /**
     * Clears one of the search boxes and displays all the parts or products in Inventory.
     */
    @FXML
    void clearText(MouseEvent event
    ) {
        Object source = event.getSource();
        TextField field = (TextField) source;
        field.setText("");
        if (partSearchBox == field) {
            if (partInventory.size() != 0) {
                partsTable.setItems(partInventory);
            }
        }
        if (productSearchBox == field) {
            if (productInventory.size() != 0) {
                productsTable.setItems(productInventory);
            }
        }
    }

    /**
     * Go to Add Part Screen.
     */
    @FXML
    private void addPart(MouseEvent event
    ) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/AddPart.fxml"));
            AddPartController controller = new AddPartController(inv);

            loader.setController(controller);
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {

        }
    }

    /** Loads the ModifyPartController. The method displays an error message if no part is selected. */
    @FXML
    private void modifyPart(MouseEvent event
    ) {
        try {
            Part selected = partsTable.getSelectionModel().getSelectedItem();
            if (partInventory.isEmpty()) {
                errorWindow(1);
                return;
            }
            if (!partInventory.isEmpty() && selected == null) {
                errorWindow(2);
                return;
            } else {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/ModifyPart.fxml"));
                ModifyPartController controller = new ModifyPartController(inv, selected);

                loader.setController(controller);
                Parent root = loader.load();
                Scene scene = new Scene(root);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.setResizable(false);
                stage.show();
//                ModifyPartController c  = (ModifyPartController)loader.getController();
//                c.setSelectPart(selected);
            }
        } catch (IOException e) {

        }
    }
    @FXML
    private void deletePart(MouseEvent event
    ) {
        Part removePart = partsTable.getSelectionModel().getSelectedItem();
        if (partInventory.isEmpty()) {
            errorWindow(1);
            return;
        }
        if (!partInventory.isEmpty() && removePart == null) {
            errorWindow(2);
            return;
        } else {
            boolean confirm = confirmationWindow(removePart.getName());
            if (!confirm) {
                return;
            }
            inv.deletePart(removePart);
            partInventory.remove(removePart);
            partsTable.refresh();

        }
    }
    @FXML
    private void deleteProduct(MouseEvent event
    ) {
        boolean deleted = false;
        Product removeProduct = productsTable.getSelectionModel().getSelectedItem();
        if (productInventory.isEmpty()) {
            errorWindow(1);
            return;
        }
        if (!productInventory.isEmpty() && removeProduct == null) {
            errorWindow(2);
            return;
        }
        if (removeProduct.getPartsListSize() == 0) {
            boolean confirm = confirmDelete(removeProduct.getName());
            if (!confirm) {
                return;
            }
        } else {
            infoWindow(removeProduct.getName());
            return;
        }
        inv.removeProduct(removeProduct.getProductID());
        productInventory.remove(removeProduct);
        productsTable.setItems(productInventory);
        productsTable.refresh();
    }

    /**
     * Go to Modify Product Screen.
     */
    @FXML
    private void modifyProduct(MouseEvent event
    ) {
        //Example of a runtime error being corrected, preventing a null from being passed
        try {
            Product productSelected = productsTable.getSelectionModel().getSelectedItem();
            if (productInventory.isEmpty()) {
                errorWindow(1);
                return;
            }
            if (!productInventory.isEmpty() && productSelected == null) {
                errorWindow(2);
                return;

            } else {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/ModifyProduct.fxml"));
                ModifyProductController controller = new ModifyProductController(inv, productSelected);

                loader.setController(controller);
                Parent root = loader.load();
                Scene scene = new Scene(root);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.setResizable(false);
                stage.show();
            }
        } catch (IOException e) {

        }
    }

    @FXML
    private void addProduct(MouseEvent event
    ) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/AddProduct.fxml"));
            AddProductController controller = new AddProductController(inv);

            loader.setController(controller);
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();

        } catch (IOException e) {

        }
    }

    private void errorWindow(int code) {
        if (code == 1) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Empty Inventory!");
            alert.setContentText("There's nothing to select!");
            alert.showAndWait();
        }
        if (code == 2) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Invalid Selection");
            alert.setContentText("You must select an item!");
            alert.showAndWait();
        }

    }

    private boolean confirmationWindow(String name) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Delete part");
        alert.setHeaderText("Are you sure you want to delete: " + name);
        alert.setContentText("Click ok to confirm");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            return true;
        } else {
            return false;
        }
    }

    private boolean confirmDelete(String name) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Delete product");
        alert.setHeaderText("Are you sure you want to delete: " + name );
        alert.setContentText("Click ok to confirm");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            return true;
        } else {
            return false;
        }
    }

    private void infoWindow(String name) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Confirmed");
        alert.setHeaderText(null);
        alert.setContentText(name + " still has parts assigned to it and has NOT been deleted!");
        alert.showAndWait();
    }

    private <T> TableColumn<T, Double> formatPrice() {
        TableColumn<T, Double> costCol = new TableColumn("Price");
        costCol.setCellValueFactory(new PropertyValueFactory<>("Price"));
        // Format as currency
        costCol.setCellFactory((TableColumn<T, Double> column) -> {
            return new TableCell<T, Double>() {
                @Override
                protected void updateItem(Double item, boolean empty) {
                    if (!empty) {
                        setText("$" + String.format("%10.2f", item));
                    } else {
                        setText("");
                    }
                }
            };
        });
        return costCol;
    }

}
