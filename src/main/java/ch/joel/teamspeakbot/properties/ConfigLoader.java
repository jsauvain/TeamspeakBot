package ch.joel.teamspeakbot.properties;

import ch.joel.teamspeakbot.TSBOTConfiguration;
import ch.joel.teamspeakbot.util.ReflectionUtil;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Properties;

public class ConfigLoader {

	private static final File CONFIG_FILE = new File("config.properties");

	private Properties properties = new Properties();

	public TSBOTConfiguration load() {
		if (!CONFIG_FILE.exists()) {
			createConfigFile();
		}
		initProperties();
		return createTSBotConfig();

	}

	private TSBOTConfiguration createTSBotConfig() {
		List<Field> fields = ReflectionUtil.getFieldsFromClass(TSBOTConfiguration.class);
		TSBOTConfiguration configuration = new TSBOTConfiguration();
		for (Field field : fields) {
			ReflectionUtil.setValue(field, configuration, properties.getProperty(field.getName()));
		}
		return configuration;
	}

	private void initProperties() {
		try (FileInputStream inputStream = new FileInputStream(CONFIG_FILE)) {
			properties.load(inputStream);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	private void createConfigFile() {
		try (InputStream inputStream = this.getClass().getResourceAsStream("/config.properties");
			 OutputStream outputStream = new FileOutputStream(CONFIG_FILE)) {
			IOUtils.copy(inputStream, outputStream);
		} catch (IOException ex) {
			ex.printStackTrace();
		}

	}
}
