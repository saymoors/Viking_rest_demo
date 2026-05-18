package ru.mephi.vikingdemo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import ru.mephi.vikingdemo.model.BeardStyle;
import ru.mephi.vikingdemo.model.HairColor;
import ru.mephi.vikingdemo.model.Viking;
import ru.mephi.vikingdemo.service.VikingAnalysisService;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController
@RequestMapping("/api/vikings/analysis")
@Tag(name = "Анализ викингов", description = "Операции анализа викингов через лямбда-функции")
public class VikingAnalysisController {

    private final VikingAnalysisService analysisService;

    public VikingAnalysisController(VikingAnalysisService analysisService) {
        this.analysisService = analysisService;
    }

    @GetMapping("/count/age")
    @Operation(summary = "Подсчет викингов по условию возраста", operationId = "countByAge")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Результат подсчета")
    })
    public Map<String, Object> countByAge(
            @RequestParam String condition,
            @RequestParam int value1,
            @RequestParam(required = false) Integer value2) {
        long count = analysisService.countByAgeCondition(condition, value1, value2);
        String conditionText = switch (condition.toLowerCase()) {
            case "greater" -> "старше " + value1;
            case "less" -> "младше " + value1;
            case "range" -> "в диапазоне " + value1 + "-" + value2;
            case "outside" -> "вне диапазона " + value1 + "-" + value2;
            default -> condition;
        };
        return Map.of(
                "condition", conditionText,
                "count", count
        );
    }

    @GetMapping("/count/beard-and-hair")
    @Operation(summary = "Подсчет викингов по форме бороды и цвету волос", operationId = "countByBeardAndHair")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Результат подсчета")
    })
    public Map<String, Object> countByBeardAndHair(
            @RequestParam BeardStyle beardStyle,
            @RequestParam HairColor hairColor) {
        long count = analysisService.countByBeardAndHair(beardStyle, hairColor);
        return Map.of(
                "beardStyle", beardStyle,
                "hairColor", hairColor,
                "count", count
        );
    }

    @GetMapping("/count/axes-one-or-two")
    @Operation(summary = "Подсчет викингов с 1 или 2 топорами", operationId = "countByOneOrTwoAxes")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Результат подсчета")
    })
    public Map<String, Object> countByAxes() {
        long count = analysisService.countByAxeCount();
        return Map.of(
                "description", "Викинги с 1 или 2 топорами",
                "count", count
        );
    }

    @GetMapping("/random-tall")
    @Operation(summary = "Получить случайного викинга ростом выше 180 см", operationId = "getRandomTallViking")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Найден случайный высокий викинг"),
            @ApiResponse(responseCode = "404", description = "Высокие викинги не найдены")
    })
    public Viking getRandomTallViking() {
        return analysisService.getRandomTallViking()
                .orElseThrow(() -> new NoSuchElementException("Высокие викинги не найдены"));
    }

    @GetMapping("/legendary")
    @Operation(summary = "Получить всех викингов с легендарным снаряжением", operationId = "getLegendaryVikings")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список викингов с легендарным снаряжением")
    })
    public List<Viking> getLegendaryVikings() {
        return analysisService.getVikingsWithLegendaryEquipment();
    }

    @GetMapping("/red-beard-sorted")
    @Operation(summary = "Получить рыжебородых викингов, отсортированных по возрасту", operationId = "getRedBeardedSorted")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Отсортированный список рыжебородых викингов")
    })
    public List<Viking> getRedBeardedSorted() {
        return analysisService.getRedBeardedVikingsSortedByAge();
    }

    @GetMapping("/ids/max")
    @Operation(summary = "Найти максимальный ID викинга", operationId = "getMaxId")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Максимальный ID найден")
    })
    public Map<String, Object> getMaxId() {
        List<Viking> vikings = analysisService.getAllVikings();
        int maxId = vikings.isEmpty() ? -1 : vikings.size() - 1;
        return Map.of(
                "maxId", maxId,
                "totalVikings", vikings.size()
        );
    }

    @GetMapping("/ids/even")
    @Operation(summary = "Получить всех викингов с четными ID", operationId = "getEvenIds")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список викингов с четными ID")
    })
    public Map<String, Object> getEvenIds() {
        List<Viking> vikings = analysisService.getAllVikings();

        List<Map<String, Object>> evenVikings = IntStream.range(0, vikings.size())
                .filter(id -> id % 2 == 0)
                .mapToObj(id -> {
                    Viking v = vikings.get(id);
                    return Map.<String, Object>of(
                            "id", id,
                            "name", v.name(),
                            "age", v.age()
                    );
                })
                .collect(Collectors.toList());

        return Map.of(
                "evenVikings", evenVikings,
                "count", evenVikings.size()
        );
    }
}