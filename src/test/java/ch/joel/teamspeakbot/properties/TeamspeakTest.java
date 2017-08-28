package ch.joel.teamspeakbot.properties;

import ch.joel.teamspeakbot.TSBOTConfiguration;
import ch.joel.teamspeakbot.config.ConfigLoader;
import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3ApiAsync;
import com.github.theholywaffle.teamspeak3.TS3Config;
import com.github.theholywaffle.teamspeak3.TS3Query;
import com.github.theholywaffle.teamspeak3.api.wrapper.Channel;
import org.junit.Test;

public class TeamspeakTest {
	@Test
	public void testChannelIds() {
		ConfigLoader configLoader = new ConfigLoader();
		TSBOTConfiguration configuration = configLoader.load();

		final TS3Config config = new TS3Config();
		config.setHost(configuration.getServer().getHost());

		TS3Query query = new TS3Query(config);
		query.connect();

		TS3Api api = query.getApi();
		TS3ApiAsync apiAsync = query.getAsyncApi();
		// tsbot YVTilttC
		api.login(configuration.getServer().getUsername(), configuration.getServer().getPassword());
		api.selectVirtualServerById(1);
		api.setNickname(configuration.getNickname());

		for (Channel channel : api.getChannels()) {
			System.out.println(channel.getId() + " " + channel.getName());
		}
	}

}
