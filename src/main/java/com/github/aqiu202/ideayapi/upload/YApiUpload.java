package com.github.aqiu202.ideayapi.upload;

import com.github.aqiu202.ideayapi.config.xml.YApiProjectProperty;
import com.github.aqiu202.ideayapi.constant.YApiConstants;
import com.github.aqiu202.ideayapi.model.*;
import com.github.aqiu202.ideayapi.parser.base.ContentTypeResolver;
import com.github.aqiu202.ideayapi.util.HttpClientUtils;
import com.github.aqiu202.ideayapi.util.PsiDocUtils;
import com.github.aqiu202.ideayapi.util.StringUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;

import java.io.IOException;
import java.util.*;

/**
 * 上传到yapi
 *
 * @author aqiu 2019/1/31 11:41 AM
 */
public class YApiUpload {

    private final Gson gson = new Gson();

    private static final Map<String, Integer> MENUS = new HashMap<>();

    /**
     * <p>调用保存接口</p>
     *
     * @author aqiu 2019/5/15
     */
    public YApiResponse<List<YApiSaveResponse>> uploadSave(YApiProjectProperty property, YApiSaveParam yapiSaveParam,
                                                           @SuppressWarnings("unused") String path)
            throws IOException {
        if (StringUtils.isEmpty(yapiSaveParam.getTitle())) {
            yapiSaveParam.setTitle(yapiSaveParam.getPath());
        }
        YApiHeader yapiHeader = new YApiHeader();
        if (ContentTypeResolver.FORM_VALUE.equals(yapiSaveParam.getReq_body_type())) {
            yapiHeader.setName("Content-Type");
            yapiHeader.setValue(ContentTypeResolver.FORM);
        } else {
            yapiHeader.setName("Content-Type");
            yapiHeader.setValue(ContentTypeResolver.JSON);
        }
        if (Objects.isNull(yapiSaveParam.getReq_headers())) {
            Set<YApiHeader> list = new LinkedHashSet<>();
            list.add(yapiHeader);
            yapiSaveParam.setReq_headers(list);
        } else {
            yapiSaveParam.getReq_headers().add(yapiHeader);
        }
        YApiResponse<Integer> yapiResponse = this.getMenuIdOrCreate(property, yapiSaveParam);
        if (yapiResponse.getErrcode() == 0 && yapiResponse.getData() != null) {
            yapiSaveParam.setCatid(String.valueOf(yapiResponse.getData()));
            String response = HttpClientUtils
                    .ObjectToString(HttpClientUtils.getHttpclient().execute(
                            this.getHttpPost(property.getUrl() + YApiConstants.yapiSave,
                                    gson.toJson(yapiSaveParam))), "utf-8");
            return gson.fromJson(response, new TypeToken<YApiResponse<List<YApiSaveResponse>>>() {
            }.getType());
        } else {
            throw new IOException(yapiResponse.getErrmsg());
        }
    }


    /**
     * 获得httpPost
     */
    private HttpPost getHttpPost(String url, String body) {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Content-type", "application/json;charset=utf-8");
        HttpEntity reqEntity = new StringEntity(body == null ? "" : body, "UTF-8");
        httpPost.setEntity(reqEntity);
        return httpPost;
    }

    private HttpGet getHttpGet(String url) {
        return HttpClientUtils
                .getHttpGet(url, "application/json", "application/json; charset=utf-8");
    }


    /**
     * <p>获得分类或者创建分类</p>
     *
     * @author aqiu 2019/5/15
     */
    public YApiResponse<Integer> getMenuIdOrCreate(YApiProjectProperty property,
                                                   YApiSaveParam yapiSaveParam) {
        String projectId = Integer.toString(property.getProjectId());
        String yApiUrl = property.getUrl();
        String menu = yapiSaveParam.getMenu();
        if (StringUtils.isBlank(menu)) {
            yapiSaveParam.setMenu(YApiConstants.menu);
        }
        Integer menuId = findMenuByName(menu);
        if (Objects.nonNull(menuId)) {
            return new YApiResponse<>(menuId);
        }
        try {
            String response = HttpClientUtils.ObjectToString(HttpClientUtils.getHttpclient().execute(
                            this.getHttpGet(yApiUrl + YApiConstants.yapiCatMenu + "?project_id="
                                    + projectId + "&token=" + yapiSaveParam.getToken())),
                    "utf-8");
            YApiResponse<List<YApiMenuResponse>> yapiResponse = gson.fromJson(response, new TypeToken<YApiResponse<List<YApiMenuResponse>>>() {
            }.getType());
            if (yapiResponse != null && yapiResponse.getErrcode() == 0) {
                List<YApiMenuResponse> list = yapiResponse.getData();
                for (YApiMenuResponse yapiMenuResponse : list) {
                    if (yapiMenuResponse.getName().equals(yapiSaveParam.getMenu())) {
                        this.addMenu(yapiMenuResponse);
                        return new YApiResponse<>(yapiMenuResponse.get_id());
                    }
                }
            }
            YApiMenuParam yapiMenuParam = new YApiMenuParam(yapiSaveParam.getMenu(),
                    property.getProjectId(), yapiSaveParam.getToken());
            String menuDesc = yapiSaveParam.getMenuDesc();
            if (Objects.nonNull(menuDesc)) {
                yapiMenuParam.setDesc(menuDesc);
            }
            String responseCat = HttpClientUtils
                    .ObjectToString(HttpClientUtils.getHttpclient().execute(
                            this.getHttpPost(yApiUrl + YApiConstants.yapiAddCat,
                                    gson.toJson(yapiMenuParam))), "utf-8");
            YApiResponse<YApiMenuResponse> yApiResponse = gson.fromJson(responseCat, new TypeToken<YApiResponse<YApiMenuResponse>>() {
            }.getType());
            YApiMenuResponse yapiMenuResponse = yApiResponse.getData();
            this.addMenu(yapiMenuResponse);
            return new YApiResponse<>(yapiMenuResponse.get_id());
        } catch (Exception e) {
            return new YApiResponse<>(0, e.toString());
        }
    }

    private void addMenu(YApiMenuResponse yapiMenuResponse) {
        MENUS.put(yapiMenuResponse.getName(), yapiMenuResponse.get_id());
    }

    public static Map<String, Integer> getMenus() {
        return MENUS;
    }

    public static Integer findMenuByName(String menuName) {
        return MENUS.get(menuName);
    }

    public static void clearCache() {
        MENUS.clear();
        PsiDocUtils.clearCache();
    }

}
