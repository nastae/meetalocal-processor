package lt.govilnius.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    public SecurityConfig() {
        super();
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http
                .formLogin()
                .loginPage("/auth/login")
                .failureUrl("/auth/login-error")
                .and()
                .logout()
                .logoutUrl("/appLogout")
                .logoutSuccessUrl("/auth/login")
                .and()
                .authorizeRequests()
                .antMatchers("/meet-management", "/meet-management/**").hasRole("ADMIN")
                .antMatchers("/engagement-management", "/engagement-management/**").hasRole("ADMIN")
                .antMatchers("/volunteer-management", "/volunteer-management/**").hasRole("ADMIN")
                .antMatchers("/home", "/").hasRole("ADMIN")
                .and()
                .exceptionHandling();
    }

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth
                .inMemoryAuthentication()
                .withUser("admin").password("{noop}VilniusGo2020").roles("ADMIN");
    }
}
