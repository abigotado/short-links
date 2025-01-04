package org.abigotado;

import org.abigotado.repository.LinkRepository;
import org.abigotado.repository.impl.LinkRepositoryImpl;
import org.abigotado.service.LinkService;
import org.abigotado.controller.LinkController;

public class Main {
    public static void main(String[] args) {
        LinkRepository linkRepository = new LinkRepositoryImpl();
        LinkService linkService = new LinkService(linkRepository);
        LinkController linkController = new LinkController(linkService);

        linkController.start();

    }
}