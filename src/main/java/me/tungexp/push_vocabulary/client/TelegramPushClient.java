package me.tungexp.push_vocabulary.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import me.tungexp.push_vocabulary.dto.TelegramPushRequest;

@FeignClient(name = "telegram-push", url = "${endpoint.telegram:https://api.telegram.org}")
public interface TelegramPushClient {

    @RequestMapping(method = RequestMethod.POST, value = "/bot{botToken}/sendMessage",
        consumes = "application/json", produces = "application/json")
    void push(@PathVariable String botToken, @RequestBody TelegramPushRequest request);

}
