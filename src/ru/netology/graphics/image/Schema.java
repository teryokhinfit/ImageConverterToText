package ru.netology.graphics.image;

public class Schema extends Object implements TextColorSchema {

    private char[] chars;

    public Schema() {
        char[] chars = {'█', '▓', '▒', '░', '▨', '□', '◌', '◜'};
        this.chars = chars;
    }

    @Override
    public char convert(int color) {
        return chars[color / 32];
    }
}
