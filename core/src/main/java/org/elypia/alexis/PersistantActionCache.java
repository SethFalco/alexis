package org.elypia.alexis;

import org.elypia.alexis.persistence.entities.ActionData;
import org.elypia.alexis.persistence.repositories.ActionRepository;
import org.elypia.commandler.api.ActionCache;
import org.elypia.commandler.event.Action;

import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

@Priority(1)
@ApplicationScoped
@Alternative
public class PersistantActionCache implements ActionCache {

    private ActionRepository actionRepo;

    @Inject
    public PersistantActionCache(ActionRepository actionRepo) {
        this.actionRepo = actionRepo;
    }

    @Override
    public void put(Action action) {
        ActionData data = new ActionData(action.getId().toString(), action.getContent(), action.getControllerType(), action.getMethodName());
        actionRepo.save(data);
    }

    @Override
    public Action get(Serializable serializable) {
        ActionData data = actionRepo.findBy(serializable);
        return new Action(data.getId(), data.getContent(), data.getControllerType(), data.getMethodName(), List.of());
    }

    @Override
    public Action remove(Serializable serializable) {
        ActionData data = new ActionData(serializable.toString());
        actionRepo.remove(data);
        return null;
    }

    @Override
    public Collection<Action> getAll() {
        return actionRepo.findAll().stream()
            .map((o) -> new Action(o.getId(), o.getContent(), o.getControllerType(), o.getMethodName()))
            .collect(Collectors.toUnmodifiableList());
    }
}
