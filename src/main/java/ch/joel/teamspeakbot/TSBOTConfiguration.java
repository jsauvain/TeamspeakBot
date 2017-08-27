package ch.joel.teamspeakbot;

import ch.joel.teamspeakbot.config.ServerAuthConfig;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class TSBOTConfiguration {

	private ServerAuthConfig server;
	private String nickname;
	private int afkChannel;
	private String botJoinedServerMessage;
	private String welcomeUserMessage;
	private String afkMovedMessage;
	private List<String> forbiddenNames;
	private String forbiddenNamesRegex;
	private String forbiddenNameKickMessage;

}
