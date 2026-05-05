package ru.mephi.vikingdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import ru.mephi.vikingdemo.gui.VikingDesktopFrame;

import javax.swing.SwingUtilities;
import ru.mephi.vikingdemo.controller.VikingAnalysisListener;
import ru.mephi.vikingdemo.controller.VikingListener;
import ru.mephi.vikingdemo.service.VikingService;

@SpringBootApplication
public class VikingDemoApplication {

    public static void main(String[] args) {
        System.out.println(java.awt.GraphicsEnvironment.isHeadless());
        SpringApplication app = new SpringApplication(VikingDemoApplication.class);
        app.setHeadless(false);

        ConfigurableApplicationContext context = app.run(args);

        VikingService vikingService = context.getBean(VikingService.class);
        VikingListener vikingListener = context.getBean(VikingListener.class);
        VikingAnalysisListener analysisListener = context.getBean(VikingAnalysisListener.class);

        SwingUtilities.invokeLater(() -> {
            VikingDesktopFrame frame = new VikingDesktopFrame(vikingService, vikingListener, analysisListener);
            vikingListener.setGui(frame);
            analysisListener.setGui(frame);
            frame.setVisible(true);
        });
    }
}