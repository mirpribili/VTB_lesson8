
package com.geekbrains.lesson8.stream_api;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

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

    public static boolean myOperation(int n, int coef){
        try {
            return n / coef < 10;
        }catch (ArithmeticException e){
            e.printStackTrace();
            return false;
        }
    }
    public static void main(String[] args) {
// обработка исключений
        int coef = 0;
        //Stream.of(1,2,3,4,5,6,7,8).filter(n -> {return n / coef < 10;}).collect(Collectors.toList());
        Stream.of(1,2,3,4,5,6,7,8).filter(n -> myOperation(n, coef)).collect(Collectors.toList());

// Binary Supplier Unary  операторы
        // Binary - из двух разнотипных получить один объект как в reduce()
        /*
        Stream.of(1).reduce(0, new BinaryOperator<Integer>() {
            @Override
            public Integer apply(Integer integer, Integer integer2) {
                return null;
            }
        })
         */
        // Supplier


// параллельные stream
        IntStream.rangeClosed(0, 1000).parallel().filter(n -> {
            try {
                Thread.sleep(10);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName());
            return n % 2 == 0;
        }).count();



//  интересные задачи
        try {
        // почему поиск уникальных элементов в стриме не сработало?
            Files.lines(Paths.get("src/main/text.txt"))
                    .map(line -> line.split("\\s"))
                    .distinct() // ого это поток массивов! и да эти массивы уникальные! а не элементы
                    .forEach(arr -> System.out.println(Arrays.toString(arr)));
            /*
            [A, B, V, H, G, A, A]
            [C, D, B, A, U, X, C]
            [A, B, F, D, X, D, B]
            [A, G, F, E, Q, Q, Q]
             */
            System.out.println("========================");
            // попытка 2
            Files.lines(Paths.get("src/main/text.txt"))
                    .map(line -> line.split("\\s"))
                    .map(Arrays::stream)
                    .distinct()
                    .forEach(System.out::println); // увы тут поток потоков строк а не массивы с уникальными элементами
            /*
            java.util.stream.ReferencePipeline$Head@7ef20235
            java.util.stream.ReferencePipeline$Head@27d6c5e0
            java.util.stream.ReferencePipeline$Head@4f3f5b24
            java.util.stream.ReferencePipeline$Head@15aeb7ab
                */
            System.out.println("========================");
            //  попытка 3
            System.out.println(Files.lines(Paths.get("src/main/text.txt"))
                    .map(line -> line.split("\\s"))
                    .flatMap(Arrays::stream)// магия! объединяет несколько потоков в один!
                    .distinct()
                    .collect(Collectors.joining(", ", "Уникальные слова: ", ".")));
            // Уникальные слова: A, B, V, H, G, C, D, U, X, F, E, Q.
        }catch (IOException e){
            e.printStackTrace();
        }



        streamSimpleTask();

        // Теперь по шагам о каждой модификации потока из прим. выше.
        // дана коллекция состоя. из чисел
        List<Integer> integers = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8));
        // если хотим преобразовать к потоку данных (трубы с нашими данными)
// есть 2 типа операций над потоками Промежуточные и Конечные.
        // без терминальной операции ничего не выполнится например .forEach();

    // решение через анонимный внутренний класс
        List<Integer> out = integers.stream().filter(new Predicate<Integer>() {
            @Override
            public boolean test(Integer integer) {
                return integer % 2 == 0;
            }
        }).collect(Collectors.toList());

        System.out.println(out);

    // решение через лямбду
        List<Integer> out2 = integers.stream().filter( (n) -> {return n % 2 == 0;} ).collect(Collectors.toList());
        System.out.println(out2);

// чем collect() отличается от forEach() ?
        // collect возвращает либо коллекцию, либо строку.
        // а если мы хотим как то поработать с объектами ТО:
        integers.stream().filter( (n) -> n % 2 == 1 ).forEach(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) {
                System.out.println(integer);
            }
        });
        //  we can curtail it
        integers.stream().filter( (n) -> n % 2 == 1 ).forEach(integer -> System.out.println(integer));

        System.out.println("----------------- -1");

        System.out.println("-----------------0");
//  варианты создания stream
        // Stream.of("AA", "BBB", "CCCC", "D").forEach();
        // Arrays.stream(new String[] {"A", "A"}).forEach();
// методы для stream
        List<Integer> list = new ArrayList<>(Arrays.asList(1,2,3,4,5,6,7,8,9));
    // distinct - аналог set  только уник остаются

    //  findAny() вернет optional контейнер в котором объект может быть а, может и не быть
        list.stream().filter(n -> n > 10).findAny().ifPresent(System.out::println);
//  лямбду можно сохранить в переменную!
        Function<String, Integer> _strToLen = String::length;
        Function<String, Integer> strToLen = s -> s.length();
        Predicate<Integer> evenNumberFilter = n -> n % 2 == 0;
        Function<Integer, Integer> cube = n -> n * n * n;
        Stream.of(1, 2, 3).map(n -> Math.pow(n, 3));
        // or
        Stream.of(1, 2, 3).map(cube);
    // reduce() - уменьшаем кол-во объектов тут путем сложения
        int sum = 0;
        for (Integer o: list) sum += 0;
        // or
        int streamSum = list.stream().reduce(0, (a, b) -> a + b);
        list.stream().mapToInt(v -> v).sum();
    // skip() - пропускает некое количество элементов



        System.out.println(list.stream().allMatch(n -> n < 10));
        System.out.println(list.stream().anyMatch(n -> n == 4));
        System.out.println(list.stream().noneMatch(n -> n == 2));
        System.out.println("-----------------1");


    // map - преобразование одно объекта в другой его
            // пусть надо строки в числа перевести
        Stream.of("AA", "BBB", "CCCC", "D").map(new Function<String, Integer>() {
            @Override
            public Integer apply(String s) {
                return s.length();
            }
        }).forEach(System.out::println);
        //  we can curtail it
        System.out.println("-----------------2");
        Stream.of("AA", "BBB", "CCCC", "D").map(s -> s.length()).forEach(System.out::println);
        System.out.println("-----------------3");
        Stream.of(1,2,3,4).map(i -> i*10).limit(2).forEach(n -> System.out.println(n));
        System.out.println("-----------------4");
        Stream.of("AA","BbB").map(String::length).collect(Collectors.toList());
        System.out.println("-----------------5");
        class User{
            String name;
            public User(String name){
                this.name = name;
            }
        }
        //  из строк получаю объекты
        Stream.of("Bob", "Max", "John").map(User::new).collect(Collectors.toList());
        Stream.of("Bob", "Max", "John").map(s -> new User(s)).collect(Collectors.toList());
        System.out.println("-----------------6");
        IntStream intStream = IntStream.of(1, 2, 3, 4);
        LongStream longStream = LongStream.of(1L, 2L, 3L, 4L);
        IntStream rangedIntStream = IntStream.rangeClosed(1, 100);
        System.out.println("-----------------7");
        /*
        Операция reduce() выполняет роль сумматора по всем элементам стрима. Всего в данный момент
        поддерживается три частных случая такой операции, но мы рассмотрим один. Найдём наибольшее
        значение в стриме:*/
        Stream<Integer> stream = Stream.of(1, 2, 3, 24, 5, 6);
        stream.reduce((i1, i2) -> i1 > i2 ? i1 : i2)
                .ifPresent(System.out::println);
        /*
        reduce() принимает функцию аккумулятора BinaryOperator. На самом деле это BiFunction — когда
        оба операнда имеют один и тот же тип (в данном случае — Integer). Пример функции сравнивает по
        паре входящих чисел и возвращает наибольшее.*/

        System.out.println("-----------------8");
        Stream.of("dd2", "aa2", "bb1", "bb3", "cc4")
                .map(s -> {
                    System.out.println("map: " + s);
                    return s.toUpperCase();
                })
                .filter(s -> {
                    System.out.println("filter: " + s);
                    return s.startsWith("A");
                })
                .forEach(s -> System.out.println("forEach: " + s));
               /*
        map() и filter() вызываются 5 раз для каждой строчки в коллекции, а forEach — только один раз. Но
        можно сократить количество выполнений, изменив порядок операций. Посмотрим, что произойдёт,
        если filter() окажется в начале цепи:      */
        Stream.of("dd2", "aa2", "bb1", "bb3", "cc4")
            .filter(s -> {
                System.out.println("filter: " + s);
                return s.startsWith("a");
            })
            .map(s -> {
                System.out.println("map: " + s);
                return s.toUpperCase();
            })
            .forEach(s -> System.out.println("forEach: " + s));
                                       /*
        Теперь map вызывается только один раз и выполняется быстрее для большого количества входных
        элементов. Это стоит иметь в виду при составлении комплексного метода цепи.
                                                          */
        System.out.println("-----------------8");


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
// задача есть строка со словами разделенными пробелами и мы хотим посчитать кол-во уникальных строк
    private static void simpleStringEx(){
        System.out.println(Arrays.stream("A B CC B C AA A A A B C".split("\\s")).distinct().count());
    }
}