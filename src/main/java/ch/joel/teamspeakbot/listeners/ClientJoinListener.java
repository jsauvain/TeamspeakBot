package ch.joel.teamspeakbot.listeners;

import ch.joel.teamspeakbot.TSBOTConfiguration;
import com.github.theholywaffle.teamspeak3.TS3ApiAsync;
import com.github.theholywaffle.teamspeak3.api.event.ClientJoinEvent;
import com.github.theholywaffle.teamspeak3.api.event.TS3EventAdapter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ClientJoinListener extends TS3EventAdapter {

	private final TS3ApiAsync apiAsync;
	private final TSBOTConfiguration configuration;

	@Override
	public void onClientJoin(ClientJoinEvent e) {
		apiAsync.pokeClient(e.getClientId(), configuration.getWelcomeUserMessage().replace("%p", e.getClientNickname()));
	}
}
