package com.zygoo132.studyspace.controller;

import com.zygoo132.studyspace.entity.Combo;
import com.zygoo132.studyspace.repository.ComboRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/combo")
public class ComboController {

    @Autowired
    private ComboRepository comboRepository;

    @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("combos", comboRepository.findAll());
        model.addAttribute("newCombo", new Combo());
        return "combo_list";
    }

    @PostMapping("/add")
    public String addCombo(@ModelAttribute Combo combo) {
        comboRepository.save(combo);
        return "redirect:/combo/list";
    }

    @GetMapping("/edit/{id}")
    public String editCombo(@PathVariable Long id, Model model) {
        Combo combo = comboRepository.findById(id).orElseThrow();
        model.addAttribute("combo", combo);
        return "combo_edit";
    }

    @PostMapping("/update")
    public String updateCombo(@ModelAttribute Combo combo) {
        comboRepository.save(combo);
        return "redirect:/combo/list";
    }

    @PostMapping("/delete/{id}")
    public String deleteCombo(@PathVariable Long id) {
        comboRepository.deleteById(id);
        return "redirect:/combo/list";
    }
}
