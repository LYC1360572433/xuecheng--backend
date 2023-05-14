package com.xuecheng.content;

import com.xuecheng.content.model.dto.CourseCategoryTreeDto;
import com.xuecheng.content.service.CourseCategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest//单元测试需要加的注解
public class CourseCategoryServiceTests {

    @Autowired
    CourseCategoryService courseCategoryService;

    @Test
    public void testCourseCategoryService(){

        List<CourseCategoryTreeDto> courseCategoryTreeDtos = courseCategoryService.queryTreeNodes("1");
        System.out.println(courseCategoryTreeDtos);
    }
}