package main.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static main.entity.RolePermission.MODERATE;
import static main.entity.RolePermission.WRITE;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;

    @Autowired
    public SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers( "/api/post/moderation", "/api/moderation").hasAuthority(MODERATE.name())
                .antMatchers(HttpMethod.PUT , "/api/settings").hasAuthority(MODERATE.name())
                .antMatchers("/api/auth/logout", "/api/post/*", "/api/profile/my",
                        "/api/comment", "/api/image", "/api/statistics/my").hasAnyAuthority(WRITE.name(), MODERATE.name())
                .antMatchers("/**", "/api/auth/*").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin().disable()
                .httpBasic();
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    protected DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

}



//  Требуется
//          moderator
//          PUT  /api/settings
//          GET  /api/post/moderation
//          POST /api/moderation

//          All
//          POST /api/profile/my
//          POST /api/comment
//          POST /api/image
//          GET  /api/statistics/my
//          GET /api/auth/logout
//          POST /api/post/dislike
//          POST /api/post/like
//          PUT  /api/post/{ID}
//          POST /api/post
//          GET  /api/post/my




//  Не требуется
//          GET  /api/statistics/all

//          GET  /api/post/{ID}
//          GET  /api/post/byTag
//          GET  /api/post/byDate
//          GET  /api/post
//          GET  /api/post/search

//          GET  /api/tag
//          GET  /api/calendar
//          GET  /api/settings
//          GET  /api/init

//          POST /api/auth/restore
//          POST /api/auth/password
//          GET  /api/auth/check
//          POST /api/auth/login
//          POST /api/auth/register
//          GET  /api/auth/captcha