package com.werewolf.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class GameController {

    @RequestMapping(value = "/game")
    public String getGame() {
        return "game";
    }
}
