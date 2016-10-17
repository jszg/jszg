package com.xtuer.controller;

import com.xtuer.bean.Result;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@Controller
public class RestController {
    @GetMapping("/rest-form")
    public String restForm() {
        return "rest-form.fm";
    }

    @GetMapping("/rest/{id}")
    @ResponseBody
    public Result handleGet(@PathVariable int id, @RequestParam String name, ModelMap map) {
        map.addAttribute("id", id);
        map.addAttribute("name", name);
        return Result.ok(map).setMessage("GET handled");
    }

    // 查询
    @GetMapping("/rest")
    @ResponseBody
    public Result handleGet(@RequestParam String name) {
        return Result.ok(name).setMessage("GET handled");
    }

    // 更新
    @PutMapping("/rest")
    @ResponseBody
    public Result handlePut(@RequestBody Map map) {
        return Result.ok(map).setMessage("UPDATE handled");
    }

    // 创建
    @PostMapping("/rest")
    @ResponseBody
    public Result handlePost() {
        return Result.ok(null).setMessage("CREATE handled");
    }

    // 删除
    @DeleteMapping("/rest")
    @ResponseBody
    public Result handleDelete() {
        return Result.ok(null).setMessage("DELETE handled");
    }
}
