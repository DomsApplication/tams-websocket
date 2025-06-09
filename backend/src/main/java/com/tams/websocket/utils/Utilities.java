package com.tams.websocket.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tams.websocket.command.commands.models.BaseRequestMessage;
import com.tams.websocket.command.commands.models.BaseResponseMessage;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
public class Utilities {

    public static final String COMMAND_PACKAGE = "com.shavika.websocket.common.commands.";
    public static final String YYY_MM_DD_T_HH_MM_SS_Z = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    public static final String YYY_MM_DD_T_HH_MM_SS_X = "yyyy-M-d-'T'H:m:s'Z'";

    public static final String YYY_MM_DD = "yyyy-M-d";

    public static final String HH_MM_SS = "H:m:s";

    private static final String SECRET_KEY = "Shav!ka_SKMC";

    public static String getSystemEv(String key) {
        return System.getenv(key);
    }

    public static XmlMapper XmlMapperInstance() {
        return new XmlMapper();
    }

    public static ObjectMapper ObjectMapperInstance() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }

    /**
     * @param xmlInput
     * @return
     */
    public static Document getXMLDocument(String xmlInput) {
        try {
            // Initialize DocumentBuilderFactory
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            // Parse the XML string into a Document
            Document document = builder.parse(new java.io.ByteArrayInputStream(xmlInput.getBytes(StandardCharsets.UTF_8)));
            // Normalize the XML structure
            document.getDocumentElement().normalize();
            return document;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param xmlString
     * @return
     */
    public static String getRequestRouteType(String xmlString, String elementName) {
        try {
            Document document = Utilities.getXMLDocument(xmlString);
            // Get the NodeList of elements by tag name "Request"
            NodeList nodeList = document.getElementsByTagName(elementName);
            // Check if the NodeList is not empty
            if (nodeList.getLength() > 0) {
                // Get the first element in the NodeList
                Element element = (Element) nodeList.item(0);
                // Get the text content of the element
                return element.getTextContent().trim();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T extends BaseRequestMessage> T convertToMessage(String xmlString, Class<T> type) {
        try {
            return XmlMapperInstance().readValue(xmlString, type);
        } catch (com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException ex) {
            log.error("Error in convertToMessage:::", ex.getMessage());
        } catch (IOException e) {
            log.error("Error in convertToMessage:::", e.getMessage());
        }
        return null;
    }

    public static <T extends BaseResponseMessage> T convertToResponseMessage(String xmlString, Class<T> type) {
        try {
            return XmlMapperInstance().readValue(xmlString, type);
        } catch (com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException ex) {
            log.error("Error in convertToMessage:::", ex.getMessage());
        } catch (IOException e) {
            log.error("Error in convertToMessage:::", e.getMessage());
        }
        return null;
    }

    public static <T> String convertToXmlString(T message) {
        try {
            String response = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
            return response + XmlMapperInstance().writeValueAsString(message);
        } catch (JsonProcessingException e) {
            return "<Message><Error>Failed to serialize response</Error></Message>";
        }
    }

    public static String generateUUID4() {
        return UUID.randomUUID().toString();
    }

    public static LocalDateTime convertStringTimestamp(String datetime, String format) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format, Locale.ENGLISH);
            LocalDateTime date = LocalDateTime.parse(datetime, formatter);
            return date;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static LocalDate convertString2Date(String str_date, String format) {
        try {
            LocalDateTime ldt = Utilities.convertStringTimestamp(str_date, format);
            System.out.println("LDT-date:::" + ldt);
            return ldt.toLocalDate();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static LocalTime convertString2Time(String str_date, String format) {
        try {
            LocalDateTime ldt = Utilities.convertStringTimestamp(str_date, format);
            System.out.println("LDT-time:::" + ldt);
            return ldt.toLocalTime();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String formatLocalDateTimeToUTCString(LocalDateTime dateTime, String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return dateTime.atOffset(ZoneOffset.UTC).format(formatter);
    }

    public static String getCurrentTimeStamp(String format, ZoneId zoneId) {
        LocalDateTime now = LocalDateTime.now(zoneId);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return now.format(formatter);
    }

    public static String getCurrentTimeStamp(String format) {
        return getCurrentTimeStamp(format, ZoneOffset.UTC);
    }

    public static LocalDateTime getCurrentDateTime() {
        return LocalDateTime.now();
    }

    public static long getCurrentTimestampInMills() {
        return System.currentTimeMillis();
    }

    public static LocalDateTime getDateTimeFromMills(long millsec) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(millsec), ZoneId.systemDefault());
    }

    public static <T> String object2JsonString(T message) {
        try {
            return ObjectMapperInstance().writeValueAsString(message);
        } catch (JsonProcessingException e) {
            return "{ \"error\": " + e.getMessage() + "}";
        }
    }

    /**
     * Method to convert CamelCase to snake_case
     * For camelToSnake("GetUserData"), the output will be "get_user_data".
     */
    public static String camelToSnake(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        StringBuilder result = new StringBuilder();

        // Iterate through each character in the input string
        for (int i = 0; i < input.length(); i++) {
            char currentChar = input.charAt(i);

            // Check if the current character is uppercase
            if (Character.isUpperCase(currentChar)) {
                // If it's the first character, add it in lowercase
                if (i > 0) {
                    result.append('_'); // Add underscore before the uppercase letter
                }
                result.append(Character.toLowerCase(currentChar));
            } else {
                result.append(currentChar);
            }
        }

        return result.toString();
    }

    /**
     * Method to convert Snake_case to CamelCase
     * For snakeToCamel("get_user_data"), the output will be "getUserData".
     */
    public static String snakeToCamel(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        StringBuilder result = new StringBuilder();
        boolean nextUpperCase = false;

        // Iterate through each character in the input string
        for (int i = 0; i < input.length(); i++) {
            char currentChar = input.charAt(i);

            if (currentChar == '_') {
                nextUpperCase = true; // Set flag to capitalize the next character
            } else {
                if (nextUpperCase) {
                    result.append(Character.toUpperCase(currentChar));
                    nextUpperCase = false;
                } else {
                    result.append(Character.toLowerCase(currentChar));
                }
            }
        }

        return result.toString();
    }

    /**
     * capitalizeFirstLetter("setUserData") -> Output: SetUserData
     *
     * @param input
     * @return
     */
    public static String capitalizeFirstLetter(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        // Capitalize the first letter and leave the rest of the string unchanged
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }

    public static String lowercaseFirstLetter(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        // Lowercase the first letter and leave the rest of the string unchanged
        return input.substring(0, 1).toLowerCase() + input.substring(1);
    }

    /**
     * Convert "Yes" or "No" strings to Boolean values.
     *
     * @param value The input string ("Yes" or "No").
     * @return true if value is "Yes", false if value is "No".
     * @throws IllegalArgumentException if the input value is invalid.
     */
    public static boolean convertToBoolean(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid value for Yes/No conversion: " + value);
        }
        String trimmedValue = value.trim().toLowerCase();
        if (trimmedValue.equals("yes")) {
            return true;
        } else if (trimmedValue.equals("no")) {
            return false;
        } else {
            throw new IllegalArgumentException("Invalid value for Yes/No conversion: " + value);
        }
    }

    public static String encodeName(String normalName) {
        try {
            // Convert the normal string to UTF-16 LE bytes
            byte[] utf16Bytes = normalName.getBytes(StandardCharsets.UTF_16LE);
            // Encode the bytes to Base64
            return Base64.getEncoder().encodeToString(utf16Bytes);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to encode Name: " + e.getMessage(), e);
        }
    }

    public static String decodeName(String encodedName) {
        try {
            // Decode from Base64 to bytes
            byte[] decodedBytes = Base64.getDecoder().decode(encodedName);
            // Convert the bytes to a UTF-16 LE string
            return new String(decodedBytes, StandardCharsets.UTF_16LE);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to decode Name: " + e.getMessage(), e);
        }
    }

    public static String decodeUserPeriod(int periodValue) {
        // Extract year, month, and day from the integer
        int year = ((periodValue >> 16) & 0xFF) + 2000; // Year is encoded as (Year-2000) << 16
        int month = (periodValue >> 8) & 0xFF;          // Month is encoded as (Month << 8)
        int day = periodValue & 0xFF;                   // Day is the lower 8 bits

        // Format the date as a string in the format YYYY-MM-DD
        return String.format("%04d-%02d-%02d", year, month, day);
    }

    public static Map<String, Map<String, Boolean>> decodeFingers(int fingersValue) {
        // Prepare the result map
        Map<String, Map<String, Boolean>> fingerInfo = new HashMap<>();

        // Check the bits for each finger (assuming max 5 fingers)
        for (int n = 0; n < 5; n++) {
            int enrolledBit = (fingersValue >> (2 * n)) & 1;
            int duressBit = (fingersValue >> (2 * n + 1)) & 1;

            Map<String, Boolean> fingerDetails = new HashMap<>();
            fingerDetails.put("enrolled", enrolledBit == 1);
            fingerDetails.put("duress", duressBit == 1);

            fingerInfo.put(String.valueOf(n + 1), fingerDetails);
        }

        return fingerInfo;
    }

    public static byte[] convertBase64Binary(String fingerData) {
        // Decode the base64-encoded data into binary
        return Base64.getDecoder().decode(fingerData);
    }

    public static LocalDateTime convertToDateTime(String dateStr) {
        if (!dateStr.contains("T")) {
            dateStr = dateStr + "-T00:00:00Z";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(YYY_MM_DD_T_HH_MM_SS_X);
        return LocalDateTime.parse(dateStr, formatter);
    }

    public static String decodeBase64String(String base64EncodedString) {
        try {
            if (null == base64EncodedString || base64EncodedString.isEmpty()) {
                return base64EncodedString;
            }
            // Decode the Base64 string to bytes
            byte[] decodedBytes = Base64.getDecoder().decode(base64EncodedString);
            // Convert the bytes to a normal string (assumes UTF-8 encoding)
            return new String(decodedBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to decode Base64 string: " + e.getMessage(), e);
        }
    }

    public static String encodeBase64String(String normalString) {
        try {
            if (null == normalString || normalString.isEmpty()) {
                return normalString;
            }
            // Convert the string to bytes (UTF-8 encoding)
            byte[] utf8Bytes = normalString.getBytes(StandardCharsets.UTF_8);
            // Encode the bytes to a Base64 string
            return Base64.getEncoder().encodeToString(utf8Bytes);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to encode string to Base64: " + e.getMessage(), e);
        }
    }

    public static String convertToCustomFormat(Object dt, String format) {
        if (dt instanceof LocalDateTime) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
            return ((LocalDateTime) dt).format(formatter);
        } else if (dt instanceof String) {
            return (String) dt;
        } else {
            log.error("Expected LocalDateTime or String, got " + dt.getClass());
            throw new IllegalArgumentException("Invalid type for datetime conversion");
        }
    }

    public static String byteArrayToBase64(byte[] byteArray) {
        if (byteArray == null) {
            throw new IllegalArgumentException("Input byte array cannot be null");
        }
        return Base64.getEncoder().encodeToString(byteArray);
    }

    private static String adjustKeyLength(String key) {
        int AES_KEY_LENGTH = 32;
        if (key.length() < AES_KEY_LENGTH) {
            return String.format("%1$-" + AES_KEY_LENGTH + "s", key).replace(' ', '*'); // Pad with '*'
        } else {
            return key.substring(0, AES_KEY_LENGTH); // Truncate to required length
        }
    }

    public static String encryptPassword(String password) throws  Exception {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(Utilities.adjustKeyLength(SECRET_KEY).getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            byte[] encrypted = cipher.doFinal(password.getBytes());
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            throw new RuntimeException("Error encrypting password", e);
        }
    }

    public static String decryptPassword(String encryptedPassword) throws  Exception {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(Utilities.adjustKeyLength(SECRET_KEY).getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            byte[] decoded = Base64.getDecoder().decode(encryptedPassword);
            return new String(cipher.doFinal(decoded));
        } catch (Exception e) {
            throw new RuntimeException("Error decrypting password", e);
        }
    }


    public static Claims parseToken(String token) throws Exception {
        return Jwts
                .parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public static Map<String, Object> extractAllClaims(String token) throws  Exception {
        Claims claims = Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        Map<String, Object> claimsMap = new HashMap<>();
        for (Map.Entry<String, Object> entry : claims.entrySet()) {
            claimsMap.put(entry.getKey(), entry.getValue());
        }
        claimsMap.put("expiration", claims.getExpiration());
        claimsMap.put("userId", claims.get("userId", String.class));
        return claimsMap;
    }

    private static SecretKey getSignInKey() throws  Exception  {
        //byte[] bytes = Base64.getDecoder().decode(Utilities.adjustKeyLength(SECRET_KEY).getBytes(StandardCharsets.UTF_8));
        byte[] bytes = adjustKeyLength(SECRET_KEY).getBytes(StandardCharsets.UTF_8);
        return new SecretKeySpec(bytes, "HmacSHA256");
    }

    public static boolean validateToken(String token, String existingUserId) throws Exception {
        Map<String, Object> claims = extractAllClaims(token);
        String userId = (String) claims.get("userId");
        Date expiration = (Date) claims.get("expiration");

        if (userId == null) {
            throw new RuntimeException("UserId is not found.");
        }

        if (userId.equals(existingUserId)) {
            throw new RuntimeException("UserId is invalid.");
        }

        if (expiration == null) {
            throw new RuntimeException("Token is invalid.");
        }

        if (expiration.before(new Date())) {
            throw new RuntimeException("Token is expired.");
        }
        return true;
    }

    public static void main(String[] args) throws Exception {
        String str_date = "2024-11-3-T11:41:50Z";
        /*String format = "yyyy-M-d-'T'H:m:s'Z'";
        LocalDateTime ldt = Utilities.convertStringTimestamp(str_date, format);
        System.out.println("LDT:::" + ldt);
        LocalDate date = ldt.toLocalDate();
        System.out.println("date:::" + date);
        LocalTime time = ldt.toLocalTime();
        System.out.println("time:::" + time);
        */

        LocalDateTime dateTme = Utilities.convertStringTimestamp(str_date, Utilities.YYY_MM_DD_T_HH_MM_SS_X);
        String date = Utilities.formatLocalDateTimeToUTCString(dateTme, "d/M/yyyy");
        String time = Utilities.formatLocalDateTimeToUTCString(dateTme, Utilities.HH_MM_SS);
        System.out.println("date:::" + date);
        System.out.println("time:::" + time);
    }

}