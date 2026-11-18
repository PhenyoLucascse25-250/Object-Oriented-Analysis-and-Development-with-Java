public class Customer {
    private String customerID;
    private String firstName;
    private String lastName;
    private String Phone;
    private String idNumber;
    private String email;

    public Customer(String customerID, String firstName, String lastName, String Phone, String idNumber, String email) {
        this.customerID = customerID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.Phone = Phone;
        this.idNumber = idNumber;
        this.email = email;
    }

    public String getCustomerID() { return customerID; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getPhone() { return Phone; }
    public String getIdNumber() { return idNumber; }
    public String getEmail() { return email; }
}