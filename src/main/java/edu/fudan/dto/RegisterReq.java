package edu.fudan.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import static edu.fudan.config.Constants.PASSWORD_REGEX;

public class RegisterReq {

    @Email(message = "invalid email.")
    @NotBlank
    private String email;

    @NotBlank(message = "name can't be empty")
    private String name;

    @Pattern(regexp = PASSWORD_REGEX, message = "invalid password.")
    @NotBlank
    private String password;

    public RegisterReq() {
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }
}
