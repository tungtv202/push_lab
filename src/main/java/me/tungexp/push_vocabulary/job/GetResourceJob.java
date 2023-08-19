package me.tungexp.push_vocabulary.job;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import me.tungexp.push_vocabulary.client.MyTelegramClient;
import me.tungexp.push_vocabulary.client.VocabularyResourceClient;
import me.tungexp.push_vocabulary.dto.VocabularyWord;

@Component
@RequiredArgsConstructor
@Slf4j
public class GetResourceJob implements CommandLineRunner {

    public static final int MINIMUM_WORD_LENGTH = 8;

    private final VocabularyResourceClient resourceClient;
    private final MyTelegramClient telegramClient;

    static HashMap<String, Pair<AtomicInteger, List<String>>> filter = new HashMap<>();

    @Override
    public void run(String... args) {
        List<VocabularyWord> vocabularyWords = resourceClient.get();


        vocabularyWords.forEach(e -> {
            if (filter.containsKey(e.getWord())) {
                filter.get(e.getWord()).getKey().incrementAndGet();
                filter.get(e.getWord()).getValue().add(e.getMeaning());
            } else {
                ArrayList<String> objects = new ArrayList<>();
                objects.add(e.getMeaning());
                filter.put(e.getWord(), Pair.of(new AtomicInteger(1), objects));
            }
        });

       filter.forEach((k, v) -> {
           if (v.getKey().get() > 1) {
               log.info("Word: " + k + " count: " + v.getKey().get());
               v.getValue().forEach(e -> log.info("Meaning: " + e));
           }});



        List<VocabularyWord> collect = vocabularyWords.parallelStream()
            .filter(w -> w.getWord().length() > MINIMUM_WORD_LENGTH && w.getMeaning().length() > MINIMUM_WORD_LENGTH)
            .filter(w -> !w.getWord().contains("Click để thêm"))
            .toList();

        while (true) {
            VocabularyWord randomWord = randomWord(collect);
            try {
                telegramClient.push(String.format("------\n- English: %s\n- Vietnamese: %s", randomWord.getWord(), randomWord.getMeaning()));
            } catch (Exception e) {
                log.info("Error when pushing to telegram: " + e.getMessage());
            }
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        // writeFile(collect);
    }

    private static void writeFile(List<VocabularyWord> collect) {
        try {
            var s = new ObjectMapper()
                .writerWithDefaultPrettyPrinter()
                .writeValueAsBytes(collect);
            FileOutputStream outputStream = new FileOutputStream("/Users/tungtv/workplace/push_vocabulary/filename1");
            outputStream.write(s);
            outputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // get random element from a list
    public static VocabularyWord randomWord(List<VocabularyWord> list) {
        return list.get((int) (Math.random() * list.size()));
    }





}
