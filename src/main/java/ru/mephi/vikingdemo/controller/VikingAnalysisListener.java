package ru.mephi.vikingdemo.controller;

import org.springframework.stereotype.Component;
import ru.mephi.vikingdemo.gui.VikingDesktopFrame;
import ru.mephi.vikingdemo.model.BeardStyle;
import ru.mephi.vikingdemo.model.HairColor;
import ru.mephi.vikingdemo.model.Viking;
import ru.mephi.vikingdemo.service.VikingAnalysisService;

import javax.swing.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class VikingAnalysisListener {

    private final VikingAnalysisService analysisService;
    private VikingDesktopFrame gui;

    public VikingAnalysisListener(VikingAnalysisService analysisService) {
        this.analysisService = analysisService;
    }

    public void setGui(VikingDesktopFrame gui) {
        this.gui = gui;
    }

    public void showAgeCount(String condition, int value1, Integer value2) {
        long count = analysisService.countByAgeCondition(condition, value1, value2);
        String conditionText = switch (condition.toLowerCase()) {
            case "greater" -> "старше " + value1;
            case "less" -> "младше " + value1;
            case "range" -> "в диапазоне " + value1 + "-" + value2;
            case "outside" -> "вне диапазона " + value1 + "-" + value2;
            default -> condition;
        };
        JOptionPane.showMessageDialog(gui,
                "Количество викингов " + conditionText + ": " + count,
                "Анализ по возрасту", JOptionPane.INFORMATION_MESSAGE);
    }

    public void showBeardAndHairCount(BeardStyle beard, HairColor hair) {
        long count = analysisService.countByBeardAndHair(beard, hair);
        String beardText = getBeardStyleRussian(beard);
        String hairText = getHairColorRussian(hair);
        JOptionPane.showMessageDialog(gui,
                "Викингов с " + beardText + " бородой\nи " + hairText + " волосами: " + count,
                "Анализ бороды и волос", JOptionPane.INFORMATION_MESSAGE);
    }

    public void showAxeCount(int axeCount) {
        long count = analysisService.countByAxeCount(axeCount);
        String axeText = axeCount == 1 ? "1 топором" : axeCount + " топорами";
        JOptionPane.showMessageDialog(gui,
                "Викингов с " + axeText + ": " + count,
                "Анализ топоров", JOptionPane.INFORMATION_MESSAGE);
    }

    public void showRandomTallViking() {
        analysisService.getRandomTallViking().ifPresentOrElse(
                v -> JOptionPane.showMessageDialog(gui,
                        "Случайный высокий викинг:\n" +
                                v.name() + " (" + v.heightCm() + " см)",
                        "Высокий викинг", JOptionPane.INFORMATION_MESSAGE),
                () -> JOptionPane.showMessageDialog(gui,
                        "Высокие викинги не найдены!",
                        "Ошибка", JOptionPane.ERROR_MESSAGE)
        );
    }

    public void showLegendaryVikings() {
        List<Viking> vikings = analysisService.getVikingsWithLegendaryEquipment();
        if (vikings.isEmpty()) {
            JOptionPane.showMessageDialog(gui,
                    "Викинги с легендарным снаряжением не найдены!",
                    "Легендарные викинги", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        StringBuilder sb = new StringBuilder("Викинги с легендарным снаряжением:\n\n");
        vikings.forEach(v -> sb.append("• ").append(v.name())
                .append(" (").append(v.age()).append(" лет)\n"));
        JOptionPane.showMessageDialog(gui, sb.toString(),
                "Легендарные викинги (" + vikings.size() + ")", JOptionPane.INFORMATION_MESSAGE);
    }

    public void showRedBeardedSorted() {
        List<Viking> vikings = analysisService.getRedBeardedVikingsSortedByAge();
        if (vikings.isEmpty()) {
            JOptionPane.showMessageDialog(gui,
                    "Рыжебородые викинги не найдены!",
                    "Рыжебородые викинги", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        StringBuilder sb = new StringBuilder("Рыжебородые викинги (по возрасту):\n\n");
        vikings.forEach(v -> sb.append("• ").append(v.name())
                .append(" (").append(v.age()).append(" лет)\n"));
        JOptionPane.showMessageDialog(gui, sb.toString(),
                "Рыжебородые викинги (" + vikings.size() + ")", JOptionPane.INFORMATION_MESSAGE);
    }

    public void showMaxId() {
        List<Viking> vikings = analysisService.getAllVikings();
        if (vikings.isEmpty()) {
            JOptionPane.showMessageDialog(gui,
                    "Список викингов пуст!",
                    "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int maxId = vikings.size() - 1;
        JOptionPane.showMessageDialog(gui,
                "Максимальный ID: " + maxId + "\nВсего викингов: " + vikings.size(),
                "Максимальный ID", JOptionPane.INFORMATION_MESSAGE);
    }

    public void showEvenIds() {
        List<Viking> vikings = analysisService.getAllVikings();
        if (vikings.isEmpty()) {
            JOptionPane.showMessageDialog(gui,
                    "Список викингов пуст!",
                    "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<Integer> evenIds = IntStream.range(0, vikings.size())
                .filter(id -> id % 2 == 0)
                .boxed()
                .collect(Collectors.toList());

        StringBuilder sb = new StringBuilder("Четные ID викингов:\n\n");
        for (Integer id : evenIds) {
            Viking v = vikings.get(id);
            sb.append("• ID ").append(id).append(": ").append(v.name())
                    .append(" (").append(v.age()).append(" лет)\n");
        }

        JOptionPane.showMessageDialog(gui, sb.toString(),
                "Четные ID (" + evenIds.size() + ")", JOptionPane.INFORMATION_MESSAGE);
    }

    private String getBeardStyleRussian(BeardStyle style) {
        return switch (style) {
            case SHORT -> "короткой";
            case LONG -> "длинной";
            case BRAIDED -> "заплетенной";
            case FORKED -> "раздвоенной";
            case CLEAN_SHAVEN -> "бритой";
        };
    }

    private String getHairColorRussian(HairColor color) {
        return switch (color) {
            case Blond -> "светлыми";
            case Red -> "рыжими";
            case Brown -> "каштановыми";
            case Black -> "черными";
            case Gray -> "седыми";
        };
    }
}