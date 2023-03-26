package com.coderabbit214.bibliothecarius;

import com.coderabbit214.bibliothecarius.scene.Scene;
import com.coderabbit214.bibliothecarius.scene.SceneService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class IndexController {

    private final SceneService sceneService;

    public IndexController(SceneService sceneService) {
        this.sceneService = sceneService;
    }

    @RequestMapping("/")
    public String index(Model model) {
        List<Scene> list = sceneService.list();
        List<String> names = list.stream().map(Scene::getName).toList();
        model.addAttribute("scenes", names);
        return "index";
    }
}
