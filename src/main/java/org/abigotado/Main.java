package org.abigotado;

import org.abigotado.links.repository.LinkRepository;
import org.abigotado.links.repository.impl.LinkRepositoryImpl;
import org.abigotado.links.service.LinkService;
import org.abigotado.links.presentation.controller.LinkCliController;

public class Main {
    public static void main(String[] args) {
        LinkRepository linkRepository = new LinkRepositoryImpl();
        LinkService linkService = new LinkService(linkRepository);
        LinkCliController linkController = new LinkCliController(linkService);

        linkController.start();

    }
}