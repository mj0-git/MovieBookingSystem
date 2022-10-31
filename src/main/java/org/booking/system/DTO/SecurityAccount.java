package org.booking.system.DTO;

import javax.persistence.Entity;
import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="SECURITYACCOUNT")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

// Class
public class SecurityAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    @Column(name="USERNAME", nullable=false)
    private String userName;
    @Column(name="PASSWORD", nullable=false)
    private String password;
    private String token;
}