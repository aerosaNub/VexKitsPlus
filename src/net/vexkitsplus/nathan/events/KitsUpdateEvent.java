package net.vexkitsplus.nathan.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class KitsUpdateEvent extends Event {

    private static HandlerList handlerList = new HandlerList();

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    private final Type type;
    private final String kitName;

    public KitsUpdateEvent(Type type, String kitName) {
        super();
        this.type = type;
        this.kitName = kitName;
    }

    public Type getType() {
        return type;
    }

    public String getKitName() {
        return kitName;
    }

    public enum Type {
        CREATE, DELETE, EDIT
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }
}

