package com.back.entity.vo;

import com.back.entity.pojo.Recomm;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecommVo {

    private Long uid;

    private String rdid;

    private String topic;

    private String content;

    public static RecommVo of(Recomm data){
        RecommVo vo = new RecommVo();
        vo.setUid(data.getUid());
        vo.setRdid(data.getRdid());
        vo.setTopic(data.getTopic());
        vo.setContent(data.getContent());
        return vo;
    }
}
