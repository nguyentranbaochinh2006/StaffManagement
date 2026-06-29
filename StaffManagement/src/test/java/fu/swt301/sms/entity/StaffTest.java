/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fu.swt301.sms.entity;

/**
 *
 * @author Lenovo
 */

import java.math.BigDecimal;
import java.sql.Date;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StaffTest {

    @Test
    public void testGetterSetter() {

        Staff s = new Staff();

        Role role = new Role();
        role.setRoleID(1);
        role.setRoleName("Admin");

        s.setStaffID(10);
        s.setStaffCode("NV001");
        s.setFullName("Nguyen Van A");
        s.setGender(true);
        s.setPhoneNumber("0123456789");
        s.setEmail("a@gmail.com");
        s.setPassword("123");
        s.setDepartment("IT");
        s.setPosition("Developer");
        s.setSalary(new BigDecimal("15000000"));
        s.setHireDate(Date.valueOf("2025-01-01"));
        s.setDateOfBirth(Date.valueOf("2000-01-01"));
        s.setRole(role);
        s.setIsActive(true);

        assertEquals(10,s.getStaffID());
        assertEquals("NV001",s.getStaffCode());
        assertEquals("Nguyen Van A",s.getFullName());
        assertTrue(s.isGender());
        assertEquals("0123456789",s.getPhoneNumber());
        assertEquals("a@gmail.com",s.getEmail());
        assertEquals("123",s.getPassword());
        assertEquals("IT",s.getDepartment());
        assertEquals("Developer",s.getPosition());
        assertEquals(new BigDecimal("15000000"),s.getSalary());
        assertEquals(Date.valueOf("2025-01-01"),s.getHireDate());
        assertEquals(Date.valueOf("2000-01-01"),s.getDateOfBirth());
        assertEquals(1,s.getRole().getRoleID());
        assertTrue(s.isIsActive());

    }

}
