package net.metzlar.parsers;

import org.jsoup.nodes.Element;

public interface Parser<Type> {
    Type parse(Element docElement);
}
