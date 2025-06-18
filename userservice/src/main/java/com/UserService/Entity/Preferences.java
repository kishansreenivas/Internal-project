package com.UserService.Entity;

import java.util.UUID;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "preferences")
public class Preferences {

    @Id
    @GeneratedValue
    private UUID id;
    private boolean newsletterSubscribed;
    private String language;
    private String region;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
