package com.lotrel.ltserwer.reportsModule.service.csv;

import com.lotrel.ltserwer.reportsModule.domain.csv.CsvSprintResponse;
import com.lotrel.ltserwer.reportsModule.domain.report.ReportTaskResponse;
import com.lotrel.ltserwer.reportsModule.infrastructure.exception.FileNotCreatedException;
import com.lotrel.ltserwer.reportsModule.service.CsvExcelService;
import io.jsonwebtoken.lang.Strings;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class CsvService {

    private final CsvExcelService excelService;

    private static final String PATH_SPRINT = Path.of("." + File.separator + "files") + File.separator + "report" + File.separator + "sprint" + File.separator;

    public Map<String, String> createCsv(CsvSprintResponse response) throws IOException {
        final List<String[]> data = generateData(response);

        final String name = "Sprint_" + response.getDescription().replace(' ', '-') + "_" + OffsetDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy_HH.mm.ss"));
        final String path = PATH_SPRINT + name + ".csv";
        File csvFile = new File(path);

        if (!csvFile.exists()) {
            Files.createDirectories(Paths.get(PATH_SPRINT));
            csvFile = new File(path);
        }

        try (PrintWriter printWriter = new PrintWriter(csvFile, StandardCharsets.UTF_8)) {
            data.stream()
                    .map(this::convertToCsv)
                    .forEach(printWriter::println);
        }

        if (!csvFile.exists()) {
            throw new FileNotCreatedException(name);
        }

        excelService.createExcelFile();
        return Map.of(name, path);
    }

    private List<String[]> generateData(CsvSprintResponse response) {
        final String[] headerSprint = {"ID", "NAZWA SPRINTU", "DATA ROZPOCZĘCIA", "DATA ZAKOŃCZENIA", "AKTYWNY"};
        final String[] dataSprint = {String.valueOf(response.getId()), response.getDescription(), String.valueOf(response.getBeginDate()), String.valueOf(response.getEndDate()), mapActiveValue(response.isActive())};
        final String[] headerTasks = {"KOD ZADANIA", "NAZWA ZADANIA", "PRZYPISANY", "TYP", "STATUS"};
        final List<String[]> dataTasks = new ArrayList<>();
        for (ReportTaskResponse task: response.getTasks()) {
            dataTasks.add(new String[]{task.getTaskId(), task.getTaskName(), task.getAssignee(), task.getUrgencyType().name(), task.getCurrentWorkflow().name()});
        }

        final List<String[]> data = new ArrayList<>(List.of(headerSprint, dataSprint, Strings.toStringArray(Collections.emptyList()), headerTasks));
        data.addAll(dataTasks);

        return data;
    }

    private static String mapActiveValue(boolean active) {
        if (active) return "TAK";
        else return "NIE";
    }

    private String convertToCsv(String[] data) {
        return Stream.of(data)
                .map(this::escapeSpecialCharacters)
                .collect(Collectors.joining(","));
    }

    private String escapeSpecialCharacters(String data) {
        String escapedData = data.replaceAll("\\R", " ");
        if (data.contains(",") || data.contains("\"") || data.contains("'")) {
            data = data.replace("\"", "\"\"");
            escapedData = "\"" + data + "\"";
        }
        return escapedData;
    }
}
