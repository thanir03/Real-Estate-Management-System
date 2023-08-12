public class Credential {
  private String username;
  private String password;

  public Credential(String username, String password) {
    this.username = encryptString(username);
    this.password = encryptString(password);
  }

  public String getPassword() {
    return password;
  }

  public String getUsername() {
    return username;
  }

  private String encryptString(String credential) {
    // Encrypt String
    return "";
  }

  public static boolean validateUsername(String username) {
    // Check whether username exist in the database
    return false;
  }

  public static boolean validatePassword(String password) {
    // Check whether password satisfies minimum requirements
    return false;
  }

}
