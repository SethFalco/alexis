package fun.falco.alexis.core;

import org.apache.deltaspike.core.api.projectstage.ProjectStage;
import org.apache.deltaspike.core.util.ProjectStageProducer;

import com.google.cloud.logging.LogEntry;
import com.google.cloud.logging.LoggingEnhancer;

/**
 * Adds a label to all logs with the {@link ProjectStage}
 * of the application.
 *
 * @author seth@falco.fun (Seth Falco)
 * @since 3.0.0
 */
public class ProjectStageLoggingEnhancer implements LoggingEnhancer {

    /**
     * The name of the label which will store the {@link ProjectStage}.
     * Name is written in camelCase to fit with default log values.
     */
    private static final String LABEL_NAME = "projectStage";

    @Override
    public void enhanceLogEntry(LogEntry.Builder builder) {
        ProjectStage projectStage = ProjectStageProducer.getInstance().getProjectStage();
        builder.addLabel(LABEL_NAME, projectStage.toString());
    }
}
