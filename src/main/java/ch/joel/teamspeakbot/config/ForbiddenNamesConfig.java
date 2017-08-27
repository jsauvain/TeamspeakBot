package ch.joel.teamspeakbot.config;

import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class ForbiddenNamesConfig {

	private List<String> forbiddenNames;
	private String forbiddenNamesRegex;
	private String forbiddenNameKickMessage;

}
