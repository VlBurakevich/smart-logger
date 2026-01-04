package com.solution.authenticationservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OauthAccountId that = (OauthAccountId) o;
        return (Objects.equals(provider, that.provider))
                && (Objects.equals(providerUserId, that.providerUserId));
    }

    @Override
    public int hashCode() {
        return Objects.hash(provider, providerUserId);
    }

    @Override
    public String toString() {
        return "OauthAccountId {" +
                "provider=" + provider +
                "providerUserId=" + providerUserId +
                "}";
    }
}
