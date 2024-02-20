package com.westminster.shop;

import java.io.*;
import java.util.*;


public class WestminsterShoppingManager implements ShoppingManager { //provides implementation for interface ShoppingManager
    public static ArrayList<Products> ProductList;//array list to store the products
    public static ArrayList<User> Users = new ArrayList<>();//array list to store the users

    public WestminsterShoppingManager() {

        ProductList = new ArrayList<>();
    }
    public static ArrayList getProductList() {

        return ProductList;
    }
    private static Scanner input = new Scanner(System.in);
    private String ProductID;
    private String ProductName;
    private int NumberOfItems;
    private double Price;

    private final int MAX_PRODUCTS = 50;    // Maximum number of products that can be added to the shopping cart

    // Method to check if a user exists
    // Method to check if a user exists in the UserDetails file
    private boolean userExistsInFile(String username) {
        try {
            File file = new File("UserDetails.ser");
            if (file.exists()) {
                FileInputStream fileInputStream = new FileInputStream(file);
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

                while (fileInputStream.available() > 0) {
                    User savedUser = (User) objectInputStream.readObject();
                    if (savedUser.getUserName().equals(username)) {
                        objectInputStream.close();
                        return true;
                    }
                }
                objectInputStream.close();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Method to validate user login
    // Method to validate user login
    private User validateUserLogin() {

        while (true) {
            System.out.println("Enter username: ");
            String username = input.nextLine();

            if (userExistsInFile(username)) {
                // User exists, check password
                System.out.println("Enter password: ");
                String password = input.nextLine();

                try {
                    File file = new File("UserDetails.ser");
                    FileInputStream fileInputStream = new FileInputStream(file);
                    ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

                    while (fileInputStream.available() > 0) {
                        User savedUser = (User) objectInputStream.readObject();
                        if (savedUser.getUserName().equals(username) && savedUser.getPassword().equals(password)) {
                            objectInputStream.close();
                            System.out.println("Login successful!");
                            return savedUser;
                        }
                    }
                    objectInputStream.close();
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }

                System.out.println("Incorrect password. Please try again.");
            } else {
                // User does not exist, ask for signup
                System.out.println("User not found. Do you want to sign up? (yes/no)");
                String signUpChoice = input.nextLine().toLowerCase();

                if (signUpChoice.equals("yes")) {
                    signUp();
                } else {
                    System.out.println("Login aborted.");
                    return null;
                }
            }
        }
    }

    // Method to handle user signup
    private void signUp() {
        try {
            System.out.println("Enter username: ");
            String username = input.nextLine();

            boolean validInput = false;
            String email = null;
            while (!validInput) {
                System.out.println("Enter email: ");
                email = input.nextLine();
                if (email.contains("@")) {
                    validInput = true;
                } else {
                    System.out.println("Email should contain '@'. Please enter a valid email.");
                }
            }
            System.out.println("Enter password: ");
            String password = input.nextLine();

            User newUser = new User(username, password, email);
            newUser.setFirstTimeDiscount(true);
            Users.add(newUser);

            // Save user details to a file
            saveUserDetails(newUser);

            System.out.println("Signup successful!");

        } catch (Exception e) {
            System.out.println("An error occurred while signing up.");
            e.printStackTrace();
        }
    }
    private void saveAllUserDetails(List<User> users) {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("UserDetails.ser")) ) {
            for (User user : users) {
                objectOutputStream.writeObject(user);
            }
        } catch (IOException e) {
            System.out.println("Error saving user details to file.");
            e.printStackTrace();
        }
    }

    // Method to save user details to a file
    // Modify the saveUserDetails method
    private void saveUserDetails(User newUser) {
        List<User> allUsers = new ArrayList<>(Users); // Copy existing users
        allUsers.add(newUser); // Add the new user
        saveAllUserDetails(allUsers); // Save all users back to the file
    }
    private Products ClothingProduct() {
        RequestProductDetails();
        System.out.println("Enter Size: ");
        String Size = input.nextLine();
        System.out.println("Enter color: ");
        String color = input.nextLine();
        return new Clothing(ProductID, ProductName, NumberOfItems, Price, Size, color);
    }


    private Products ElectronicProduct() {
        RequestProductDetails();
        System.out.println("Enter Brand: ");
        String Brand = input.nextLine();
        System.out.println("Enter Warranty Period: ");
        while (!input.hasNextInt()) {
            System.out.println("Warranty period should be a number. Please enter a valid number.");
            input.next();
        }
        int WarrantyPeriod = input.nextInt();
        input.nextLine();
        return new Electronics(ProductID, ProductName, NumberOfItems, Price, Brand, WarrantyPeriod);
    }
    private void RequestProductDetails() {
        boolean validInput = false;
        while (!validInput) {
            System.out.println("Enter productID: ");
            ProductID = input.nextLine();
            validInput = true;
            for (Products product : ProductList) {
                if (product.getProductID().equals(ProductID)) {
                    System.out.println("Product ID already exists. Please enter a unique ID.");
                    validInput = false;
                    break;
                }
            }
        }

        validInput = false;
        while (!validInput) {
            System.out.println("Enter product name: ");
            ProductName = input.nextLine();
            if (ProductName.matches(".*\\d.*")) {
                System.out.println("Product name should not contain numbers. Please enter a valid name.");
            } else {
                validInput = true;
            }
        }

        System.out.println("Enter number of items: ");
        while (!input.hasNextInt()) {
            System.out.println("Number of items should be a number. Please enter a valid number.");
            input.next();
        }
        NumberOfItems = input.nextInt();
        input.nextLine();

        System.out.println("Enter price: ");
        while (!input.hasNextDouble()) {
            System.out.println("Price should be a number. Please enter a valid price.");
            input.next();
        }
        Price = input.nextDouble();
        input.nextLine();
    }
    @Override
    public void addProducts() {
        System.out.println("Enter Type of Product: Electronic or Clothing (E/C)");
        String type = input.nextLine().toUpperCase(Locale.ROOT);
        if (ProductList.size() < MAX_PRODUCTS) {
            if (type.equals("E")) {
                Electronics newProduct = (Electronics) ElectronicProduct();
                ProductList.add(newProduct);
                System.out.println(newProduct.getProductID() + " Added successfully");
            } else if (type.equals("C")) {
                Clothing newProduct = (Clothing) ClothingProduct();
                ProductList.add(newProduct);
                System.out.println(newProduct.getProductID() + " Added successfully");
            } else {
                System.out.println("Invalid type");
            }
        } else {
            System.out.println("Cannot add more products");
        }
    }

    @Override
    public void removeProducts() {

        if (ProductList.isEmpty()) {
            System.out.println("No products to remove");
        }
        System.out.println("Enter productID: ");
        String removeProductID = input.nextLine();
        boolean found = false;
        for (int i = 0; i < ProductList.size(); i++) {
            if (removeProductID.equals(ProductList.get(i).getProductID())) {
                found = true;
                getProductType(ProductList.get(i));
                System.out.println("Product" + "(ID: " + ProductList.get(i).getProductID() + ")" +" TYPE: "+ProductList.get(i).getCategory()+  " removed successfully");

                ProductList.remove(i);
                System.out.println(ProductList.size() + " Products remaining");
                break;
            }
        }
        if (!found) {
            System.out.println("Product not found");
        }
    }
    protected String getProductType(Products product) {
        StringBuilder productType = new StringBuilder();
        if (product instanceof Electronics) {
            productType.append("Product type: Electronics");
            return productType.toString();
        } else if (product instanceof Clothing) {
            productType.append("Product type: Clothing");
            return productType.toString();
        }
        return null;
    }
    @Override
    public void printListOfProducts() {
        System.out.println("------------------------------------------------");
        System.out.println(ProductList.size() + "Products available");
        System.out.println("------------------------------------------------");
        Collections.sort(ProductList, Comparator.comparing(Products::getProductID));

        for(Products product: ProductList){
            System.out.println(formatProductInfo(product));
            System.out.println("------------------------------------------------");
        }
    }
    private  String formatProductInfo(Products product){
        StringBuilder productInfo = new StringBuilder();
        productInfo.append("");
        productInfo.append("Product ID: ").append(product.getProductID()).append("\n");
        productInfo.append("Product Name: ").append(product.getProductName()).append("\n");
        productInfo.append("No of Items: ").append(product.getNumberOfItems()).append("\n");
        productInfo.append("Price: ").append(product.getPrice()).append("\n");
        if(product instanceof Electronics){
            productInfo.append(getProductType(product)).append("\n");
            productInfo.append("Warranty period: ").append(((Electronics) product).getWarrantyPeriod()).append("\n");
            productInfo.append("Brand: ").append(((Electronics) product).getBrand()).append("\n");
        } else if (product instanceof  Clothing) {
            productInfo.append(getProductType(product)).append("\n");
            productInfo.append("Size: ").append(((Clothing) product).getSize()).append("\n");
            productInfo.append("Color: ").append(((Clothing) product).getColor()).append("\n");
        }
        return productInfo.toString();
    }
    @Override
    public void saveProducts() {
        try {
            FileWriter myWriter = new FileWriter("Products.txt");
            myWriter.write("-----------------------------------------");
            myWriter.write(ProductList.size() + "Products available");
            myWriter.write("------------------------------------------\n");
            Collections.sort(ProductList, Comparator.comparing(Products::getProductID));
            for (Products product : ProductList) {
                myWriter.write(formatProductInfo(product));
                myWriter.write("\n------------------------------------------------\n");
            }
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            ManagerMenu();
            e.printStackTrace();
        }
    }
    @Override
    public void loadProducts() {
        try {
            File file = new File("Products.txt");
            Scanner scanner = new Scanner(file);
            // Skip lines until the start of product information
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.startsWith("-----------------------------------------")) {
                    break;
                }
            }
            // Read product information
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.startsWith("Product ID: ")) {
                    // Process product information
                    String productID = line.substring("Product ID: ".length());
                    // Read the rest of the product information
                    String productName = scanner.nextLine().substring("Product Name: ".length());
                    int numberOfItems = Integer.parseInt(scanner.nextLine().substring("No of Items: ".length()));
                    double price = Double.parseDouble(scanner.nextLine().substring("Price: ".length()));
                    // Determine the product type
                    String productType = scanner.nextLine().substring("Product type: ".length());
                    // Read additional information based on the product type
                    if (productType.trim().equals("Electronics")) {
                        int warrantyPeriod = Integer.parseInt(scanner.nextLine().substring("Warranty period: ".length()));
                        String brand = scanner.nextLine().substring("Brand: ".length());
                        // Create and add Electronics object to ProductList
                        ProductList.add(new Electronics(productID, productName, numberOfItems, price, brand, warrantyPeriod));
                    } else if (productType.trim().equals("Clothing")) {
                        String size = scanner.nextLine().substring("Size: ".length());
                        String color = scanner.nextLine().substring("Color: ".length());
                        // Create and add Clothing object to ProductList
                        ProductList.add(new Clothing(productID, productName, numberOfItems, price, size, color));
                    }
                    // Move to the next line
                    scanner.nextLine();
                }
            }
            scanner.close();
            System.out.println("Products loaded successfully.");
        } catch (FileNotFoundException e) {
            System.out.println("File not found: Products.txt");
        }
    }
    public static void main(String[] args) {
        int choice = 0;
        boolean validInput = false;

        while (!validInput) {
            try {
                System.out.println("Welcome to Westminster Shopping Manager");
                System.out.println("Please select an option(1/2)");
                System.out.println("Open GUI press-1");
                System.out.println("Open manager settings press-2");
                System.out.println("Enter option: ");
                choice = input.nextInt();
                input.nextLine();  // Consume the newline character

                if (choice == 1 || choice == 2) {
                    validInput = true;  // Exit the loop if the input is 1 or 2
                } else {
                    System.out.println("Invalid option. Please enter 1 or 2.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                input.nextLine();  // Consume the invalid input
            }
        }

        if (choice == 1) {
            User loggedInUser = new WestminsterShoppingManager().validateUserLogin();
            WestminsterShoppingManager customer = new WestminsterShoppingManager();
            customer.loadProducts();
            if (loggedInUser != null) {
                new Shop();
            }
        } else if (choice == 2) {
            ValidateManager();
            ManagerMenu();
        }
    }
    private static  void ValidateManager(){
        while (true){
            System.out.println("Enter Manager password: ");
            String inputValidation = input.nextLine();
             final String managerPassword = "20221486"; // store to file in encript
            if(inputValidation.equals(managerPassword)){
                break;
            }else {
                System.out.println("Wrong password");
            }
        }
    }
    public static void ManagerMenu() {
        WestminsterShoppingManager Manager = new WestminsterShoppingManager();
        boolean keepGoing = true;

        while (keepGoing) {
            try {
                System.out.println("Welcome to Westminster Manager settings");
                System.out.println("Please select an option(1/2/3/4/5/6)");
                System.out.println("1. Add products ");
                System.out.println("2. Delete products ");
                System.out.println("3. Print list of products ");
                System.out.println("4. Save products to file ");
                System.out.println("5. Load products from file ");
                System.out.println("6. Exit");
                System.out.println("Enter option: ");
                int choice = input.nextInt();
                input.nextLine();  // Consume the newline character

                switch (choice) {
                    case 1:
                        Manager.addProducts();
                        break;
                    case 2:
                        Manager.removeProducts();
                        break;
                    case 3:
                        Manager.printListOfProducts();
                        break;
                    case 4:
                        Manager.saveProducts();
                        break;
                    case 5:
                        Manager.loadProducts();
                        break;
                    case 6:
                        System.out.println("Exited from program!");
                        keepGoing = false;
                        break;
                    default:
                        System.out.println("Invalid option. Please enter a number between 1 and 6.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                input.nextLine();  // Consume the invalid input
            }
        }
    }
}
