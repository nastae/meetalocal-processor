package lt.govilnius.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    public SecurityConfig() {
        super();
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
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
                .withUser("mandarinas").password("{noop}B1f1Jogurtas").roles("ADMIN");
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("*"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
