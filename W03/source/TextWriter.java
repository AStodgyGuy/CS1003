import java.util.ArrayList;
import java.util.HashMap;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;

public class TextWriter {

    /*
        This class takes the ArrayList of Records and writes a .txt fle formatted as per specifiication.
    */
    public TextWriter(ArrayList<Record> al, String exportDestination) {
        //remove the first line in the arrayList as that contains the column headers
        al.remove(0);

        //A new Hashmap to store the total cost of all the invoices using invoice number as a key
        HashMap<String, Double> hmap = new HashMap<String, Double>();

        //initial target invoice number
        String target = al.get(0).getInvoiceNumber();

        //aray to store and calcuate total invoice prices
        ArrayList<Double> priceArrayList = new ArrayList<Double>();

        //create buffered writer to write to file
        try {
            FileWriter fw = new FileWriter(exportDestination);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw);

            //loop through every Record in the record ArrayList
            for (int j = 0; j < al.size(); j++) {

                if (target.equals(al.get(j).getInvoiceNumber())) {
                    target = coreFunctionality(al, j, priceArrayList, exportDestination, out);
                    checkForLast(al, j, priceArrayList, hmap, exportDestination, out);

                /*
                    if the target invoice number and the invoice number in the next position of the Record Arraylist do not match,
                    this is a new invoice number since the data is assumed to be already sorted
                */
                } else {

                    //display the total of the previous prices of the invoice together
                    writeTotalCost(calculateTotalCost(priceArrayList, al.get(j - 1).getInvoiceNumber(), hmap), exportDestination, out);

                    //clear the price arraylist
                    priceArrayList.clear();

                    target = coreFunctionality(al, j, priceArrayList, exportDestination, out);
                    checkForLast(al, j, priceArrayList, hmap, exportDestination, out);
                }
            }

            //writes the maximum and minimum total costs to the file
            writeMaximumAndMinimum(hmap, exportDestination, out, al.get(0).getInvoiceNumber());

            //close the writer
            out.close();

        } catch (IOException e) {
            System.out.println("Unable to create new file, file may already exist!");
        }
    }

    /*
        method which calculates the total cost of an invoice since the priceArrayList stores the
        cost for each line (i.e unit price * quantity for that particular invoice line).
        The total cost for that invoice is mapped to a hashmap using its invoice number as a key
        This method returns the total cost of the invoice so that it can be wrritten into the txt file
    */
    private double calculateTotalCost(ArrayList<Double> priceArrayList, String invoiceNumber, HashMap<String, Double> hmap) {
        double total = 0;

        for (double price : priceArrayList) {
            total = total + price;
        }

        //inserting the total cost into a hashmap using the invoice number as a key
        hmap.put(invoiceNumber, total);

        return total;
    }

    /*
        method which writes the specified format of the otuput file as specified in the specification
    */
    private void writeInvoiceDetails(ArrayList<Record> al, int j, String exportDestination, PrintWriter out) {
        out.println("Invoice Number: " + al.get(j).getInvoiceNumber());
        out.println("Stock Code: " + al.get(j).getStockCode());
        out.println("Description: " + al.get(j).getDescription());
        out.println("Quantity: " + al.get(j).getQuantity());
        out.println("Unit Price: " + al.get(j).getUnitPrice());
    }

    /*
        method which writes to total cost of the invoice to the output file and adds in
        a new line to separate the different invoices
    */
    private void writeTotalCost(double totalCost, String exportDestination, PrintWriter out) {
        out.println("Total Price: " + totalCost);
        out.println();
    }

    /*
        method which finds the maximum value of an invoice and minimum value of an invoice.
        All invoice numbers that start with a "C" must be ignored since they are cancelled orders, so
        a loop is created to filter the cancelled orders out. Minimum and maximum are obtained by
        comparing the next value in the hashmap with the previous highest/lowest value in the hashmap.
        Once the max and min are found, the specified format of this information is written to the
        output file
    */
    private void writeMaximumAndMinimum(HashMap<String, Double> hmap, String exportDestination, PrintWriter out, String invoiceNumber) {
        String max_invoiceNumber = "";
        String min_invoiceNumber = "";

        double maximum = hmap.get(invoiceNumber);
        double minimum = hmap.get(invoiceNumber);

        for (String w : hmap.keySet()) {
            if (!(w.startsWith("C"))) {
                if (hmap.get(w) > maximum) {
                    maximum = hmap.get(w);
                    max_invoiceNumber = w;
                }
                if (hmap.get(w) > 0 && hmap.get(w) < minimum) {
                    minimum = hmap.get(w);
                    min_invoiceNumber = w;
                }
            }
        }
        out.println("Minimum priced Invoice Number: " + min_invoiceNumber + " with " + minimum);
        out.println("Maximum priced Invoice Number: " + max_invoiceNumber + " with " + maximum);
    }

    /*
        method which sends information to the writeInvoiceDetails method for it to write the formatted text
        to the output file.
        Then it multiplies the current record's unit price and quantity to obtain total cost for that particular
        line in the invoice and adds it to a price array.
        This method then sends back the current invoice number as the target invoice number.
    */
    private String coreFunctionality(ArrayList<Record> al, int j, ArrayList<Double> priceArrayList, String exportDestination, PrintWriter out) {

        writeInvoiceDetails(al, j, exportDestination, out);
        priceArrayList.add(al.get(j).getUnitPrice() * al.get(j).getQuantity());
        String target = al.get(j).getInvoiceNumber();

        return target;
    }

    /*
        method which checks if the current invoice number is the last record in the arrayList
    */
    private void checkForLast(ArrayList<Record> al, int j, ArrayList<Double> priceArrayList, HashMap<String, Double> hmap, String exportDestination, PrintWriter out) {
        if (j == al.size() - 1) {
            writeTotalCost(calculateTotalCost(priceArrayList, al.get(j).getInvoiceNumber(), hmap), exportDestination, out);
        }
    }

}
