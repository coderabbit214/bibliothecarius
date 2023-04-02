# Bibliothecarius


## 关于

### Bibliothecarius是什么

Bibliothecarius是一个本地数据分析工具，可根据自定义prompt进行对话。该工具支持多种模型，可以进行横向对比，并支持数据隔离和多种数据类型。

### Bibliothecarius能做什么

- 个人或企业知识库问答助手。
- 分析书籍或材料。
- 专属于某个群组的ai助手。
- 甚至可以组织ai陪你玩剧本杀。
- .....

## 计划

- [x] 多轮对话
- [ ] 更好的UI
- [ ] 支持更多文件类型
  - [x] txt
  - [x] pdf
  - [x] md
  - [ ] docx
- [x] 支持更多的模型
  - [x] gpt3.5
  - [x] [本地化模型](doc%2Fzh%2F%E6%9C%AC%E5%9C%B0%E5%8C%96%E6%A8%A1%E5%9E%8B%E6%8E%A5%E5%85%A5%E6%8C%87%E5%8D%97.md)
    - [x] [ChatGLM](doc%2Fzh%2FChatGLM-6B%E6%8E%A5%E5%85%A5.md)
- [x] [本地化向量计算](./doc/zh/本地化向量计算接入指南.md)
  - [x]  [text2vec](./doc/zh/本地化向量计算接入text2vec-chinese.md)
  

## 快速开始

### 环境准备

#### 使用我们为你准备好的`docker-compose.yaml`

下载代码后docker-compose初始化环境

```bash
docker-compose up -d
```
#### 自己构建环境
- MySQL
  - 使用已有的数据库：`./mysql/init/init.sql`中有初始化sql，执行即可。 
- Qdrant
  - 参考[官方安装](https://qdrant.tech/documentation/quick_start/)


### 运行服务

1. 修改`./bibliothecarius/config/application.yaml`配置文件，包括：

   - MySQL配置
   - opanai key
   - Qdrant 服务地址
   - 存储配置

2. docker-compose启动后端服务

   ```bash
   cd bibliothecarius
   docker-compose up -d
   ```

3. 验证后端服务

   进入http://127.0.0.1:8080/

4. 如果您要嵌入到您的服务中，可以不启用前端服务。

   前端项目`./web`

   

### [接口文档](https://www.apifox.cn/apidoc/shared-0dfab7c9-3d3f-498a-b4c2-88b5e6b99a01)

Bibliothecarius的接口主要分为两部分：

1. dataset：您的本地数据相关接口，包括数据集的操作以及为数据集添加删除数据的接口。
2. scene：您的模型请求参数以及prompt和数据集关联接口。



## 例子

您可以使用以下例子熟悉Bibliothecarius。

- [分析书籍或材料](example/analyze_book/README.zh.md)
- 个人或企业知识库问答助手



