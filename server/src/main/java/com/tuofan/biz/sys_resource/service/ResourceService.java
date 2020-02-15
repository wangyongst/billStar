package com.tuofan.biz.sys_resource.service;

import com.google.common.collect.Lists;
import com.tuofan.biz.sys_resource.dao.ResourceDao;
import com.tuofan.biz.sys_resource.dto.CreateImgCmd;
import com.tuofan.biz.sys_resource.entity.Resource;
import com.tuofan.core.helper.ModelConvertHelper;
import com.tuofan.core.persistence.service.CrudRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ResourceService extends CrudRepository<ResourceDao, Resource> {

    public Resource createImg(CreateImgCmd createImgCmd) {
        Resource model = ModelConvertHelper.convert(createImgCmd, Resource.class);
        model.setTypeImage();
        int result = this.create(model);
        log.info(Integer.toString(result));
        return model;
    }

    public List<Resource> createImgList(String modelName, Integer modelId, Set<String> imgUrls) {
        List<CreateImgCmd> list = this.newCmdList(modelName, modelId, imgUrls);
        return this.createImgList(list);
    }

    public List<Resource> createImgList(List<CreateImgCmd> list) {
        List<Resource> models = ModelConvertHelper.convertList(list, Resource.class);
        models.forEach(Resource::setTypeImage);
        this.createList(models);
        return models;
    }

    public List<Resource> listModelImage(String modelName, Integer modelId) {
        Resource query = new Resource();
        query.setTypeImage();
        query.setModelId(modelId);
        query.setModelName(modelName);
        return this.listAll(query);
    }

    public Set<String> listModelImageUrl(String modelName, Integer modelId) {
        List<Resource> list = this.listModelImage(modelName, modelId);
        return list.stream().map(Resource::getUrl).collect(Collectors.toSet());
    }

    private List<CreateImgCmd> newCmdList(String modelName, Integer id, Set<String> urls) {
        List<CreateImgCmd> list = Lists.newArrayList();
        urls.forEach(ele ->
                list.add(new CreateImgCmd(modelName, id, ele))
        );
        return list;
    }

    public Integer deleteByUrls(Set<String> urls) {
        // todo 处理实际资源的删除
        if (CollectionUtils.isEmpty(urls)) {
            return 0;
        }
        Example example = this.createExample();
        Example.Criteria criteria = example.and();
        criteria.andIn("url", urls);
        return this.dao.deleteByExample(example);
    }

    public Integer deleteByModelIdAndName(String modelName, Integer modelId) {
        Example example = this.createExample();
        Example.Criteria criteria = example.and();
        criteria.andEqualTo("modelName", modelName);
        criteria.andEqualTo("modelId", modelId);
        return this.dao.deleteByExample(example);
    }
}
