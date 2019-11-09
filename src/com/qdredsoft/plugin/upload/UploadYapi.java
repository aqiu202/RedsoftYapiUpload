package com.qdredsoft.plugin.upload;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qdredsoft.plugin.constant.YapiConstants;
import com.qdredsoft.plugin.model.YapiCatMenuParam;
import com.qdredsoft.plugin.model.YapiCatResponse;
import com.qdredsoft.plugin.model.YapiHeaderDTO;
import com.qdredsoft.plugin.model.YapiResponse;
import com.qdredsoft.plugin.model.YapiSaveParam;
import com.qdredsoft.plugin.util.HttpClientUtil;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;

/**
 * 上传到yapi
 *
 * @date 2019/1/31 11:41 AM
 */
public class UploadYapi {


    private Gson gson = new Gson();

    public static Map<String, Map<String, Integer>> catMap = new HashMap<>();

    /**
     * @description: 调用保存接口
     * @param: [yapiSaveParam, attachUpload, path]
     * @return: com.qbb.dto.YapiResponse
     * @date: 2019/5/15
     */
    public YapiResponse uploadSave(YapiSaveParam yapiSaveParam, String path)
            throws IOException {
        if (Strings.isNullOrEmpty(yapiSaveParam.getTitle())) {
            yapiSaveParam.setTitle(yapiSaveParam.getPath());
        }
        YapiHeaderDTO yapiHeaderDTO = new YapiHeaderDTO();
        if ("form".equals(yapiSaveParam.getReq_body_type())) {
            yapiHeaderDTO.setName("Content-Type");
            yapiHeaderDTO.setValue("application/x-www-form-urlencoded");
            yapiSaveParam.setReq_body_form(yapiSaveParam.getReq_body_form());
        } else {
            yapiHeaderDTO.setName("Content-Type");
            yapiHeaderDTO.setValue("application/json");
            yapiSaveParam.setReq_body_type("json");
        }
        if (Objects.isNull(yapiSaveParam.getReq_headers())) {
            List list = new ArrayList();
            list.add(yapiHeaderDTO);
            yapiSaveParam.setReq_headers(list);
        } else {
            yapiSaveParam.getReq_headers().add(yapiHeaderDTO);
        }
        YapiResponse yapiResponse = this.getCatIdOrCreate(yapiSaveParam);
        if (yapiResponse.getErrcode() == 0 && yapiResponse.getData() != null) {
            yapiSaveParam.setCatid(String.valueOf(yapiResponse.getData()));
            String response = HttpClientUtil.ObjectToString(HttpClientUtil.getHttpclient().execute(
                    this.getHttpPost(yapiSaveParam.getYapiUrl() + YapiConstants.yapiSave,
                            gson.toJson(yapiSaveParam))), "utf-8");
            return gson.fromJson(response, YapiResponse.class);
        } else {
            return yapiResponse;
        }
    }


    /**
     * 获得httpPost
     */
    private HttpPost getHttpPost(String url, String body) {
        HttpPost httpPost = null;
        try {
            httpPost = new HttpPost(url);
            httpPost.setHeader("Content-type", "application/json;charset=utf-8");
            HttpEntity reqEntity = new StringEntity(body == null ? "" : body, "UTF-8");
            httpPost.setEntity(reqEntity);
        } catch (Exception e) {
        }
        return httpPost;
    }

    private HttpGet getHttpGet(String url) {
        return HttpClientUtil
                .getHttpGet(url, "application/json", "application/json; charset=utf-8");
    }


    /**
     * @description: 获得分类或者创建分类或者
     * @param: [yapiSaveParam]
     * @return: com.qbb.dto.YapiResponse
     * @date: 2019/5/15
     */
    public YapiResponse getCatIdOrCreate(YapiSaveParam yapiSaveParam) {
        Map<String, Integer> catMenuMap = catMap.get(yapiSaveParam.getProjectId().toString());
        if (catMenuMap != null) {
            if (!Strings.isNullOrEmpty(yapiSaveParam.getMenu())) {
                if (Objects.nonNull(catMenuMap.get(yapiSaveParam.getMenu()))) {
                    return new YapiResponse(catMenuMap.get(yapiSaveParam.getMenu()));
                }
            } else {
                if (Objects.nonNull(catMenuMap.get(YapiConstants.menu))) {
                    return new YapiResponse(catMenuMap.get(YapiConstants.menu));
                }
                yapiSaveParam.setMenu(YapiConstants.menu);
            }
        }
        String response;
        try {
            response = HttpClientUtil.ObjectToString(HttpClientUtil.getHttpclient().execute(
                    this.getHttpGet(
                            yapiSaveParam.getYapiUrl() + YapiConstants.yapiCatMenu + "?project_id="
                                    + yapiSaveParam
                                    .getProjectId() + "&token=" + yapiSaveParam.getToken())),
                    "utf-8");
            YapiResponse yapiResponse = gson.fromJson(response, YapiResponse.class);
            Map<String, Integer> catMenuMapSub = catMap
                    .get(yapiSaveParam.getProjectId().toString());
            if (yapiResponse.getErrcode() == 0) {
                List<YapiCatResponse> list = (List<YapiCatResponse>) yapiResponse.getData();
                list = gson.fromJson(gson.toJson(list), new TypeToken<List<YapiCatResponse>>() {
                }.getType());
                for (YapiCatResponse yapiCatResponse : list) {
                    if (yapiCatResponse.getName().equals(yapiSaveParam.getMenu())) {
                        this.addMenu(catMenuMapSub, yapiCatResponse, yapiSaveParam);
                        return new YapiResponse(yapiCatResponse.get_id());
                    }
                }
            }
            YapiCatMenuParam yapiCatMenuParam = new YapiCatMenuParam(yapiSaveParam.getMenu(),
                    yapiSaveParam.getProjectId(), yapiSaveParam.getToken());
            String menuDesc = yapiSaveParam.getMenuDesc();
            if (Objects.nonNull(menuDesc)) {
                yapiCatMenuParam.setDesc(menuDesc);
            }
            String responseCat = HttpClientUtil
                    .ObjectToString(HttpClientUtil.getHttpclient().execute(
                            this.getHttpPost(yapiSaveParam.getYapiUrl() + YapiConstants.yapiAddCat,
                                    gson.toJson(yapiCatMenuParam))), "utf-8");
            YapiCatResponse yapiCatResponse = gson
                    .fromJson(gson.fromJson(responseCat, YapiResponse.class).getData().toString(),
                            YapiCatResponse.class);
            this.addMenu(catMenuMapSub, yapiCatResponse, yapiSaveParam);
            return new YapiResponse(yapiCatResponse.get_id());
        } catch (IOException e) {
            e.printStackTrace();
            return new YapiResponse(0, e.toString());
        }
    }

    private void addMenu(Map<String, Integer> catMenuMapSub, YapiCatResponse yapiCatResponse,
            YapiSaveParam yapiSaveParam) {
        if (catMenuMapSub != null) {
            catMenuMapSub.put(yapiCatResponse.getName(), yapiCatResponse.get_id());
        } else {
            catMenuMapSub = new HashMap<>();
            catMenuMapSub.put(yapiCatResponse.getName(), yapiCatResponse.get_id());
            catMap.put(yapiSaveParam.getProjectId().toString(), catMenuMapSub);
        }
    }


}
