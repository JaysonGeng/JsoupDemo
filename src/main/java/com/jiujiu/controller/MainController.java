package com.jiujiu.controller;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author michaelod
 * @date 2018/12/27
 */

@Controller
public class MainController {

    @RequestMapping("/")
    public String main() {

        return "index";
    }


    @RequestMapping("/search")
    public String search(@RequestParam("q") String q, @RequestParam("start") String start, Model model) {
        if (start == null || "".equals(start)) {
            start = "0";
        }
        Collection<String> urls = getUrls(q,start);

        model.addAttribute("urls",urls);

        return "result";
    }

    public Collection<String> getUrls(String q, String start) {

        Collection<String>  urls = new ArrayList<>();

        Connection.Response response = null;
        try {
            response = Jsoup.connect("https://ja.ukiyo-e.org/search")
                    .method(Connection.Method.GET)
                    .ignoreContentType(true)
                    .data("q", q, "start", start)
                    .userAgent("Mozilla/5.0 (X11; Linux x86_64…) Gecko/20100101 Firefox/59.0").execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String body = response.body();

        Document doc = Jsoup.parse(body);
        Elements element = doc.select(".row");
        Elements linkss = element.select("img[src]");
        for (Element link : linkss) {
            String linkHref = link.attr("src");
            String imgUrl = linkHref.replace("thumbs", "images");
            urls.add(imgUrl);
        }
        return urls;
    }

}
