package balotenketo.balotaro.auth

import com.stormpath.spring.config.StormpathWebSecurityConfigurer.stormpath
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter

@Configuration
@EnableWebSecurity
open class SpringSecurityWebAppConfig : WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity) {
        http.apply(stormpath()).and().authorizeRequests().antMatchers(
                "/",
                "/version",
                "/register",
                "/vote/**"
        ).permitAll()
    }
}