package org.booking.system.DTO;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.ElementCollection;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="ACCOUNT")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

// Class
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    @Column(name="USERNAME", nullable=false)
    private String userName;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name="PASSWORD", nullable=false)
    private String password;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name="ROLE", nullable=false)
    private String role;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String token;
    private String firstName;
    private String lastName;
    private String address;
    @Column(name="EMAIL", nullable=false)
    private String email;
    private String phoneNumber;
    @ElementCollection
    private List<String> favorites;
}