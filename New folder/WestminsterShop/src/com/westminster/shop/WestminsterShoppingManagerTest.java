package com.westminster.shop;

import com.westminster.shop.WestminsterShoppingManager;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.Scanner;

import static org.junit.Assert.*;

public class WestminsterShoppingManagerTest {
    private WestminsterShoppingManager shoppingManager;

    @BeforeEach
    public void setUp() {
        shoppingManager = new WestminsterShoppingManager();
    }
    @Test
    public void testGuiOpens() {
        // Create a new thread to run the GUI
        SwingUtilities.invokeLater(() -> {
            // Call the method that opens the GUI
            Shop shop = new Shop();
        });

        // Wait for the GUI to open
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Check if a frame is showing
        boolean frameShowing = false;
        for (Frame frame : Frame.getFrames()) {
            if (frame.isVisible()) {
                frameShowing = true;
                break;
            }
        }

        assertTrue("The GUI did not open", frameShowing);
    }
    @Test
    public void testAddProducts_MaximumLimit() {
        // Add 100 products
        Products product1 = new Electronics("123", "Laptop", 3, 899.99, "Dell", 15);
    }
//    @Test
//    public void testAddProducts() {
//        WestminsterShoppingManager shoppingManager = new WestminsterShoppingManager();
//
//        // Simulate user input
//        String input = "\nE\n123\nTest Product\n10\n20.0\nSony\n12\n";
//        System.setIn(new ByteArrayInputStream(input.getBytes()));
//
//        // Run the addProducts method
//        shoppingManager.addProducts();
//
//        // Check if the product was added successfully
//        assertEquals(1, WestminsterShoppingManager.ProductList.size());
//        Products addedProduct = WestminsterShoppingManager.ProductList.get(0);
//        assertEquals("123", addedProduct.getProductID());
//        assertEquals("Test Product", addedProduct.getProductName());
//        assertEquals(10, addedProduct.getNumberOfItems());
//        assertEquals(20.0, addedProduct.getPrice(), 0.001);
//        assertEquals("Sony", ((Electronics) addedProduct).getBrand());
//        assertEquals(12, ((Electronics) addedProduct).getWarrantyPeriod());
//    }
    @Test
    public void testDeleteProducts() {

        shoppingManager = new WestminsterShoppingManager();
        // Add a sample product for testing
        Products product2 = new Electronics("456", "Sample Product", 5, 25.0, "Samsung", 10);


        // Simulate user input
        String input = "/n456";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        // Run the removeProducts method
        shoppingManager.removeProducts();

        // Check if the product was removed successfully
        assertEquals(0, WestminsterShoppingManager.ProductList.size());
    }

    @Test
    public void testSaveProducts() throws IOException {
        // Create a shopping manager
        WestminsterShoppingManager shoppingManager = new WestminsterShoppingManager();

        // Add sample products for testing
        Products product1 = new Electronics("123", "Laptop", 3, 899.99, "Dell", 15);
        Products product2 = new Clothing("456", "T-shirt", 10, 19.99, "Large", "Blue");
        shoppingManager.getProductList().add(product1);
        shoppingManager.getProductList().add(product2);

        // Run the saveProducts method
        shoppingManager.saveProducts();

        // Check if the file was created and contains the expected content
        File file = new File("Products.txt");
        assertTrue(file.exists());

        Scanner scanner = new Scanner(file);

        // Check the first few lines for correct formatting
        assertEquals("-----------------------------------------2Products available------------------------------------------", scanner.nextLine().trim());


        // Check each product line
        assertTrue(scanner.hasNextLine());
        assertEquals("Product ID: 123", scanner.nextLine().trim());
        assertEquals("Product Name: Laptop", scanner.nextLine().trim());
        assertEquals("No of Items: 3", scanner.nextLine().trim());
        assertEquals("Price: 899.99", scanner.nextLine().trim());
        assertEquals("Product type: Electronics", scanner.nextLine().trim());
        assertEquals("Warranty period: 15", scanner.nextLine().trim());
        assertEquals("Brand: Dell", scanner.nextLine().trim());
        assertEquals("", scanner.nextLine().trim());
        assertEquals("------------------------------------------------", scanner.nextLine().trim());

        assertTrue(scanner.hasNextLine());
        assertEquals("Product ID: 456", scanner.nextLine().trim());
        assertEquals("Product Name: T-shirt", scanner.nextLine().trim());
        assertEquals("No of Items: 10", scanner.nextLine().trim());
        assertEquals("Price: 19.99", scanner.nextLine().trim());
        assertEquals("Product type: Clothing", scanner.nextLine().trim());
        assertEquals("Size: Large", scanner.nextLine().trim());
        assertEquals("Color: Blue", scanner.nextLine().trim());
        assertEquals("", scanner.nextLine().trim());
        assertEquals("------------------------------------------------", scanner.nextLine().trim());

        // Ensure there are no more lines in the file
        assertFalse(scanner.hasNextLine());
    }

    @Test
    public void testLoadProducts() throws IOException {
        shoppingManager = new WestminsterShoppingManager();
        // Create a file with sample product data
        PrintWriter writer = new PrintWriter("Products.txt");
        writer.println("------------------------------------------------");
        writer.println("1 Products available");
        writer.println("------------------------------------------------");
        writer.println("Product ID: 456");
        writer.println("Product Name: Sample Product");
        writer.println("No of Items: 5");
        writer.println("Price: 25.0");
        writer.println("Product type: Electronics");
        writer.println("Warranty period: 10");
        writer.println("Brand: Samsung");
        writer.println("------------------------------------------------");
        writer.close();

        // Run the loadProducts method
        shoppingManager.loadProducts();

        // Check if the ProductList contains the expected products
        assertEquals(1, WestminsterShoppingManager.ProductList.size());
        Products loadedProduct = WestminsterShoppingManager.ProductList.get(0);
        assertEquals("456", loadedProduct.getProductID());
        assertEquals("Sample Product", loadedProduct.getProductName());
        assertEquals(5, loadedProduct.getNumberOfItems());
        assertEquals(25.0, loadedProduct.getPrice(), 0.001);
        assertEquals("Samsung", ((Electronics) loadedProduct).getBrand());
        assertEquals(10, ((Electronics) loadedProduct).getWarrantyPeriod());
    }
}