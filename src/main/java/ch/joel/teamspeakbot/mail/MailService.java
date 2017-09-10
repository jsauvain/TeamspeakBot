package ch.joel.teamspeakbot.mail;

import org.simplejavamail.email.Email;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.Mailer;
import org.simplejavamail.mailer.config.TransportStrategy;

public class MailService {

	private Mailer mailer;

	public MailService(String host, int port, String username, String password, TransportStrategy strategy) {
		this.mailer = new Mailer(host, port, username, password, strategy);
	}

	public void sendMail(Mail mail, EmailAddress from, EmailAddress to) {
		Email email = new EmailBuilder()
				.from(from.getName(), from.getMail())
				.to(to.getName(), to.getMail())
				.subject(mail.getSubject())
				.text(mail.getText())
				.textHTML(mail.getHtml())
				.build();
		mailer.sendMail(email);
	}

}
