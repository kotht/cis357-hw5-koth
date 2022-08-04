package main.hw5_koth;

import java.util.*;

/**
 * Sale class is used to process a sale. It is used by the register class, and contians the SalesLineItem class,
 * and uses the Payment class.
 */
public class Sale {
    /** Member Variables */
    private List<SalesLineItem> saleLineItems;
    private Date date;
    private boolean isComplete = false;
    private Payment payment;
    private final float TAX_RATE = 1.06f;

    /**
     * Default constructor for Sale
     */
    public Sale(){
        this.saleLineItems = new ArrayList<>();
        this.date = new Date();
        this.isComplete = false;
    }

    public List<SalesLineItem> getSLI(){
        return saleLineItems;
    }

    /**
     * This method, when invoked, will also invoke the add method of the salesLineItems class to create a salesLineItem.
     * @param spec - ProductSpecification
     * @param quantity - Quantity of items user request to purchase
     */
    public void makeLineItem(ProductSpecification spec, int quantity) {
        saleLineItems.add(new SalesLineItem(spec,quantity));
    }

    /**
     * This method, when invoked, will make the boolean member variable (isComplete) true.
     */
    public void becomeComplete() {
        isComplete = true;
    }

    /**
     * This method, when invoked, will check if the itemCode passed as a param is unique or already exists. If it
     * already exists it will return false, else if no salesLineItem contains this itemCode it will return true.
     * @param itemCode
     * @return boolean
     */
    public boolean unique(String itemCode){
        for(SalesLineItem item : saleLineItems){
            if(item.getId().equals(itemCode))
                return false;
        }
        return true;
    }

    /**
     * This method, when invoked, will check if any itemCode already exists and will add the quanitity to that item.
     * @param itemCode
     * @param quantity
     */
    public void addToItem(String itemCode, int quantity){
        for(SalesLineItem item : saleLineItems){
            if(item.getId().equals(itemCode)){
                item.setQuantity(quantity);
            }
        }
    }

    /**
     * This method, when invoked, will calculate the subtotal of the sale. It will return a float that represents the
     * sale subtotal.
     * @return float - salesSubtotal
     */
    public float getSubtotal(){
        float subTotal = 0;
        Iterator it = saleLineItems.iterator();
        while(it.hasNext()) {
            SalesLineItem sli = (SalesLineItem) it.next();
            subTotal += sli.getSubtotal();
        }
        return subTotal;
    }

    /**
     * This method, when invoked, will calculate the total of the sale. If itemID begins with B it does not have sales
     * tax, however all other items do have sales tax.
     * @return float - salesTotal
     */
    public float getTotal() {
        float total = 0;
        Iterator it = saleLineItems.iterator();
        while(it.hasNext()) {
            SalesLineItem sli = (SalesLineItem) it.next();
            if(sli.getId().charAt(0) == ('B'))
                total += sli.getSubtotal();
            else
                total += (sli.getSubtotal() * TAX_RATE);
        }
        return total;
    }

    /**
     * This method, when invoked, will allow the user to make a payment for the sale. This method allows the Payment
     * class to store the values of the payment, as a means of encapsulation for security purposes. This method will
     * then prompt the user accordingly based on their input.
     * @param cashTendered - amount user has tendered
     * @param
     */
    public void makePayment(float cashTendered) {
        try{
            String totalsFormat = "%-19s  $ %7.2f";
            payment = new Payment(cashTendered);
            while(payment.getAmount() < getTotal()){
                System.out.println("!!! Not enough. Enter again.");
                System.out.print("Tendered amount \t $   ");
                payment.setAmount(cashTendered);
            }
            System.out.printf(totalsFormat, "Change", 0 - (getTotal() - payment.getAmount()));
            System.out.println();
            System.out.println("----------------------------\n");
        } catch (InputMismatchException e){
            System.out.println("!!! Invalid data type");
        }
    }

    /**
     * This method, when invoked, will output a formatted receipt to the user. The receipt will contain all
     * saleLineItems for the sale, with their quantity. It will also contain the subtotal, total with tax, and
     * prompt the user for payment.
     */
    public void printSaleReceipt(){
        String totalsFormat = "%-19s  $ %7.2f";

        System.out.println("----------------------------");
        System.out.println("Items list:");

        saleLineItems.sort(Comparator.comparing(SalesLineItem::getId));
        saleLineItems.forEach(e->System.out.printf(e.toString()));

        System.out.printf(totalsFormat, "Subtotal", this.getSubtotal());
        System.out.println();
        System.out.printf(totalsFormat, "Total with Tax (6%)", this.getTotal());
        System.out.println();
        System.out.print("Tendered amount \t $   ");
    }

//    public String toString(){
//
//    }

}
