package ch.joel.teamspeakbot.listeners;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3ApiAsync;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

@RequiredArgsConstructor
public class AfkListener implements Runnable {

	private final TS3ApiAsync apiAsync;
	private final TS3Api api;
	private final Map<Integer, Integer> lastVisitedChannelBeforeAfk = new HashMap<>();

	private int afkChannelId;

	@Override
	public void run() {
		afkChannelId = api.getChannelByNameExact("Afk Channel", true).getId();
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				apiAsync.getClients().onSuccess(clients -> {
					for (Client client : clients) {
						int clientId = client.getId();
						if (client.isInputMuted() && client.isOutputMuted()) {
							lastVisitedChannelBeforeAfk.put(clientId, client.getChannelId());
							apiAsync.moveClient(clientId, afkChannelId);
						} else if (lastVisitedChannelBeforeAfk.containsKey(clientId)) {
							apiAsync.moveClient(clientId, lastVisitedChannelBeforeAfk.remove(clientId));
						}
					}
				});
			}
		}, 1000, 1000);
	}

}
