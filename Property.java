import java.util.ArrayList;

public class Property {
  private String propertyId;

  private String sellerId;
  private Address address;
  private int numberOfRooms;
  private int[] priceRange;
  private int floorSize;
  private boolean isListed;
  private ArrayList<String> facilityList;
  private ArrayList<Appointment> appointmentList;

  public Property(String sellerId, String propertyId) {
    this.sellerId = sellerId;
    this.propertyId = propertyId;
  }

  public void setPropertyId(String propertyId) {
    this.propertyId = propertyId;
  }

  public void setSellerId(String sellerId) {
    this.sellerId = sellerId;
  }

  public void setAddress(Address address) {
    this.address = address;
  }

  public void setAppointmentList(ArrayList<Appointment> appointmentList) {
    this.appointmentList = appointmentList;
  }

  public void setFacilityList(ArrayList<String> facilityList) {
    this.facilityList = facilityList;
  }

  public void setFloorSize(int floorSize) {
    this.floorSize = floorSize;
  }

  public void setListed(boolean isListed) {
    this.isListed = isListed;
  }

  public void setNumberOfRooms(int numberOfRooms) {
    this.numberOfRooms = numberOfRooms;
  }

  public void setPriceRange(int[] priceRange) {
    this.priceRange = priceRange;
  }

  public Address getAddress() {
    return address;
  }

  public ArrayList<Appointment> getAppointmentList() {
    return appointmentList;
  }

  public ArrayList<String> getFacilityList() {
    return facilityList;
  }

  public int getFloorSize() {
    return floorSize;
  }

  public int getNumberOfRooms() {
    return numberOfRooms;
  }

  public int[] getPriceRange() {
    return priceRange;
  }

  public String getPropertyId() {
    return propertyId;
  }

  public String getSellerId() {
    return sellerId;
  }

  public boolean getIsListed() {
    return isListed;
  }

}
