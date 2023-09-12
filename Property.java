import java.util.ArrayList;

// Property class

public class Property {
  private final String propertyId;
  private final String sellerId;
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
    for (Seller seller : Main.sellerList) {
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

  public void setAddress(Address address) {
    this.address = address;
  }

  public void addAppointmentId(String appointmentId) {
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
    StringBuilder str = new StringBuilder();
    str.append(propertyId).append(Helper.writeFileSplitter);
    str.append(sellerId).append(Helper.writeFileSplitter);
    str.append(numberOfRooms).append(Helper.writeFileSplitter);
    str.append(floorSize).append(Helper.writeFileSplitter);
    str.append(isListed ? "true" : "false").append(Helper.writeFileSplitter);
    str.append(getAddress().getHouseNumber()).append(Helper.writeFileSplitter);
    str.append(getAddress().getStreet()).append(Helper.writeFileSplitter);
    str.append(getAddress().getCity()).append(Helper.writeFileSplitter);
    str.append(getAddress().getState()).append(Helper.writeFileSplitter);
    str.append(getAddress().getPostcode()).append(Helper.writeFileSplitter);
    str.append("[").append(priceRange[0]).append(",").append(priceRange[1]).append("]").append(Helper.writeFileSplitter);
    str.append("[");
    for (int i = 0; i < facilityList.size(); i++) {
      String facility = facilityList.get(i);
      str.append(facility);
      if (i != facilityList.size() - 1) {
        str.append(Helper.arraySplitter);
      }
    }
    str.append("]").append(Helper.writeFileSplitter);
    str.append("[");
    for (int i = 0; i < appointmentIdList.size(); i++) {
      String appointmentId = appointmentIdList.get(i);
      str.append(appointmentId);
      if (i != appointmentIdList.size() - 1) {
        str.append(Helper.arraySplitter);
      }
    }
    str.append("]" + "\n");
    return str.toString();
  }

  // generate string to display to the console
  @Override
  public String toString() {
    StringBuilder str = new StringBuilder();
    str.append("Property ID : ").append(propertyId).append("\n");
    str.append("Address : ").append(address.toString()).append("\n");
    str.append("Number of Rooms : ").append(numberOfRooms).append("\n");
    str.append("Floor Size : ").append(floorSize).append(" square feet ").append("\n");
    str.append("Price Range : " + "RM ").append(priceRange[0]).append(" to ").append("RM ").append(priceRange[1]).append("\n");
    str.append("Listed : ").append(isListed ? "Yes" : "No").append("\n");
    str.append("Facility : ");
    if (facilityList.size() == 0) {
      str.append("None \n");
    } else {
      for (int i = 0; i < facilityList.size(); i++) {
        str.append(facilityList.get(i)).append(i == facilityList.size() - 1 ? "\n" : " , ");
      }
    }
    str.append("\nSeller Details\n");
    str.append(getSeller().toString()).append("\n");
    return str.toString();
  }

}
