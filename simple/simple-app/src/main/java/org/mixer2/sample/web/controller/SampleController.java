package org.mixer2.sample.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SampleController {

    @RequestMapping(value = {"/", "/index"})
    public String index(Model model) {
        return "index";
    }

    @RequestMapping(value = "/foo")
    public String foo(Model model) {
        return "foo";
    }

    @RequestMapping(value = "/valueJson",  produces="text/javascript; charset=UTF-8")
    @ResponseBody
    public String valueJson(Model model) {
    	return "var valueJson = { message: 'サーバサイドが返すjsonのmessageです' };";
    }
    // 本当は純粋なjsonのみを返すべき。javascriptコード自体を返しているのはカッコ悪いかもしれない。どうにかしたい。

}
