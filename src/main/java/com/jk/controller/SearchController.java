package com.jk.controller;

import com.alibaba.fastjson.JSONObject;
import com.jk.bean.Blog_Info;

import com.jk.util.RestClientFactory;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 王杰
 * @create 2019/3/9
 * @since 1.0.0
 */
@RestController
public class SearchController {

    @RequestMapping("getInfoAndTileInfo")
    public List<Blog_Info> getInfoAndTileInfo(@RequestParam(value = "queryString") String queryString,@RequestParam(value = "status")Integer status) throws IOException {
        List<Blog_Info> list=new ArrayList<>();
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.preTags("<span style='color:blue'>");
        highlightBuilder.postTags("</span>");
        highlightBuilder.field("titleinfo");

        SearchRequest searchRequest = new SearchRequest("blog");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        MatchQueryBuilder matchQuery=null;
        if(status==1){
           matchQuery = QueryBuilders.matchQuery("info", queryString);
        }
        if(status==2){
            matchQuery = QueryBuilders.matchQuery("titleinfo", queryString);
        }
        boolQuery.must(matchQuery);
        searchSourceBuilder.query(boolQuery);

        searchSourceBuilder.highlighter(highlightBuilder);

        searchRequest.source(searchSourceBuilder);
        SearchResponse search = RestClientFactory.getHighLevelClient().search(searchRequest);
        System.out.println(search);
        SearchHit[] hits = search.getHits().getHits();
        for (SearchHit hit : hits) {
            String string = hit.getSourceAsString();
            Blog_Info titleinfo = JSONObject.parseObject(string, Blog_Info.class);
            Map<String, HighlightField> fields = hit.getHighlightFields();
            HighlightField nameField = fields.get("titleinfo");
            if (nameField != null) {
                titleinfo.setTitleinfo(nameField.fragments()[0].toString());
            }
            list.add(titleinfo);
        }
        return list;
    }
}
