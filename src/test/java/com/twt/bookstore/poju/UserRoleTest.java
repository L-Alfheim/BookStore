package com.twt.bookstore.poju;

import org.junit.jupiter.api.Test;

public class UserRoleTest {
    @Test
    void testRoleFromString() {

        String admin = "admin";
        String user = "user";

        UserRole userRole = UserRole.roleFromString(user);
        UserRole adminRole = UserRole.roleFromString(admin);

        System.out.println(userRole.toString() + adminRole.toString());
    }
}
