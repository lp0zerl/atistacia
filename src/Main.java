package com.gridnine.testing;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        System.out.println("🚀 МОДУЛЬ ФИЛЬТРАЦИИ АВИАПЕРЕЛЁТОВ");
        System.out.println();

        // Получаем тестовый набор перелётов
        List<Flight> allFlights = FlightBuilder.createFlights();

        // Создаем процессор фильтрации
        FlightFilterProcessor processor = new FlightFilterProcessor();

        // Отображаем все исходные перелёты
        FlightDisplay.displayFlights("ВСЕ ТЕСТОВЫЕ ПЕРЕЛЁТЫ", allFlights);

        // Применяем фильтры по одному
        applyIndividualFilters(processor, allFlights);

        // Применяем комбинированные фильтры
        applyCombinedFilters(processor, allFlights);

        System.out.println("✅ ФИЛЬТРАЦИЯ ЗАВЕРШЕНА");
    }

    private static void applyIndividualFilters(FlightFilterProcessor processor, List<Flight> allFlights) {
        System.out.println("ПРИМЕНЕНИЕ ФИЛЬТРОВ ПО ОТДЕЛЬНОСТИ:");
        System.out.println();

        // Фильтр 1: Вылет до текущего момента времени
        FlightFilter departureFilter = new DepartureBeforeNowFilter();
        FlightDisplay.displayFilterInfo(departureFilter);
        List<Flight> filtered1 = processor.filter(allFlights, departureFilter);
        FlightDisplay.displayFlights("После фильтрации - вылет до текущего момента", filtered1);

        // Фильтр 2: Сегменты с датой прилёта раньше даты вылета
        FlightFilter arrivalFilter = new ArrivalBeforeDepartureFilter();
        FlightDisplay.displayFilterInfo(arrivalFilter);
        List<Flight> filtered2 = processor.filter(allFlights, arrivalFilter);
        FlightDisplay.displayFlights("После фильтрации - прилёт раньше вылета", filtered2);

        // Фильтр 3: Общее время на земле превышает два часа
        FlightFilter groundTimeFilter = new ExcessiveGroundTimeFilter();
        FlightDisplay.displayFilterInfo(groundTimeFilter);
        List<Flight> filtered3 = processor.filter(allFlights, groundTimeFilter);
        FlightDisplay.displayFlights("После фильтрации - время на земле > 2 часов", filtered3);
    }

    private static void applyCombinedFilters(FlightFilterProcessor processor, List<Flight> allFlights) {
        System.out.println("ПРИМЕНЕНИЕ КОМБИНИРОВАННЫХ ФИЛЬТРОВ:");
        System.out.println();

        // Очищаем предыдущие фильтры и добавляем все три
        processor.clearFilters();
        processor.addFilter(new DepartureBeforeNowFilter())
                .addFilter(new ArrivalBeforeDepartureFilter())
                .addFilter(new ExcessiveGroundTimeFilter());

        System.out.printf("Активных фильтров: %d%n", processor.getFilterCount());
        List<Flight> combinedFiltered = processor.filter(allFlights);
        FlightDisplay.displayFlights("После комбинированной фильтрации (все правила)", combinedFiltered);
    }
}
