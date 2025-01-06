package org.abigotado.links.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.abigotado.config.Messages;
import org.abigotado.links.entity.Link;
import org.abigotado.exceptions.LinkAlreadyExistsException;
import org.abigotado.links.presentation.MenuOptions;
import org.abigotado.links.service.LinkService;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.util.Optional;
import java.util.Scanner;
import java.util.UUID;

@RequiredArgsConstructor
public class LinkCliController {

    private final LinkService linkService;
    private UUID userId;

    public void start() {
        initializeUser();
        Scanner scanner = new Scanner(System.in);
        System.out.println(Messages.WELCOME_MESSAGE);

        while (true) {
            showMenu();
            System.out.print(Messages.MENU_PROMPT);

            String input = scanner.nextLine().trim();

            if (!input.matches("\\d+")) {
                System.out.println(Messages.INVALID_INPUT);
                continue;
            }

            int choice = Integer.parseInt(input) - 1;
            MenuOptions selectedOption = MenuOptions.fromIndex(choice);

            if (selectedOption == null) {
                System.out.println(Messages.INVALID_INPUT);
                continue;
            }

            switch (selectedOption) {
                case CREATE_SHORT_LINK -> createShortLink(scanner);
                case ENTER_SHORT_URL -> redirectToLink(scanner);
                case EXIT -> {
                    System.out.println(Messages.GOODBYE_MESSAGE);
                    return;
                }
            }
        }
    }

    private void showMenu() {
        System.out.println("\n" + Messages.MENU_HEADER);
        MenuOptions[] options = MenuOptions.values();
        for (int i = 0; i < options.length; i++) {
            System.out.println((i + 1) + ". " + options[i].getDescription());
        }
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

    private boolean userExists(UUID inputId) {
        return linkService.userHasLinks(inputId);
    }

    private void generateNewUserId() {
        userId = UUID.randomUUID();
        System.out.println(Messages.NEW_USER_ID + userId);
    }

    private void createShortLink(Scanner scanner) {
        System.out.print(Messages.ENTER_URL);
        String longLink = scanner.nextLine();

        System.out.print(Messages.ENTER_CLICKS);
        String clicksInput = scanner.nextLine();
        Integer clicksLeft = clicksInput.isEmpty() ? null : Integer.parseInt(clicksInput);

        try {
            Link link = linkService.createShortLink(longLink, userId, clicksLeft, null);
            System.out.println(Messages.LINK_CREATED + link.getShortLink());
        } catch (LinkAlreadyExistsException e) {
            System.out.println(e.getMessage());
        }
    }

    private void redirectToLink(Scanner scanner) {
        System.out.print(Messages.ENTER_SHORT_URL);
        String shortLink = scanner.nextLine();

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
        new Thread(() -> {
            try {
                Desktop.getDesktop().browse(uri);
            } catch (IOException e) {
                System.out.println(Messages.LINK_OPEN_ERROR + e.getMessage());
            }
        }).start();
    }
}