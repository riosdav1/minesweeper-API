package com.deviget.minesweeper.payload.request;

import javax.validation.constraints.NotBlank;

/**
 * Payload class used for user login requests.
 * 
 * @author david.rios
 */
public class LoginRequest {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
