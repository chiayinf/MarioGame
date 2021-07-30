package com.chiayinfan.game.Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class MyTextInputListener implements Input.TextInputListener {
    private String name;

    @Override
    public void input(String text) {
        System.out.println(text);
        name = text;
    }

    @Override
    public void canceled() {
        MyTextInputListener listenerz = new MyTextInputListener();
        Gdx.input.getTextInput(listenerz, "Enter your name", "...", "");
    }

    public String getText() {
        return name;
    }
}
