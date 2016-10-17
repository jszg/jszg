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
        return Result.ok().setMessage("GET handled").setData(map);
    }

    // 查询
    @GetMapping("/rest")
    @ResponseBody
    public Result handleGet(@RequestParam String name) {
        return Result.ok().setMessage("GET handled").setData(name);
    }

    // 更新
    @PutMapping("/rest")
    @ResponseBody
    public Result handlePut(@RequestBody Map map) {
        return Result.ok().setMessage("UPDATE handled").setData(map);
    }

    // 创建
    @PostMapping("/rest")
    @ResponseBody
    public Result handlePost() {
        return Result.ok().setMessage("CREATE handled");
    }

    // 删除
    @DeleteMapping("/rest")
    @ResponseBody
    public Result handleDelete() {
        return Result.ok().setMessage("DELETE handled");
    }
}
