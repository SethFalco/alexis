package fun.falco.alexis.core.modules.runescape;

import java.util.List;
import java.util.Map;

import org.elypia.elypiai.runescape.QuestStatus;
import org.elypia.elypiai.runescape.data.CompletionStatus;

public class QuestStatusModel {

    private String username;
    private Map<CompletionStatus, List<QuestStatus>> questStatuses;

    public QuestStatusModel(String username, Map<CompletionStatus, List<QuestStatus>> questStatuses) {
        this.username = username;
        this.questStatuses = questStatuses;
    }

    /**
     * @return
     *     The username of the player. This may not have the exact
     *     capitalization as registered as RuneScape.
     */
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Map<CompletionStatus, List<QuestStatus>> getQuestStatuses() {
        return questStatuses;
    }

    public void setQuestStatuses(Map<CompletionStatus, List<QuestStatus>> questStatuses) {
        this.questStatuses = questStatuses;
    }
}
