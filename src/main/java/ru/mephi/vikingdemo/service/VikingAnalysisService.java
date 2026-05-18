package ru.mephi.vikingdemo.service;

import org.springframework.stereotype.Service;
import ru.mephi.vikingdemo.model.BeardStyle;
import ru.mephi.vikingdemo.model.HairColor;
import ru.mephi.vikingdemo.model.Viking;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class VikingAnalysisService {

    private final VikingService vikingService;

    public VikingAnalysisService(VikingService vikingService) {
        this.vikingService = vikingService;
    }

    public long countByAgeCondition(String condition, int value1, Integer value2) {
        List<Viking> vikings = vikingService.findAll();

        Predicate<Viking> agePredicate = switch (condition.toLowerCase()) {
            case "greater" -> v -> v.age() > value1;
            case "less" -> v -> v.age() < value1;
            case "range" -> v -> v.age() >= value1 && v.age() <= (value2 != null ? value2 : value1);
            case "outside" -> v -> v.age() < value1 || v.age() > (value2 != null ? value2 : value1);
            default -> v -> true;
        };

        return vikings.stream().filter(agePredicate).count();
    }

    public long countByBeardAndHair(BeardStyle beardStyle, HairColor hairColor) {
        List<Viking> vikings = vikingService.findAll();

        Predicate<Viking> beardAndHairPredicate = v ->
                v.beardStyle() == beardStyle && v.hairColor() == hairColor;

        return vikings.stream().filter(beardAndHairPredicate).count();
    }

    public long countByAxeCount() {
        List<Viking> vikings = vikingService.findAll();

        return vikings.stream()
                .filter(v -> {
                    long axeCount = v.equipment().stream()
                            .filter(e -> e.name().equalsIgnoreCase("Axe"))
                            .count();
                    return axeCount == 1 || axeCount == 2;
                })
                .count();
    }

    public Optional<Viking> getRandomTallViking() {
        List<Viking> vikings = vikingService.findAll();

        List<Viking> tallVikings = vikings.stream()
                .filter(v -> v.heightCm() > 180)
                .collect(Collectors.toList());

        if (tallVikings.isEmpty()) {
            return Optional.empty();
        }

        Random random = new Random();
        return Optional.of(tallVikings.get(random.nextInt(tallVikings.size())));
    }

    public List<Viking> getVikingsWithLegendaryEquipment() {
        List<Viking> vikings = vikingService.findAll();

        Predicate<Viking> legendaryPredicate = v ->
                v.equipment().stream()
                        .anyMatch(e -> "Legendary".equalsIgnoreCase(e.quality()));

        return vikings.stream()
                .filter(legendaryPredicate)
                .collect(Collectors.toList());
    }

    public List<Viking> getRedBeardedVikingsSortedByAge() {
        List<Viking> vikings = vikingService.findAll();

        return vikings.stream()
                .filter(v -> v.hairColor() == HairColor.Red)
                .sorted(Comparator.comparingInt(Viking::age))
                .collect(Collectors.toList());
    }

    public List<Viking> getAllVikings() {
        return vikingService.findAll();
    }
}