package ru.mephi.vikingdemo.service;

import org.springframework.stereotype.Service;
import ru.mephi.vikingdemo.model.BeardStyle;
import ru.mephi.vikingdemo.model.EquipmentItem;
import ru.mephi.vikingdemo.model.HairColor;
import ru.mephi.vikingdemo.model.Viking;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class VikingService {
    // каждый раз при изменении создаётся новая копия списка 
    private final CopyOnWriteArrayList<Viking> vikings = new CopyOnWriteArrayList<>();
    private final VikingFactory vikingFactory;
    @Autowired
    public VikingService(VikingFactory vikingFactory) {
        this.vikingFactory = vikingFactory;
    }
    
    public List<Viking> findAll() {
        return List.copyOf(vikings);
    }

    public Viking createRandomViking() {
        Viking viking = vikingFactory.createRandomViking();

        vikings.add(viking);
        return viking;
    }

    public Viking createViking(String name, int age, int heightCm, HairColor hairColor, BeardStyle beardStyle, List<EquipmentItem> equipment) {
        System.out.println(0);
        Viking viking = new Viking(name, age, heightCm, hairColor, beardStyle, equipment);
        System.out.println(1);
        vikings.add(viking);
        return viking;
    }
}
