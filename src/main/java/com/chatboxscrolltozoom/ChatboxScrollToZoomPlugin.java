package com.chatboxscrolltozoom;

import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.events.WidgetLoaded;
import net.runelite.api.widgets.ComponentID;
import net.runelite.api.widgets.InterfaceID;
import net.runelite.api.widgets.Widget;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.input.KeyListener;
import net.runelite.client.input.KeyManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

import java.awt.event.KeyEvent;

@PluginDescriptor(
	name = "Chatbox Scroll to Zoom",
	description = "Scroll to zoom while hovering over the chatbox",
	tags = {"chat"}
)
public class ChatboxScrollToZoomPlugin extends Plugin implements KeyListener {
	private static final int CONTROL_KEY_CODE = 17;

	// the correct arguments for this script were identified via inspection of
	// the arguments of the ScriptEvent that is triggered when scrolling in
	// the main part of the viewport
	private static final Integer[] ZOOM_SCRIPT_ARGS = {39, -2147483646};

	// the correct arguments for this script were identified via inspection of
	// the arguments of the ScriptEvent that is triggered when scrolling in
	// the chatbox normally
	private static final Integer[] SCROLL_SCRIPT_ARGS = {36, 10617389, 10616888, -2147483646};


	@Inject
	private Client client;

	@Inject
	private KeyManager keyManager;

	@Override
	protected void startUp() {
		zoomMode();
		keyManager.registerKeyListener(this);
	}

	@Override
	protected void shutDown() {
		scrollMode();
		keyManager.unregisterKeyListener(this);
	}

	@Subscribe
	public void onWidgetLoaded(WidgetLoaded widgetLoaded) {
		if (widgetLoaded.getGroupId() == InterfaceID.CHATBOX) {
			zoomMode();
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == CONTROL_KEY_CODE) {
			scrollMode();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == CONTROL_KEY_CODE) {
			zoomMode();
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	private void scrollMode() {
		Widget chatboxWidget = client.getWidget(ComponentID.CHATBOX_MESSAGE_LINES);
		if (chatboxWidget != null) {
			chatboxWidget.setOnScrollWheelListener(SCROLL_SCRIPT_ARGS);
		}
	}

	private void zoomMode() {
		Widget chatboxWidget = client.getWidget(ComponentID.CHATBOX_MESSAGE_LINES);
		if (chatboxWidget != null) {
			chatboxWidget.setOnScrollWheelListener(ZOOM_SCRIPT_ARGS);
		}
	}
}
