package com.chensoul.rose.security.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

@Setter
@Getter
public class SecurityUser extends User {

    private static final long serialVersionUID = -797397440703066079L;

    private List<String> tenants;

    public SecurityUser(
            String username,
            String password,
            List<String> tenants,
            boolean enabled,
            boolean accountNonExpired,
            boolean credentialsNonExpired,
            boolean accountNonLocked,
            Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.tenants = tenants;
    }

    public SecurityUser(
            String username,
            String password,
            Collection<? extends GrantedAuthority> authorities,
            List<String> tenants) {
        super(username, password, authorities);
        this.tenants = tenants;
    }

    public SecurityUser(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        this(username, password, authorities, new ArrayList<>());
    }
}
