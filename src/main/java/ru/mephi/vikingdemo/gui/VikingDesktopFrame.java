package ru.mephi.vikingdemo.gui;

import ru.mephi.vikingdemo.controller.VikingAnalysisListener;
import ru.mephi.vikingdemo.controller.VikingListener;
import ru.mephi.vikingdemo.model.Viking;
import ru.mephi.vikingdemo.service.VikingService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class VikingDesktopFrame extends JFrame {

    private final VikingService vikingService;
    private final VikingListener listener;
    private final VikingAnalysisListener analysisListener;
    private final VikingTableModel tableModel = new VikingTableModel();

    public VikingDesktopFrame(VikingService vikingService, VikingListener listener, VikingAnalysisListener analysisListener) {
        this.vikingService = vikingService;
        this.listener = listener;
        this.analysisListener = analysisListener;

        setTitle("Viking Demo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(new Dimension(1000, 500));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JLabel header = new JLabel("Список викингов", SwingConstants.CENTER);
        header.setFont(header.getFont().deriveFont(Font.BOLD, 18f));
        add(header, BorderLayout.NORTH);

        JTable vikingTable = new JTable(tableModel);
        vikingTable.setRowHeight(28);
        add(new JScrollPane(vikingTable), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout());

        JButton createButton = new JButton("Создать случайного викинга");
        createButton.addActionListener(event -> onCreateViking());
        bottomPanel.add(createButton);

        JButton generateButton = new JButton("Массовая генерация");
        generateButton.addActionListener(event -> onGenerateMultiple());
        bottomPanel.add(generateButton);

        JButton analysisButton = new JButton("Анализ викингов");
        analysisButton.addActionListener(event -> openAnalysisFrame());
        bottomPanel.add(analysisButton);

        JButton idOperationsButton = new JButton("Операции с ID");
        idOperationsButton.addActionListener(event -> openIdOperationsDialog());
        bottomPanel.add(idOperationsButton);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void onCreateViking() {
        Viking viking = vikingService.createRandomViking();
        tableModel.addViking(viking);
    }

    private void onGenerateMultiple() {
        String input = JOptionPane.showInputDialog(this,
                "Введите количество викингов для генерации:", "10");
        if (input != null && !input.trim().isEmpty()) {
            try {
                int count = Integer.parseInt(input.trim());
                if (count > 0) {
                    List<Viking> vikings = vikingService.createMultipleVikings(count);
                    listener.addMultipleToGui(vikings);
                    JOptionPane.showMessageDialog(this,
                            "Успешно сгенерировано " + count + " викингов!",
                            "Успех", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Введите положительное число!",
                            "Ошибка", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "Пожалуйста, введите корректное число!",
                        "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void openAnalysisFrame() {
        AnalysisFrame analysisFrame = new AnalysisFrame(analysisListener);
        analysisFrame.setVisible(true);
    }

    private void openIdOperationsDialog() {
        String[] options = {"Найти максимальный ID", "Показать четные ID"};
        int choice = JOptionPane.showOptionDialog(this,
                "Выберите операцию с ID викингов:",
                "Операции с ID",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);

        if (choice == 0) {
            analysisListener.showMaxId();
        } else if (choice == 1) {
            analysisListener.showEvenIds();
        }
    }

    public void addNewViking(Viking viking) {
        tableModel.addViking(viking);
    }

    public void removeOldViking(int index) {
        tableModel.removeViking(index);
    }

    public void updateOldViking(int index, Viking viking) {
        tableModel.updateViking(index, viking);
    }
}