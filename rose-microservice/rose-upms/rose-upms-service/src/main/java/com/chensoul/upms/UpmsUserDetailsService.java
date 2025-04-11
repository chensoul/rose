package com.chensoul.upms;

import com.chensoul.security.util.SecurityUser;
import com.chensoul.upms.entity.Credential;
import com.chensoul.upms.entity.User;
import com.chensoul.upms.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * TODO Comment
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since TODO
 */
@Service
@RequiredArgsConstructor
public class UpmsUserDetailsService implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findUserByPhone(username);
        if (user == null) {
            throw new AuthenticationServiceException("用户名不存在");
        }

        Credential credential = userService.findUserCredentialByUserId(user.getId());
        if (credential == null) {
            throw new AuthenticationServiceException("用户凭证不存在");
        }

        return new SecurityUser(
                user.getName(),
                credential.getPassword(),
                AuthorityUtils.createAuthorityList(user.getAuthority().name()));
    }
}
