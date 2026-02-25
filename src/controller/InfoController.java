package ru.hogwarts.school.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Stream;

@RestController
public class InfoController {

    @Value("${server.port}")
    private String serverPort;

    @GetMapping("/port")
    public String getPort() {
        return serverPort;
    }

    // NEW FOR LESSON 4.5: параллельное вычисление суммы от 1 до 1_000_000
    @GetMapping("/sum")
    public Integer getParallelSum() {
        // Используем параллельный стрим для ускорения
        return Stream.iterate(1, a -> a + 1)
                .parallel()
                .limit(1_000_000)
                .reduce(0, Integer::sum);
    }
}