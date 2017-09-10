package ch.joel.teamspeakbot.listeners;

import ch.joel.teamspeakbot.TSBotApplication;
import ch.joel.teamspeakbot.mail.EmailAddress;
import ch.joel.teamspeakbot.mail.Mail;
import ch.joel.teamspeakbot.objects.Rank;
import ch.joel.teamspeakbot.test.SparkRest;
import com.github.theholywaffle.teamspeak3.api.event.TS3EventAdapter;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import com.github.theholywaffle.teamspeak3.api.wrapper.ServerGroup;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static j2html.TagCreator.*;

@RequiredArgsConstructor
public class WroteMeListener extends TS3EventAdapter {

	private final TSBotApplication application;

	@Override
	public void onTextMessage(TextMessageEvent e) {
		application.getApiAsync().whoAmI().onSuccess(serverQueryInfo -> {
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
					case "request":
						request(e, args);
				}
			}
		});
	}

	private void request(TextMessageEvent e, String[] args) {
		System.out.println(e.getInvokerName() + " called request");
		String requestSubject = args[0];
		String invoker = e.getInvokerName();
		Integer rankId = application.getApi().getServerGroups().stream().filter(serverGroup -> serverGroup.getName().equals(requestSubject)).map(ServerGroup::getId).findFirst().orElse(null);
		if (rankId == null)
			return;
		Rank rank = new Rank(e.getInvokerId(), rankId);
		String key = SparkRest.generateId();
		application.getRest().getRankMap().put(key, rank);
		String htmlMessage = html(
				body(
						h1(application.getConfiguration().getServer().getHost() + " request email").withStyle("color: blue"),
						p("If you wish to give " + invoker + " the rank " + requestSubject + " click on this link"),
						a("Allow").withHref(application.getConfiguration().getRestHost() + "/rank/" + key)
				)
		).render();
		Mail mail = new Mail(invoker + " requests " + requestSubject, "View as HTML Text", htmlMessage);
		EmailAddress fromMail = new EmailAddress(application.getConfiguration().getMail().getUsername(), application.getConfiguration().getNickname());
		EmailAddress toMail = new EmailAddress(application.getConfiguration().getMail().getAdminMail(), application.getConfiguration().getMail().getAdminMail().split("@")[0]);
		application.getMailService().sendMail(mail, fromMail, toMail);
		System.out.println("sent email");
		application.getApiAsync().sendPrivateMessage(e.getInvokerId(), "You have sent an email please wait");
	}

	private void broadcast(TextMessageEvent e, String[] args) {
		System.out.println(e.getInvokerName() + " called broadcast");
		if (application.getApi().getServerGroupsByClient(application.getApi().getClientInfo(e.getInvokerId())).stream().anyMatch(serverGroup -> serverGroup.getName().equals("Admin"))) {
			StringBuilder message = new StringBuilder();
			for (String arg : args) {
				message.append(arg).append(" ");
			}
			application.getApiAsync().broadcast(message.toString());
		}
	}

	private void kickAll(TextMessageEvent e) {
		System.out.println(e.getInvokerName() + " called kickall");
		if (application.getApi().getServerGroupsByClient(application.getApi().getClientInfo(e.getInvokerId())).stream().anyMatch(serverGroup -> serverGroup.getName().equals("Admin"))) {
			List<Client> clients = application.getApi().getClients();
			clients.removeIf(cl -> cl.getId() == application.getApi().whoAmI().getId() || cl.getId() == e.getInvokerId());
			if (!clients.isEmpty())
				application.getApiAsync().kickClientFromServer(clients.toArray(new Client[clients.size()]));
		}
	}

	private void moveAll(TextMessageEvent e) {
		System.out.println(e.getInvokerName() + " called moveall");
		if (application.getApi().getServerGroupsByClient(application.getApi().getClientInfo(e.getInvokerId())).stream().anyMatch(serverGroup -> serverGroup.getName().equals("Admin"))) {
			int channelId = application.getApi().getClientInfo(e.getInvokerId()).getChannelId();
			for (Client client : application.getApi().getClients()) {
				if (client.getChannelId() != channelId)
					application.getApiAsync().moveClient(client.getId(), channelId);
			}
		}
	}

}
