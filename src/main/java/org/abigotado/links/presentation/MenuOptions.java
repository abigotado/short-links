package org.abigotado.links.presentation;

import lombok.Getter;
import org.abigotado.config.Messages;

@Getter
public enum MenuOptions {
    CREATE_SHORT_LINK(Messages.CREATE_SHORT_URL_OPTION),
    ENTER_SHORT_URL(Messages.ENTER_SHORT_URL_OPTION),
    EXIT(Messages.EXIT_OPTION);

    private final String description;

    MenuOptions(String description) {
        this.description = description;
    }

    public static MenuOptions fromIndex(int index) {
        MenuOptions[] options = values();
        if (index >= 0 && index < options.length) {
            return options[index];
        }
        return null;
    }
}
