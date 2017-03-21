package leader.security;

import leader.controller.SystemInfo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component("mysecurityRule")
public class MySecurityChecker {
	public boolean check(Authentication authentication, HttpServletRequest request) {

		UserDetails userDetail = SystemInfo.currentUserDetails();
		System.out.println("MySecurityChecker for url " + request.getRequestURL() + " cur user " + userDetail);
		return userDetail != null && userDetail.getUsername().equals("leader");

	}
}
