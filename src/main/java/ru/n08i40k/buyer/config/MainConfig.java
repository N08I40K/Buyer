package ru.n08i40k.buyer.config;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MainConfig {
    String lang;

    public MainConfig() {
        lang = "ru-RU";
    }
}

