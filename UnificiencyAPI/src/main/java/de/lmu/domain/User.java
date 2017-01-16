package de.lmu.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.Email;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by robertMueller on 03.01.17.
 */

@Entity
@Data
@AllArgsConstructor // remmove this when working with a database
public class User {

    User(){}

    @Id
    @GeneratedValue
    private long id;
    private String nickname;

    private String email;
    private String major;

    @JsonIgnore
    private String password;

    @Override
    public String toString(){
        return "Nickname: " + nickname + ", E-Mail: " + email + ", Major: " + major;
    }
}
