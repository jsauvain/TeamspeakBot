package ch.joel.teamspeakbot;

import ch.joel.teamspeakbot.listeners.AfkListener;
import ch.joel.teamspeakbot.listeners.ClientJoinListener;
import ch.joel.teamspeakbot.listeners.ForbiddenNamesListener;
import ch.joel.teamspeakbot.listeners.WroteMeListener;
import ch.jooel.config.YamlConfigurationFactory;
import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3ApiAsync;
import com.github.theholywaffle.teamspeak3.TS3Config;
import com.github.theholywaffle.teamspeak3.TS3Query;
import com.github.theholywaffle.teamspeak3.api.event.TS3EventType;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;

import java.io.File;

public class TSBotApplication {

	private TSBOTConfiguration configuration;
	private TS3ApiAsync apiAsync;
	private TS3Api api;

	public static void main(String[] args) {
		new TSBotApplication().start();

	}

	private void start() {
		loadConfig();
		setupTeamSpeakConnection();
		welcomeUsers();

		api.registerAllEvents();
		api.addTS3Listeners(new ClientJoinListener(apiAsync, configuration), new WroteMeListener(api, apiAsync));
		new AfkListener(apiAsync, configuration).run();
		new ForbiddenNamesListener(apiAsync, configuration).run();
		System.out.println("Bot started");
	}

	private void loadConfig() {
		YamlConfigurationFactory factory = new YamlConfigurationFactory();
		if (!new File("config.yml").exists())
			factory.copyFromResource("/config.yml", "config.yml");
		configuration = factory.load(TSBOTConfiguration.class, "config.yml");
		System.out.println(configuration);
	}

	private void setupTeamSpeakConnection() {
		final TS3Config config = new TS3Config();
		config.setHost(configuration.getServer().getHost());

		TS3Query query = new TS3Query(config);
		query.connect();

		api = query.getApi();
		apiAsync = query.getAsyncApi();
		api.login(configuration.getServer().getUsername(), configuration.getServer().getPassword());
		api.selectVirtualServerById(1);
		api.setNickname(configuration.getNickname());
	}

	private void welcomeUsers() {
		apiAsync.getClients().onSuccess(clients -> {
			for (Client client : clients) {
				apiAsync.whoAmI().onSuccess(serverQueryInfo -> {
					if (client.getId() != serverQueryInfo.getId())
						apiAsync.sendPrivateMessage(client.getId(), configuration.getBotJoinedServerMessage().replace("%p", client.getNickname()));
				});
			}
		});


	}

}
