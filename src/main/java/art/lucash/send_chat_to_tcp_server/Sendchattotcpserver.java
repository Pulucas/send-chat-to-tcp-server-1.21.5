package art.lucash.send_chat_to_tcp_server;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Sendchattotcpserver implements ModInitializer {
	public static final String MOD_ID = "sendchattotcpserver";
	
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	private static ChatForwardingServer forwardingServer;

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("Hello Fabric world from Send Chat To TCP Server!");
		forwardingServer = new ChatForwardingServer(25586);
		forwardingServer.start();
		ServerMessageEvents.CHAT_MESSAGE.register((message, sender, params) -> {
			final String content = message.getContent().getString();
			forwardingServer.sendMessage(content);
		});
	}
}