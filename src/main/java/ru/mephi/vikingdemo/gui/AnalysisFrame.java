package ru.mephi.vikingdemo.gui;

import ru.mephi.vikingdemo.controller.VikingAnalysisListener;
import ru.mephi.vikingdemo.model.BeardStyle;
import ru.mephi.vikingdemo.model.HairColor;

import javax.swing.*;
import java.awt.*;

public class AnalysisFrame extends JFrame {
    private final VikingAnalysisListener listener;

    public AnalysisFrame(VikingAnalysisListener listener) {
        this.listener = listener;

        setTitle("Анализ викингов");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 500);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(0, 1, 5, 5));

        JLabel titleLabel = new JLabel("Анализ викингов", SwingConstants.CENTER);
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 14f));
        add(titleLabel);

        add(createButton("Количество викингов старше 30", e -> listener.showAgeCount("greater", 30, null)));
        add(createButton("Количество викингов младше 25", e -> listener.showAgeCount("less", 25, null)));
        add(createButton("Количество викингов 20-35 лет", e -> listener.showAgeCount("range", 20, 35)));

        add(createButton("Блондины с длинной бородой", e -> listener.showBeardAndHairCount(BeardStyle.LONG, HairColor.Blond)));
        add(createButton("Шатены с короткой бородой", e -> listener.showBeardAndHairCount(BeardStyle.SHORT, HairColor.Brown)));

        add(createButton("Викинги с 1 или 2 топорами", e -> listener.showAxeCount()));

        add(createButton("Случайный высокий викинг (>180 см)", e -> listener.showRandomTallViking()));
        add(createButton("Викинги с легендарным снаряжением", e -> listener.showLegendaryVikings()));
        add(createButton("Рыжебородые викинги по возрасту", e -> listener.showRedBeardedSorted()));

        pack();
    }

    private JButton createButton(String text, java.awt.event.ActionListener action) {
        JButton button = new JButton(text);
        button.addActionListener(action);
        return button;
    }
}