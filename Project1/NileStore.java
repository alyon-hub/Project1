import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.*;

import java.text.DecimalFormat;
import java.text.Format;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

/*
 * Name: Avriel Lyon
 * Course: CNT 4714 - Fall 2022
 * Assignment title: Project 1 - Event-driven Enterprise Simulation
 * Date: Friday September 15, 2022
 */

public class NileStore extends JFrame {
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  private static final int WIDTH = 700;
  private static final int HEIGHT = 300;
  private static final int FIELD = 10;
  private JLabel jlItemID, jlQuantity, jlDetails, jlSubtotal;
  private JButton jbProcess, jbViewOrder, jbNewOrder, jbConfirmItem, jbFinishOrder, jbExit;
  private JTextField tfItemID, tfQuantity, tfDetails, tfSubtotal;
  private ArrayList < String > inventoryLine = new ArrayList < String > (); //Creates an Array List of the current item in the inventory 
  private ArrayList < order > shoppingCart = new ArrayList < order > (); //Creates an Array List of the shopping cart
  private double shoppingCartSubTotal; //Keeps track of the total after user confirms item
  private int itemNumber = 1; //Number of items in the cart
  private NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(); //Formats currency
  private DecimalFormat decimalFormat = new DecimalFormat("0.00"); //Formats decimals

  public NileStore() throws FileNotFoundException {
    //Sets title and size of the window
    setTitle("Nile Dot Com - Fall 2022");
    setSize(WIDTH, HEIGHT);

    Container pane = getContentPane();
    GridLayout grid6by2 = new GridLayout(6, 2, 8, 2);
    GridLayout grid4by2 = new GridLayout(4, 2, 8, 4);

    JPanel northPanel = new JPanel();
    JPanel southPanel = new JPanel();

    northPanel.setLayout(grid6by2);
    southPanel.setLayout(grid4by2);

    //Create Java Labels
    jlItemID = new JLabel("Enter Item ID for Item #1:  ", SwingConstants.RIGHT);
    jlQuantity = new JLabel("Enter quantity for Item #1:  ", SwingConstants.RIGHT);
    jlDetails = new JLabel("Details for Item #1:  ", SwingConstants.RIGHT);
    jlSubtotal = new JLabel("Order subtotal for 0 item(s):  ", SwingConstants.RIGHT);

    //Create Java Text Fields
    tfItemID = new JTextField(FIELD);
    tfQuantity = new JTextField(FIELD);
    tfDetails = new JTextField(FIELD);
    tfSubtotal = new JTextField(FIELD);

    //Create Java Buttons
    jbProcess = new JButton("Process Item #1");
    jbViewOrder = new JButton("View Order");
    jbNewOrder = new JButton("New Order");
    jbConfirmItem = new JButton("Confirm Item #1");
    jbFinishOrder = new JButton("Finish Order");
    jbExit = new JButton("Exit");

    //Disable buttons for now
    tfDetails.setEnabled(false);
    tfSubtotal.setEnabled(false);
    jbConfirmItem.setEnabled(false);
    jbViewOrder.setEnabled(false);
    jbFinishOrder.setEnabled(false);

    //Makes the disabled text fields easier to read
    tfSubtotal.setDisabledTextColor(Color.BLACK);
    tfDetails.setDisabledTextColor(Color.BLACK);

    //Add items to northPanel
    northPanel.add(jlItemID);
    northPanel.add(tfItemID);
    northPanel.add(jlQuantity);
    northPanel.add(tfQuantity);
    northPanel.add(jlDetails);
    northPanel.add(tfDetails);
    northPanel.add(jlSubtotal);
    northPanel.add(tfSubtotal);

    //Add items to southPanel
    southPanel.add(jbProcess);
    southPanel.add(jbConfirmItem);
    southPanel.add(jbViewOrder);
    southPanel.add(jbFinishOrder);
    southPanel.add(jbNewOrder);
    southPanel.add(jbExit);

    //Add panels to pane
    pane.add(northPanel, BorderLayout.NORTH);
    pane.add(southPanel, BorderLayout.SOUTH);

    //Add action listener to buttons
    jbExit.addActionListener(new ExitButtonHandler());
    jbProcess.addActionListener(new ProcessButtonHandler());
    jbConfirmItem.addActionListener(new ConfirmButtonHandler());
    jbNewOrder.addActionListener(new NewButtonHandler());
    jbViewOrder.addActionListener(new ViewButtonHandler());
    jbFinishOrder.addActionListener(new FinishButtonHandler());
  }

  //Processes the current item, determining if it is valid
  private class ProcessButtonHandler implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      String outputMessage, priceData, itemIDFromFile, itemInStockFromFile, itemDescriptionFromFile, itemIDFromText, itemQuantityFromText, item = null;
      String[] itemInfo = null;
      boolean foundID = false, isItemQtyOK = false, isItemInStock = true;
      int quantity;

      File inventoryFile = new File("inventory.txt");
      Scanner aScanner;

      try {
        aScanner = new Scanner(inventoryFile);

        itemIDFromText = tfItemID.getText();
        itemQuantityFromText = tfQuantity.getText();
        quantity = Integer.parseInt(itemQuantityFromText);

        //Creates room for the values to be added
        inventoryLine.add("0");
        inventoryLine.add("0");
        inventoryLine.add("0");
        inventoryLine.add("0");
        inventoryLine.add("0");
        inventoryLine.add("0");

        while (aScanner.hasNext()) {
          item = aScanner.nextLine();
          itemInfo = item.split(","); //Splits string so that I can split the values

          //Setting the values of the current item to strings	
          itemIDFromFile = itemInfo[0].trim();
          itemDescriptionFromFile = itemInfo[1].trim();
          itemInStockFromFile = itemInfo[2].trim();
          priceData = itemInfo[3].trim();

          //Setting the Boolean value
          isItemInStock = Boolean.parseBoolean(itemInStockFromFile);

          //If the itemIDs match
          if (itemIDFromFile.equals(itemIDFromText)) {
            foundID = true;
            inventoryLine.set(0, itemIDFromFile);
            inventoryLine.set(1, itemDescriptionFromFile);
            inventoryLine.set(2, itemInStockFromFile);
            inventoryLine.set(3, priceData);
            //If the item quantity is in the limit
            if (quantity >= 1) {
              isItemQtyOK = true;
            }
            //Since the item was found, leave the loop
            break;
          }
        }

        //If the item was not found, print the error
        if (!foundID) {
          outputMessage = "item ID " + itemIDFromText + " not in file";
          JOptionPane.showMessageDialog(null, outputMessage, "Nile Dot Com - ERROR", JOptionPane.ERROR_MESSAGE);
          clear();
        } else {
          //If the item quantity wasn't okay, or nothing was entered
          if (!isItemQtyOK) {
            outputMessage = "Invalid input for number of line items or quantity of items";
            JOptionPane.showMessageDialog(null, outputMessage, "Nile Dot Com - ERROR", JOptionPane.ERROR_MESSAGE);
            clear();
          } 
          //If the item wasn't in stock, print the error
          if (!isItemInStock) {
            outputMessage = "Sorry... that item is out of stock, please try another item";
            JOptionPane.showMessageDialog(null, outputMessage, "Nile Dot Com - ERROR", JOptionPane.ERROR_MESSAGE);
            clear();
          }
          else {
            double price = Double.parseDouble(inventoryLine.get(3));
            double discountCalc = getDiscount(quantity);
            double totalPriceForItem = (double)(price * quantity) - ((price * quantity) * (discountCalc / 100));
            int discount = (int) discountCalc;
            String displayItem = inventoryLine.get(0) + "  " + inventoryLine.get(1) + "  " +
              currencyFormat.format(price) + "  " + quantity + "  " + discount + "%  " + currencyFormat.format(totalPriceForItem);
            tfDetails.setText(displayItem);
            jbConfirmItem.setEnabled(true);
            jbProcess.setEnabled(false);
          }
        }

        //Close scanner after use
        aScanner.close();

      } catch (FileNotFoundException e1) {
        e1.printStackTrace();
      } catch (NumberFormatException e1) {
        outputMessage = "Invalid input for number of line items or quantity of items";
        JOptionPane.showMessageDialog(null, outputMessage, "Nile Dot Com - ERROR", JOptionPane.ERROR_MESSAGE);
        clear();
      }
    }
  }

  //Adds item to the shopping cart
  private class ConfirmButtonHandler implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      String itemQuantityFromText = tfQuantity.getText();
      int quantity = Integer.parseInt(itemQuantityFromText);
      double price = Double.parseDouble(inventoryLine.get(3));
      double discountCalc = getDiscount(quantity);
      int discount = (int) discountCalc;
      double totalPriceForItem = (double)(price * quantity) - ((price * quantity) * (discountCalc / 100));

      order item = new order();

      item.setItemID(inventoryLine.get(0));
      item.setDescription(inventoryLine.get(1));
      item.setStock(inventoryLine.get(2));
      item.setPrice(inventoryLine.get(3));
      item.setQuantity(itemQuantityFromText);

      String temp = String.valueOf(discount);
      item.setDiscount(temp);

      temp = String.valueOf(totalPriceForItem);
      item.setTotalPrice(temp);

      //Adds the confirmed item to the shopping cart
      shoppingCart.add(item);

      //Calculates the total
      shoppingCartSubTotal += totalPriceForItem;

      //Updates text fields and buttons
      tfSubtotal.setText("" + currencyFormat.format(shoppingCartSubTotal) + "");
      jbViewOrder.setEnabled(true);
      jbFinishOrder.setEnabled(true);

      String outputmessage = "Item #" + itemNumber + " accepted. Added to your cart.";
      JOptionPane.showMessageDialog(null, outputmessage, "Nile Dot Com - Item Confirmed", JOptionPane.PLAIN_MESSAGE);

      //Increments itemNumber
      itemNumber++;

      //Create Java Labels
      jlItemID.setText("Enter Item ID for Item #" + itemNumber + ":  ");
      jlQuantity.setText("Enter quantity for Item #" + itemNumber + ":  ");
      jlDetails.setText("Details for Item #" + itemNumber + ":  ");
      jlSubtotal.setText("Order subtotal for " + (itemNumber-1) + " item(s):  ");

      //Create Java Buttons
      jbProcess.setEnabled(true);
      jbConfirmItem.setEnabled(false);
      jbProcess.setText("Process Item #" + itemNumber);
      jbConfirmItem.setText("Confirm Item #" + itemNumber);

      //Clears the text fields
      clear();
    }
  }

  //Shows the current order in a new window
  private class ViewButtonHandler implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      String outputStr = "";

      for (int i = 0; i < itemNumber - 1; i++) {
        Double totalPriceFormat = Double.parseDouble(shoppingCart.get(i).getTotalPrice());
        outputStr += ((i + 1) + ". " + shoppingCart.get(i).getItemID() + " " + shoppingCart.get(i).getDescription() +
          " $" + shoppingCart.get(i).getPrice() + " " + shoppingCart.get(i).getQuantity() + " " + shoppingCart.get(i).getDiscount() + "% " + currencyFormat.format(totalPriceFormat) + "\n");
      }

      JOptionPane.showMessageDialog(null, outputStr, "Nile Dot Com - Current Shopping Cart Status", JOptionPane.INFORMATION_MESSAGE);
    }
  }

  //Shows finalized order in window and adds it to an invoice
  private class FinishButtonHandler implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      Format dateFormat = new SimpleDateFormat("MM/dd/YY, hh:mm:ss a z");
      Format permutationDate = new SimpleDateFormat("ddMMYYYYhhmm");
      StringBuilder outputStr = new StringBuilder();
      StringBuilder invoice = new StringBuilder();
      String permDate = permutationDate.format(new Date());
      String currentDate = dateFormat.format(new Date());
      String items = "", taxStr, taxRate, orderSubTotal, orderTotalStr, thankYou;
      String lines = "";
      double tax = 0.06 * shoppingCartSubTotal;
      double orderTotal = tax + shoppingCartSubTotal;
      int display = itemNumber - 1;

      String totalMessage = "Date: " + currentDate.replaceFirst("0", "") + "\n\nNumber of line items: " + display + "\n\nItem# / ID / Title / Price / Qty / Disc % / Subtotal:\n";

      //Creates string with every item in the shoppingCart
      for (int i = 0; i < itemNumber - 1; i++) {
        Double totalPriceFormat = Double.parseDouble(shoppingCart.get(i).getTotalPrice());
        items += ((i + 1) + ". " + shoppingCart.get(i).getItemID() + " " + shoppingCart.get(i).getDescription() +
          " $" + shoppingCart.get(i).getPrice() + " " + shoppingCart.get(i).getQuantity() + " " + shoppingCart.get(i).getDiscount() + "% " + currencyFormat.format(totalPriceFormat) + "\n");
      }

      orderSubTotal = "\n\nOrder Subtotal: " + currencyFormat.format(shoppingCartSubTotal) + "\n\n";

      taxRate = "Tax rate: 6%\n\n";

      taxStr = Double.toString(tax);
      taxStr = "Tax amount: " + currencyFormat.format(tax) + "\n\n";

      orderTotalStr = Double.toString(orderTotal);
      orderTotalStr = "ORDER TOTAL: " + currencyFormat.format(orderTotal) + "\n\n";

      thankYou = "Thanks for shopping at Nile Dot Com!\n";

      //Appending strings to the StringBuilder
      outputStr.append(totalMessage);
      outputStr.append(items);
      outputStr.append(orderSubTotal);
      outputStr.append(taxRate);
      outputStr.append(taxStr);
      outputStr.append(orderTotalStr);
      outputStr.append(thankYou);

      JOptionPane.showMessageDialog(null, outputStr, "Nile Dot Com - FINAL INVOICE", JOptionPane.INFORMATION_MESSAGE);

      for (int i = 0; i < itemNumber - 1; i++) {
        Double totalPriceFormat = Double.parseDouble(shoppingCart.get(i).getTotalPrice());
        Double discountDecimal = Double.parseDouble(shoppingCart.get(i).getDiscount()) / 100;

        lines += (permDate + ", " + shoppingCart.get(i).getItemID() + ", " + shoppingCart.get(i).getDescription() +
          ", " + shoppingCart.get(i).getPrice() + ", " + shoppingCart.get(i).getQuantity() + ", " + discountDecimal + ", $" + decimalFormat.format(totalPriceFormat) + ", " + currentDate.replaceFirst("0", "") + " \n");
      }

      invoice.append(lines);

      File file = new File("transactions.txt");
      FileWriter writer = null;

      try {
        try {
          //Allows the file to store multiple transactions
          writer = new FileWriter(file, true);
        } catch (IOException e1) {
          e1.printStackTrace();
        }
        //Appends StringBuilder to the file
        writer.append(invoice);
      } catch (IOException e1) {
        e1.printStackTrace();
      } finally {
        if (writer != null)
          try {
            writer.close();
          } catch (IOException e1) {
            e1.printStackTrace();
          }
      }
      
      tfItemID.setEnabled(false);
      tfQuantity.setEnabled(false);
    }
  }

  //Resets everything
  private class NewButtonHandler implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      //Reset Java Labels
      jlItemID.setText("Enter Item ID for Item #1:  ");
      jlQuantity.setText("Enter quantity for Item #1:  ");
      jlDetails.setText("Details for Item #1:  ");
      jlSubtotal.setText("Order subtotal for 0 item(s):  ");

      //Reset Java Text Fields
      tfItemID.setText("");
      tfQuantity.setText("");
      tfDetails.setText("");
      tfSubtotal.setText("");

      //Reset Java Buttons
      jbProcess.setText("Process Item #1");
      jbConfirmItem.setText("Confirm Item #1");
      jbFinishOrder.setText("Finish Order");
      jbExit.setText("Exit");

      //Disable buttons and text fields
      //Enable the user input text fields
      tfItemID.setEnabled(true);
      tfQuantity.setEnabled(true);
      tfSubtotal.setEnabled(false);
      tfDetails.setEnabled(false);
      jbConfirmItem.setEnabled(false);
      jbViewOrder.setEnabled(false);
      jbFinishOrder.setEnabled(false);

      //Makes the disabled text fields easier to read
      tfSubtotal.setDisabledTextColor(Color.BLACK);
      tfDetails.setDisabledTextColor(Color.BLACK);

      //Reset itemNumber, arrays, and shoppingCartSubTotal
      itemNumber = 1;
      inventoryLine.clear();
      shoppingCart.clear();
      shoppingCartSubTotal = 0;
    }
  }

  //Exits the program
  private class ExitButtonHandler implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      System.exit(0);
    }
  }
  
  /*
   * Name: Avriel Lyon
   * Course: CNT 4714 - Fall 2022
   * Assignment title: Project 1 - Event-driven Enterprise Simulation
   * Date: Friday September 15, 2022
   */

  //Get discount amount based on quantity of items
  public int getDiscount(int quantity) {
    if (quantity >= 1 && quantity <= 4) {
      return 0;
    }
    if (quantity >= 5 && quantity <= 9) {
      return 10;
    }
    if (quantity >= 10 && quantity <= 14) {
      return 15;
    }
    if (quantity >= 15) {
      return 20;
    }
    return 0;
  }

  //Clears the text fields
  public void clear() {
    tfItemID.setText("");
    tfQuantity.setText("");
  }

  //Where it all begins
  public static void main(String[] args) throws FileNotFoundException {
    JFrame NileStore = new NileStore();
    NileStore.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    NileStore.setLocationRelativeTo(null);
    NileStore.setVisible(true);

  }
}