package com.fzx.dianping.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fzx.dianping.common.BusinessException;
import com.fzx.dianping.common.EmBusinessError;
import com.fzx.dianping.dal.ShopModelMapper;
import com.fzx.dianping.model.CategoryModel;
import com.fzx.dianping.model.SellerModel;
import com.fzx.dianping.model.ShopModel;
import com.fzx.dianping.service.CategoryService;
import com.fzx.dianping.service.SellerServie;
import com.fzx.dianping.service.ShopService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.http.HttpRequest;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @Author: fanZhengxu
 * @Description:
 * @Date: Create in 19:16 2020/2/15
 */
@Service
public class ShopServiceImpl implements ShopService {

    @Autowired
    private ShopModelMapper shopModelMapper;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SellerServie sellerServie;

    @Autowired
    private RestHighLevelClient highLevelClient;

    @Override
    public ShopModel crete(ShopModel shopModel) throws BusinessException {
        shopModel.setCreatedAt(new Date());
        shopModel.setUpdatedAt(new Date());
        SellerModel sellerModel = sellerServie.get(shopModel.getSellerId());
        if (sellerModel == null) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "商户不存在");
        }
        if (sellerModel.getDisabledFlag().intValue() == 1) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "商户已禁用");
        }
        CategoryModel categoryModel = categoryService.get(shopModel.getCategoryId());
        if (categoryModel == null) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "类目不存在");
        }
        shopModelMapper.insertSelective(shopModel);
        return get(shopModel.getId());
    }

    @Override
    public ShopModel get(Integer id) {
        ShopModel shopModel = shopModelMapper.selectByPrimaryKey(id);
        if (shopModel == null) {
            return null;
        }
        shopModel.setSellerModel(sellerServie.get(shopModel.getSellerId()));
        shopModel.setCategoryModel(categoryService.get(shopModel.getCategoryId()));
        return shopModel;
    }

    @Override
    public List<ShopModel> selectAll() {
        List<ShopModel> shopModelList = shopModelMapper.selectAll();
        shopModelList.forEach(shopModel -> {
            shopModel.setSellerModel(sellerServie.get(shopModel.getSellerId()));
            shopModel.setCategoryModel(categoryService.get(shopModel.getCategoryId()));
        });
        return shopModelList;
    }

    @Override
    public Integer countAllShop() {
        return shopModelMapper.countAllShop();
    }

    @Override
    public List<ShopModel> recommend(BigDecimal longitude, BigDecimal latitude) {
        List<ShopModel> shopModelList = shopModelMapper.recommend(longitude, latitude);
        shopModelList.forEach(shopModel -> {
            shopModel.setSellerModel(sellerServie.get(shopModel.getSellerId()));
            shopModel.setCategoryModel(categoryService.get(shopModel.getCategoryId()));
        });
        return shopModelList;
    }

    @Override
    public List<ShopModel> search(BigDecimal longitude, BigDecimal latitude, String keyword, Integer orderby, Integer categoryId, String tags) {
        List<ShopModel> shopModelList = shopModelMapper.search(longitude, latitude, keyword, orderby, categoryId, tags);
        shopModelList.forEach(shopModel -> {
            shopModel.setSellerModel(sellerServie.get(shopModel.getSellerId()));
            shopModel.setCategoryModel(categoryService.get(shopModel.getCategoryId()));
        });
        return shopModelList;
    }

    @Override
    public List<Map<String, Object>> searchGroupByTags(String keyword, Integer categoryId, String tags) {
        return shopModelMapper.searchGroupByTags(keyword, categoryId, tags);
    }

    @Override
    public Map<String, Object> searchEs(BigDecimal longitude, BigDecimal latitude, String keyword, Integer orderby, Integer categoryId, String tags) throws IOException {
        JSONObject result = new JSONObject();
        //_source
        result.put("_source", "*");
        //script_fields
        result.put("script_fields", new JSONObject());
        result.getJSONObject("script_fields").put("distance", new JSONObject());
        result.getJSONObject("script_fields").getJSONObject("distance").put("script", new JSONObject());
        result.getJSONObject("script_fields").getJSONObject("distance").getJSONObject("script")
                .put("source", "haversin(lat, lon, doc['location'].lat, doc['location'].lon)");
        result.getJSONObject("script_fields").getJSONObject("distance").getJSONObject("script")
                .put("lang", "expression");
        result.getJSONObject("script_fields").getJSONObject("distance").getJSONObject("script")
                .put("params", new JSONObject());
        result.getJSONObject("script_fields").getJSONObject("distance").getJSONObject("script")
                .getJSONObject("params").put("lat", latitude);
        result.getJSONObject("script_fields").getJSONObject("distance").getJSONObject("script")
                .getJSONObject("params").put("lon", longitude);

        //query
        result.put("query", new JSONObject());
        result.getJSONObject("query").put("function_score", new JSONObject());
        result.getJSONObject("query").getJSONObject("function_score")
                .put("query", new JSONObject());
        result.getJSONObject("query").getJSONObject("function_score")
                .getJSONObject("query").put("bool", new JSONObject());
        result.getJSONObject("query").getJSONObject("function_score")
                .getJSONObject("query").getJSONObject("bool")
                .put("must", new JSONObject());
        result.getJSONObject("query").getJSONObject("function_score")
                .getJSONObject("query").getJSONObject("bool").put("must", new JSONArray());
        result.getJSONObject("query").getJSONObject("function_score")
                .getJSONObject("query").getJSONObject("bool").getJSONArray("must")
                .add(new JSONObject());

        int mustIndex = 0;
        Map<String, Object> ciXingMap = analyzeCategoryKeyword(keyword);
        Boolean isAffectFilter = false;
        Boolean isAffectOrder = true;
        if (ciXingMap.keySet().size() > 0 && isAffectFilter) {
            int filterQueryIndex = 0;
            result.getJSONObject("query").getJSONObject("function_score")
                    .getJSONObject("query").getJSONObject("bool").getJSONArray("must")
                    .getJSONObject(mustIndex).put("bool", new JSONObject());
            result.getJSONObject("query").getJSONObject("function_score")
                    .getJSONObject("query").getJSONObject("bool").getJSONArray("must")
                    .getJSONObject(mustIndex).getJSONObject("bool").put("should", new JSONArray());
            result.getJSONObject("query").getJSONObject("function_score")
                    .getJSONObject("query").getJSONObject("bool").getJSONArray("must")
                    .getJSONObject(mustIndex).getJSONObject("bool")
                    .getJSONArray("should").add(new JSONObject());
            result.getJSONObject("query").getJSONObject("function_score")
                    .getJSONObject("query").getJSONObject("bool").getJSONArray("must")
                    .getJSONObject(mustIndex).getJSONObject("bool")
                    .getJSONArray("should").getJSONObject(filterQueryIndex).put("match", new JSONObject());
            result.getJSONObject("query").getJSONObject("function_score")
                    .getJSONObject("query").getJSONObject("bool").getJSONArray("must")
                    .getJSONObject(mustIndex).getJSONObject("bool")
                    .getJSONArray("should").getJSONObject(filterQueryIndex)
                    .getJSONObject("match").put("name", new JSONObject());
            result.getJSONObject("query").getJSONObject("function_score")
                    .getJSONObject("query").getJSONObject("bool").getJSONArray("must")
                    .getJSONObject(mustIndex).getJSONObject("bool")
                    .getJSONArray("should").getJSONObject(filterQueryIndex)
                    .getJSONObject("match").getJSONObject("name").put("query", keyword);
            result.getJSONObject("query").getJSONObject("function_score")
                    .getJSONObject("query").getJSONObject("bool").getJSONArray("must")
                    .getJSONObject(mustIndex).getJSONObject("bool")
                    .getJSONArray("should").getJSONObject(filterQueryIndex)
                    .getJSONObject("match").getJSONObject("name").put("boost", 0.1);

            for (String key : ciXingMap.keySet()) {
                filterQueryIndex++;
                Integer ciXingCategoryId = (Integer) ciXingMap.get(key);
                result.getJSONObject("query").getJSONObject("function_score")
                        .getJSONObject("query").getJSONObject("bool").getJSONArray("must")
                        .getJSONObject(mustIndex).getJSONObject("bool")
                        .getJSONArray("should").add(new JSONObject());
                result.getJSONObject("query").getJSONObject("function_score")
                        .getJSONObject("query").getJSONObject("bool").getJSONArray("must")
                        .getJSONObject(mustIndex).getJSONObject("bool")
                        .getJSONArray("should").getJSONObject(filterQueryIndex)
                        .put("term", new JSONObject());
                result.getJSONObject("query").getJSONObject("function_score")
                        .getJSONObject("query").getJSONObject("bool").getJSONArray("must")
                        .getJSONObject(mustIndex).getJSONObject("bool")
                        .getJSONArray("should").getJSONObject(filterQueryIndex)
                        .getJSONObject("term").put("category_id", new JSONObject());
                result.getJSONObject("query").getJSONObject("function_score")
                        .getJSONObject("query").getJSONObject("bool").getJSONArray("must")
                        .getJSONObject(mustIndex).getJSONObject("bool")
                        .getJSONArray("should").getJSONObject(filterQueryIndex)
                        .getJSONObject("term").getJSONObject("category_id")
                        .put("value", ciXingCategoryId);
                result.getJSONObject("query").getJSONObject("function_score")
                        .getJSONObject("query").getJSONObject("bool").getJSONArray("must")
                        .getJSONObject(mustIndex).getJSONObject("bool")
                        .getJSONArray("should").getJSONObject(filterQueryIndex)
                        .getJSONObject("term").getJSONObject("category_id")
                        .put("boost", 0.1);
            }
        } else {
            result.getJSONObject("query").getJSONObject("function_score")
                    .getJSONObject("query").getJSONObject("bool").getJSONArray("must")
                    .getJSONObject(mustIndex).put("match", new JSONObject());
            result.getJSONObject("query").getJSONObject("function_score")
                    .getJSONObject("query").getJSONObject("bool").getJSONArray("must")
                    .getJSONObject(mustIndex).getJSONObject("match").put("name", new JSONObject());
            result.getJSONObject("query").getJSONObject("function_score")
                    .getJSONObject("query").getJSONObject("bool").getJSONArray("must")
                    .getJSONObject(mustIndex).getJSONObject("match")
                    .getJSONObject("name").put("query", keyword);
            result.getJSONObject("query").getJSONObject("function_score")
                    .getJSONObject("query").getJSONObject("bool").getJSONArray("must")
                    .getJSONObject(mustIndex).getJSONObject("match")
                    .getJSONObject("name").put("boost", 0.1);
        }

        mustIndex++;
        result.getJSONObject("query").getJSONObject("function_score")
                .getJSONObject("query").getJSONObject("bool").getJSONArray("must")
                .add(new JSONObject());
        result.getJSONObject("query").getJSONObject("function_score")
                .getJSONObject("query").getJSONObject("bool").getJSONArray("must")
                .getJSONObject(mustIndex).put("term", new JSONObject());
        result.getJSONObject("query").getJSONObject("function_score")
                .getJSONObject("query").getJSONObject("bool").getJSONArray("must")
                .getJSONObject(mustIndex).getJSONObject("term").put("seller_disabled_flag", 0);

        if (tags != null) {
            mustIndex++;
            result.getJSONObject("query").getJSONObject("function_score")
                    .getJSONObject("query").getJSONObject("bool").getJSONArray("must")
                    .add(new JSONObject());
            result.getJSONObject("query").getJSONObject("function_score")
                    .getJSONObject("query").getJSONObject("bool").getJSONArray("must")
                    .getJSONObject(mustIndex).put("term", new JSONObject());
            result.getJSONObject("query").getJSONObject("function_score")
                    .getJSONObject("query").getJSONObject("bool").getJSONArray("must")
                    .getJSONObject(mustIndex).getJSONObject("term").put("tags", tags);
        }

        if (categoryId != null) {
            mustIndex++;
            result.getJSONObject("query").getJSONObject("function_score")
                    .getJSONObject("query").getJSONObject("bool").getJSONArray("must")
                    .add(new JSONObject());
            result.getJSONObject("query").getJSONObject("function_score")
                    .getJSONObject("query").getJSONObject("bool").getJSONArray("must")
                    .getJSONObject(mustIndex).put("term", new JSONObject());
            result.getJSONObject("query").getJSONObject("function_score")
                    .getJSONObject("query").getJSONObject("bool").getJSONArray("must")
                    .getJSONObject(mustIndex).getJSONObject("term").put("category_id", categoryId);
        }

        //functions
        result.getJSONObject("query").getJSONObject("function_score")
                .put("functions", new JSONArray());
        int functionsIndex = 0;
        if (orderby == null) {
            //gauss
            result.getJSONObject("query").getJSONObject("function_score")
                    .getJSONArray("functions").add(new JSONObject());
            result.getJSONObject("query").getJSONObject("function_score")
                    .getJSONArray("functions").getJSONObject(functionsIndex)
                    .put("gauss", new JSONObject());
            result.getJSONObject("query").getJSONObject("function_score")
                    .getJSONArray("functions").getJSONObject(functionsIndex)
                    .getJSONObject("gauss").put("location", new JSONObject());
            result.getJSONObject("query").getJSONObject("function_score")
                    .getJSONArray("functions").getJSONObject(functionsIndex)
                    .getJSONObject("gauss").getJSONObject("location")
                    .put("origin", latitude.toString() + "," + longitude.toString());
            result.getJSONObject("query").getJSONObject("function_score")
                    .getJSONArray("functions").getJSONObject(functionsIndex)
                    .getJSONObject("gauss").getJSONObject("location")
                    .put("scale", "100km");
            result.getJSONObject("query").getJSONObject("function_score")
                    .getJSONArray("functions").getJSONObject(functionsIndex)
                    .getJSONObject("gauss").getJSONObject("location")
                    .put("offset", "0km");
            result.getJSONObject("query").getJSONObject("function_score")
                    .getJSONArray("functions").getJSONObject(functionsIndex)
                    .getJSONObject("gauss").getJSONObject("location")
                    .put("decay", 0.5);
            result.getJSONObject("query").getJSONObject("function_score")
                    .getJSONArray("functions").getJSONObject(functionsIndex)
                    .put("weight", 9);
            //field_value_factor
            functionsIndex++;
            result.getJSONObject("query").getJSONObject("function_score")
                    .getJSONArray("functions").add(new JSONObject());
            result.getJSONObject("query").getJSONObject("function_score")
                    .getJSONArray("functions").getJSONObject(functionsIndex)
                    .put("field_value_factor", new JSONObject());
            result.getJSONObject("query").getJSONObject("function_score")
                    .getJSONArray("functions").getJSONObject(functionsIndex)
                    .getJSONObject("field_value_factor").put("field", "remark_score");
            result.getJSONObject("query").getJSONObject("function_score")
                    .getJSONArray("functions").getJSONObject(functionsIndex)
                    .put("weight", 0.2);
            //field_value_factor
            functionsIndex++;
            result.getJSONObject("query").getJSONObject("function_score")
                    .getJSONArray("functions").add(new JSONObject());
            result.getJSONObject("query").getJSONObject("function_score")
                    .getJSONArray("functions").getJSONObject(functionsIndex)
                    .put("field_value_factor", new JSONObject());
            result.getJSONObject("query").getJSONObject("function_score")
                    .getJSONArray("functions").getJSONObject(functionsIndex)
                    .getJSONObject("field_value_factor").put("field", "seller_remark_score");
            result.getJSONObject("query").getJSONObject("function_score")
                    .getJSONArray("functions").getJSONObject(functionsIndex)
                    .put("weight", 0.1);

            if (ciXingMap.keySet().size() > 0 && isAffectOrder) {
                for (String key : ciXingMap.keySet()) {
                    functionsIndex++;
                    result.getJSONObject("query").getJSONObject("function_score")
                            .getJSONArray("functions").add(new JSONObject());
                    result.getJSONObject("query").getJSONObject("function_score")
                            .getJSONArray("functions").getJSONObject(functionsIndex)
                            .put("filter", new JSONObject());
                    result.getJSONObject("query").getJSONObject("function_score")
                            .getJSONArray("functions").getJSONObject(functionsIndex)
                            .getJSONObject("filter").put("term", new JSONObject());
                    result.getJSONObject("query").getJSONObject("function_score")
                            .getJSONArray("functions").getJSONObject(functionsIndex)
                            .getJSONObject("filter").getJSONObject("term")
                            .put("category_id", ciXingMap.get(key));
                    result.getJSONObject("query").getJSONObject("function_score")
                            .getJSONArray("functions").getJSONObject(functionsIndex)
                            .put("weight",0.2);
                }
            }

            result.getJSONObject("query").getJSONObject("function_score").put("score_mode", "sum");
            result.getJSONObject("query").getJSONObject("function_score").put("boost_mode", "sum");
        } else {
            result.getJSONObject("query").getJSONObject("function_score")
                    .getJSONArray("functions").add(new JSONObject());
            result.getJSONObject("query").getJSONObject("function_score")
                    .getJSONArray("functions").getJSONObject(functionsIndex)
                    .put("field_value_factor", new JSONObject());
            result.getJSONObject("query").getJSONObject("function_score")
                    .getJSONArray("functions").getJSONObject(functionsIndex)
                    .getJSONObject("field_value_factor").put("field", "price_per_man");
            result.getJSONObject("query").getJSONObject("function_score").put("score_mode", "sum");
            result.getJSONObject("query").getJSONObject("function_score").put("boost_mode", "replace");
        }

        //sort
        result.put("sort", new JSONArray());
        result.getJSONArray("sort").add(new JSONObject());
        result.getJSONArray("sort").getJSONObject(0).put("_score", new JSONObject());
        if (orderby == null) {
            result.getJSONArray("sort").getJSONObject(0)
                    .getJSONObject("_score").put("order", "desc");
        } else {
            result.getJSONArray("sort").getJSONObject(0)
                    .getJSONObject("_score").put("order", "asc");
        }

        result.put("aggs", new JSONObject());
        result.getJSONObject("aggs").put("group_by_tags", new JSONObject());
        result.getJSONObject("aggs").getJSONObject("group_by_tags").put("terms", new JSONObject());
        result.getJSONObject("aggs").getJSONObject("group_by_tags")
                .getJSONObject("terms").put("field", "tags");

        String reqJson = result.toJSONString();
        Request request = new Request("GET", "/shop/_search");
        request.setJsonEntity(reqJson);
        Response response = highLevelClient.getLowLevelClient().performRequest(request);
        String responseStr = EntityUtils.toString(response.getEntity());
        JSONObject jsonObject = JSONObject.parseObject(responseStr);
        JSONArray jsonArray = jsonObject.getJSONObject("hits").getJSONArray("hits");
        List<ShopModel> shopModelList = Lists.newArrayList();
        for (int i = 0;i < jsonArray.size(); i++) {
            JSONObject jsonObj = jsonArray.getJSONObject(i);
            Integer id = new Integer(jsonObj.get("_id").toString());
            BigDecimal distance = new BigDecimal(jsonObj.getJSONObject("fields").getJSONArray("distance").get(0).toString());
            ShopModel shopModel = get(id);
            shopModel.setDistance(distance.multiply(new  BigDecimal(1000).setScale(0, BigDecimal.ROUND_CEILING)).intValue());
            shopModelList.add(shopModel);
        }

        JSONArray tagsArray = jsonObject.getJSONObject("aggregations")
                .getJSONObject("group_by_tags").getJSONArray("buckets");
        List<Map<String, Object>> tagsList = Lists.newArrayList();
        for (int i = 0; i < tagsArray.size(); i++) {
            JSONObject jsonObj = tagsArray.getJSONObject(i);
            Map<String, Object> tagMap = Maps.newHashMap();
            tagMap.put("tags", jsonObj.getString("key"));
            tagMap.put("num", jsonObj.getString("doc_count"));
            tagsList.add(tagMap);
        }
        JSONObject resultObj = new JSONObject();
        resultObj.put("tags", tagsList);
        resultObj.put("shop", shopModelList);
        return resultObj;
    }

    private Map<String, Object> analyzeCategoryKeyword(String keyword) throws IOException {
        Map<String, Object> res = Maps.newHashMap();
        Request request = new Request("GET", "/shop/_analyze");
        request.setJsonEntity("{\n" +
                "  \"text\": \"" + keyword + "\",\n" +
                "  \"field\": \"name\"\n" +
                "}");
        Response response = highLevelClient.getLowLevelClient().performRequest(request);
        String s = EntityUtils.toString(response.getEntity());
        JSONObject jsonObject = JSONObject.parseObject(s);
        JSONArray tokens = jsonObject.getJSONArray("tokens");
        for (int i = 0; i < tokens.size(); i++) {
            String token = tokens.getJSONObject(i).getString("token");
            Integer categoryId = getCategoryIdByToken(token);
            if (categoryId != null) {
                res.put(token, categoryId);
            }
        }
        return res;
    }

    private Integer getCategoryIdByToken(String token) {
        for (Integer key : categoryWorkMap.keySet()) {
            List<String> tokenList = categoryWorkMap.get(key);
            if (tokenList.contains(token)) {
                return key;
            }
        }
        return null;
    }

    private Map<Integer, List<String>> categoryWorkMap = Maps.newHashMap();

    @PostConstruct
    public void init() {
        categoryWorkMap.put(1, Lists.newArrayList());
        categoryWorkMap.put(2, Lists.newArrayList());

        categoryWorkMap.get(1).add("吃饭");
        categoryWorkMap.get(1).add("下午茶");

        categoryWorkMap.get(2).add("休息");
        categoryWorkMap.get(2).add("睡觉");
        categoryWorkMap.get(2).add("住宿");
    }
}
