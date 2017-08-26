package ch.joel.teamspeakbot.properties;

import ch.jooel.config.YamlConfigurationFactory;
import ch.joel.teamspeakbot.TSBOTConfiguration;
import ch.joel.teamspeakbot.util.ReflectionUtil;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Properties;

public class ConfigLoader {

	private static final File CONFIG_FILE = new File("config.yml");

	public TSBOTConfiguration load() {
		if (!CONFIG_FILE.exists()) {
			createConfigFile();
		}
		return createTSBotConfig();

	}

	private TSBOTConfiguration createTSBotConfig() {
		YamlConfigurationFactory factory = new YamlConfigurationFactory();
		return factory.load("config.yml", TSBOTConfiguration.class);
	}

	private void createConfigFile() {
		try (InputStream inputStream = this.getClass().getResourceAsStream("/config.yml");
			 OutputStream outputStream = new FileOutputStream(CONFIG_FILE)) {
			IOUtils.copy(inputStream, outputStream);
		} catch (IOException ex) {
			ex.printStackTrace();
		}

	}
}
