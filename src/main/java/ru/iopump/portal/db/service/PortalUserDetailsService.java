package ru.iopump.portal.db.service;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.iopump.portal.db.dao.PortalUserRepository;
import ru.iopump.portal.db.entity.PortalUser;
import ru.iopump.portal.db.entity.PortalUserRole;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PortalUserDetailsService implements UserDetailsService {

    private final PortalUserRepository userRepository;

    @Autowired
    public PortalUserDetailsService(PortalUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private static Collection<? extends GrantedAuthority> getAuthorities(PortalUser user) {
        return user.getRoles().stream()
                .map(PortalUserRole::getName)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        final PortalUser user = userRepository.findOneByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return new PortalUserPrincipal(user);
    }

    @Data
    private static class PortalUserPrincipal implements UserDetails {

        private String password;
        private final String username;
        private final Set<? extends GrantedAuthority> authorities;
        private final boolean accountNonExpired;
        private final boolean accountNonLocked;
        private final boolean credentialsNonExpired;
        private final boolean enabled;

        private PortalUserPrincipal(PortalUser user) {
            this.password = user.getPassword();
            this.username = user.getUsername();
            this.authorities = getAuthorities(user);
            this.accountNonExpired = !user.getExpired();
            this.accountNonLocked = !user.getLocked();
            this.credentialsNonExpired = !user.getExpired();
            this.enabled = user.getEnabled();
        }

        private static Set<? extends GrantedAuthority> getAuthorities(PortalUser user) {
            return user.getRoles().stream()
                    .map(PortalUserRole::getName)
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toSet());
        }
    }
}