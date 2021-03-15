package com.etz.fraudeagleeyemanager.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.*;
import java.util.Objects;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class AccountProductKey implements Serializable{

    @Column(name = "code")
    private String code;

    @Column(name = "account_id")
    private Long account_id;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AccountProductKey)) return false;
        AccountProductKey that = (AccountProductKey) o;
        return code.equals(that.code) && account_id.equals(that.account_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, account_id);
    }
}
