package com.fullstack.starter.util;

import com.fullstack.starter.entity.User;
import com.fullstack.starter.exception.BusinessException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TokenUtilsTest {

    private final TokenUtils tokenUtils = new TokenUtils(
            "unit-test-token-secret-with-at-least-32-characters",
            60
    );

    @Test
    void generatesAndParsesSignedToken() {
        User user = new User();
        user.setId(7L);
        user.setUsername("tester");
        user.setRole("ADMIN");
        long expiresAt = tokenUtils.expiresAtFromNow();

        TokenSubject subject = tokenUtils.parse(tokenUtils.generate(user, expiresAt));

        assertEquals(7L, subject.getUserId());
        assertEquals("tester", subject.getUsername());
        assertEquals("ADMIN", subject.getRole());
        assertEquals(expiresAt, subject.getExpiresAt());
    }

    @Test
    void rejectsExpiredOrTamperedToken() {
        User user = new User();
        user.setId(9L);
        user.setUsername("tester");
        user.setRole("USER");

        String expired = tokenUtils.generate(user, System.currentTimeMillis() - 1);
        BusinessException expiredError = assertThrows(BusinessException.class, () -> tokenUtils.parse(expired));
        assertEquals(401, expiredError.getCode());

        String valid = tokenUtils.generate(user, tokenUtils.expiresAtFromNow());
        BusinessException tamperedError = assertThrows(
                BusinessException.class,
                () -> tokenUtils.parse(valid.substring(0, valid.length() - 1) + "x")
        );
        assertEquals(401, tamperedError.getCode());
    }
}
