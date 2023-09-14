package com.example.application_team3;

import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    UserAccountControl obj = new UserAccountControl();

    @Test
    public void testValidEmail() {
        assertTrue(obj.isValidEmail("User@example.com"));
        assertTrue(obj.isValidEmail("user123@student.kau.se"));
    }

    @Test
    public void testInvalidEmail() {
        assertFalse(obj.isValidEmail(""));
        assertFalse(obj.isValidEmail("invalid-email"));
        assertFalse(obj.isValidEmail("user@.com"));
        assertFalse(obj.isValidEmail("@example.com"));
        assertFalse(obj.isValidEmail("Måns@student.kau.se"));

    }

    @Test
    public void testIsInValidPassword() {
        assertFalse(obj.isValidPassword(""));
        assertFalse(obj.isValidPassword("a"));
        assertFalse(obj.isValidPassword("12345678"));
        assertFalse(obj.isValidPassword("12345678a"));
        assertFalse(obj.isValidPassword("abcdefg"));
    }

    @Test
    public void testIsValidPassword() {
        assertTrue(obj.isValidPassword("Abcdefg1"));
        assertTrue(obj.isValidPassword("Äöååöä22"));
        assertTrue(obj.isValidPassword("123456Aa"));
    }

    @Test
    public void testValidUsername() {
        assertTrue(obj.isValidUsername("My_name123"));
        assertTrue(obj.isValidUsername("user-123"));
    }

    @Test
    public void testInvalidUsername() {
        assertFalse(obj.isValidUsername(""));
        assertFalse(obj.isValidUsername("user@name"));
        assertFalse(obj.isValidUsername("aa"));
        assertFalse(obj.isValidUsername("username with spaces"));
    }

    @Test
    public void testValidName() {
        assertTrue(obj.isValidName("John Doe"));
        assertTrue(obj.isValidName("Förnamn Efternamn"));
        assertTrue(obj.isValidName("John"));
    }

    @Test
    public void testInvalidName() {
        assertFalse(obj.isValidName(""));
        assertFalse(obj.isValidName("123"));
        assertFalse(obj.isValidName("Doe2"));

        assertFalse(obj.isValidName("John@Doe"));
    }
}