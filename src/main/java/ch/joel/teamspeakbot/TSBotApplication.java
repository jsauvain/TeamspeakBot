package ch.joel.teamspeakbot;

import ch.joel.teamspeakbot.listeners.ClientJoinListener;
import ch.joel.teamspeakbot.listeners.WroteMeListener;
import ch.joel.teamspeakbot.properties.ConfigLoader;
import ch.joel.teamspeakbot.properties.TSBOTConfiguration;
import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3ApiAsync;
import com.github.theholywaffle.teamspeak3.TS3Config;
import com.github.theholywaffle.teamspeak3.TS3Query;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;

public class TSBotApplication {

	public static void main(String[] args) {
		ConfigLoader configLoader = new ConfigLoader();
		TSBOTConfiguration configuration = configLoader.load();

		final TS3Config config = new TS3Config();
		config.setHost(configuration.getHost());

		TS3Query query = new TS3Query(config);
		query.connect();

		TS3Api api = query.getApi();
		TS3ApiAsync apiAsync = query.getAsyncApi();
		// tsbot YVTilttC
		api.login(configuration.getUsername(), configuration.getPassword());
		api.selectVirtualServerById(1);
		api.setNickname(configuration.getNickname());
		for (Client client : api.getClients()) {
			if (client.getId() != api.whoAmI().getId())
				api.sendPrivateMessage(client.getId(), configuration.getNickname() + ", here to help!");
		}
		api.registerAllEvents();
		api.addTS3Listeners(new ClientJoinListener(api), new WroteMeListener(api, apiAsync));
		api.sendChannelMessage("Bot started");
	}

}
