package com.elypia.alexis.discord;

import com.elypia.alexis.utils.BotUtils;
import com.elypia.alexis.utils.ExitCode;
import net.dv8tion.jda.core.entities.User;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;

public class ChatbotConfiguration {

    // Database
    private boolean enabled;
    private String ip;
    private int port;
    private String user;
    private String password;

    // Discord
    private String token;
    private String defaultPrefix;
    private String[] defaultStatuses;
    private long[] developers;

    // Debug
    private boolean enforcePrefix;
    private boolean developersOnly;

    public static ChatbotConfiguration getConfiguration(String path) {
        try (FileReader reader = new FileReader(path)) {
            StringBuilder builder = new StringBuilder();
            int i;

            while ((i = reader.read()) != -1)
                builder.append((char)i);

            JSONObject object = new JSONObject(builder.toString());
            return new ChatbotConfiguration(object);
        } catch (FileNotFoundException ex) {
            JSONObject object = ChatbotConfiguration.generateConfigTemplate();

            try (FileWriter writer = new FileWriter(path)) {
                String json = object.toString(4);
                writer.write(json);

                ExitCode code = ExitCode.GENERATED_NEW_CONGIG;
                BotUtils.LOGGER.log(Level.INFO, code.getMessage() + path);
                writer.close();
                System.exit(code.getStatusCode());
            } catch (IOException e) {
                ExitCode code = ExitCode.FAILED_TO_WRITE_CONFIG;
                BotUtils.LOGGER.log(Level.SEVERE, code.getMessage() + path, e);
                System.exit(code.getStatusCode());
            }
        } catch (IOException ex){
            ExitCode code = ExitCode.FAILED_TO_READ_CONFIG;
            BotUtils.LOGGER.log(Level.SEVERE, code.getMessage() + path, ex);
            System.exit(code.getStatusCode());
        }

        return null;
    }

    public static JSONObject generateConfigTemplate() {
        JSONObject database = new JSONObject();
        database.put("enabled", "true");
        database.put("ip", "localhost");
        database.put("port", 27017);
        database.put("user", "username");
        database.put("password", "password");
        JSONArray statusesArray = new JSONArray();

        statusesArray.put("game_status_one");
        statusesArray.put("game_status_two");

        JSONArray developersArray = new JSONArray();
        developersArray.put(10000000000000000L);
        developersArray.put(20000000000000000L);

        JSONObject debug = new JSONObject();
        debug.put("enforce_prefix", true);
        debug.put("developers_only", true);

        JSONObject discord = new JSONObject();
        discord.put("token", "discord_bot_token");
        discord.put("default_prefix", "default_bot_prefix");
        discord.put("default_statuses", statusesArray);
        discord.put("developers", developersArray);
        discord.put("debug", debug);

        JSONObject object = new JSONObject();
        object.put("database", database);
        object.put("discord", discord);

        return object;
    }

    public ChatbotConfiguration(JSONObject object) {
        // Database
        JSONObject database = object.optJSONObject("database");

        if (database != null) {
            enabled = database.optBoolean("enabled");

            if (enabled) {
                ip = database.optString("ip", null);
                port = database.optInt("port");
                user = database.optString("user", null);
                password = database.optString("password", null);
            }
        }

        // Discord
        JSONObject discord = object.optJSONObject("discord");

        if (discord == null) {
            ExitCode code = ExitCode.MALFORMED_CONFIG_DISCORD;
            BotUtils.LOGGER.log(Level.SEVERE, code.getMessage());
            System.exit(code.getStatusCode());
        }

        token = discord.optString("token", null);

        if (token == null) {
            ExitCode code = ExitCode.MALFORMED_CONFIG_TOKEN;
            BotUtils.LOGGER.log(Level.SEVERE, code.getMessage());
            System.exit(code.getStatusCode());
        }

        defaultPrefix = discord.optString("default_prefix", null);

        if (defaultPrefix == null) {
            ExitCode code = ExitCode.MALFORMED_CONFIG_PREFIX;
            BotUtils.LOGGER.log(Level.SEVERE, code.getMessage());
            System.exit(code.getStatusCode());
        }

        // Discord Statuses
        JSONArray statusesArray = discord.optJSONArray("default_statuses");

//        if (statusesArray != null) {
//            int statusesLength = statusesArray.length();
//            defaultStatuses = new String[statusesLength];
//
//            for (int i = 0; i < statusesLength; i++) {
//                String s = statusesArray.optString(i, null);
//
//                if (s != null)
//                    defaultStatuses[i] = statusesArray.getString(i);
//                else
//                    BotUtils.LOGGER.log(Level.WARNING, "Config, default Statuses should only contain Strings; value omitted.");
//            }
//        }

        // Discord Developers
        JSONArray developersArray = discord.optJSONArray("developers");

        if (developersArray != null) {
            int developersLength = developersArray.length();
            developers = new long[developersLength];

            for (int i = 0; i < developersLength; i++) {
                long l = developersArray.optLong(i);

                if (l != 0)
                    developers[i] = developersArray.getLong(i);
                else
                    BotUtils.LOGGER.log(Level.WARNING, "Config, developers should only contain Discord IDs; value omitted.");
            }
        }

        // Discord debug
        JSONObject debug = discord.optJSONObject("debug");

        if (debug != null) {
            enforcePrefix = debug.optBoolean("enforce_prefix", true);
            developersOnly = debug.optBoolean("developers_only", true);
        } else {
            enforcePrefix = true;
            developersOnly = true;
        }
    }

    public boolean isDeveloper(User user) {
        return isDeveloper(user.getIdLong());
    }

    public boolean isDeveloper(long id) {
        if (developers == null)
            return false;

        for (long l : developers) {
            if (l == id)
                return true;
        }

        return false;
    }

    public boolean isDatabaseEnabled() {
        return enabled;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public String getToken() {
        return token;
    }

    public String getDefaultPrefix() {
        return defaultPrefix;
    }

    public String[] getDefaultStatuses() {
        return defaultStatuses;
    }

    public long getLeadDeveloper() {
        return developers[0];
    }

    public long[] getDevelopers() {
        return developers;
    }

    public boolean enforcePrefix() {
        return enforcePrefix;
    }

    public boolean developersOnly() {
        return developersOnly;
    }
}
