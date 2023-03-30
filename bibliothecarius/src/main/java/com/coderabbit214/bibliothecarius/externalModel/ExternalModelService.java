package com.coderabbit214.bibliothecarius.externalModel;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.coderabbit214.bibliothecarius.common.exception.BusinessException;
import com.coderabbit214.bibliothecarius.model.ModelInterface;
import com.coderabbit214.bibliothecarius.scene.SceneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @author Mr_J
 * @since 2023-03-29
 */
@Service
public class ExternalModelService extends ServiceImpl<ExternalModelMapper, ExternalModel> {

    private final SceneService sceneService;

    public ExternalModelService(@Lazy SceneService sceneService) {
        this.sceneService = sceneService;
    }

    /**
     * add
     *
     * @param externalModel
     * @return
     */
    public ExternalModel add(ExternalModel externalModel) {
        if (checkName(externalModel)) {
            throw new BusinessException("that name already exists");
        }
        this.save(externalModel);
        return externalModel;
    }


    public void update(ExternalModel externalModel) {
        ExternalModel old = this.getById(externalModel);
        if (!Objects.equals(old.getName(), externalModel.getName())) {
            throw new BusinessException("The name cannot be modified");
        }

        if (externalModel.getId() == null) {
            throw new BusinessException("id is null");
        }
        if (checkName(externalModel)) {
            throw new BusinessException("that name already exists");
        }
        this.updateById(externalModel);
    }

    public void delete(Long id) {
        if (id == null) {
            throw new BusinessException("id is null");
        }
        ExternalModel externalModel = this.getById(id);
        Long count = sceneService.countByModelType(externalModel.getName());
        if (count > 0) {
            throw new BusinessException("there are scene in this model,please delete them first");
        }
        this.removeById(id);
    }

    private boolean checkName(ExternalModel externalModel) {
        List<String> modelTypes = ModelInterface.MODEL_TYPES;
        if (modelTypes.contains(externalModel.getName())) {
            return true;
        }
        LambdaQueryWrapper<ExternalModel> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ExternalModel::getName, externalModel.getName());
        wrapper.ne(externalModel.getId() != null, ExternalModel::getId, externalModel.getId());
        return this.count(wrapper) > 0;
    }

    public List<String> getNameList() {
        return this.list().stream().map(ExternalModel::getName).distinct().toList();
    }

    public ExternalModel getByName(String modelType) {
        return this.getOne(new LambdaQueryWrapper<ExternalModel>().eq(ExternalModel::getName, modelType));
    }
}
