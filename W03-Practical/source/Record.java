public class Record {

    //static variables where the column headers are stored in the csv
    private static final int INVOICE_NUMBER_IN_CSV = 0;
    private static final int STOCK_CODE_IN_CSV = 1;
    private static final int DESCRIPTION_IN_CSV = 2;
    private static final int QUANTITY_IN_CSV = 3;
    private static final int INVOICE_DATE_IN_CSV = 4;
    private static final int INVOICE_UNIT_PRICE_IN_CSV = 5;
    private static final int CUSTOMER_ID_IN_CSV = 6;
    private static final int COUNTRY_IN_CSV = 7;

    private String invoiceNumber;
    private String stockCode;
    private String description;
    private Integer quantity;
    private String invoiceDate;
    private Double unitPrice;
    private Integer customerID;
    private String country;

    //constructor for Record
    public Record(String[] fieldArray) {
        setInvoiceNumber(fieldArray[INVOICE_NUMBER_IN_CSV]);
        setStockCode(fieldArray[STOCK_CODE_IN_CSV]);
        setDescription(fieldArray[DESCRIPTION_IN_CSV]);
        setQuantity(fieldArray[QUANTITY_IN_CSV]);
        setInvoiceDate(fieldArray[INVOICE_DATE_IN_CSV]);
        setUnitPrice(fieldArray[INVOICE_UNIT_PRICE_IN_CSV]);
        setCustomerID(fieldArray[CUSTOMER_ID_IN_CSV]);
        setCountry(fieldArray[COUNTRY_IN_CSV]);
    }

    //setters
    private void setInvoiceNumber(String newInvoiceNumber) {
        invoiceNumber = newInvoiceNumber;
    }

    private void setStockCode(String newStockCode) {
        stockCode = newStockCode;
    }

    private void setDescription(String newDescription) {
        description = newDescription;
    }

    private void setQuantity(String newQuantity) {
        try {
            quantity = Integer.parseInt(newQuantity);
        } catch (NumberFormatException e) {
            quantity = null;
        }
    }

    private void setInvoiceDate(String newInvoiceDate) {
        invoiceDate = newInvoiceDate;
    }

    private void setUnitPrice(String newUnitPrice) {
        try {
            unitPrice = Double.parseDouble(newUnitPrice);
        } catch (NumberFormatException e) {
            unitPrice = null;
        }
    }

    private void setCustomerID(String newCustomerID) {
        try {
            customerID = Integer.parseInt(newCustomerID);
        } catch (NumberFormatException e) {
            customerID = null;
        }
    }

    private void setCountry(String newCountry) {
        country = newCountry;
    }

    //getters
    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public String getStockCode() {
        return stockCode;
    }

    public String getDescription() {
        return description;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public int getCustomerID() {
        return customerID;
    }

    public String getCountry() {
        return country;
    }

    public boolean getIsCanceled() {
        return isCanceled;
    }
}
