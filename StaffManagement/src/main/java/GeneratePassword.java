/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Lenovo
 */
import org.mindrot.jbcrypt.BCrypt;

public class GeneratePassword {

    public static void main(String[] args) {

        String password = "123456";

        String hash = BCrypt.hashpw(password, BCrypt.gensalt());

        System.out.println(hash);

    }

}
