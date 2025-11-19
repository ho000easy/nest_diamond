package com.nest.diamond.web.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true) // 替代原来的 @EnableGlobalMethodSecurity
public class WebSecurityConfig {

    @Value("${nest_diamond.user}")
    private String user;

    @Value("${nest_diamond.password}")
    private String password;

    // 和你原来保持一致的“访客可访问”资源
    private static final String[] GUEST_RESOURCES = new String[]{
            "/airdrop", "/airdrop/*",
            "/airdropItem", "/airdropItem/*",
            "/seed/all", "/reconItemDetail", "/reconItemDetail/*",
            "/exchangeWithdrawTicketV2",
            "/exchangeWithdrawTicket/*",
            "/exchangeWithdrawTicket/*/*",
            "/reconConfig/*",
            "/manual", "/manual/*",
            "/globalVariable?name=grafanaTimelineSingle",
            "/operationInstance", "/operationInstance/*",
            "/addressAnalyze","/addressAnalyze/*",
            "/airdropOperationManual","/airdropOperationManual/*",
            "/subOperationInstance","/subOperationInstance/*",
            "/account/findIpProxyIds",
            "/ipProxy", "/ipProxy/*",
            "/polyMarket","/polyMarket/*",
            "/manualAds","/manualAds/*","/multi_otp"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 忽略 /public/** 的 CSRF
                .csrf(csrf -> csrf.ignoringRequestMatchers("/public/**"))
                .authorizeHttpRequests(auth -> auth
                        // 白名单
                        .requestMatchers("/public/**").permitAll()
                        // 静态资源需要 ADMIN/USER
                        .requestMatchers("/css/**", "/js/**", "/image/**").hasAnyRole("ADMIN", "USER")
                        // 访客资源：ADMIN/USER 均可访问
                        .requestMatchers(GUEST_RESOURCES).hasAnyRole("ADMIN", "USER")
                        // 其它路径默认需要 ADMIN
                        .requestMatchers("/**").hasRole("ADMIN")
                        // 兜底
                        .anyRequest().authenticated()
                )
                // 表单登录 + HTTP Basic
                .formLogin(Customizer.withDefaults())
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        // 和你原 configure(AuthenticationManagerBuilder) 的用户定义一致
        UserDetails admin = User.withUsername(user)
                .password(passwordEncoder.encode(password))
                .roles("ADMIN")
                .build();


        return new InMemoryUserDetailsManager(admin);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
