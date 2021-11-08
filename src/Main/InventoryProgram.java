package Main;

import Model.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/** This is the InventoryProgram, which implements an application for the management of parts and products.*/
public class InventoryProgram extends Application {


    /**
     * FUTURE ENHANCEMENT:
     * A good improvement in the future would be to control which parts can be duplicated within the product.
     */

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Inventory inv = new Inventory();
        addTestData(inv);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/MainScreen.fxml"));
        Controllers.MainScreenController controller = new Controllers.MainScreenController(inv);
        loader.setController(controller);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.getIcons().add(new Image("file:src/inventory.png"));
        stage.show();
    }

    void addTestData(Inventory inv) {
        //InHouse Parts
        Part A = new InHouse(1, "Test Part A", 6.99, 20, 10, 100, 101);
        Part B = new InHouse(2, "Test Part B", 8.99, 22, 10, 100, 103);
        inv.addPart(A);
        inv.addPart(B);
        //OutSourced Parts
        Part C = new OutSourced(3, "Test Part C", 4.99, 20, 10, 100, "Company A");
        Part D = new OutSourced(4, "Test Part D", 6.99, 18, 20, 100, "Company B");
        inv.addPart(C);
        inv.addPart(D);
        //allProducts
        Product prod1 = new Product(100, "Test Product 1", 18.99, 40, 10, 100);
        prod1.addAssociatedPart(A);
        prod1.addAssociatedPart(C);
        inv.addProduct(prod1);
        Product prod2 = new Product(200, "Test Product 2", 18.99, 48, 10, 100);
        prod2.addAssociatedPart(B);
        prod2.addAssociatedPart(D);
        prod2.addAssociatedPart(C);
        inv.addProduct(prod2);
        Product prod3 = new Product(300, "Test Product 3", 18.99, 60, 10, 100);
        prod3.addAssociatedPart(A);
        inv.addProduct(prod3);
        Product prod4 = new Product(400, "Test Product 4", 9.99, 40, 10, 100);
        prod4.addAssociatedPart(D);
        inv.addProduct(prod4);

    }

}
