package main.hw5_koth;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

/**
 * Register class is used to implement a cash register in the store
 */
public class Register {
    /** Member Variables */
    private Sale sale;
    private ProductCatalog catalog;
    private double dailyTotal = 0;
    private boolean salesCompleted = false;

    /**
     * Constructor used to obtain the catalog for the register.
     * @param catalog Contains all items
     */
    public Register(ProductCatalog catalog){
        this.catalog = catalog;
    }

    /**
     * This method is the primary controller of sales logic. This method will use conditional logic to invoke the
     * necessary method to complete the sale in an according fashion.
     */
    public void initSale(){
        Scanner sc = new Scanner(System.in); // create scanner obj
        char c;
        String itemCode;

        // Do while user would like to complete a sale; c = 'Y'
        do{
            c = promptSale(sc); // prompt sale to get user input (Y/N)
            if(c == 'Y'){ // verify c == 'Y'
                System.out.println("--------------------");
                makeNewSale(); // invoke makeNewSale to create new instance of sale obj
                do{ // Continue sale until product code = -1; user has selected all items
                    itemCode = promptItemCode(sc); // obtain itemCode
                    switch (itemCode) { // switch statement to based on itemCode value
                        case "0000":
                            catalog.printProdTable();
                            break;
                        case "-1": break;
                        default:
                            if(catalog.prodExists(itemCode)){
                                // obtain name of itemCode and invoke printItemName method
                                printItemName(catalog.getSpecification(itemCode).getName());
                                int quantity;
                                do{
                                    quantity = promptItemQuantity(sc);
                                } while(quantity <=0 );
                                // obtain itemPrice and quantity and invoke printItemTotal method
                                printItemTotal(catalog.getSpecification(itemCode).getPrice(), quantity);

                                if(sale.unique(itemCode))
                                    enterItem(itemCode,quantity);
                                else
                                    sale.addToItem(itemCode, quantity);
                            }
                            else
                                System.out.println("!!! Invalid product code\n");
                    }
                    if(itemCode=="-1") // break if itemCode = -1
                        break;
                }while(!itemCode.equals("-1"));
                sale.printSaleReceipt(); // invoke printSaleReceipt in sale obj instance
//                makePayment(); // invoke makePayment method
                sale.becomeComplete();
            }
        }while (c == 'Y');
        if(c == 'N'){
            // Output a formatted prompt for the total of the day
            System.out.println("\n===================");
            System.out.println("The total sale for the day is  $  " + String.format("%.2f",dailyTotal));
            setSalesCompleted(true);
        }
    }

    /**
     * This method will create a new sale instance, if a new sale is started.
     */
    public void makeNewSale(){sale = new Sale();}

//    public void endSale(){
//        if(sale.isComplete())
//        sale.becomeComplete();
//    }

    /**
     * This method is used to make a payment on the sale. The method implements the use of a try-catch block to handle
     * an inputMismatch exception. In the try block the method will invoke the makePayment method in the sales class to
     * handle the rest of the payment as the register class cannot directly access the payment class (security purpose).
     * The method will then wait until the callee has completed and will invoke the getSubtotal method in the sale
     * class. This value is then added to the running dailyTotal.
     * @param
     * */
    public void makePayment(float amt){
        try{
            sale.makePayment(amt); // invoke makePayment of sale
            dailyTotal += sale.getSubtotal(); // add subtotal to daily total once transaction has completed
        }
        catch (InputMismatchException e){
            System.out.println("!!! Invalid data type");
        }

    }

    /**
     * This method will print the Item total.
     * @param price - Price of the item
     * @param quantity - Quantity user would like to purchase
     */
    public void printItemTotal(float price, int quantity){
        System.out.print("         item total: $   " + String.format("%.2f",price * quantity) +"\n");
    }

    /**
     * When invoked this function will invoke the makeLineItem method in the sale obj instance with the required params.
     * @param id - ItemId user entered
     * @param quantity - Quantity user entered
     */
    public void enterItem(String id, int quantity) {
        if(sale.unique(id))
            sale.makeLineItem(catalog.getSpecification(id), quantity);
        else
            sale.addToItem(id, quantity);
    }

    /**
     * This method prompts the user for a sale. The method will only return the value of char when the user has entered
     * a valid value (Y/N).
     * @param sc Scanner Object
     * @return char - Y/N
     */
    public char promptSale(Scanner sc){
        char c;
        do{
            System.out.print("Beginning a new sale (Y/N) ");
            c = Character.toUpperCase(sc.next().charAt(0));
            if(c == 'Y' || c=='N')
                break;
            System.out.println("Please enter a valid value (Y/N)");
        } while(c!= 'Y' || c!='N');
        return c;
    }

    /**
     * This method prompts the user for the itemCode. It then collects this value with the use of a scanner and returns
     * the itemCode.
     * @param sc Scanner Object
     * @return String itemCode
     */
    public String promptItemCode (Scanner sc){
        System.out.print("Enter product code:  ");
        return sc.next().trim();
    }

    /**
     * This method will print the name of the item after invoked.
     * @param name - Name of item
     */
    public void printItemName(String name){
        System.out.println("         item name: " + name);
    }

    /**
     * This method will prompt the user for quantity of items they would like to purchase. It implements a
     * try-catch-finally block to catch a NumberFormatException and return a valid quantity.
     * @param sc Scanner Object
     * @return int - quantity of items user is purchasing
     */
    public int promptItemQuantity(Scanner sc){
        int quantity = 0;
        try{
            System.out.print("Enter quantity:      ");
            quantity = Integer.parseInt(sc.next().trim());
        }
        catch (NumberFormatException e){
            System.out.println("!!! Invalid data type");
        }
        finally{
            return quantity;
        }
    }

    /**
     * When invoked this method will set the boolean value of salesCompleted to true, meaning that all sales have been
     * completed for the day.
     * @param value - Boolean (T/F) - sales completed or not completed
     */
    public void setSalesCompleted(boolean value){
        this.salesCompleted = value;
    }

    /**
     * This method will return the value of the member variable salesCompleted. This method will only be invoked to
     * by the Store class.
     * @return boolean - salesCompleted
     */
    public boolean getSaleCompleted(){
        return this.salesCompleted;
    }

    public float getItemTotal(){
        return sale.getSubtotal();
    }

    public float getTotal(){
        return sale.getTotal();
    }

    public List<SalesLineItem> getSalesLineItems(){
        return sale.getSLI();
    }

    public double getDailyTotal(){
        return dailyTotal;
    }
}
