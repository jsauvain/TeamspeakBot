package ch.joel.teamspeakbot;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TSBOTConfiguration {

	private String host;
	private String username;
	private String password;
	private String nickname;

	private int afkChannel;


}
