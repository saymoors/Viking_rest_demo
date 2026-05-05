package ru.mephi.vikingdemo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import ru.mephi.vikingdemo.model.Viking;
import ru.mephi.vikingdemo.service.VikingService;

import java.util.List;

@RestController
@RequestMapping("/api/vikings")
@Tag(name = "Vikings", description = "Операции с викингами")
public class VikingController {

    private final VikingService vikingService;
    private final VikingListener vikingListener;

    public VikingController(VikingService vikingService, VikingListener vikingListener) {
        this.vikingService = vikingService;
        this.vikingListener = vikingListener;
    }

    @GetMapping
    @Operation(summary = "Получить список созданных викингов", operationId = "getAllVikings")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список успешно получен")
    })
    public List<Viking> getAllVikings() {
        System.out.println("GET /api/vikings called");
        return vikingService.findAll();
    }

    @GetMapping("/test")
    @Operation(summary = "Получить список тестовых викингов", operationId = "getTest")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список успешно получен")
    })
    public List<String> test() {
        System.out.println("GET /api/vikings/test called");
        return List.of("Ragnar", "Bjorn");
    }

    @PostMapping("/post")
    @Operation(summary = "Добавить случайного викинга", operationId = "addRandomViking")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Случайный викинг успешно добавлен")
    })
    public void addViking(){
        vikingListener.testAdd();
    }

    @PostMapping()
    @Operation(summary = "Добавить конкретного викинга", operationId = "addViking")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Викинг успешно добавлен"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные викинга")
    })
    public void addViking(@RequestBody Viking viking) {
        vikingListener.add(
                viking.name(),
                viking.age(),
                viking.heightCm(),
                viking.hairColor(),
                viking.beardStyle(),
                viking.equipment()
        );
    }

    @DeleteMapping()
    @Operation(summary = "Удалить викинга по индексу", operationId = "deleteViking")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Викинг успешно удалён"),
            @ApiResponse(responseCode = "404", description = "Викинг с указанным индексом не найден")
    })
    public void deleteViking(@RequestParam int index) {
        vikingListener.delete(index);
    }

    @PutMapping()
    @Operation(summary = "Обновить параметры викинга по индексу", operationId = "updateViking")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Викинг успешно обновлён"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные викинга"),
            @ApiResponse(responseCode = "404", description = "Викинг с указанным индексом не найден")
    })
    public void updateViking(@RequestParam int index, @RequestBody Viking viking) {
        vikingListener.update(index, viking);
    }

    @PostMapping("/generate")
    @Operation(summary = "Массовая генерация случайных викингов", operationId = "generateVikings")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Викинги успешно сгенерированы"),
            @ApiResponse(responseCode = "400", description = "Некорректное количество")
    })
    public List<Viking> generateVikings(@RequestParam int count) {
        List<Viking> vikings = vikingService.createMultipleVikings(count);
        vikingListener.addMultipleToGui(vikings);
        return vikings;
    }
}