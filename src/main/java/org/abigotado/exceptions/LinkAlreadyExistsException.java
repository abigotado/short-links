package org.abigotado.exceptions;

public class LinkAlreadyExistsException extends RuntimeException {
    private final String existingShortLink;

    public LinkAlreadyExistsException(String existingShortLink) {
        super("Ссылка уже существует: " + existingShortLink);
        this.existingShortLink = existingShortLink;
    }

    public String getExistingShortLink() {
        return existingShortLink;
    }
}
