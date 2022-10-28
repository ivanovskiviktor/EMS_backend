package graduatethesis.performancemonitoringsystem.security;


import graduatethesis.performancemonitoringsystem.service.interfaces.UserService;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;




    @Configuration
    @EnableWebSecurity
    @EnableGlobalMethodSecurity(prePostEnabled = true)
    public class WebSecurity extends WebSecurityConfigurerAdapter {

        private final BCryptPasswordEncoder bCryptPasswordEncoder;
        private final UserService userService;
        private final JWTUtils jwtUtils;
        private final UserDetailsService userDetailsService;

        public WebSecurity(BCryptPasswordEncoder bCryptPasswordEncoder, UserService userService, JWTUtils jwtUtils, UserDetailsService userDetailsService) {
            this.bCryptPasswordEncoder = bCryptPasswordEncoder;
            this.userService = userService;
            this.jwtUtils = jwtUtils;
            this.userDetailsService = userDetailsService;
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.cors()
                    .and().csrf().disable()
                    .authorizeRequests().antMatchers("/rest/lxogin","/rest/user/signup").permitAll()
                    .antMatchers("/rest/**").authenticated()
                    .and()
                    .addFilter(new JWTAuthorizationFilter(authenticationManager(), userService))
                    .addFilter(new JWTAuthenticationFilter(authenticationManager(), userService, jwtUtils));


        }

        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
            return authenticationConfiguration.getAuthenticationManager();
        }


        @Bean
        CorsConfigurationSource corsConfigurationSource() {
            final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
            CorsConfiguration config = new CorsConfiguration().applyPermitDefaultValues();
            config.addAllowedOrigin("*");
            config.addAllowedHeader("*");

            config.addExposedHeader("WWW-Authenticate");
            config.addExposedHeader("Access-Control-Allow-Origin");
            config.addExposedHeader("Access-Control-Allow-Headers");

            config.addAllowedMethod("OPTIONS");
            config.addAllowedMethod("HEAD");
            config.addAllowedMethod("GET");
            config.addAllowedMethod("PUT");
            config.addAllowedMethod("POST");
            config.addAllowedMethod("DELETE");
            config.addAllowedMethod("PATCH");
            source.registerCorsConfiguration("/**", config);
            return source;
        }


        @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
        @Override
        public AuthenticationManager authenticationManagerBean() throws Exception {
            return super.authenticationManagerBean();
        }

    }



