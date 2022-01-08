package com.dparadig.auth_server.settings.configuration;
import com.dparadig.auth_server.settings.security.oauth2.CustomUser;
import com.dparadig.auth_server.settings.security.oauth2.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import javax.servlet.ServletContext;
@Configuration
@EnableWebSecurity
@ComponentScan(basePackageClasses = CustomUser.class)
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {
    @Autowired
    ServletContext servletContext;
    @Autowired
    private CustomUserDetailsService userDetailsService;
    @Autowired
    JsonToUrlEncodedAuthenticationFilter jsonFilter;
    @Autowired
    public void globalUserDetails(final AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
    @Bean(name = "passwordEncoder")
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Override
    protected void configure(final HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .addFilterBefore(jsonFilter, ChannelProcessingFilter.class)
                // we don't need CSRF because our token is invulnerable
                //.csrf().disable()
                // don't create session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                //.antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .antMatchers("/status","/swagger-resources/**","/swagger-ui.html", "/v2/api-docs", "/webjars/**"
                        ,"/swagger-resources" ,"/configuration/ui","/configuration/**","/configuration/security","/swagger-ui/**","/swagger-ui","/csrf","/").permitAll()
                .anyRequest().authenticated().and().formLogin(Customizer.withDefaults()).httpBasic(Customizer.withDefaults());
        httpSecurity.headers().contentTypeOptions();
        httpSecurity.headers().xssProtection();
        httpSecurity.headers().httpStrictTransportSecurity();
        httpSecurity.headers().frameOptions();
        httpSecurity.headers().cacheControl();
    }
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("dparadig")
                .password(passwordEncoder().encode("ddp4r4d1g"))
                .authorities("ADMIN");
    }
}