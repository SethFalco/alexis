package com.elypia.alexis.test;

import com.elypia.commandler.ModulesContext;
import com.elypia.commandler.doc.DocBuilder;
import com.elypia.commandler.doc.entities.*;
import com.elypia.commandler.export.DocExporter;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.io.IOException;
import java.util.List;

public class Misc {

    @Test
    public void test() throws IOException {
        ModulesContext context = new ModulesContext();
        context.addPackage("com.elypia.alexis.commandler.modules");

        AppData data = new AppData()
            .setName("Alexis")
            .setLogo("https://i.ibb.co/sJ0zt5m/alexis.png")
            .setFavicon(new Favicon("png", "https://i.ibb.co/sJ0zt5m/alexis.png"))
            .setStyle(
                new Style(ColorScheme.getDarkSceme(),
                new FontFamily("Raleway", "https://fonts.googleapis.com/css?family=Raleway"))
            )
            .setReadme("C:\\Users\\Seth\\Google Drive\\IntelliJ\\Alexis\\README.md")
            .setDescription("Hey! I'm a bot!")
            .setSocial(List.of(
                new Social("fab fa-discord", "https://discord.gg/hprGMaM", new Color(114, 137, 218)),
                new Social( "fab fa-gitlab", "https://gitlab.com/Elypia", new Color(226, 67, 41)),
                new Social("fab fa-twitter", "https://twitter.com/Elypia", new Color(21, 161, 242))
            ));

        DocBuilder builder = new DocBuilder()
            .setContext(context)
            .setData(data);

        builder.build();
    }

    @Test
    public void exportTest() {
        ModulesContext context = new ModulesContext();
        context.addPackage("com.elypia.alexis.commandler.modules");

        DocExporter exporter = new DocExporter().setContext(context);

        System.out.println(exporter.toJson());
    }
}

