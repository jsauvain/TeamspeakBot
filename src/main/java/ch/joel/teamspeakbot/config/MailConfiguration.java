package ch.joel.teamspeakbot.config;

import lombok.Getter;
import lombok.Setter;
import org.simplejavamail.mailer.config.TransportStrategy;

@Getter
@Setter
public class MailConfiguration {

	private String host;
	private int port;
	private String username;
	private String password;
	private TransportStrategy strategy;

	private String adminMail;

}
