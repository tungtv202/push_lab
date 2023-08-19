package me.tungexp.push_vocabulary.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import me.tungexp.push_vocabulary.dto.VocabularyWord;

//@FeignClient(name = "vocabulary-resource", url = "https://raw.githubusercontent.com/trannguyenhan/bilingualcrawl-vietnamese-english/master/bilingualcrawl/toomva_learn.json")
public interface VocabularyResourceClient {


    @RequestMapping(method = RequestMethod.GET, value = "", produces = "application/json", consumes = "application/json")
    List<VocabularyWord> get();
}
