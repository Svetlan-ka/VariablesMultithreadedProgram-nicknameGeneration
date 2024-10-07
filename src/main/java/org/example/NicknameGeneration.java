package org.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class NicknameGeneration {
    static AtomicInteger counterWord3 = new AtomicInteger();
    static AtomicInteger counterWord4 = new AtomicInteger();
    static AtomicInteger counterWord5 = new AtomicInteger();

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    public static void counterNickname() throws InterruptedException {
        List<Thread> threads = new ArrayList<>();
        Random random = new Random();
        String[] texts = new String[100_000];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("abc", 3 + random.nextInt(3));
            System.out.println(texts[i]);
        }

        Thread thread1 = new Thread(() -> {
            for (String nickname : texts) {
                int middleWord = nickname.length() / 2 + 1;
                for (int i = 0; i < nickname.length(); i++) {
                    char rightLetter = nickname.charAt(i);
                    char leftLetter = nickname.charAt(nickname.length() - i - 1);

                    if (rightLetter == leftLetter) {
                        if (i == middleWord) {
                            increaseCounters(nickname.length());
                        }
                    } else {
                        break;
                    }
                }
            }

        });
        threads.add(thread1);
        thread1.start();

        Thread thread2 = new Thread(() -> {
            for (String nickname : texts) {
                char letter = nickname.charAt(0);
                int count = 0;
                for (int i = 0; i < nickname.length(); i++) {
                    if (letter == nickname.charAt(i)) {
                        count++;
                        if (nickname.length() == count)
                            increaseCounters(nickname.length());
                    }
                }
            }
        });
        threads.add(thread2);
        thread2.start();

        Thread thread3 = new Thread(() -> {
            for (String nickname : texts) {
                if (Arrays.equals(nickname.chars().toArray(), nickname.chars().sorted().toArray()))
                    increaseCounters(nickname.length());
            }
        });
        threads.add(thread3);
        thread3.start();

        for (Thread thread : threads) {
            thread.join();
        }
        printResult();
    }

    public static void increaseCounters(int wordLength) {
        switch (wordLength) {
            case 3 -> counterWord3.incrementAndGet();
            case 4 -> counterWord4.incrementAndGet();
            case 5 -> counterWord5.incrementAndGet();
        }
    }

    public static void printResult() {
        System.out.println("Красивых слов с длиной 3: " + counterWord3 + " шт");
        System.out.println("Красивых слов с длиной 4: " + counterWord4 + " шт");
        System.out.println("Красивых слов с длиной 5: " + counterWord5 + " шт");
    }
}
