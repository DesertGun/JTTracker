package ee.desertgun.jttracker.config;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class UserProfileGravatarHash {
  public static String hex(byte[] array) {
    StringBuffer sb = new StringBuffer();
    for (byte b : array) {
      sb.append(Integer.toHexString((b
        & 0xFF) | 0x100).substring(1, 3));
    }
    return sb.toString();
  }

  public static String md5Hex(String message) {
    try {
      MessageDigest md =
        MessageDigest.getInstance("MD5");
      return hex(md.digest(message.getBytes("CP1252")));
    } catch (NoSuchAlgorithmException | UnsupportedEncodingException ignored) {
    }
    return null;
  }
}
