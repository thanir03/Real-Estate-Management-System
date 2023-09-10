import java.time.LocalDateTime;
import java.util.ArrayList;

public class Seller extends User {
  private ArrayList<String> propertyListId;

  public Seller(String fullName, String emailAddress, String phoneNum, LocalDateTime dob, Credential credential) {
    super(fullName, emailAddress, phoneNum, dob, credential);
  }

  public void setPropertyList(ArrayList<String> propertyList) {
    this.propertyListId = propertyList;
  }

  public ArrayList<Property> getPropertyList() {
    ArrayList<Property> properties = PropertyDatabase.read();
    ArrayList<Property> sellerProperties = new ArrayList<>();
    for (Property property : properties) {
      if (property.getSellerId().equals(this.getCredential().getUsername())) {
        sellerProperties.add(property);
      }
    }
    return sellerProperties;
  }

  public void deleteProperty(String deletedPropertyId) {
    for (int i = 0; i < propertyListId.size(); i++) {
      if (propertyListId.get(i).equals(deletedPropertyId)) {
        System.out.println(propertyListId.get(i));
        propertyListId.remove(i);
        break;
      }
    }
  }


  public ArrayList<String> getPropertyListId() {
    return propertyListId;
  }

  public boolean addPropertyList(String propertyId) {
    propertyListId.add(propertyId);
    // update the database
    return true;
  }

  public String fileString() {
    String str = "";
    str += this.getCredential().getUsername() + Helper.writeFileSplitter;
    str += this.getCredential().getPassword() + Helper.writeFileSplitter;
    str += this.getFullName() + Helper.writeFileSplitter;
    str += this.getEmailAddress() + Helper.writeFileSplitter;
    str += this.getDOB().format(Helper.dateFormat) + Helper.writeFileSplitter;
    str += this.getPhoneNum() + Helper.writeFileSplitter;
    str += "[";
    for (int i = 0; i < propertyListId.size(); i++) {
      str += propertyListId.get(i) + (i == propertyListId.size() - 1 ? "]" : Helper.arraySplitter);
    }
    if (propertyListId.size() == 0)
      str += "]";
    str += "\n";
    return str;
  }

  @Override
  public String toString() {
    String str = "Name : " + getFullName() + " , Email Address : " + getEmailAddress() + " , Phone Number : "
        + getPhoneNum();
    return str;
  }

}
