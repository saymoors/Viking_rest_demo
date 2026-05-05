package ru.mephi.vikingdemo.service;

import org.springframework.stereotype.Service;
import ru.mephi.vikingdemo.model.BeardStyle;
import ru.mephi.vikingdemo.model.EquipmentItem;
import ru.mephi.vikingdemo.model.HairColor;
import ru.mephi.vikingdemo.model.Viking;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;

@Service
public class VikingService {
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

    public List<Viking> createMultipleVikings(int count) {
        if (count <= 0) {
            throw new IllegalArgumentException("Количество должно быть положительным");
        }

        return IntStream.range(0, count)
                .mapToObj(i -> {
                    Viking viking = vikingFactory.createRandomViking();
                    vikings.add(viking);
                    return viking;
                })
                .collect(Collectors.toList());
    }

    public Viking createViking(String name, int age, int heightCm, HairColor hairColor, BeardStyle beardStyle, List<EquipmentItem> equipment) {
        Viking viking = new Viking(name, age, heightCm, hairColor, beardStyle, equipment);
        vikings.add(viking);
        return viking;
    }

    public int removeViking(int index) throws IndexOutOfBoundsException {
        if (index < 0 || index >= vikings.size()) {
            throw new IndexOutOfBoundsException("Такого викинга нет!");
        }
        vikings.remove(index);
        return index;
    }

    public Viking updateViking(int index, Viking viking) throws IndexOutOfBoundsException {
        if (index < 0 || index >= vikings.size()) {
            throw new IndexOutOfBoundsException("Такого викинга нет!");
        }
        vikings.set(index, viking);
        return viking;
    }
}