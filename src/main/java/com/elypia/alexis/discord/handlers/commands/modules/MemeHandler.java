package com.elypia.alexis.discord.handlers.commands.modules;

import com.elypia.alexis.discord.annotation.Command;
import com.elypia.alexis.discord.annotation.Module;
import com.elypia.alexis.discord.annotation.Parameter;
import com.elypia.alexis.discord.events.MessageEvent;
import com.elypia.alexis.discord.handlers.commands.impl.CommandHandler;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

@Module(
    aliases = "Meme",
    help = "Check out memes or even generate them!"
)
public class MemeHandler extends CommandHandler {

    @Command(
        aliases = "generate",
        help = "Choose a meme and caption it.",
        params = {
            @Parameter(
                param = "meme",
                help = "The meme template you want to use.",
                type = String.class
            ),
            @Parameter(
                param = "body",
                help = "The text you wish to display.",
                type = String.class
            )
        }
    )
    public void generateMeme(MessageEvent event) throws IOException {
        String body = event.getParams()[1];

        String path = "/resources/meme_templates/change_my_mind.jpg";
        File file = new File(getClass().getResource(path).getFile());
        BufferedImage image = ImageIO.read(file);

        Graphics graphics = image.getGraphics();
        graphics.setFont(graphics.getFont().deriveFont(40f));
        graphics.drawString(body, 354, 282);
        graphics.dispose();

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", bytes);
        event.getChannel().sendFile(bytes.toByteArray(), "change_my_mind.jpg").queue();
    }
}
