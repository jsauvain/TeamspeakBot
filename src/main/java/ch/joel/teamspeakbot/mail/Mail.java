package ch.joel.teamspeakbot.mail;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Mail {

	private String subject;
	private String text;
	private String html;

}
