package org.hdxy.bangong.config.security;

import org.hdxy.bangong.pojo.Admin;
import org.hdxy.bangong.service.IAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private IAdminService iAdminService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Admin admin = iAdminService.getAdminByUserName(username);
        if (admin != null) {
            return new User(
                    admin.getUsername(),
                    null,
                    AuthorityUtils.commaSeparatedStringToAuthorityList(""));
        }

        return null;
    }

}
