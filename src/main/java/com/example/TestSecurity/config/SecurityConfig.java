package com.example.TestSecurity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception{

        // 경로에 대한 인가 설정
        httpSecurity
                .authorizeHttpRequests(auth -> auth // 위에서부터 url에 대해서 접근권한이 적용되고, 이후 앞서 접근권한이 적용된 url에 대해서는 재부여되는 접근권한이 무시되므로 순서가 중요함
                        .requestMatchers("/", "/login", "/join", "/joinProc").permitAll() // 모든 권한에 대해서 해당 url 접근 허용
                        .requestMatchers("/admin").hasRole("ADMIN") // "ADMIN" roll을 가진 사용자에 한해서 해당 url 접근 허용
                        .requestMatchers("/my/**").hasAnyRole("ADMIN", "USER") // "ADMIN"이나 "USER" 중 하나 이상 권한을 가진 사용자라면 해당 url에 접근 허용
                        .anyRequest().authenticated() // 어떤요청에 대해서라도 로그인된 사용자라면 해당 url 접근 허용
                );


        // 로그인에 대한 설정
        // spring security에서 내부적인 알고리즘으로 로그인 처리를 진행하는 방식이 있는데, 아래 메서드를 통해서 진행된다.
        // loginPage 인자로 전달된 url로(로그인 페이지) 요청이 들어왔을 때부터 로그인 처리 알고리즘이 시작되고,
        // 로그인 요청("/loginProc")이 들어오는 부분은 온전히 spring Security가 맡아서 처리한다.
        httpSecurity
                .formLogin((auth) -> auth.loginPage("/login")
                        .loginProcessingUrl("/loginProc")
                        .permitAll()
                );

        // 토큰 사용에 대한 설정
        // 로그인 요청을 할 때 사이트 위변조 방지를 위해 csrf 토큰을 같이 보내야 하는데, 아래 설정은 해당 옵션을 꺼둔다.
        httpSecurity.csrf(auth -> {
            auth.disable();
        });

        httpSecurity.sessionManagement(auth -> {
            auth
                    .maximumSessions(1) // 하나의 아이디에 대한 다중 로그인 허용 개수
                    .maxSessionsPreventsLogin(true); // 다중 로그인 개수를 초과하였을 경우 처리 방법 / true: 초과시 새로운 로그인 차단 / false : 초과시 기존 세션 하나 삭제
        });

        // 세션 고정 공격을 보호하기 위한 로그인 성공시 세션 설정 방법은 sessionManagement() 메소드의 sessionFixation() 메소드를 통해서 설정할 수 있다.
        httpSecurity.sessionManagement(auth -> {
            // 세션 토큰 아이디와 내부 세션을 기존과 동일하게 유지함
            //auth.sessionFixation().none();
            // 세션 쿠키와 내부 세션을 로그인 진행 시 새로 생성, 로그인 되지 않은 시점의 anonymous 세션에서 login 세션으로 새로 생성됨
            //auth.sessionFixation().newSession();
            // 세션은 동일하지만 세션 쿠키의 id 값을 변경하고 로그인 유저에게도 해당 세션 쿠키 id 값으 새로 발급하면서 해커가 기존 가지고있던 세션 쿠키 id로 세션에 간섭하는 것을 방지함
            auth.sessionFixation().changeSessionId();
        });

        return httpSecurity.build();
    }



}
