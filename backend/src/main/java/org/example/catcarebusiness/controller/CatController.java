package org.example.catcarebusiness.controller;

import org.example.catcarebusiness.entity.Cat;
import org.example.catcarebusiness.service.CatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cat") // 接口的统一前缀路径
public class CatController {

    @Autowired
    private CatService catService;

    /**
     * 1. 录入猫咪档案
     * 前端通过 POST 请求提交猫咪的 JSON 数据
     */
    @PostMapping("/add")
    public String addCat(@RequestBody Cat cat) {
        cat.setCreatorId(1L);
        boolean saved = catService.save(cat);
        return saved ? "流浪猫档案录入成功！" : "录入失败，请检查数据。";
    }

    /**
     * 2. 获取猫咪列表
     * 前端 catList / feed / report 页面获取所有猫咪
     */
    @GetMapping("/list")
    public List<Cat> getCatList() {
        return catService.list();
    }

    /**
     * 3. 获取单只猫咪详情
     * 前端 catDetail 页面按 ID 查询一只猫咪
     */
    @GetMapping("/{id}")
    public Cat getCatById(@PathVariable Long id) {
        return catService.getById(id);
    }

    /**
     * 4. 修改猫咪绝育/TNR状态
     * 前端传入猫咪 id 和新的状态值 (0-未绝育, 1-已绝育, 2-已剪耳标)
     */
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

    /**
     * 5. 删除垃圾/错误数据
     * 根据主键 ID 彻底从数据库删除
     */
    @DeleteMapping("/delete/{id}")
    public String deleteCat(@PathVariable Long id) {
        boolean removed = catService.removeById(id);
        return removed ? "垃圾数据删除成功！" : "删除失败，该档案可能已被删除。";
    }
}
