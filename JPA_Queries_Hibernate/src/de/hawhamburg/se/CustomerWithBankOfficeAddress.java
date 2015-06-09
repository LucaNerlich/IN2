package de.hawhamburg.se;

/**
 * Base declarations for class CustomerWithBankOfficeAddress.
 *
 * @author Bernd Kahlbrandt
 */
public class CustomerWithBankOfficeAddress {

    private String customerName;
    private String officeStreet;

    public CustomerWithBankOfficeAddress(final String customerName,
                                         final String officeStreet) {
        this.setCustomerName(customerName);
        this.setOfficeStreet(officeStreet);
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(final String customerName) {
        this.customerName = customerName;
    }

    public String getOfficeStreet() {
        return officeStreet;
    }

    public void setOfficeStreet(final String officeStreet) {
        this.officeStreet = officeStreet;
    }
}
