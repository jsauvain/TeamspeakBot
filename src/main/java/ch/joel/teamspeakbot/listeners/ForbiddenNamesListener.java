package ch.joel.teamspeakbot.listeners;

import ch.joel.teamspeakbot.TSBOTConfiguration;
import com.github.theholywaffle.teamspeak3.TS3ApiAsync;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import lombok.AllArgsConstructor;

import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@AllArgsConstructor
public class ForbiddenNamesListener implements Runnable {

	private TS3ApiAsync apiAsync;
	private TSBOTConfiguration configuration;

	@Override
	public void run() {
		Pattern pattern = Pattern.compile(configuration.getForbiddenNames().getForbiddenNamesRegex());
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				apiAsync.getClients().onSuccess(clients -> {
					for (Client client : clients) {
						Matcher matcher = pattern.matcher(client.getNickname());
						if (configuration.getForbiddenNames().getForbiddenNames().contains(client.getNickname()) || !matcher.matches()) {
							apiAsync.getServerGroupsByClientId(client.getDatabaseId()).onSuccess(serverGroups -> {
								if (serverGroups.stream().noneMatch(serverGroup -> serverGroup.getName().equals("Admin"))) {
									apiAsync.kickClientFromServer(configuration.getForbiddenNames().getForbiddenNameKickMessage().replace("%p", client.getNickname()), client.getId());
									System.out.println(client.getNickname() + " was kicked for nickname");
								}
							});
						}
					}
				});
			}
		}, 5000, 5000);
	}
}
