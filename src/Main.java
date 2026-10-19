//за каждые 5 купленных шоколадок 1 дают бесплатно
//при этом на 3 фантика от шоколажок дают бесплатно 1 шоколадку
//при налиции заданных ресурсов и цены на 1 шоколадку определить сколько можно купить их

import java.util.concurrent.Semaphore;

class ChocolateExchange {
    private int totalShokolad;
    private int totalFantik;

    private final Semaphore semaphore = new Semaphore(1);

    public ChocolateExchange(int initialShokolad, int initialFantik) {
        this.totalShokolad = initialShokolad;
        this.totalFantik = initialFantik;
    }


    public void processExchange(String threadName) {//метод
        try {

            semaphore.acquire();

            int bonusFromShok = totalShokolad / 5;
            totalShokolad += bonusFromShok;
            totalFantik += totalShokolad;

            System.out.println("[ " + threadName + "  Всего шоколадок: " + totalShokolad + ", фантиков: " + totalFantik);

            while (totalFantik >= 3) {
                int newShok = totalFantik / 3;//сколько получим шоколадок
                totalShokolad += newShok;
                totalFantik = (totalFantik % 3) + newShok;
                System.out.println("[ " + threadName + " ] Обменял фантики. + " + newShok + " шоколадок осталось фантиков: " + totalFantik);
            }

            System.out.println("[ " + threadName + " ] Завершил операции. Итого шоколадок: "
                    + totalShokolad + ", осталось фантиков: " + totalFantik);

        } catch (InterruptedException e) {
            System.err.println("Поток был прерван: " + e.getMessage());
            Thread.currentThread().interrupt();
        } finally {
            semaphore.release();
            System.out.println(threadName );
        }
    }
}

class ChokoThread extends Thread {
    private final ChocolateExchange exchange;

    public ChokoThread(String name, ChocolateExchange exchange) {
        super(name);
        this.exchange = exchange;
    }

    public void run() {
        exchange.processExchange(getName());
    }
}

public class Main {
    public static void main(String[] args) throws InterruptedException {
        ChocolateExchange exchange = new ChocolateExchange(7, 1);

        ChokoThread t1 = new ChokoThread("Поток-1", exchange);
        ChokoThread t2 = new ChokoThread("Поток-2", exchange);

        t1.start();
        t2.start();

        t1.join();
        t2.join();

    }
}