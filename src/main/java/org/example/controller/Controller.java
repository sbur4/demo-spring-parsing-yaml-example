package org.example.controller;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.example.model.Person;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.inspector.TagInspector;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@RestController
@RequestMapping("/api")
public class Controller {
    @GetMapping("/hello")
    public ResponseEntity<Person> readYml() {
//        Yaml yaml = new Yaml();

        LoaderOptions loaderoptions = new LoaderOptions();
        TagInspector taginspector =
                tag -> tag.getClassName().equals(Person.class.getName());
        loaderoptions.setTagInspector(taginspector);
        Yaml yaml = new Yaml(new Constructor(Person.class, loaderoptions));

//        Representer customRepresenter = new Representer(new DumperOptions());
//        customRepresenter.addClassTag(Person.class, Tag.MAP);
//
//        Yaml yaml = new Yaml(new Constructor(Person.class, new LoaderOptions()),
//                customRepresenter);

        InputStream inputStream = Controller.class
                .getClassLoader()
                .getResourceAsStream("test.yaml");

//        Map<String, Object> obj = yaml.load(inputStream);

        Person person = yaml.load(inputStream);

        return new ResponseEntity<>(person, HttpStatus.OK);
    }

    @SneakyThrows
    @GetMapping("/buy")
    public ResponseEntity<Map<String, Object>> writeYml() {
        // Create a Map to hold the data
        Map<String, Object> data = new HashMap<>();
        data.put("name", "Alice");
        data.put("age", 30);
        data.put("city", "New York");
        data.put("isStudent", false);
        data.put("hobbies", Stream.of("run", "code", "travel").collect(Collectors.toList()));

        // Create a Yaml object with DumperOptions for formatting
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setPrettyFlow(true);
        Yaml yaml = new Yaml(options);

        // Write the data to a YAML file
        Path filePath = Path.of("output.yaml");
        String yamlString = yaml.dump(data);
        Files.writeString(filePath, yamlString, StandardOpenOption.CREATE, StandardOpenOption.WRITE);

        return ResponseEntity.ok(data);
    }
}