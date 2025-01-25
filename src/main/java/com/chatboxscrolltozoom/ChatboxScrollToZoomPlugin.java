package com.chatboxscrolltozoom;

import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.KeyCode;
import net.runelite.api.ScriptEvent;
import net.runelite.api.events.WidgetLoaded;
import net.runelite.api.widgets.ComponentID;
import net.runelite.api.widgets.InterfaceID;
import net.runelite.api.widgets.JavaScriptCallback;
import net.runelite.api.widgets.Widget;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

@PluginDescriptor(
	name = "Chatbox Scroll to Zoom",
	description = "Scroll to zoom while hovering over the chatbox. Hold the CONTROL key to scroll through messages as normal.",
	tags = {"chat"}
)
public class ChatboxScrollToZoomPlugin extends Plugin {
	// this has value 0 when the scroll-to-zoom game setting is enabled,
	// and has value 1 when it is disabled
	private static final int SCROLL_TO_ZOOM_VARBIT_ID = 6357;

	@Inject
	private Client client;

	@Override
	protected void startUp() {
		setChatboxWidgetOnScrollWheelListener();
	}

	@Override
	protected void shutDown() {
		revertChatboxWidgetOnScrollWheelListener();
	}

	@Subscribe
	public void onWidgetLoaded(WidgetLoaded widgetLoaded) {
		if (widgetLoaded.getGroupId() == InterfaceID.CHATBOX) {
			setChatboxWidgetOnScrollWheelListener();
		}
	}

	private void setChatboxWidgetOnScrollWheelListener() {
		Widget chatboxWidget = client.getWidget(ComponentID.CHATBOX_MESSAGE_LINES);
		if (chatboxWidget != null) {
			chatboxWidget.setOnScrollWheelListener((JavaScriptCallback) scriptEvent -> {
				if (client.isKeyPressed(KeyCode.KC_CONTROL) || client.getVarbitValue(SCROLL_TO_ZOOM_VARBIT_ID) == 1) {
					runScrollScript(scriptEvent);
				} else {
					runZoomScript(scriptEvent);
				}
			});
		}
	}

	private void revertChatboxWidgetOnScrollWheelListener() {
		Widget chatboxWidget = client.getWidget(ComponentID.CHATBOX_MESSAGE_LINES);
		if (chatboxWidget != null) {
			chatboxWidget.setOnScrollWheelListener((JavaScriptCallback) scriptEvent -> {
				runScrollScript(scriptEvent);
			});
		}
	}

	private void runScrollScript(ScriptEvent scriptEvent) {
		// the correct arguments for this script were identified via inspection of
		// the arguments of the ScriptEvent that is triggered when scrolling in
		// the chatbox normally
		client.runScript(36, 10617389, 10616888, scriptEvent.getMouseY());
	}

	private void runZoomScript(ScriptEvent scriptEvent) {
		// the correct arguments for this script were identified via inspection of
		// the arguments of the ScriptEvent that is triggered when scrolling in
		// the main part of the viewport
		client.runScript(39, scriptEvent.getMouseY());
	}
}
