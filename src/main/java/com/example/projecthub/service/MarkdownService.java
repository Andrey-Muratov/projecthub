package com.example.projecthub.service;

import java.util.List;
import org.commonmark.Extension;
import org.commonmark.ext.autolink.AutolinkExtension;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.stereotype.Service;

@Service("markdownService")
public class MarkdownService {

    private final Parser parser;
    private final HtmlRenderer renderer;
    private final Safelist safelist;

    public MarkdownService() {
        List<Extension> extensions = List.of(AutolinkExtension.create());
        this.parser = Parser.builder().extensions(extensions).build();
        this.renderer = HtmlRenderer.builder().extensions(extensions).build();

        this.safelist = Safelist.basicWithImages()
                .removeTags("img")
                .addAttributes("a", "rel", "target");
    }

    public String render(String markdown) {
        if (markdown == null || markdown.isBlank()) {
            return "";
        }
        Node doc = parser.parse(markdown);
        String html = renderer.render(doc);

        String safe = Jsoup.clean(html, "/", safelist);

        return safe.replace("<a href", "<a target=\"_blank\" rel=\"noopener noreferrer nofollow\" href");
    }
}
