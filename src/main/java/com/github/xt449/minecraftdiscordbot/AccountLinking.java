package com.github.xt449.minecraftdiscordbot;

import com.google.common.collect.HashBiMap;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Jonathan Taclott (xt449 / BinaryBanana)
 */
abstract class AccountLinking {

	private static final File file = new File("account_links.db");
	private static final File filePending = new File("account_links_pending.db");

	static final HashBiMap<UUID, String> links = HashBiMap.create();
	static final HashMap<UUID, String> linksPending = new HashMap<>();

	public static void load() {
		links.clear();
		try {
			final List<String> lines = Files.readAllLines(file.toPath());
			for(String line : lines) {
				final String[] parts = line.split("\u0000");
				//{minecraft uuid as string}\u0000{discord id as long as string}
				links.put(UUID.fromString(parts[0]), parts[1]);
			}
		} catch(IOException exc) {
			exc.printStackTrace();
		}

		linksPending.clear();
		try {
			final List<String> lines = Files.readAllLines(filePending.toPath());
			for(String line : lines) {
				final String[] parts = line.split("\u0000");
				//{minecraft uuid as string}\u0000{discord id as long as string}
				final UUID uuid = UUID.fromString(parts[0]);
				// checking that the linked set does not contain a pending link
				if(links.get(uuid) == null && links.inverse().get(parts[1]) == null) {
					linksPending.put(uuid, parts[1]);
				}
			}
		} catch(IOException exc) {
			exc.printStackTrace();
		}
	}

	public static void save() {
		try {
			Files.write(file.toPath(), links.entrySet().stream().map(kvp -> kvp.getKey().toString() + '\u0000' + kvp.getValue()).collect(Collectors.toList()));
		} catch(IOException exc) {
			exc.printStackTrace();
		}

		try {
			Files.write(filePending.toPath(), linksPending.entrySet().stream().map(kvp -> kvp.getKey().toString() + '\u0000' + kvp.getValue()).collect(Collectors.toList()));
		} catch(IOException exc) {
			exc.printStackTrace();
		}
	}

	public static String getLink(UUID uuid) {
		return links.get(uuid);
	}

	public static UUID getLink(String discordId) {
		return links.inverse().get(discordId);
	}

	public static boolean hasLink(UUID uuid) {
		return links.containsKey(uuid);
	}

	public static boolean hasLink(String id) {
		return links.inverse().containsKey(id);
	}

	/**
	 * @return true if linking has begun; false if already linked
	 */
	public static boolean beginLinking(UUID uuid, String id) {
		if(links.containsKey(uuid) || links.containsValue(id)) {
			return false;
		}
		linksPending.put(uuid, id);
		return true;
	}

	/**
	 * @return true if successful or already linked; false if linking not yet begun
	 */
	public static boolean finishLinking(UUID uuid, String id) {
		if(links.containsKey(uuid) || links.containsValue(id)) {
			return true;
		}
		if(id.equals(linksPending.get(uuid))) {
			linksPending.remove(uuid);
			links.put(uuid, id);
			return true;
		}
		return false;
	}
}
