package tacos.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

//import org.springframework.security.core.userdetails.User;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import tacos.data.UserRepository;

import tacos.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Configuration
public class SecurityConfig {
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

//  public interface UserDetailsService {
//    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
//  }

//  // 方式1 使用内存使用
//  @Bean
//  public UserDetailsService userDetailsService(PasswordEncoder encoder) {
//    List<UserDetails> userList = new ArrayList<>();
//    userList.add(new User(
//        "buzz", encoder.encode("password"),
//        Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"))));
//    userList.add(new User(
//        "woody", encoder.encode("password"),
//        Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"))));
//    return new InMemoryUserDetailsManager(userList);
//  }

  @Bean
  public UserDetailsService userDetailsService(UserRepository userRepo) {
    return username -> {
      User user = userRepo.findByUsername(username);

      if (user != null) {
        return user;
      }

      throw new UsernameNotFoundException(
          "User '" + username + "' not found");
    };
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    return http
      .authorizeRequests()
        .antMatchers("/design", "/orders").hasRole("USER")
        .antMatchers("/", "/**").permitAll()
      .and()
        .formLogin()
        .loginPage("/login")

      .and()
        .logout()
        .logoutSuccessUrl("/")

      // Make H2-Console no secured; for debug purposes
      .and()
        .csrf()
          .ignoringAntMatchers("/h2-console/**")
      // Allow pages to be loaded in frames from the same origin; needed for H2-Console
      .and()
        .headers()
          .frameOptions()
            .sameOrigin()

      .and()
      .build();
  }

//  等价写法
//  @Bean
//  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//    return http
//      .authorizeRequests()
//        .antMatchers("/design", "/orders").access("hasRole('USER')")
//        .antMatchers("/", "/**").access("permitAll()")
//        .and()
//        .build();
//  }.

//  有权限的周二才可创建订单
//  @Bean
//  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//    return http
//        .authorizeRequests()
//        .antMatchers("/design", "/design")
//        .access("hasRole('USER') && " +
//            "T(java.util.Calendar).getInstance().get(" +
//            "T(java.util.Calendar).DAY_OF_WEEK) == " +
//            "T(java.util.Calendar).TUESDAY")
//        .antMatchers("/", "/**").access("permitAll()")
//        .and()
//        .build();
//  }

}
