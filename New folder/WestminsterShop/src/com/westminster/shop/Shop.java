package com.westminster.shop;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static com.westminster.shop.WestminsterShoppingManager.ProductList;

public class Shop extends JFrame {

    //constants
    private static final String PRODUCT_ID = "Product ID";
    private static final String PRODUCT_NAME = "Product Name";
    private static final String CATEGORY = "Category";
    private static final String PRICE = "Price($)";
    private static final String INFO = "Info";

    private static final String NUMOFITEMS = "Availability";

    private ArrayList<User> users = new ArrayList<>();


    private JComboBox<String> comboBox;
    private JTable table;

    private JButton checkBox;
    private ShoppingCart shoppingCart;
    private JFrame shoppingFrame;
    private DefaultTableModel shoppingModel;
    private JTable cartTable;
    private JLabel totalPriceLabel;
    private JLabel finalPriceLabel;
    private JLabel discountedPriceLabel;

    private JLabel firstPurchaseLabel;
    private String currentUserName;

    // Create a JPanel for the product details
    private JPanel detailsPanel = new JPanel();

    private ArrayList<Products> displayedProducts = new ArrayList<>();

    public Shop() {

        shoppingCart = new ShoppingCart();



        setTitle("Westminster Shopping Centre"); //frame. is not needed because we are already in the class
        setSize(1000, 1000);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // exits the program when the window is closed
        setLayout(new BorderLayout()); // sets the layout of the frame

        ImageIcon img = new ImageIcon("src/com/westminster/shop/WestminsterShoppingLogo.png"); // creates an image icon
        setIconImage(img.getImage()); // sets the image icon to the frame
        getContentPane().setBackground(new Color(245, 245, 220));// sets the background color of the frame

        JPanel header = new JPanel(); // creates a panel
        header.setBounds(0, 0, 1000, 200); // sets the position and size of the panel
        this.add(header, BorderLayout.NORTH); // adds the panel to the frame

        JPanel middle = new JPanel(); // creates a panel

        setLayout(new BorderLayout());

// Create a mainPanel to hold the header and middle panels
        JPanel mainPanel = new JPanel(new BorderLayout());
        add(mainPanel, BorderLayout.CENTER);

        mainPanel.add(header, BorderLayout.NORTH);
        mainPanel.add(middle, BorderLayout.CENTER);

        JLabel label = new JLabel("Select Product Category"); // creates a label
        header.add(label); // adds the label to the panel
        label.setBounds(450, 60, 200, 25); // sets the position and size of the label
        label.setVisible(true); // sets the label to be visible

        String[] productCategory = {"All", "Clothing", "Electronics",};
        comboBox = new JComboBox(productCategory); // creates a combo box
        comboBox.setBounds(700, 60, 70, 25); // sets the position and size of the combo box
        header.add(comboBox); // adds the combo box to the panel
        comboBox.addActionListener(this::actionperformed);


        checkBox = new JButton("Sort A-Z");
        checkBox.setBounds(700, 60, 70, 25);
        header.add(checkBox);
        checkBox.setFocusable(false);
        checkBox.setForeground(Color.BLACK);
        checkBox.setBackground(Color.LIGHT_GRAY);
        checkBox.setBorder(BorderFactory.createEtchedBorder());
        checkBox.setVisible(true);
        checkBox.addActionListener(this::actionperformed);

        JButton ShoppingCart = new JButton("Shopping Cart"); // creates a button
        ShoppingCart.setBounds(800, 60, 100, 50); // sets the position and size of the button
        ShoppingCart.setFocusable(false); // sets the button to be not focusable
        ShoppingCart.setForeground(Color.WHITE); // sets the color of the text on the button
        ShoppingCart.setBackground(Color.BLACK); // sets the background color of the button
        ShoppingCart.setBorder(BorderFactory.createEtchedBorder());// sets the border of the button
        header.add(ShoppingCart); // adds the button to the panel
        ShoppingCart.setVisible(true); // sets the button to be visible
        ShoppingCart.addActionListener(this::openShoppingCart);// adds an action listener to the button


        String[] column = {PRODUCT_ID, PRODUCT_NAME, CATEGORY, PRICE, INFO, NUMOFITEMS}; // creates an array of strings
        DefaultTableModel model = new DefaultTableModel(column, 0) {
            @Override // overrides the isCellEditable method to make the cells not editable
            public boolean isCellEditable(int row, int column) {
                return false; // sets the cells to be not editable
            }
        };
        table = new JTable(model); // creates a table

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                int numberOfItems = (int) table.getModel().getValueAt(row, 5); // gets the number of items from the table
                if (numberOfItems < 3) {
                    c.setBackground(Color.PINK);
                } else {
                    c.setBackground(Color.white);
                }
                return c;
            }
        });
        TableColumnModel columnModel = table.getColumnModel();
        TableColumn columns = columnModel.getColumn(5);
        columnModel.removeColumn(columns);

        if (ProductList == null) {
            ProductList = new ArrayList<>();
        }
        for (Products product : ProductList) {
            addProductToTable(product, model);
        }

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(600, 300)); // Set your preferred size here
        middle.add(scrollPane);
        table.setVisible(true); // sets the table to be visible



        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.PAGE_AXIS)); // Set the layout to BoxLayout with PAGE_AXIS
        detailsPanel.setPreferredSize(new Dimension(detailsPanel.getWidth(), 400)); // Increase the vertical size of the detailsPanel
        this.add(detailsPanel, BorderLayout.SOUTH);

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getSource() != table) {
                    detailsPanel.removeAll();
                    detailsPanel.revalidate();
                    detailsPanel.repaint();
                }
            }
        });
// Add a MouseListener to the JTable
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    // Get the selected product from the displayedProducts list
                    Products selectedProduct = displayedProducts.get(selectedRow);
                    updateProductDescriptionPanel(selectedProduct);
                }
            }
        });
        // Simulate the selection of "All" in the comboBox
        comboBox.setSelectedItem("All");
        ActionEvent comboBoxEvent = new ActionEvent(comboBox, ActionEvent.ACTION_PERFORMED, "All");
        actionperformed(comboBoxEvent);

        setVisible(true);

    }
    private void addProductToTable(Products product, DefaultTableModel model) { // method to add products to the table
        String productID = product.getProductID();
        String productName = product.getProductName();
        String category = product.getCategory();
        double price = product.getPrice();
        String info = product.getInfo();
        int numberOfItems = product.getNumberOfItems();


        Object[] row = {productID, productName, category, price, info, numberOfItems};// creates an array of objects
        model.addRow(row); // adds the row to the table
    }

    // Separate method to handle the ActionListener
    private void actionperformed(ActionEvent e) {
        if (e.getSource() == comboBox) { // If the source of the event is the combo box
            String selectedCategory = (String) comboBox.getSelectedItem(); // Get the selected category as a string
            DefaultTableModel model = (DefaultTableModel) table.getModel();// Get the table model from the JTable
            model.setRowCount(0); // Clear the table by setting the row count to 0

            displayedProducts.clear();

            for (Products product : ProductList) { // Iterate through the products
                if (product.getCategory().equals(selectedCategory) || selectedCategory.equals("All")) { // If the product category matches the selected category or the selected category is "All"
                    addProductToTable(product, model); // Add the product to the table
                    displayedProducts.add(product); // Add the product to the displayedProducts list
                }
            }
        }
        if (e.getSource() == checkBox) {
            sortTable();
        }
    }

    private void sortTable() {
        String selectedCategory = (String) comboBox.getSelectedItem();
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);

        ArrayList<Products> sortedProducts = new ArrayList<>();
        for (Products product : ProductList) {
            if (product.getCategory().equals(selectedCategory) || selectedCategory.equals("All")) {
                sortedProducts.add(product);
            }
        }
        Collections.sort(sortedProducts, Comparator.comparing(Products::getProductName)); // Sort the products by name
        for (Products product : sortedProducts) { // Iterate through the sorted products
            addProductToTable(product, model); // Add the product to the table
        }
    }
    private void viewDetials(ActionEvent e) {
        JPanel detailsPanel = new JPanel(); // creates a panel
        this.add(detailsPanel, BorderLayout.SOUTH); // adds the panel to the frame
    }
    private void openShoppingCart(ActionEvent e) {
        if (shoppingFrame == null) {
            shoppingFrame = new JFrame("Shopping Cart");
            shoppingFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
            shoppingFrame.setSize(600, 400);
            shoppingFrame.setTitle("Shopping Cart");
            //create a Jtable for the shopping cart
            String[] coloumnNames = {"Product", "Quantity", "Price"};
            shoppingModel = new DefaultTableModel(coloumnNames, 0){
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            cartTable = new JTable(shoppingModel);
            shoppingFrame.add(new JScrollPane(cartTable), BorderLayout.CENTER);

            // Create a panel to display the prices
            JPanel pricePanel = new JPanel();
            pricePanel.setLayout(new GridLayout(5, 1)); // Set the layout to GridLayout
            shoppingFrame.add(pricePanel, BorderLayout.SOUTH);

            // Create JLabels for displaying the prices
            totalPriceLabel = new JLabel("Total Price: $0.0       ", JLabel.RIGHT);
            pricePanel.add(totalPriceLabel);

            discountedPriceLabel = new JLabel("Discounted price : $0.0      ", JLabel.RIGHT);
            pricePanel.add(discountedPriceLabel);

            firstPurchaseLabel = new JLabel("First purchase discount: $0.0      ", JLabel.RIGHT);
            pricePanel.add(firstPurchaseLabel);

            finalPriceLabel = new JLabel("Final Price: $0.0     ", JLabel.RIGHT);
            pricePanel.add(finalPriceLabel);
            pricePanel.add(new JLabel("   "));


        }
        updateShoppingCartTable();
        applyDiscount();
        shoppingFrame.setVisible(true);
    }

    private void updateShoppingCartTable() {
        if (shoppingModel != null) {
            shoppingModel.setRowCount(0);

            for (int i = 0; i < shoppingCart.getProductsCart().size(); i++) {
                Products product = shoppingCart.getProductsCart().get(i);
                Integer quantity = shoppingCart.getQuantity().get(i);
                Double price = product.getPrice() * quantity;
                Object[] row = {product.getProduct(), quantity, price};
                shoppingModel.addRow(row);
            }
            // Calculate and update the total price label after updating the shopping cart table
            double totalPrice = calculateTotalPrice();
            totalPriceLabel.setText("Total Price: $" + totalPrice+"     ");
            totalPriceLabel.setVisible(true);

            double discount = totalPrice - applyDiscount();
            discountedPriceLabel.setText("Discounted price: $" + discount+"      ");

            double finalPrice = totalPrice - discount;
            finalPriceLabel.setText("Final price: $" + finalPrice+"      ");


        }
    }
    public double calculateTotalPrice() {
        double totalPrice = 0.0;
        //for loop to iterate through the products in the shopping cart
        for (int i = 0; i < shoppingCart.getProductsCart().size(); i++) {
            Products product = shoppingCart.getProductsCart().get(i);
            int quantity = shoppingCart.getQuantity().get(i);
            totalPrice += product.getPrice() * quantity;// add the price of each product to the total price
        }
        return totalPrice;
    }

   private double applyDiscount() {
       if (shoppingCart.isEligibleForDiscount()) {
           double fullPrice = calculateTotalPrice();
           double discountedPrice = fullPrice * 0.8;
           return discountedPrice;
       }
       return calculateTotalPrice();
   }
   public void applyFirstDiscounts(User user){
       double totalPrice = calculateTotalPrice();
       double dicountedPrice =0.0;
       if (user.isFirstTimeDiscount()) {
           dicountedPrice = totalPrice *0.1;
       }
       if (firstPurchaseLabel != null) {
           firstPurchaseLabel.setText("First purchase: $" + dicountedPrice);
       }
       updateShoppingCartTable();
   }
    private void updateProductListTable() {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        for (Products product : ProductList) {
            addProductToTable(product, model);
        }
    }

    private void updateProductDescriptionPanel(Products product) {
        // Clear the detailsPanel
        detailsPanel.removeAll();

        // Add the product details to the detailsPanel
        detailsPanel.add(new JLabel("---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------"));
        detailsPanel.add(new JLabel("   Selected Product Details"));
        detailsPanel.add(new JLabel("   "));
        detailsPanel.add(new JLabel("   Product ID: " + product.getProductID()));
        detailsPanel.add(new JLabel("   "));
        detailsPanel.add(new JLabel("   Category: " + product.getCategory()));
        detailsPanel.add(new JLabel("   "));
        detailsPanel.add(new JLabel("   Product Name: " + product.getProductName()));
        detailsPanel.add(new JLabel("   "));
        if (product.getCategory().equals("Electronics")) {
            detailsPanel.add(new JLabel("   Brand: " + ((Electronics) product).getBrand()));
            detailsPanel.add(new JLabel("   "));
            detailsPanel.add(new JLabel("   Warranty Period: " + ((Electronics) product).getWarrantyPeriod()));
            detailsPanel.add(new JLabel("   "));
        }
        if (product.getCategory().equals("Clothing")) {
            detailsPanel.add(new JLabel("   Size: " + ((Clothing) product).getSize()));
            detailsPanel.add(new JLabel("   "));
            detailsPanel.add(new JLabel("   Color: " + ((Clothing) product).getColor()));
            detailsPanel.add(new JLabel("   "));
        }
        detailsPanel.add(new JLabel("   Items Available: " + product.getNumberOfItems()));
        detailsPanel.add(new JLabel("   "));
        detailsPanel.add(new JLabel("   Price: " + product.getPrice()));
        detailsPanel.add(new JLabel("   "));

        JButton addToCart = new JButton("Add to Cart");
        detailsPanel.add(addToCart);
        detailsPanel.add(new JLabel("   "));
        JButton removeFromCart = new JButton("Remove from Cart");
        detailsPanel.add(removeFromCart);

        addToCart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (product.getNumberOfItems() > 0) {
                    shoppingCart.addProducts(product);
                    product.decreaseNumberOfItems();
                    updateShoppingCartTable();
                    // Update the product list table based on the current selection in the comboBox
                    ActionEvent comboBoxEvent = new ActionEvent(comboBox, ActionEvent.ACTION_PERFORMED, (String) comboBox.getSelectedItem());
                    actionperformed(comboBoxEvent);
                    updateProductDescriptionPanel(product);
                } else {
                    JOptionPane.showMessageDialog(null, "Error: Product is out of stock.");
                }
            }
        });
        removeFromCart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get the selected product from the details panel
                Products selectedProduct = product;

                // Check if the product is in the shopping cart
                int productIndex = shoppingCart.getProductsCart().indexOf(selectedProduct);
                if (productIndex != -1) {
                    // Remove the product from the shopping cart
                    shoppingCart.getProductsCart().remove(productIndex);
                    int quantity = shoppingCart.getQuantity().remove(productIndex);

                    // Increase the number of items of the product
                    selectedProduct.increaseNumberOfItems(quantity);

                    // Update the shopping cart table
                    updateShoppingCartTable();

                    // Update the product list table based on the current selection in the comboBox
                    ActionEvent comboBoxEvent = new ActionEvent(comboBox, ActionEvent.ACTION_PERFORMED, (String) comboBox.getSelectedItem());
                    actionperformed(comboBoxEvent);

                    // Update the product description panel
                    updateProductDescriptionPanel(selectedProduct);
                } else {
                    // If the product is not in the shopping cart, show a message
                    JOptionPane.showMessageDialog(null, "This product is not in your shopping cart.");
                }
            }
        });
        // Refresh the detailsPanel
        detailsPanel.revalidate();
        detailsPanel.repaint();
    }
}


