package ch.joel.teamspeakbot.properties;

import ch.joel.teamspeakbot.TSBOTConfiguration;
import ch.joel.teamspeakbot.config.ConfigLoader;
import org.junit.Test;

public class ConfigLoaderTest {
	@Test
	public void load() throws Exception {
		ConfigLoader loader = new ConfigLoader();
		TSBOTConfiguration configuration = loader.load();
		System.out.println(configuration);
	}

}