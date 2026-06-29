/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fu.swt301.sms.entity;

/**
 *
 * @author Lenovo
 */

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RoleTest {

    @Test
    public void testGetterSetter() {

        Role role = new Role();

        role.setRoleID(1);
        role.setRoleName("Admin");

        assertEquals(1, role.getRoleID());
        assertEquals("Admin", role.getRoleName());
    }

}
