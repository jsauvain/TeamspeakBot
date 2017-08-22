package ch.joel.teamspeakbot.listeners;

import com.github.theholywaffle.teamspeak3.TS3ApiAsync;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

@RequiredArgsConstructor
public class AfkListener implements Runnable {

	private Map<Integer, Integer> lastVisitedChannelBeforeAfk = new HashMap<>();
	private final TS3ApiAsync apiAsync;
	private final int afkChannel;

	@Override
	public void run() {
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				apiAsync.getClients().onSuccess(clients -> {
					for (Client client : clients) {
						int clientId = client.getId();
						if (client.isInputMuted() && client.isOutputMuted() && client.getChannelId() != afkChannel) {
							lastVisitedChannelBeforeAfk.put(clientId, client.getChannelId());
							apiAsync.moveClient(clientId, afkChannel);
						} else if (lastVisitedChannelBeforeAfk.containsKey(clientId)) {
							apiAsync.moveClient(clientId, lastVisitedChannelBeforeAfk.remove(clientId));
						}
					}
				});
			}
		}, 1000);
	}
}
