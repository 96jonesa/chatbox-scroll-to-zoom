package com.chatboxscrolltozoom;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class ChatboxScrollToZoomPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(ChatboxScrollToZoomPlugin.class);
		RuneLite.main(args);
	}
}
