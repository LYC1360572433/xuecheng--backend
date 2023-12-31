package com.xuecheng.learning.service;

import com.xuecheng.base.model.RestResponse;

/**
 * @description 学习过程管理service接口
 */
public interface LearningService {

    /**
     * @param courseId    课程id
     * @param teachplanId 课程计划id
     * @param mediaId     视频文件id
     * @return com.xuecheng.base.model.RestResponse<java.lang.String>
     * @description 获取教学视频
     */
    public RestResponse<String> getVideo(String userId, Long courseId, Long teachplanId, String mediaId);
}
