package org.example.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.model.Person;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.inspector.TagInspector;

import java.io.InputStream;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api")
public class Controller {
    @GetMapping("/hello")
    public ResponseEntity<Person> hello() {
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
}