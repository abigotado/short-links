package org.abigotado.links.presentation;

import lombok.Getter;
import org.abigotado.config.Messages;

@Getter
public enum MenuOptions {
    CREATE_SHORT_LINK(Messages.CREATE_SHORT_URL_OPTION),
    ENTER_SHORT_URL(Messages.ENTER_SHORT_URL_OPTION),
    CHANGE_USER(Messages.CHANGE_USER_OPTION),
    EXIT(Messages.EXIT_OPTION);

    private final String description;

    MenuOptions(String description) {
        this.description = description;
    }
}
