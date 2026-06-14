package org.example.catcarebusiness.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.example.catcarebusiness.config.JwtUtils;
import org.example.catcarebusiness.entity.Cat;
import org.example.catcarebusiness.service.CatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cat")
public class CatController {

    @Autowired
    private CatService catService;

    @Autowired
    private JwtUtils jwtUtils;

    /** 从请求头提取当前登录用户ID */
    private Long getCurrentUserId(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return jwtUtils.getUserIdFromToken(header.substring(7));
        }
        return null;
    }

    /** 录入猫咪档案 */
    @PostMapping("/add")
    public String addCat(@RequestBody Cat cat, HttpServletRequest request) {
        cat.setCreatorId(getCurrentUserId(request));
        boolean saved = catService.save(cat);
        return saved ? "流浪猫档案录入成功！" : "录入失败，请检查数据。";
    }

    /** 获取猫咪列表 */
    @GetMapping("/list")
    public List<Cat> getCatList() {
        return catService.list();
    }

    /** 获取单只猫咪详情 */
    @GetMapping("/{id}")
    public Cat getCatById(@PathVariable Long id) {
        return catService.getById(id);
    }

    /** 修改猫咪绝育/TNR状态（仅管理员） */
    @PutMapping("/tnr/{id}")
    public String updateTnrStatus(@PathVariable Long id, @RequestParam Integer tnrStatus) {
        Cat cat = catService.getById(id);
        if (cat == null) {
            return "未找到该猫咪的档案！";
        }
        cat.setTnrStatus(tnrStatus);
        boolean updated = catService.updateById(cat);
        return updated ? "猫咪绝育状态修改成功！" : "修改失败。";
    }

    /** 删除猫咪档案（仅管理员） */
    @DeleteMapping("/delete/{id}")
    public String deleteCat(@PathVariable Long id) {
        boolean removed = catService.removeById(id);
        return removed ? "垃圾数据删除成功！" : "删除失败，该档案可能已被删除。";
    }
}
