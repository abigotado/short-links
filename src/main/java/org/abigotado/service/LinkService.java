package org.abigotado.service;

import org.abigotado.entity.Link;
import org.abigotado.repository.LinkRepository;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public class LinkService {

    private final LinkRepository linkRepository;

    public LinkService(LinkRepository linkRepository) {
        this.linkRepository = linkRepository;
    }

    public Link createShortLink(String longLink, UUID userId, int clicksLeft, LocalDateTime expirationDate) {
        Optional<Link> existingLink = linkRepository.findByShortLink(longLink);

        if (existingLink.isPresent() && existingLink.get().getUserId().equals(userId)) {
            return existingLink.get();
        }

        String shortLink = generateShortLink();
        Link link = Link.builder()
                        .id(UUID.randomUUID())
                        .longLink(longLink)
                        .shortLink(shortLink)
                        .userId(userId)
                        .clicksLeft(clicksLeft)
                        .expirationDate(expirationDate)
                        .build();

        linkRepository.saveLink(link);
        return link;
    }

    public Optional<String> getLongLink(String shortLink) {
        Optional<Link> optionalLink = linkRepository.findByShortLink(shortLink);
        if (optionalLink.isEmpty()) {
            return Optional.empty();
        }

        Link link = optionalLink.get();

        if (link.getExpirationDate().isBefore(LocalDateTime.now())) {
            linkRepository.deleteExpiredLinks();
            return Optional.empty();
        }

        if (link.getClicksLeft() <= 0) {
            return Optional.empty();
        }

        linkRepository.decrementClicksLeft(link.getId());

        return Optional.of(link.getLongLink());
    }

    public void redirectToLongLink(String shortLink) {
        Optional<String> longLink = getLongLink(shortLink);
        if (longLink.isPresent()) {
            try {
                if (Desktop.isDesktopSupported()) {
                    Desktop desktop = Desktop.getDesktop();
                    desktop.browse(new URI(longLink.get()));
                    System.out.println("Перенаправление на: " + longLink.get());
                } else {
                    System.out.println("Открытие ссылок не поддерживается на этой системе.");
                }
            } catch (IOException | URISyntaxException e) {
                System.out.println("Ошибка при попытке открыть URL: " + e.getMessage());
            }
        } else {
            System.out.println("Ссылка недоступна (истек срок действия или исчерпан лимит переходов).");
        }
    }

    public boolean userHasLinks(UUID userId) {
        return linkRepository.findAll().stream()
                             .anyMatch(link -> link.getUserId().equals(userId));
    }

    public void deleteExpiredLinks() {
        linkRepository.deleteExpiredLinks();
    }

    private String generateShortLink() {
        return UUID.randomUUID().toString().substring(0, 6);
    }
}