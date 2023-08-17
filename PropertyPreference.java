import java.util.ArrayList;

public class PropertyPreference {
  private int numberOfRooms;
  private String city;
  private ArrayList<String> facilityList;

  public String getCity() {
    return city;
  }

  public ArrayList<String> getFacilityList() {
    return facilityList;
  }

  public void setFacilityList(ArrayList<String> facilityList) {
    this.facilityList = facilityList;
  }

  public int getNumberOfRooms() {
    return numberOfRooms;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public void setNumberOfRooms(int numberOfRooms) {
    this.numberOfRooms = numberOfRooms;
  }

}
