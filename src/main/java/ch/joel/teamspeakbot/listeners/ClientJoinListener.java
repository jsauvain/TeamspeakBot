package ch.joel.teamspeakbot.listeners;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.event.ClientJoinEvent;
import com.github.theholywaffle.teamspeak3.api.event.TS3EventAdapter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ClientJoinListener extends TS3EventAdapter {

	private final TS3Api api;

	@Override
	public void onClientJoin(ClientJoinEvent e) {
		api.pokeClient(e.getClientId(), "Willkomme uf jooel.ch, " + e.getClientNickname());
	}
}
