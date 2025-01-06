package org.abigotado.links.service;

import org.abigotado.config.AppConfig;
import org.abigotado.config.Messages;
import org.abigotado.links.entity.Link;
import org.abigotado.exceptions.LinkAlreadyExistsException;
import org.abigotado.links.repository.LinkRepository;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class LinkService {

    private final LinkRepository linkRepository;

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);


    public LinkService(LinkRepository linkRepository) {
        this.linkRepository = linkRepository;
        deleteExpiredLinks();
        scheduleExpiredLinkDeletion();
    }

    public Link createShortLink(String longLink, UUID userId, Integer clicksLeft, LocalDateTime expirationDate) {
        Optional<Link> existingLink = linkRepository.findByLongLinkAndUserId(longLink, userId);
        if (existingLink.isPresent()) {
            throw new LinkAlreadyExistsException(existingLink.get().getShortLink());
        }

        if (clicksLeft == null) {
            clicksLeft = AppConfig.DEFAULT_CLICKS;
        }
        if (expirationDate == null) {
            expirationDate = LocalDateTime.now().plusDays(AppConfig.DEFAULT_EXPIRATION_DAYS);
        }

        String shortLink = AppConfig.SHORT_LINK_PREFIX + generateShortLink();
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
            throw new IllegalStateException(Messages.LINK_EXPIRED);
        }

        if (link.getClicksLeft() <= 0) {
            throw new IllegalStateException(Messages.LINK_LIMIT_EXCEEDED);
        }

        linkRepository.decrementClicksLeft(link.getId());
        return Optional.of(link.getLongLink());
    }

    public Optional<URI> getLongLinkUri(String shortLink) {
        Optional<String> longLink = getLongLink(shortLink);

        if (longLink.isEmpty()) {
            return Optional.empty();
        }

        try {
            return Optional.of(new URI(longLink.get()));
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(Messages.INVALID_URL_FORMAT + longLink.get(), e);
        }
    }

    public boolean userHasLinks(UUID userId) {
        return linkRepository.findAll().stream().anyMatch(link -> link.getUserId().equals(userId));
    }

    public void deleteExpiredLinks() {
        linkRepository.deleteExpiredLinks();
    }

    private String generateShortLink() {
        return UUID.randomUUID().toString().substring(0, 6);
    }

    private void scheduleExpiredLinkDeletion() {
        scheduler.scheduleAtFixedRate(this::deleteExpiredLinks, 0, 1, TimeUnit.DAYS);
    }
}