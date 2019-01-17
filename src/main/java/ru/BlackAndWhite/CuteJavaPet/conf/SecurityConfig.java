package ru.BlackAndWhite.CuteJavaPet.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.BlackAndWhite.CuteJavaPet.services.servicesImpl.UserDetailsServiceImpl;

//import org.springframework.security.authentication.encoding.ShaPasswordEncoder;

//
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Bean
    @Override
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                .antMatchers(new String[]{"/img/**", "/css/**", "/fonts/**", "/js/**"})
                .permitAll()
                .antMatchers(new String[]{"/", "/registration"})
                .permitAll()
                .antMatchers("/admin", "/icons", "/uploadIcons")
                .access("hasRole('ROLE_ADMIN')")
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/download")
                .failureUrl("/login?error=true")
                .permitAll()
                .and()
                .logout()
                .logoutSuccessUrl("/login?logout=true")
                .invalidateHttpSession(true)
                .permitAll()
                .and()
                .csrf()
                .disable();


        //                .antMatchers("/protected/**").access("hasRole('ROLE_ADMIN')")
//                .antMatchers("/confidential/**").access("hasRole('ROLE_SUPERADMIN')")
//                .and().formLogin().defaultSuccessUrl("/", true);
    }


/*     регистрируем нашу реализацию UserDetailsService
     а также PasswordEncoder для приведения пароля в формат SHA1
*/
    @Autowired
    public void registerGlobalAuthentication(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
