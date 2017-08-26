package ch.joel.teamspeakbot.listeners;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3ApiAsync;
import com.github.theholywaffle.teamspeak3.api.event.TS3EventAdapter;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class WroteMeListener extends TS3EventAdapter {

	private final TS3Api api;
	private final TS3ApiAsync apiAsync;

	@Override
	public void onTextMessage(TextMessageEvent e) {
		if (e.getInvokerId() != api.whoAmI().getId()) {
			switch (e.getMessage()) {
				case "!moveall":
					if (api.getServerGroupsByClient(api.getClientInfo(e.getInvokerId())).stream().anyMatch(serverGroup -> serverGroup.getName().equals("Admin"))) {
						int channelId = api.getClientInfo(e.getInvokerId()).getChannelId();
						for (Client client : api.getClients()) {
							if (client.getChannelId() != channelId)
								apiAsync.moveClient(client.getId(), channelId);
						}
					}
					break;
				case "!kickall":
					if (api.getServerGroupsByClient(api.getClientInfo(e.getInvokerId())).stream().anyMatch(serverGroup -> serverGroup.getName().equals("Admin"))) {
						List<Client> clients = api.getClients();
						clients.removeIf(cl -> cl.getId() == api.whoAmI().getId() || cl.getId() == e.getInvokerId());
						if (!clients.isEmpty())
							apiAsync.kickClientFromServer(clients.toArray(new Client[clients.size()]));
					}
			}
		}
	}
}
