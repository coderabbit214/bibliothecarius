package com.coderabbit214.bibliothecarius.externalVector;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.coderabbit214.bibliothecarius.common.exception.BusinessException;
import com.coderabbit214.bibliothecarius.dataset.DatasetService;
import com.coderabbit214.bibliothecarius.model.ModelInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @author Mr_J
 * @since 2023-03-31
 */
@Service
public class ExternalVectorService extends ServiceImpl<ExternalVectorMapper, ExternalVector> {

    private final DatasetService datasetService;

    public ExternalVectorService(@Lazy DatasetService datasetService) {
        this.datasetService = datasetService;
    }

    public ExternalVector add(ExternalVector externalVector) {
        if (checkName(externalVector)) {
            throw new BusinessException("that name already exists");
        }
        this.save(externalVector);
        return externalVector;
    }


    public void update(ExternalVector externalVector) {
        ExternalVector old = this.getById(externalVector);
        if (!Objects.equals(old.getName(), externalVector.getName())) {
            throw new BusinessException("The name cannot be modified");
        }

        if (externalVector.getId() == null) {
            throw new BusinessException("id is null");
        }
        if (checkName(externalVector)) {
            throw new BusinessException("that name already exists");
        }
        this.updateById(externalVector);
    }

    public void delete(Long id) {
        if (id == null) {
            throw new BusinessException("id is null");
        }
        ExternalVector externalVector = this.getById(id);
        Long count = datasetService.countByVectorType(externalVector.getName());
        if (count > 0) {
            throw new BusinessException("there are dataset in this vector,please delete them first");
        }
        this.removeById(id);
    }

    private boolean checkName(ExternalVector externalVector) {
        List<String> modelTypes = ModelInterface.MODEL_TYPES;
        if (modelTypes.contains(externalVector.getName())) {
            return true;
        }
        LambdaQueryWrapper<ExternalVector> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ExternalVector::getName, externalVector.getName());
        wrapper.ne(externalVector.getId() != null, ExternalVector::getId, externalVector.getId());
        return this.count(wrapper) > 0;
    }

    public List<String> getNameList() {
        return this.list().stream().map(ExternalVector::getName).distinct().toList();
    }

    public ExternalVector getByName(String modelType) {
        return this.getOne(new LambdaQueryWrapper<ExternalVector>().eq(ExternalVector::getName, modelType));
    }
}
