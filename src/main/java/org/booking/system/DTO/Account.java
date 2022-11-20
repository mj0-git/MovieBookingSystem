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
    private String address;
    @Column(name="EMAIL", nullable=false)
    private String email;
    private String phoneNumber;
    private long[] favorites;
}