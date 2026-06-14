package org.example.catcarebusiness.controller;

import org.example.catcarebusiness.entity.Cat;
import org.example.catcarebusiness.service.CatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
        // 由于我们在数据库注入了 id=1 的测试管理员，这里模拟当前创建者是 1 号管理员
        cat.setCreatorId(1L);
        boolean saved = catService.save(cat);
        return saved ? "流浪猫档案录入成功！" : "录入失败，请检查数据。";
    }

    /**
     * 2. 修改猫咪绝育/TNR状态
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
     * 3. 删除垃圾/错误数据
     * 根据主键 ID 彻底从数据库删除
     */
    @DeleteMapping("/delete/{id}")
    public String deleteCat(@PathVariable Long id) {
        boolean removed = catService.removeById(id);
        return removed ? "垃圾数据删除成功！" : "删除失败，该档案可能已被删除。";
    }
}