package ru.BlackAndWhite.CuteJavaPet.services.servicesImpl;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.BlackAndWhite.CuteJavaPet.services.SecurityService;


@Log4j
@Service
public class SecurityServiceImpl implements SecurityService {

//    private static final Logger logger = LoggerFactory.getLogger(SecurityServiceImpl.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;


    @Override
    public String getCurrentLoggedUsername() {
        try {
            Object userDetails = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (userDetails instanceof org.springframework.security.core.userdetails.User) {
                return ((org.springframework.security.core.userdetails.User) userDetails).getUsername();
            }
        } catch (NullPointerException e)
        {
            log.error(e);
            return null;
        }
        return null;
    }

    @Override
    public void autoLogin(String username, String password) throws Exception {
        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());

            Authentication auth = authenticationManager.authenticate(authenticationToken);
            if (auth.isAuthenticated()) {
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                log.info(String.format("Successfully %s auto logged in", username));
            }
        } catch (Exception e) {
            log.error(e);
        }
    }
}
