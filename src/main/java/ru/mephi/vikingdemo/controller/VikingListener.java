/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ru.mephi.vikingdemo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.mephi.vikingdemo.gui.VikingDesktopFrame;
import ru.mephi.vikingdemo.model.BeardStyle;
import ru.mephi.vikingdemo.model.EquipmentItem;
import ru.mephi.vikingdemo.model.HairColor;
import ru.mephi.vikingdemo.model.Viking;
import ru.mephi.vikingdemo.service.VikingService;

import java.util.List;

/**
 *
 * @author test2023
 */
@Component
public class VikingListener {
    private VikingService service;
    private VikingDesktopFrame gui;

    @Autowired
    public VikingListener(VikingService service) {
        this.service = service;
    }
    
    public void setGui(VikingDesktopFrame gui){
        this.gui = gui;
    }

    void testAdd() {
        gui.addNewViking(service.createRandomViking());
    }

    void add(String name, int age, int heightCm, HairColor hairColor, BeardStyle beardStyle, List<EquipmentItem> equipment) {
        gui.addNewViking(service.createViking(name, age, heightCm, hairColor, beardStyle, equipment));
    }

    void delete(int index) {
        try {
            gui.removeOldViking(service.removeViking(index));
        } catch (IndexOutOfBoundsException exception) {
            System.out.println(exception.getMessage());
        }
    }

    void update(int index, Viking viking) {
        try {
            gui.updateOldViking(index, service.updateViking(index, viking));
        } catch (IndexOutOfBoundsException exception) {
            System.out.println(exception.getMessage());
        }
    }
}
