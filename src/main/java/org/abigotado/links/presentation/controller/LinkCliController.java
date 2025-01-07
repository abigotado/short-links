package org.abigotado.links.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.abigotado.config.Messages;
import org.abigotado.exceptions.LinkAlreadyExistsException;
import org.abigotado.links.entity.Link;
import org.abigotado.links.presentation.MenuOptions;
import org.abigotado.links.service.LinkService;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.util.*;

@RequiredArgsConstructor
public class LinkCliController {

    private final LinkService linkService;
    private UUID userId;

    public void start() {
        Scanner scanner = new Scanner(System.in);
        System.out.println(Messages.WELCOME_MESSAGE);

        while (true) {
            showMenu();
            MenuOptions selectedOption = getMenuSelection(scanner);

            if (selectedOption == null) {
                System.out.println(Messages.INVALID_INPUT);
                continue;
            }

            switch (selectedOption) {
                case CREATE_SHORT_LINK -> createShortLink(scanner);
                case ENTER_SHORT_URL -> redirectToLink(scanner);
                case CHANGE_USER -> changeUser();
                case EXIT -> {
                    System.out.println(Messages.GOODBYE_MESSAGE);
                    System.exit(0);
                }
            }
        }
    }

    private Map<Integer, MenuOptions> getAvailableMenuOptions() {
        Map<Integer, MenuOptions> menuMap = new LinkedHashMap<>();
        MenuOptions[] options = MenuOptions.values();
        int displayNumber = 1;

        for (MenuOptions option : options) {
            if (option == MenuOptions.CHANGE_USER && userId == null) {
                continue;
            }
            menuMap.put(displayNumber, option);
            displayNumber++;
        }

        return menuMap;
    }

    private void showMenu() {
        System.out.println("\n" + Messages.MENU_HEADER);
        getAvailableMenuOptions().forEach((number, option) -> System.out.println(number
                                                                                 + ". "
                                                                                 + option.getDescription()));
    }

    private MenuOptions getMenuSelection(Scanner scanner) {
        System.out.println(Messages.RETURN_TO_MENU_MESSAGE);
        System.out.print(Messages.MENU_PROMPT);

        String input = scanner.nextLine().trim();

        if (!input.matches("\\d+")) return null;

        int displayChoice = Integer.parseInt(input);
        return getAvailableMenuOptions().get(displayChoice);
    }

    private void initializeUser() {
        Scanner scanner = new Scanner(System.in);
        System.out.print(Messages.ASK_USER_ID);
        String response = scanner.nextLine().trim().toLowerCase();

        if (response.equalsIgnoreCase("да")) {
            System.out.print(Messages.ENTER_USER_ID);
            try {
                UUID inputId = UUID.fromString(scanner.nextLine().trim());
                if (userExists(inputId)) {
                    userId = inputId;
                    System.out.println(Messages.USER_ID_FOUND + userId);
                } else {
                    System.out.println(Messages.USER_NOT_FOUND);
                    generateNewUserId();
                }
            } catch (IllegalArgumentException e) {
                System.out.println(Messages.INVALID_ID_FORMAT);
                generateNewUserId();
            }
        } else {
            generateNewUserId();
        }
    }

    private void ensureUserId() {
        if (userId == null) {
            initializeUser();
        }
    }

    private void changeUser() {
        userId = null;
        initializeUser();
    }

    private boolean userExists(UUID inputId) {
        return linkService.userHasLinks(inputId);
    }

    private void generateNewUserId() {
        userId = UUID.randomUUID();
        System.out.println(Messages.NEW_USER_ID + userId);
    }

    private void createShortLink(Scanner scanner) {
        String longLink = getInputWithReturnCheck(scanner, Messages.ENTER_URL);

        if (longLink == null) return;

        String clicksInput = getInputWithReturnCheck(scanner, Messages.ENTER_CLICKS);

        if (clicksInput == null) return;

        Integer clicksLeft = clicksInput.isEmpty() ? null : Integer.parseInt(clicksInput);

        ensureUserId();

        try {
            Link link = linkService.createShortLink(longLink, userId, clicksLeft, null);
            System.out.println(Messages.LINK_CREATED + link.getShortLink());
        } catch (LinkAlreadyExistsException e) {
            System.out.println(e.getMessage());
        }
    }

    private void redirectToLink(Scanner scanner) {
        String shortLink = getInputWithReturnCheck(scanner, Messages.ENTER_SHORT_URL);

        if (shortLink == null) return;

        ensureUserId();

        try {
            Optional<URI> uri = linkService.getLongLinkUri(shortLink);

            if (uri.isPresent()) {
                if (Desktop.isDesktopSupported()) {
                    openUrlInBackground(uri.get());
                    System.out.println(Messages.LINK_REDIRECT + uri.get());
                } else {
                    System.out.println(Messages.UNSUPPORTED_SYSTEM);
                }
            }
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println(Messages.LINK_UNAVAILABLE);
        }
    }

    private void openUrlInBackground(URI uri) {
        try {
            Desktop.getDesktop().browse(uri);
        } catch (IOException e) {
            System.out.println(Messages.LINK_OPEN_ERROR + e.getMessage());
        }
    }

    private String getInputWithReturnCheck(Scanner scanner, String prompt) {
        System.out.print(prompt);
        String input = scanner.nextLine().trim();

        if (input.equals(Messages.RETURN_TO_MENU_COMMAND)) {
            return null;
        }

        return input;
    }
}