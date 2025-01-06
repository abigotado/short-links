package org.abigotado.links.repository;

import org.abigotado.links.entity.Link;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface LinkRepository {
    void saveLink(Link link);

    Optional<Link> findByShortLink(String shortLink);

    Optional<Link> findByLongLinkAndUserId(String longLink, UUID userId);

    void deleteExpiredLinks();

    void decrementClicksLeft(UUID linkId);

    Collection<Link> findAll();

}
