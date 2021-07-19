package com.nicobrest.kamehouse.commons.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;

/**
 * EncryptionUtils tests.
 *
 * @author nbrest
 */
public class EncryptionUtilsTest {

  private static final String TEST_FILES_PATH = "src/test/resources/commons/";
  private static final String SAMPLE_CERT = TEST_FILES_PATH + "keys/sample.crt";
  private static final String SAMPLE_KEYSTORE = TEST_FILES_PATH + "keys/sample.pkcs12";
  private static final String SAMPLE_DECRYPTED_FILE = TEST_FILES_PATH + "files/input.txt";
  private static final String SAMPLE_ENCRYPTED_FILE = TEST_FILES_PATH + "files/input.enc";
  private static final String SAMPLE_ENCRYPTED_EMPTY_FILE = TEST_FILES_PATH
      + "files/input-empty.enc";

  /**
   * Use this test to encrypt a file using kamehouse keys.
   * Create a ${HOME}/input-decrypted-kamehouse.txt with the content to encrypt
   * and it will be encrypted to ${HOME}/output-encrypted-kamehouse.enc
   *
   */
  @Test
  public void createEncryptedKameHouseFileTest() {
    boolean success = true;
    String inputFileName = PropertiesUtils.getUserHome() + "/input-decrypted-kamehouse.txt";
    String outputFileName = PropertiesUtils.getUserHome() + "/output-encrypted-kamehouse.enc";
    try {
      File inputFile = new File(inputFileName);
      File outputFile = new File(outputFileName);
      byte[] inputBytes = FileUtils.readFileToByteArray(inputFile);
      X509Certificate cert = EncryptionUtils.getKameHouseCertificate();
      System.out.println("Encrypting: '" + new String(inputBytes) + "' into output file.");
      byte[] encryptedBytes = EncryptionUtils.encrypt(inputBytes, cert);
      FileUtils.writeByteArrayToFile(outputFile, encryptedBytes);
    } catch (IOException e) {
      System.out.println("Could not encrypt " + inputFileName);
    }
    assertTrue(success);
  }

  /**
   * Test encrypt and decrypt strings.
   */
  @Test
  public void encryptAndDecryptStringsTest() {
    String inputString = "mada mada dane echizen kun";

    X509Certificate cert = EncryptionUtils.getCertificate(SAMPLE_CERT);
    byte[] encryptedData = EncryptionUtils.encrypt(inputString.getBytes(), cert);

    PrivateKey privateKey = EncryptionUtils.getPrivateKey(SAMPLE_KEYSTORE, "PKCS12",
        null, "1", null);
    byte[] outputRawData = EncryptionUtils.decrypt(encryptedData, privateKey);
    String decryptedString = new String(outputRawData);

    assertNotEquals(inputString, new String(encryptedData));
    assertEquals(inputString, decryptedString);
  }

  /**
   * Test encrypt a decrypted file.
   */
  @Test
  public void encryptDecryptedFileTest() throws IOException {
    byte[] inputBytes = FileUtils.readFileToByteArray(new File(SAMPLE_DECRYPTED_FILE));
    String inputString = new String(inputBytes);

    X509Certificate cert = EncryptionUtils.getCertificate(SAMPLE_CERT);
    byte[] encryptedData = EncryptionUtils.encrypt(inputBytes, cert);
    // Write to encrypted file:
    // FileUtils.writeByteArrayToFile(new File(SAMPLE_ENCRYPTED_FILE), encryptedData);

    PrivateKey privateKey = EncryptionUtils.getPrivateKey(SAMPLE_KEYSTORE, "PKCS12",
        null, "1", null);
    byte[] outputRawData = EncryptionUtils.decrypt(encryptedData, privateKey);
    String decryptedString = new String(outputRawData);

    assertNotEquals(inputString, new String(encryptedData));
    assertEquals(inputString, decryptedString);
  }

  /**
   * Test decrypt an encrypted file.
   */
  @Test
  public void decryptEncryptedFileTest() throws IOException {
    String expectedDecrypted = "mada mada dane - pegasus seiya";
    byte[] inputBytes = FileUtils.readFileToByteArray(new File(SAMPLE_ENCRYPTED_FILE));
    String inputString = new String(inputBytes);

    PrivateKey privateKey = EncryptionUtils.getPrivateKey(SAMPLE_KEYSTORE, "PKCS12",
        null, "1", null);
    byte[] decryptedBytes = EncryptionUtils.decrypt(inputBytes, privateKey);
    String decryptedString = new String(decryptedBytes);

    assertNotEquals(inputString, decryptedString);
    assertEquals(expectedDecrypted, decryptedString);
  }

  /**
   * Test decrypt an encrypted empty file.
   */
  @Test
  public void decryptEncryptedEmptyFileTest() throws IOException {
    String expectedDecrypted = "";
    byte[] inputBytes = FileUtils.readFileToByteArray(new File(SAMPLE_ENCRYPTED_EMPTY_FILE));
    String inputString = new String(inputBytes);

    PrivateKey privateKey = EncryptionUtils.getPrivateKey(SAMPLE_KEYSTORE, "PKCS12",
        null, "1", null);
    byte[] decryptedBytes = EncryptionUtils.decrypt(inputBytes, privateKey);
    String decryptedString = new String(decryptedBytes);

    assertNotEquals(inputString, decryptedString);
    assertEquals(expectedDecrypted, decryptedString);
  }
}
