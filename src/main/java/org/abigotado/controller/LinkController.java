package org.abigotado.controller;

import lombok.RequiredArgsConstructor;
import org.abigotado.entity.Link;
import org.abigotado.exceptions.LinkAlreadyExistsException;
import org.abigotado.service.LinkService;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Scanner;
import java.util.UUID;

@RequiredArgsConstructor
public class LinkController {

    private final LinkService linkService;
    private UUID userId;

    public void start() {
        initializeUser();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Добро пожаловать в сервис сокращения ссылок!");

        while (true) {
            System.out.println("\nВыберите действие:");
            System.out.println("1. Создать короткую ссылку");
            System.out.println("2. Ввести короткий URL");
            System.out.println("3. Выйти");
            System.out.print("Введите номер действия: ");

            String input = scanner.nextLine().trim();

            if (!input.matches("\\d+")) {
                System.out.println("Пожалуйста, введите корректное число.");
                continue;
            }

            int choice = Integer.parseInt(input);

            switch (choice) {
                case 1 -> createShortLink(scanner);
                case 2 -> redirectToLink(scanner);
                case 3 -> {
                    System.out.println("Спасибо за использование сервиса!");
                    return;
                }
                default -> System.out.println("Некорректный выбор. Попробуйте снова.");
            }
        }
    }

    private void initializeUser() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("У вас уже есть ID? (да/нет): ");
        String response = scanner.nextLine().trim().toLowerCase();

        if (response.equalsIgnoreCase("да")) {
            System.out.print("Введите ваш ID: ");
            try {
                UUID inputId = UUID.fromString(scanner.nextLine().trim());
                if (userExists(inputId)) {
                    userId = inputId;
                    System.out.println("Ваш ID успешно найден: " + userId);
                } else {
                    System.out.println("Пользователь с таким ID не найден. Будет сгенерирован новый ID.");
                    generateNewUserId();
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Неверный формат ID. Будет сгенерирован новый.");
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
        System.out.println("Ваш новый ID: " + userId);
    }

    private void createShortLink(Scanner scanner) {
        System.out.print("Введите длинный URL: ");
        String longLink = scanner.nextLine();
        System.out.print("Введите количество доступных переходов: ");
        int clicksLeft = scanner.nextInt();
        scanner.nextLine();

        try {
            Link link = linkService.createShortLink(longLink, userId, clicksLeft, LocalDateTime.now().plusDays(1));
            System.out.println("Короткая ссылка создана: " + link.getShortLink());
        } catch (LinkAlreadyExistsException e) {
            System.out.println("Ссылка уже существует для данного пользователя: " + e.getExistingShortLink());
        }
    }

    private void redirectToLink(Scanner scanner) {
        System.out.print("Введите короткий URL: ");
        String shortLink = scanner.nextLine();

        try {
            Optional<URI> uri = linkService.getLongLinkUri(shortLink);

            if (uri.isPresent()) {
                if (Desktop.isDesktopSupported()) {
                    openUrlInBackground(uri.get());
                    System.out.println("Перенаправление на: " + uri.get());
                } else {
                    System.out.println("Открытие ссылок не поддерживается на этой системе.");
                }
            }
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Некорректный URL: " + e.getMessage());
        }
    }

    private void openUrlInBackground(URI uri) {
        new Thread(() -> {
            try {
                Desktop.getDesktop().browse(uri);
            } catch (IOException e) {
                System.out.println("Ошибка при открытии ссылки: " + e.getMessage());
            }
        }).start();
    }
}