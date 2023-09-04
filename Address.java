public class Address {
  private String houseNumber;
  private String street;
  private String city;
  private String state;
  private int postcode;

  public Address(String houseNumber, String street, String city, String state, int postcode) {
    this.houseNumber = houseNumber;
    this.street = street;
    this.city = city;
    this.state = state;
    this.postcode = postcode;
  }

  // accessor
  public String getCity() {
    return city;
  }

  public String getHouseNumber() {
    return houseNumber;
  }

  public String getState() {
    return state;
  }

  public int getPostcode() {
    return postcode;
  }

  public String getStreet() {
    return street;
  }

  // mutator
  public void setCity(String city) {
    this.city = city;
  }

  public void setHouseNumber(String houseNumber) {
    this.houseNumber = houseNumber;
  }

  public void setPostcode(int postcode) {
    this.postcode = postcode;
  }

  public void setState(String state) {
    this.state = state;
  }

  public void setStreet(String street) {
    this.street = street;
  }

  @Override
  public String toString() {
    String str = houseNumber + " , " + street + " , " + city + " , " + postcode + " , " + state;
    return str;
  }
}
