package com.mycompany.irr00_group_project.model;

/**
 * Class for users of the app. Users login with a username and password.
 * If multiple users are not implemented yet, use a default user, like
 *      Username: TestUser
 *      Password: UserTest.
 * 
 * @author Priyanshu Guha
 */
public class User {
    private String username;
    private String password;
    
    /**
     * Initialize a new user.
     * 
     * @param username is the username.
     * @param password is the password of the user.
     */
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
    
    // =======
    // Getters
    // =======
    
    public String getUsername() {
        return username;
    }
    
    public String getPassword() {
        return password;
    }
        
    // =======
    // Setters
    // =======
    
    public void setUsername(String newUsername) {
        this.username = newUsername;
    }
    
    public void setPassword(String newPassword) {
        password = newPassword;
    }
    
    // Methods to implement:
    //      void authenticate(inputUsername, inputPassword)
    //          (get user from database, check if password is correct)
}