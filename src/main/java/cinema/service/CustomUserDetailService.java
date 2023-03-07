package cinema.service;

import static org.springframework.security.core.userdetails.User.withUsername;

import cinema.model.User;
import java.util.Optional;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailService implements UserDetailsService {
    private final UserService userService;

    public CustomUserDetailService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> userOptional = userService.findByEmail(email);
        User user = userOptional.orElseThrow(() ->
                new UsernameNotFoundException("Can't find user by email " + email));
        UserBuilder builder;
        builder = withUsername(email);
        builder.password(user.getPassword());
        builder.roles(user.getRoles()
                    .stream()
                    .map(role -> role.getName().name())
                    .toArray(String[]::new));
        return builder.build();
    }
}
