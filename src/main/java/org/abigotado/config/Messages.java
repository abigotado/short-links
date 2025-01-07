package org.abigotado.config;

public class Messages {

    // Общие сообщения
    public static final String WELCOME_MESSAGE = "Добро пожаловать в сервис сокращения ссылок!";
    public static final String GOODBYE_MESSAGE = "Спасибо за использование сервиса!";
    public static final String INVALID_INPUT = "Пожалуйста, введите корректное число.";
    public static final String INVALID_ID_FORMAT = "Неверный формат ID. Будет сгенерирован новый.";
    public static final String USER_NOT_FOUND = "Пользователь с таким ID не найден. Будет сгенерирован новый ID.";
    public static final String USER_ID_FOUND = "Ваш ID успешно найден: ";
    public static final String NEW_USER_ID = "Ваш новый ID: ";

    // Меню
    public static final String MENU_HEADER = "Выберите действие:";
    public static final String CREATE_SHORT_URL_OPTION = "Создать короткую ссылку";
    public static final String ENTER_SHORT_URL_OPTION = "Ввести короткий URL";
    public static final String CHANGE_USER_OPTION = "Сменить пользователя";
    public static final String EXIT_OPTION = "Выйти";
    public static final String MENU_PROMPT = "Введите номер действия: ";
    public static final String JSON_LOAD_ERROR = "Не удалось загрузить ссылки из файла JSON.";
    public static final String JSON_SAVE_ERROR = "Не удалось сохранить ссылки в файл JSON.";
    public static final String RETURN_TO_MENU_COMMAND = "0";
    public static final String RETURN_TO_MENU_MESSAGE = "В любом месте приложения введите '" + RETURN_TO_MENU_COMMAND + "' для возврата в главное меню.";

    // Пользовательские сообщения
    public static final String ENTER_URL = "Введите длинный URL: ";
    public static final String ENTER_CLICKS = "Введите количество доступных переходов (или нажмите Enter для значения по умолчанию): ";
    public static final String LINK_ALREADY_EXISTS = "Ссылка уже существует для данного пользователя: ";
    public static final String LINK_CREATED = "Короткая ссылка создана: ";
    public static final String ENTER_SHORT_URL = "Введите короткий URL: ";
    public static final String LINK_REDIRECT = "Перенаправление на: ";
    public static final String UNSUPPORTED_SYSTEM = "Открытие ссылок не поддерживается на этой системе.";
    public static final String LINK_UNAVAILABLE = "Ссылка недоступна.";
    public static final String LINK_OPEN_ERROR = "Ошибка при открытии ссылки: ";
    public static final String LINK_EXPIRED = "Срок действия ссылки истёк.";
    public static final String LINK_LIMIT_EXCEEDED = "Лимит переходов по ссылке исчерпан.";
    public static final String INVALID_URL_FORMAT = "Некорректный формат URL: ";
    public static final String INVALID_HASH_FORMAT = "Ошибка генерации хеша: SHA-256 не поддерживается";

    // Инициализация пользователя
    public static final String ASK_USER_ID = "У вас уже есть ID? (да/нет): ";
    public static final String ENTER_USER_ID = "Введите ваш ID: ";

    private Messages() {}
}