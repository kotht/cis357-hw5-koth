package main.hw5_koth;

import javafx.collections.ObservableMap;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * ProductCatalog class is used to store the catalog of products. It is used by the Store class, looked-into by the
 * register class and contains the ProductSpecification class.
 */
public class ProductCatalog {
    private Map<String, ProductSpecification> productSpecs = new HashMap<>(); // empty Map declaration - (key-value)

    /**
     * This is a constructor for ProductCatalog. When invoked, this constructor will load the product specs
     * from the file. While loading the specs of the file, it will create a new instance of ProductSpecification and
     * assign the instance as a value to the Map.
     * @author Tanner J Koth
     */
    public ProductCatalog(String filePath) {
        try{
            File myFile = new File(filePath);
            Scanner sc = new Scanner(myFile);
            while(sc.hasNextLine()) {
                String[] tokens = sc.nextLine().split(",");
                this.productSpecs.put(tokens[0], new ProductSpecification(tokens[0], Float.parseFloat(tokens[2]), tokens[1]));
            }
            System.out.println(productSpecs);
            System.out.println("File import successful");
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }

    }

    /**
     * This method will take in a String as a parameter that corresponds to itemId. This id is then used to check if the
     * product Spec exists. The following logic is determined by this condition.
     * @param id - String represents itemID
     * @return ProductSpecification - object instance
     */
    public ProductSpecification getSpecification(String id) {
        if(!productSpecs.containsKey(id)){
            throw new NullPointerException("!!! Invalid product code");
        }
        return productSpecs.get(id);
    }

    /**
     * This method is used to verify that the product spec with a corresponding id to the parameter exists. Return
     * boolean based on this condition
     * @param id - String represents itemID
     * @return boolean - if product spec with id exists in catalog
     */
    public boolean prodExists(String id){
        return productSpecs.containsKey(id);
    }

    /**
     * This method allows the user to add an item to the product catalog
     * @param newSpec ProductSpecification
     */
    public void addItem(ProductSpecification newSpec){
        productSpecs.put(newSpec.getId(), newSpec);
    }

    /**
     * This method allows the user to remove an item from the product catalog that matches the param value
     * @param id - itemID
     */
    public void removeItem(String id){
        productSpecs.remove(id);
    }

    /**
     * This method allows the user to make a modification the an item in the product catalog
     * @param id - ItemID
     * @param updatedSpec
     */
    public void modifyItem(String id, ProductSpecification updatedSpec){
        productSpecs.replace(id, updatedSpec);
    }

    /**
     * This method is used to print a table of the products within the catalog. The main logic of this method is
     * outputting a formatted table.
     */
    public void printProdTable(){
        final String FORMAT = "%-9s\t%-14s\t%-10s\n";
        System.out.printf("\n"  + FORMAT, "item code", "item name", "unit price");
        productSpecs.values().forEach((v)->{
            System.out.printf(FORMAT,  v.getId(), v.getName(), String.format("%.2f", v.getPrice()));
        });
        System.out.println();
    }

    /**
     * This method, when invoked, will write the productCatalog to a file.
     * @param filePath - String representing filepath
     */
    public void writeFile(String filePath){
        PrintWriter outputFile = null;

        try{
            File myFile = new File(filePath);
            outputFile = new PrintWriter(myFile);
            for(HashMap.Entry<String, ProductSpecification> elem : productSpecs.entrySet()){
                ProductSpecification itemObjInst = elem.getValue();
                outputFile.printf("%s, %s, %.2f\n", itemObjInst.getId(), itemObjInst.getName(), itemObjInst.getPrice());
            }
        }
        catch (FileNotFoundException e){
            System.out.println("File not found");
        }
        finally {
            outputFile.close();
        }

    }

    public ArrayList<String> getKeys(){
        ArrayList<String> temp = new ArrayList<>();
        productSpecs.forEach((k,v)->{
            temp.add(k);
        });
        return temp;
    }
}
