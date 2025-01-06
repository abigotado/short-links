package org.abigotado.repository;

import org.abigotado.entity.Link;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface LinkRepository {
    public void saveLink(Link link);
    public Optional<Link> findByShortLink(String shortLink);
    public Optional<Link> findByLongLinkAndUserId(String longLink, UUID userId);
    public void deleteExpiredLinks();
    public void decrementClicksLeft(UUID linkId);
    Collection<Link> findAll();

}
