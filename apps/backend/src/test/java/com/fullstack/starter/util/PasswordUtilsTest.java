package com.fullstack.starter.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PasswordUtilsTest {

    @Test
    void hashesConfiguredDefaultPasswords() {
        assertEquals("0192023a7bbd73250516f069df18b500", PasswordUtils.md5("admin123"));
        assertEquals("e10adc3949ba59abbe56e057f20f883e", PasswordUtils.md5("123456"));
    }

    @Test
    void keepsExistingMd5AndNormalizesCase() {
        assertEquals(
                "0192023a7bbd73250516f069df18b500",
                PasswordUtils.md5IfNeeded("0192023A7BBD73250516F069DF18B500")
        );
    }
}
