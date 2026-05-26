package com.example.projecthub.service;

import com.example.projecthub.repository.UserRepository;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.stereotype.Service;

@Service("mentionsService")
public class MentionsService {

    private static final Pattern MENTION_RE =
            Pattern.compile("(?<![\\p{L}\\p{N}_])@([A-Za-z0-9._-]{2,64})");

    private static final long TTL_NANOS = java.util.concurrent.TimeUnit.SECONDS.toNanos(30);

    private final UserRepository userRepository;
    private volatile Set<String> cachedLogins = Set.of();
    private volatile long cachedAtNanos = 0L;

    public MentionsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String render(String text) {
        if (text == null || text.isBlank()) return "";
        Set<String> logins = loadLogins();
        String escaped = htmlEscape(text);

        Matcher m = MENTION_RE.matcher(escaped);
        StringBuilder out = new StringBuilder(escaped.length() + 32);
        int last = 0;
        while (m.find()) {
            String login = m.group(1);
            out.append(escaped, last, m.start());
            if (logins.contains(login.toLowerCase())) {
                out.append("<a class=\"mention\" href=\"/profile/").append(login).append("\">")
                   .append("@").append(login).append("</a>");
            } else {
                out.append(m.group());
            }
            last = m.end();
        }
        out.append(escaped, last, escaped.length());

        return out.toString().replace("\n", "<br/>");
    }

    public void invalidate() {
        this.cachedAtNanos = 0L;
        this.cachedLogins = Set.of();
    }

    private Set<String> loadLogins() {
        long now = System.nanoTime();
        if (now - cachedAtNanos < TTL_NANOS && !cachedLogins.isEmpty()) {
            return cachedLogins;
        }
        Set<String> set = new HashSet<>();
        userRepository.findAll().forEach(u -> set.add(u.getLogin().toLowerCase()));
        this.cachedLogins = Set.copyOf(set);
        this.cachedAtNanos = now;
        return cachedLogins;
    }

    private static String htmlEscape(String s) {
        return s.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}
