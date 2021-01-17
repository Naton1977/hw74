package org.example.controller;

import org.example.PostService;
import org.example.domain.entity.PostCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.SQLException;
import java.util.List;

@Controller
@RequestMapping("/category")
public class CategoryController {

    private final PostService postService;

    @Autowired
    public CategoryController(PostService postService) {
        this.postService = postService;
    }


    @GetMapping("/category")
    public String index(Model model) {
        List<PostCategory> postCategoryList = postService.selectAllFromPostCaterogy();
        model.addAttribute("category", postCategoryList);
        return "categorypage";
    }


    @PostMapping("/addCategory")
    public String addCategory(PostCategory postCategory, RedirectAttributes redirectAttributes) {
        List<PostCategory> postCategoryList = postService.selectAllFromPostCaterogy();
        for (int i = 0; i < postCategoryList.size(); i++) {
            if (postCategoryList.get(i).getCategoryName().equals(postCategory.getCategoryName())) {
                redirectAttributes.addFlashAttribute("CategoryPresent", 1);
                return "redirect:/category/category";
            }
        }
        postService.savePostCategory(postCategory);
        return "redirect:/category/category";
    }

    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable Integer id) throws SQLException {
        postService.deleteCategory(id);
        return "redirect:/category/category";
    }

}
