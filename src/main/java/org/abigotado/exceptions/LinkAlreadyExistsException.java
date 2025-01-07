package org.abigotado.exceptions;

import lombok.Getter;
import org.abigotado.config.Messages;

@Getter
public class LinkAlreadyExistsException extends RuntimeException {
    private final String existingShortLink;

    public LinkAlreadyExistsException(String existingShortLink) {
        super(Messages.LINK_ALREADY_EXISTS + existingShortLink);
        this.existingShortLink = existingShortLink;
    }

}
