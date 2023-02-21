package com.back.entity.vo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class WxArticleVo {

    //作者
    private String author;
    //文章
    private List<ArticleVo> articleVos;

    public WxArticleVo() {
    }

    public static class ArticleVo{

        private String title;
        private String link;

        public ArticleVo() {
        }

        public ArticleVo(String title, String link) {
            this.title = title;
            this.link = link;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public static ArticleVo of(Map map){
            ArticleVo vo = new ArticleVo();
            vo.setTitle((String) map.get("title"));
            vo.setLink((String) map.get("link"));
            return vo;
        }
    }
}
