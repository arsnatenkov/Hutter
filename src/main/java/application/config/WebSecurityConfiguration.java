package application.config;

import application.service.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Класс конфигурация для SBS
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private MyUserDetailsService userDetailsService;

    /**
     * Метод для аутентификации пользователя
     * @param auth "Составление пользователя по БД"
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(bCryptPasswordEncoder);
    }

    /**
     * Метод для ограничения доступа
     * @param http запрос
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        String loginPage = "/login";
        String logoutPage = "/logout";

        http.
                authorizeRequests()
                .antMatchers("/", "/offer", "/create", "/conversation", "/error", "/search", "/page").permitAll()
                .antMatchers(loginPage).permitAll()
                .antMatchers("/registration").permitAll()
                .antMatchers("/visitor/account").hasAnyAuthority("VISITOR", "ADMIN")
                .antMatchers("/conversation").hasAuthority("VISITOR")
                .antMatchers("/create").hasAuthority("VISITOR")
                .antMatchers("/admin").hasAuthority("ADMIN")
                .antMatchers("/banUser").hasAuthority("ADMIN")
                .antMatchers("/unbanUser").hasAuthority("ADMIN")
                .antMatchers("/deleteOffer").hasAuthority("ADMIN")
                .antMatchers("/deleteMessage").hasAuthority("ADMIN")
                .anyRequest()
                .authenticated()
                .and().csrf().disable()
                .formLogin()
                .loginPage(loginPage)
                .loginPage("/login")
                .failureUrl("/login?error=true")
                .defaultSuccessUrl("/visitor/account")
                .usernameParameter("user_name")
                .passwordParameter("password")
                .and().logout()
                .logoutRequestMatcher(new AntPathRequestMatcher(logoutPage))
                .logoutSuccessUrl(loginPage).and().exceptionHandling();
    }

    /**
     * Метод для игнорирования доступа к определнным Web ресурсам
     * @param web web ресурсы
     */
    @Override
    public void configure(WebSecurity web) {
        web
                .ignoring()
                .antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**");
    }

}