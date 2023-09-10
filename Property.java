import java.util.ArrayList;

// Property class

public class Property {
  private String propertyId;
  private String sellerId;
  private Address address;
  private int numberOfRooms;
  private Integer[] priceRange;
  private int floorSize;
  private boolean isListed;
  private ArrayList<String> facilityList;
  private ArrayList<String> appointmentIdList;

  public Property(String sellerId, String propertyId, int numberOfRooms, Integer[] priceRange, int floorSize,
      boolean isListed, ArrayList<String> facilityList) {
    this.sellerId = sellerId;
    this.propertyId = propertyId;
    this.numberOfRooms = numberOfRooms;
    this.priceRange = priceRange;
    this.floorSize = floorSize;
    this.isListed = isListed;
    this.facilityList = facilityList;
    this.appointmentIdList = new ArrayList<>();
  }

  public Address getAddress() {
    return address;
  }

  public ArrayList<String> getAppointmentList() {
    return appointmentIdList;
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

  public Integer[] getPriceRange() {
    return priceRange;
  }

  public String getPropertyId() {
    return propertyId;
  }

  public Seller getSeller() {
    ArrayList<Seller> sellerList = SellerDatabase.read();
    for (Seller seller : sellerList) {
      if (sellerId.equals(seller.getCredential().getUsername())) {
        return seller;
      }
    }
    return null;
  }

  public String getSellerId() {
    return sellerId;
  }

  public ArrayList<String> getAppointmentIdList() {
    return appointmentIdList;
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

  public void addAppointment(String appointmentId) {
    appointmentIdList.add(appointmentId);
  }

  public void setAppointmentList(ArrayList<String> appointmentIdList) {
    this.appointmentIdList = appointmentIdList;
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

  public void setPriceRange(Integer[] priceRange) {
    this.priceRange = priceRange;
  }

  public boolean getIsListed() {
    return isListed;
  }

  // generate string to write on the file
  public String fileString() {
    String str = "";
    str += propertyId + Helper.writeFileSplitter;
    str += sellerId + Helper.writeFileSplitter;
    str += numberOfRooms + Helper.writeFileSplitter;
    str += floorSize + Helper.writeFileSplitter;
    str += (isListed ? "true" : "false") + Helper.writeFileSplitter;
    str += getAddress().getHouseNumber() + Helper.writeFileSplitter;
    str += getAddress().getStreet() + Helper.writeFileSplitter;
    str += getAddress().getCity() + Helper.writeFileSplitter;
    str += getAddress().getState() + Helper.writeFileSplitter;
    str += getAddress().getPostcode() + Helper.writeFileSplitter;
    str += "[" + priceRange[0] + "," + priceRange[1] + "]" + Helper.writeFileSplitter;
    str += "[";
    for (int i = 0; i < facilityList.size(); i++) {
      String facility = facilityList.get(i);
      str += facility;
      if (i != facilityList.size() - 1) {
        str += Helper.arraySplitter;
      }
    }
    str += "]" + Helper.writeFileSplitter;
    str += "[";
    for (int i = 0; i < appointmentIdList.size(); i++) {
      String appointmentId = appointmentIdList.get(i);
      str += appointmentId;
      if (i != appointmentIdList.size() - 1) {
        str += Helper.arraySplitter;
      }
    }
    str += "]" + "\n";
    return str;
  }

  // generate string to display to the console
  public String display() {
    String str = "";
    str += "Property ID : " + propertyId + "\n";
    str += "Address : " + address.toString() + "\n";
    str += "Number of Rooms : " + numberOfRooms + "\n";
    str += "Floor Size : " + floorSize + " sqft " + "\n";
    str += "Price Range : " + "RM " + priceRange[0] + " to " + "RM " + priceRange[1] + "\n";
    str += "Listed : " + (isListed ? "Yes" : "No") + "\n";
    str += "Facility : ";
    if (facilityList.size() == 0) {
      str += "None \n";
    } else {
      for (int i = 0; i < facilityList.size(); i++) {
        str += facilityList.get(i) + (i == facilityList.size() - 1 ? "\n" : " , ");
      }
    }
    str += "\nSeller Details\n";
    str += getSeller().toString() + "\n";
    return str;
  }

}
