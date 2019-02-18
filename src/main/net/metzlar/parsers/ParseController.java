package net.metzlar.parsers;

import net.metzlar.renderEngine.scene.Scene;
import org.jsoup.nodes.Element;

import java.util.HashMap;
import java.util.Map;

public abstract class ParseController<Type, ParserType extends Parser<Type>> {
    protected Map<String, ParserType> parsers = new HashMap<>();
    protected Scene scene;

    public ParseController(Scene scene) {
        this.scene = scene;
    }

    public Type parse(Element docElement) {
        if (parsers.containsKey(docElement.attr("class")))
            return parsers.get(docElement.attr("class")).parse(docElement);

        System.out.printf("Unknown object class: %s\n", docElement.attr("class"));
        return null;
    }
}
