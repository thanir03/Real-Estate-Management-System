import java.util.ArrayList;
import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.Base64.Decoder;

// Credential class used by both User

public class Credential {
  private String username;
  private String password;

  public Credential(String username, String password) {
    this.username = username;
    this.password = password;
  }

  public String getPassword() {
    return password;
  }

  public String getUsername() {
    return username;
  }

  public static String encryptString(String string) {
    Encoder encoder = Base64.getEncoder();
    String encryptedString = encoder.encodeToString(string.getBytes());
    return encryptedString;
  }

  public static String decryptString(String encryptedString) {
    Decoder decoder = Base64.getDecoder();
    byte[] stringBytes = decoder.decode(encryptedString.getBytes());
    return new String(stringBytes);
  }

  public static User isValidCredentials(String username, String password, boolean isBuyer) {
    ArrayList<User> userList = new ArrayList<>();
    if (isBuyer) {
      userList.addAll(Main.buyerList);
    } else {
      userList.addAll(Main.sellerList);
    }
    String encryptedPassword = encryptString(password);
    for (User user : userList) {
      Credential userCredential = user.getCredential();
      if (userCredential.getUsername().equals(username)
          && userCredential.getPassword().equals(encryptedPassword)) {
        return user;
      }
    }
    return null;
  }

  public static boolean validateUsername(String username, boolean isBuyer) {
    ArrayList<User> userList = new ArrayList<>();
    if (isBuyer) {
      userList.addAll(Main.buyerList);
    } else {
      userList.addAll(Main.sellerList);
    }
    if (username.length() <= 3)
      return false;
    for (User user : userList) {
      if (user.getCredential().getUsername().equals(username))
        return false;
    }
    return true;
  }

  public static boolean validatePassword(String password) {
    return password.length() < 4;
  }

}
