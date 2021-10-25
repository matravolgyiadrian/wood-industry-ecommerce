package org.thesis.woodindustryecommerce.model.binding;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.thesis.woodindustryecommerce.model.User;
import org.thesis.woodindustryecommerce.util.validation.FieldMatch;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldMatch(fields = {"password", "confirmPassword"}, message = "The password fields must match")
public class RegistrationForm {
    @NotNull(message = "Cannot be empty, should be at least 3 characters")
    @NotEmpty(message = "Cannot be empty, should be at least 3 characters")
    @Size(min = 3, max = 20, message = "Username should be between 3 and 20 characters")
    private String username;

    @NotNull
    @Size(min = 3, message = "Cannot be empty, should be at least 3 characters")
    private String password;

    private String confirmPassword;

    private String email;

    @NotNull
    @Size(min = 5, message = "Cannot be empty, should be at least 5 characters")
    private String address;

    public User toUser() {
        return User.builder()
                .username(username)
                .password(password)
                .email(email)
                .address(address).build();
    }
}
