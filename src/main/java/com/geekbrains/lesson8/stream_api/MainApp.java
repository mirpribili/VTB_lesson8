package com.geekbrains.lesson8.stream_api;

public class MainApp {
    public static void main(String[] args) {
        // теперь вспомнив про анонимные внутр. классы и функц интерфейсы
        // при работе с аноноим внутр. классом мы можем свернуть его в лямбду ЕСЛИ он реализует функциональный интерфейс
        /* // БЫЛО
        new Thread(new Runnable() {
            @Override
            public void run() {
                //
            }
        }).start();
         */
        //  СТАЛА Лямбда
        //new Thread( () -> {} ).start();

// идея прокинуть в метод1 метод2 хотя бы завернутый в объект.
        doSomething(() -> {
            for (int i = 0; i < 5; i++){
                System.out.println("Java");
            }
        });

        doSomething(() -> System.out.println(100));
        // ВНЕЗАПНО 1 и тот же метод doSomething() делает разные вещи на лету перестраиваясь!
    }
    public static void doSomething(Runnable runnableObj){
        runnableObj.run();
    }
}
