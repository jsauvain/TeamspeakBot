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
		apiAsync.whoAmI().onSuccess(serverQueryInfo -> {
			if (e.getInvokerId() != serverQueryInfo.getId()) {
				if (!e.getMessage().startsWith("!"))
					return;
				String command = e.getMessage().split(" ")[0].substring(1);
				String[] args = e.getMessage().substring(command.length() + 1).trim().split(" ");
				switch (command) {
					case "moveall":
						moveAll(e);
						break;
					case "kickall":
						kickAll(e);
						break;
					case "broadcast":
						broadcast(e, args);
						break;
				}
			}
		});
	}

	private void broadcast(TextMessageEvent e, String[] args) {
		System.out.println(e.getInvokerName() + " called broadcast");
		if (api.getServerGroupsByClient(api.getClientInfo(e.getInvokerId())).stream().anyMatch(serverGroup -> serverGroup.getName().equals("Admin"))) {
			StringBuilder message = new StringBuilder();
			for (String arg : args) {
				message.append(arg).append(" ");
			}
			apiAsync.broadcast(message.toString());
		}
	}

	private void kickAll(TextMessageEvent e) {
		System.out.println(e.getInvokerName() + " called kickall");
		if (api.getServerGroupsByClient(api.getClientInfo(e.getInvokerId())).stream().anyMatch(serverGroup -> serverGroup.getName().equals("Admin"))) {
			List<Client> clients = api.getClients();
			clients.removeIf(cl -> cl.getId() == api.whoAmI().getId() || cl.getId() == e.getInvokerId());
			if (!clients.isEmpty())
				apiAsync.kickClientFromServer(clients.toArray(new Client[clients.size()]));
		}
	}

	private void moveAll(TextMessageEvent e) {
		System.out.println(e.getInvokerName() + " called moveall");
		if (api.getServerGroupsByClient(api.getClientInfo(e.getInvokerId())).stream().anyMatch(serverGroup -> serverGroup.getName().equals("Admin"))) {
			int channelId = api.getClientInfo(e.getInvokerId()).getChannelId();
			for (Client client : api.getClients()) {
				if (client.getChannelId() != channelId)
					apiAsync.moveClient(client.getId(), channelId);
			}
		}
	}

}
