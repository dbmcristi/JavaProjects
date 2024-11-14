package myfirstosm;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

public class BasicAuthenticator extends Authenticator {
	
	private final PasswordAuthentication passwordAuth;
	
	public BasicAuthenticator(String userName, String password) {
		this.passwordAuth = new PasswordAuthentication(userName, password.toCharArray());
	}
	
	public BasicAuthenticator(PasswordAuthentication passwordAuth) {
		this.passwordAuth = passwordAuth;
	}

	@Override
	protected PasswordAuthentication getPasswordAuthentication() {
		return passwordAuth;
	}
	
}