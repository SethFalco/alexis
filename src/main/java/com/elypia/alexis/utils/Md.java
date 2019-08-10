/*
 * Copyright (C) 2019-2019  Elypia
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.elypia.alexis.utils;

public final class Md {

    private Md() {
        // Do nothing
    }

    public static String bu(String body) {
        return b(u(body));
    }

    /**
     * @param body Text to wrap.
     * @return The text wrapped in bold markdown syntax.
     */
    public static String b(String body) {
        return "**" + body + "**";
    }

    /**
     * @param body Text to wrap.
     * @return The text wrapped in italics markdown syntax.
     */
    public static String i(String body) {
        return "_" + body + "_";
    }

    public static String u(String body) {
        return "__" + body + "__";
    }

    /**
     * @param body Text to wrap.
     * @return The text wrapped in strikethrough markdown syntax.
     */
    public static String s(String body) {
        return "~~" + body + "~~";
    }

    /**
     * @param body Text to wrap.
     * @return The text wrapped as a large markdown / HTML header.
     */
    public static String h1(String body) {
        return "# " + body;
    }

    /**
     * @param body Text to wrap.
     * @return The text wrapped as an h2 markdown / HTML header.
     */
    public static String h2(String body) {
        return "## " + body;
    }

    /**
     * @param body Text to wrap.
     * @return The text wrapped as an h3 markdown / HTML header.
     */
    public static String h3(String body) {
        return "### " + body;
    }

    /**
     * @param body Text to wrap.
     * @return The text wrapped as an h4 markdown / HTML header.
     */
    public static String h4(String body) {
        return "#### " + body;
    }

    /**
     * @param body Text to wrap.
     * @return The text wrapped as an h5 markdown / HTML header.
     */
    public static String h5(String body) {
        return "##### " + body;
    }

    /**
     * @param body Text to wrap.
     * @return The text wrapped as an h6 markdown / HTML header.
     */
    public static String h6(String body) {
        return "###### " + body;
    }

    /**
     * @param body The display text for the URL.
     * @param url Location the URL redirects the user.
     * @return The text wrapped as a markdown URL.
     */
    public static String a(String body, String url) {
        return "[" + body + "](" + url + ")";
    }

    /**
     * @param body The alt text for the image, displayed on hover.
     * @param url Location the image.
     * @return The text wrapped as a markdown image.
     */
    public static String img(String body, String url) {
        return "!" + a(body, url);
    }

    /**
     * @param body The text to wrap.
     * @return Text wrapped with markdown code syntax. (`)
     */
    public static String code(String body) {
        return "`" + body + "`";
    }

    /**
     * See {@link #pre(String, String)} for flavour / color.
     *
     * @param body The text to wrap.
     * @return Text wrapped with markdown codeblock syntax syntax. (```)
     */
    public static String pre(String body) {
        return pre("", body);
    }

    /**
     * @param flavour The color flavour to display the text, eg <code>java</code>.
     * @param body Text to wrap.
     * @return Text wrapped with markdown codeblock syntax. (```)
     */
    public static String pre(String flavour, String body) {
        return "```" + flavour + "\n" + body + "\n```";
    }

    /**
     * @param body The text to wrap.
     * @return Text wrapped with markdown quote syntax. (&gt;)
     */
    public static String blockquote(String body) {
        return "> " + body;
    }

    /**
     * This defaults the checkbox to unchecked. To specify
     * checked or unchecked, see {@link #checkbox(boolean, String)}.
     *
     * @param body The text to wrap.
     * @return Text wrapped with markdown checkbox syntax.
     */
    public static String checkbox(String body) {
        return checkbox(false, body);
    }

    /**
     * @param body The text to wrap.
     * @return Text wrapped with markdown checkbox syntax.
     */
    public static String checkbox(boolean checked, String body) {
        return "* [" + (checked ? "x" : " ") + "] " + body;
    }

    /**
     * Also linebreaks before and after.
     *
     * @return Markdown syntax for a line seperator.
     */
    public static String hr() {
        return "\n\n---\n";
    }

    /**
     * In markdown a line break is performed by putting
     * two spaces, then an extra linebreak \n for clarity.
     *
     * @return Markdown syntax for a linebreak.
     */
    public static String br() {
        return "  \n";
    }
}
