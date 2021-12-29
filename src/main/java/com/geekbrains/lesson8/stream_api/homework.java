package com.geekbrains.lesson8.stream_api;

import java.util.Arrays;
import java.util.Comparator;
import java.util.function.Function;
import java.util.stream.Collectors;

public class homework {
    static class Person{
        private String name;
        private int age;
        private int salary;

        public String getName(){
            return name;
        }

        public int getAge(){
            return age;
        }

        public int getSalary() {
            return salary;
        }

        public Person(String name, int age, int salary) {
            this.name = name;
            this.age = age;
            this.salary = salary;
        }
    }

    public static void main(String[] args) {
        // Создайте массив с набором слов, и с помощью Stream API найдите наиболее часто
        // встречающееся;
        String[] words = {"A", "A", "A", "A", "B", "B", "B", "C", "C", "C", "D", };
        //                     сделали поток и хитр. сборку с группировкой   оставим строки   а ключ кол-во слов
        String result = Arrays.stream(words).collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                // поток в Set  множество - ключ = значение и снова в поток
                .entrySet().stream()
                //  ищем максимальный элемент  -- ищем по значению тк у нас коллекция слово\количество
                .max(Comparator.comparingLong(e -> e.getValue()))
                // надемся что поток не пустой и выдергиваем значение
                .get().getKey();
        System.out.println(result);
        // Создайте массив объектов типа Сотрудник (с полями Имя, Возраст, Зарплата) и вычислите
        // среднюю зарплату сотрудника (-ов?);
        Person[] persons = {new Person("Bob1", 30, 50000), new Person("Bob2", 40, 46000), new Person("Bob3", 31, 52000), };
        System.out.println(Arrays.stream(persons).mapToDouble(Person::getSalary).average());

        // Напишите метод, способный найти в массиве сотрудников из п. 2 найдите N самых старших
        // сотрудников и отпечатает в консоль сообщение вида “N самых старших сотрудников зовут:
        // имя1, имя2, имяN;”.
        final int N = 2;
        //                     сортируем, но разворачиваем компаратор чтобы в обратном порядке убывания
        System.out.println(Arrays.stream(persons).sorted(((o1, o2) -> o2.age - o1.age))
                .limit(N).map(Person::getName)
                //.forEach(System.out::println);
                .collect(Collectors.joining(", ", N + " самых старших сотрудников зовут: ", ".")));
    }
}
