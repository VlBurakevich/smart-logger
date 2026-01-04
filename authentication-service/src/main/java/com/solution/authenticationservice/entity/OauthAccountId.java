package com.solution.authenticationservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OauthAccountId implements Serializable {

    @Column(name = "provider", length = 50)
    private String provider;

    @Column(name = "provider_user_id")
    private String providerUserId;
}
