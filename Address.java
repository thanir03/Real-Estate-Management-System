public class Address {
  private final String houseNumber;
  private final String street;
  private final String city;
  private final String state;
  private final int postcode;

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

  @Override
  public String toString() {
    return houseNumber + " , " + street + " , " + city + " , " + postcode + " , " + state;
  }
}
