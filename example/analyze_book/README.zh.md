# 分析书籍

## 0.前置

按照https://github.com/coderabbit214/bibliothecarius/blob/main/README.md启动项目

## 1.准备数据

1. 点击添加

![截屏2023-03-26 15.59.01](https://images-jsh.oss-cn-beijing.aliyuncs.com/coderabbit/2023/03/26/20230326-161932.png)

2. 填写参数

> 参数说明：
>
> name：名称（唯一，英文）
>
> remark：备注
>
> parsing type：向量化参数，目前只支持`openaiVector`

![截屏2023-03-26 15.59.12](https://images-jsh.oss-cn-beijing.aliyuncs.com/coderabbit/2023/03/26/20230326-162014.png)

3. 进入`dataset`

![截屏2023-03-26 16.22.56](https://images-jsh.oss-cn-beijing.aliyuncs.com/coderabbit/2023/03/26/20230326-162345.png)

4. 上传数据`./动物庄园.txt`

![截屏2023-03-26 15.59.39](https://images-jsh.oss-cn-beijing.aliyuncs.com/coderabbit/2023/03/26/20230326-162545.png)

5. 等待处理成功

![截屏2023-03-26 15.59.39的副本](https://images-jsh.oss-cn-beijing.aliyuncs.com/coderabbit/2023/03/26/20230326-162605.png)

## 2.创建Scene

1. 点击添加

![截屏2023-03-26 16.26.21](https://images-jsh.oss-cn-beijing.aliyuncs.com/coderabbit/2023/03/26/20230326-162723.png)

2. 填写数据

> 参数说明：
>
> name: 唯一标识
>
> remark：备注
>
> template：${message}替换每次提问问题，${data} 替换相关数据
>
> dataset：关联数据集，这里我们选择上方创建的
>
> modelType：对话模型，目前支持openai
>
> parameters：openai参数，相关概念可查询openai官网

![截屏2023-03-26 16.26.32](https://images-jsh.oss-cn-beijing.aliyuncs.com/coderabbit/2023/03/26/20230326-162751.png)

## 3. 测试

打开后端服务跟路径

http://127.0.0.1:8080/

提问：

![截屏2023-03-26 16.31.55](https://images-jsh.oss-cn-beijing.aliyuncs.com/coderabbit/2023/03/26/20230326-163202.png)
