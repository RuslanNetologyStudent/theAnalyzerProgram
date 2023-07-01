import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.Random;

public class Main {
    public static final BlockingQueue<String> wordsQueueA = new ArrayBlockingQueue<>(100);
    public static final BlockingQueue<String> wordsQueueB = new ArrayBlockingQueue<>(100);
    public static final BlockingQueue<String> wordsQueueC = new ArrayBlockingQueue<>(100);
    public static final int wordsSymbols = 10_000;
    public static final int textsLength = 100_000;

    public static void main(String[] args) {

        String[] word = new String[wordsSymbols];

        Thread queue = new Thread(() -> {
            for (int i = 0; i < word.length; i++) {
                word[i] = generateText("abc", textsLength);
                try {
                    wordsQueueA.put(word[i]);
                    wordsQueueB.put(word[i]);
                    wordsQueueC.put(word[i]);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        queue.start();

        Thread queueA = new Thread(() -> calc('a', wordsQueueA));
        queueA.start();

        Thread queueB = new Thread(() -> calc('b', wordsQueueB));
        queueB.start();

        Thread queueC = new Thread(() -> calc('c', wordsQueueC));
        queueC.start();

    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    public static void calc(char letter, BlockingQueue<String> textsQueue) {
        int count = 0;
        int countMax = 0;
        // String textMax = null;
        // Слова с максимальным количеством символов

        for (int i = 0; i < wordsSymbols; i++) {
            try {
                String word = textsQueue.take();
                for (int j = 0; j < word.length(); j++) {
                    if (word.charAt(j) == letter) {
                        count++;
                    }
                }
                if (count > countMax) {
                    // textMax = textC;
                    // Слова с максимальным количеством символов
                    countMax = count;
                }
                count = 0;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        // System.out.println("Наибольшее количество букв " + letter + " : " + countMax + " Слово: " + textMax);
        System.out.println("Наибольшее количество букв " + letter + " : " + countMax);
    }
}