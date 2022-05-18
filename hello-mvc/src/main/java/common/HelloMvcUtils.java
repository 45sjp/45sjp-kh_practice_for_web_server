package common;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Base64.Encoder;

public class HelloMvcUtils {
	
//	public static void main(String[] args) {
//		System.out.println(encrypt("1234", "kkk123"));
//	}
	
	/**
	 * SHA256 | SHA512 (SHA1 또는 MD5는 사용하지 말 것)
	 * 
	 * @param password
	 * @return
	 */
	public static String encrypt(String password, String salt) {
		// 1. 암호화 Hashing
		MessageDigest md = null;
		byte[] encrypted = null;
		try {
			md = MessageDigest.getInstance("SHA-512");
			byte[] input = password.getBytes("utf-8");
			byte[] saltBytes = salt.getBytes("utf-8");
			md.update(saltBytes); // salt값으로 MessageDigest객체 갱신
			encrypted = md.digest(input); // MessageDigest객체에 raw password 전달 및 hashing
			System.out.println(new String(encrypted)); // ��!gs�'"S�@)>�䘽����Ê��U�y_�m�=$邾��$�{�E�V8��iWH�� // 다 깨짐
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		// 2. 인코딩 (단순문자변환)
		Encoder encoder = Base64.getEncoder();
		return encoder.encodeToString(encrypted);
	}
}
