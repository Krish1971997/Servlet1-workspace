package bCrypt;

import org.mindrot.jbcrypt.BCrypt;

public class BCryptExample {

    public static String hashPassword(String plainPassword) {
        String hashed = BCrypt.hashpw(plainPassword, BCrypt.gensalt(12));
        return hashed;
    }
    
    public static boolean verifyPassword(String inputPassword, String storedHash) {
        return BCrypt.checkpw(inputPassword, storedHash);
    }


    public static void main(String[] args) {
        String password = "1234567";
        String hash = hashPassword(password);
        System.out.println("Stored Hash: " + hash);
        
        if (verifyPassword(password, hash)) {
            System.out.println("Login Success");
        } else {
            System.out.println("Invalid Password");
        }
    }
}
