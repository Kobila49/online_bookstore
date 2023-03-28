package com.example.online_bookstore.configuration;

import com.example.online_bookstore.enums.RoleType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class UserDetailServiceConfig {

    @Value("${bookstore.adminUser}")
    private String adminUser;
    @Value("${bookstore.adminPassword}")
    private String adminPassword;
    @Value("${bookstore.regularUser}")
    private String regularUser;
    @Value("${bookstore.regularPassword}")
    private String regularPassword;

    @Bean
    public UserDetailsService userDetailsService(BCryptPasswordEncoder bCryptPasswordEncoder) {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User.withUsername(regularUser)
                .password(bCryptPasswordEncoder.encode(regularPassword))
                .roles(RoleType.USER.name())
                .build());
        manager.createUser(User.withUsername(adminUser)
                .password(bCryptPasswordEncoder.encode(adminPassword))
                .roles(RoleType.ADMIN.name(), RoleType.USER.name())
                .build());
        return manager;
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
