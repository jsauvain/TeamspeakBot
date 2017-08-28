package ch.joel.teamspeakbot;

import ch.joel.teamspeakbot.config.ForbiddenNamesConfig;
import ch.joel.teamspeakbot.config.ServerAuthConfig;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TSBOTConfiguration {

	private ServerAuthConfig server;
	private ForbiddenNamesConfig forbiddenNames;
	private String nickname;
	private int afkChannel;
	private String botJoinedServerMessage;
	private String welcomeUserMessage;
	private String afkMovedMessage;

}
