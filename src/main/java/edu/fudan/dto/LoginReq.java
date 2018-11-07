package edu.fudan.dto;

import edu.fudan.config.Constants;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class LoginReq {

    @Email(message = "invalid email.")
    @NotBlank
    private String email;

    @Pattern(regexp = Constants.PASSWORD_REGEX, message = "invalid password.")
    @NotBlank
    private String password;

    public LoginReq() {
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
