package org.mixer2.sample.web.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.mixer2.sample.dto.Category;
import org.mixer2.sample.dto.Item;
import org.mixer2.sample.service.CategoryService;
import org.mixer2.sample.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ItemController {

    @SuppressWarnings("unused")
    private Logger logger = Logger.getLogger(ItemController.class);

    @Autowired
    protected CategoryService categoryService;

    @Autowired
    protected ItemService itemService;

    @RequestMapping(value = "/item/{itemId}", method = RequestMethod.GET)
    public String showItem(@PathVariable long itemId, Model model) {
        Item item = itemService.getItem(itemId);
        List<Category> categoryList = categoryService.getCategoryList();
        model.addAttribute("item", item);
        model.addAttribute("categoryList", categoryList);
        return "item";
    }

}
