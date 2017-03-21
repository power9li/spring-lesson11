package leader.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;

//用于@PreAuthorize的生效,基于方法的权限控制
@EnableGlobalMethodSecurity(prePostEnabled=true)
@Configuration
@EnableWebSecurity
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class JDBCSecurityConfig2 extends WebSecurityConfigurerAdapter {

	@Autowired
	private DataSource datasource;

//	@Autowired
//	private AuthFailureHandler authFailureHandler;
//
//	@Autowired
//	private AuthSuccessHandler authSuccessHandler;

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http
//			.csrf().disable()
				//任何访问都必须授权
				.authorizeRequests()
//			    .anyRequest()
//				.fullyAuthenticated()
				.antMatchers("/admin/**").hasAnyRole("admin", "users")
				.antMatchers("/manager/**").hasRole("manager")
				.antMatchers("/manager/createuser").hasAuthority("op_createuser")
				.and()
//					.formLogin().loginPage("/spring_login.html"
			.formLogin()
				//登陆成功后的处理，因为是API的形式所以不用跳转页面
				.successHandler(new AuthSuccessHandler())
				//登陆失败后的处理
				.failureHandler(new AuthFailureHandler())
				.loginPage("/mylogin")
//				.defaultSuccessUrl("/welcome") //坑爹,加了defaultSuccessUrl,上面设置的successHandler失效了.
//				.loginPage("/login")
//				.failureUrl("/login?error")
				.permitAll()
				.and()
//			.logout()
//				.logoutUrl("/registry")
//				.logoutSuccessUrl("/mylogin")
//				.permitAll().and()
			.rememberMe()
				.tokenRepository(tokenRepository())
				.tokenValiditySeconds(300)
				.and()
			.exceptionHandling()
				.authenticationEntryPoint(new RestLoginUrlAuthenticationEntryPoint("/mylogin"))
				.accessDeniedHandler(accessDeniedHandler())
				.and()
			.sessionManagement()
				.sessionFixation()//.migrateSession()
				.changeSessionId()
				.and()
			.headers()
//				.defaultsDisabled()
//					.contentTypeOptions()
//					.and()
				.httpStrictTransportSecurity()
					.maxAgeInSeconds(31536000)
					.includeSubDomains(true)
					.and()
				.xssProtection()//跨站脚本攻击
					.block(false)
					.and()
				.frameOptions()//允许同域名frame
					.sameOrigin()
					.and()
		;

	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/js/**", "/css/**", "**/images/**", "**/**/favicon.ico", "/fonts/**","/**.js");
	}

	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(jdbcUserDetailsManager());
	}

	public UserDetailsManager jdbcUserDetailsManager() throws Exception {
		JdbcUserDetailsManager userMan = new JdbcUserDetailsManager();
		userMan.setDataSource(datasource);
		userMan.setRolePrefix("ROLE_");
		userMan.setEnableGroups(true);
		return userMan;
	}

	@Bean
	public JdbcTokenRepositoryImpl tokenRepository(){
		JdbcTokenRepositoryImpl tokenRepository=new JdbcTokenRepositoryImpl();
		tokenRepository.setDataSource(datasource);
		return tokenRepository;
	}



	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Bean
	public AccessDeniedHandler accessDeniedHandler(){
		AccessDeniedHandlerImpl handler = new AccessDeniedHandlerImpl();
		handler.setErrorPage("/accessDenied");
		return handler;
	}


}