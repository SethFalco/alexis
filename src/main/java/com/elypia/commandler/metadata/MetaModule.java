package com.elypia.commandler.metadata;

import com.elypia.alexis.discord.handlers.impl.CommandHandler;
import com.elypia.commandler.annotations.command.Module;

public class MetaModule {

    private Class<? extends CommandHandler> clazz;
    private Module module;

    public <T extends CommandHandler> MetaModule(T t) {
        clazz = t.getClass();
        module = clazz.getAnnotation(Module.class);
    }

    public Class<? extends CommandHandler> getHandler() {
        return clazz;
    }

    public Module getModule() {
        return module;
    }
}
