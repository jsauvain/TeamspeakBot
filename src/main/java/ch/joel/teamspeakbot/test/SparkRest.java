package ch.joel.teamspeakbot.test;

import ch.joel.teamspeakbot.objects.Rank;
import com.github.theholywaffle.teamspeak3.TS3ApiAsync;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

import static spark.Spark.get;
import static spark.Spark.halt;

@RequiredArgsConstructor
public class SparkRest {

	@Getter
	private Map<String, Rank> rankMap = new HashMap<>();

	private final TS3ApiAsync apiAsync;

	private static char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890".toCharArray();

	public void setupServer() {
		get("/rank/:key", (request, response) -> {
			String key = request.params(":key");
			Rank rank = rankMap.remove(key);
			if (rank == null)
				return halt(400);
			apiAsync.getClientInfo(rank.getUserId()).onSuccess(result -> {
				apiAsync.addClientToServerGroup(rank.getRankId(), result.getDatabaseId());
			});
			return "OK";
		});

	}

	public static String generateId() {
		StringBuilder key = new StringBuilder();
		SecureRandom random = new SecureRandom();
		for (int i = 0; i < 32; i++) {
			key.append(chars[random.nextInt(chars.length)]);
		}
		return key.toString();
	}


}
