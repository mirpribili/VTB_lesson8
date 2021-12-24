package com.geekbrains.lesson8.stream_api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class StreamApp {
    static class Person {
        enum Position {
            ENGINEER, DIRECTOR, MANAGER;
        }
        private String name;
        private int age;
        private Position position;
        public Person (String name, int age, Position position) {
            this .name = name;
            this .age = age;
            this .position = position;
        }
    }

    public static void main(String[] args) {
        streamSimpleTask();



    }
    private static void streamSimpleTask () {
        List<Person> persons = new ArrayList<>(Arrays.asList(
                new Person( "Bob1" , 35 , Person.Position.MANAGER),
                new Person( "Bob2" , 44 , Person.Position.DIRECTOR),
                new Person( "Bob3" , 25 , Person.Position.ENGINEER),
                new Person( "Bob4" , 42 , Person.Position.ENGINEER),
                new Person( "Bob5" , 55 , Person.Position.MANAGER),
                new Person( "Bob6" , 19 , Person.Position.MANAGER),
                new Person( "Bob7" , 33 , Person.Position.ENGINEER),
                new Person( "Bob8" , 37 , Person.Position.MANAGER)
        ));
        // задача1 найти всех инженеров.
        List<Person> engineers = new ArrayList<>();
        for ( Person o : persons){
            if(o.position == Person.Position.ENGINEER){
                engineers.add(o);
            }
        }
        // задача2 отсортировать найденных по возрасту.
        engineers.sort(new Comparator<Person>() {
            @Override
            public int compare(Person o1, Person o2) {
                return o1.age - o2.age;
            }
        });
        // задача3 найти имена этих инженеров.
        List<String> engineersNames = new ArrayList<>();
        for (Person o: engineers){
            engineersNames.add(o.name);
        }
        System.out.println(engineersNames);

        // КОРОТКИЙ ПУТЬ

        // Stream api  позволяет по цепочки обрабатывать наборы данных
        List<String> engineersNamesShortPath = persons.stream()
                // заявляем что преобразуем список сотрудников в ПОТОК ДАННЫХ через .stream()
                .filter(person -> person.position == Person.Position.ENGINEER)
                // модифицируем поток (фильтрацией)
                .sorted((o1, o2) -> o1.age - o2.age)
                // сортируем
                .map((Function<Person, String>) person -> person.name)
                // преобразовываем поток типов Сотрудник в поток типа Строка
                .collect(Collectors.toList());
                // собираем поток строк Имен в список
        System.out.println(engineersNamesShortPath);
    }
}