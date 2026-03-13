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
    - [x] [本地化模型](./doc/zh/本地化模型接入指南.md)
        - [x] [ChatGLM](./doc/zh/ChatGLM-6B接入.md)
- [x] [本地化向量计算](./doc/zh/本地化向量计算接入指南.md)
  - [x]  [text2vec](./doc/zh/本地化向量计算接入text2vec-chinese.md)
  

## 快速开始

### 环境准备

#### 使用我们为你准备好的 `docker-compose.yaml`

启动前请先复制一份配置模板，并按需修改：

```bash
cp ./bibliothecarius/config/application.example.yaml ./bibliothecarius/config/application.yaml
```

至少需要检查并修改 `./bibliothecarius/config/application.yaml` 中的以下配置：

- MySQL 配置
- OpenAI key
- Qdrant 服务地址
- 存储配置

配置文件准备完成后，在仓库根目录执行：

```bash
docker compose up -d
```

> 当前 `docker-compose.yaml` 会直接挂载 `./bibliothecarius/config/application.yaml`。如果没有先执行上面的 `cp`，Compose 会直接报错，避免误创建空目录。

#### 自己构建环境
- MySQL
  - 使用已有的数据库：`./mysql/init/init.sql`中有初始化sql，执行即可。 
- Qdrant
  - 参考[官方安装](https://qdrant.tech/documentation/quick_start/)


### 运行服务

1. 如果还没有配置文件，请先复制一份：

   ```bash
   cp ./bibliothecarius/config/application.example.yaml ./bibliothecarius/config/application.yaml
   ```

2. 修改 `./bibliothecarius/config/application.yaml` 配置。

3. 启动整套服务：

   ```bash
   docker compose up -d
   ```

   如果你只想启动后端相关服务，可以执行：

   ```bash
   docker compose up -d mysql qdrant bibliothecarius
   ```

4. 验证后端服务

   进入http://127.0.0.1:8080/

5. 如果您要嵌入到自己的服务中，可以不启用前端服务。

   前端项目：`./web`

   

### [接口文档](https://www.apifox.cn/apidoc/shared-0dfab7c9-3d3f-498a-b4c2-88b5e6b99a01)

Bibliothecarius的接口主要分为两部分：

1. dataset：您的本地数据相关接口，包括数据集的操作以及为数据集添加删除数据的接口。
2. scene：您的模型请求参数以及prompt和数据集关联接口。



## 例子

您可以使用以下例子熟悉Bibliothecarius。

- [分析书籍或材料](example/analyze_book/README.zh.md)
- 个人或企业知识库问答助手



