package shop.mtcoding.securityapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // 1. CSRF 해제
        http.csrf().disable(); // postman 접근해야함 !! - CSR 할때 !!

        // 2. Form 로그인 설정
        http.formLogin()
                .loginPage("/loginForm")
                .usernameParameter("username")
                .passwordParameter("password")
                .loginProcessingUrl("/login") // POST + X-WWW-Form-urlEncoded
                // .defaultSuccessUrl("/") // --> 어디로 갈지 모를때 기억해놓는다 거기로 가게 해준다 ssesion 에 기억
                // 해놔야한다.
                .successHandler((eq, resp, authentication) -> {
                    System.out.println("디버그 : 로그인이 완료되었습니다"); // -->무조건 디버그를 써라
                    resp.sendRedirect("/");
                })
                .failureHandler((req, resp, ex) -> {
                    System.out.println("디버그 : 로그인 실패 -> " + ex.getMessage());
                });

        // 3. 인증,권한 필터 설정
        http.authorizeRequests(
                (authorize) -> authorize.antMatchers("/users/**").authenticated()
                        .antMatchers("/manager/**") // 인증+권한
                        .access("hasRole('ADMIN') or hasRole('MANAGER')")
                        .antMatchers("/admin/**").hasRole("admin") // 권한을 2개를준다
                        .anyRequest().permitAll());

        // 전부열고 하나만 닫는다 - 퍼시티브 이걸 추천한다.
        // 다막고 열어줄거만 여는건 -네가티브
        return http.build();
    }
}
