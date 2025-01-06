package org.abigotado.repository.impl;

import org.abigotado.entity.Link;
import org.abigotado.repository.LinkRepository;
import org.abigotado.utils.JsonFileHandler;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class LinkRepositoryImpl implements LinkRepository {
    private final Map<String, Link> linkStorage;

    public LinkRepositoryImpl() {
        this.linkStorage = JsonFileHandler.loadLinks();
    }

    @Override
    public Optional<Link> findByShortLink(String shortLink) {
        return Optional.ofNullable(linkStorage.get(shortLink));
    }

    @Override
    public Optional<Link> findByLongLinkAndUserId(String longLink, UUID userId) {
        return linkStorage.values()
                          .stream()
                          .filter(link -> link.getLongLink().equals(longLink) && link.getUserId().equals(userId))
                          .findFirst();
    }

    @Override
    public void saveLink(Link link) {
        linkStorage.put(link.getShortLink(), link);
        JsonFileHandler.saveLinks(linkStorage);
    }

    @Override
    public void deleteExpiredLinks() {
        linkStorage.entrySet()
                   .removeIf(entry -> entry.getValue().getExpirationDate().isBefore(java.time.LocalDateTime.now()));
        JsonFileHandler.saveLinks(linkStorage);
    }

    @Override
    public void decrementClicksLeft(UUID linkId) {
        linkStorage.values().stream().filter(link -> link.getId().equals(linkId)).findFirst().ifPresent(link -> {
            link.setClicksLeft(link.getClicksLeft() - 1);
            JsonFileHandler.saveLinks(linkStorage);
        });
    }

    @Override
    public Collection<Link> findAll() {
        return linkStorage.values();
    }
}