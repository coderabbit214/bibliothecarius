# ChatGLM-6B接入

## 1.启动项目

请前往 https://github.com/coderabbit214/ChatGLM-6B-bibliothecarius 下载并启动项目。

## 2.添加到Bibliothecarius中

请求接口 POST:/external/model 将接口地址添加到 Bibliothecarius 中。

```bash
curl --location --request POST 'http://127.0.0.1:8080/external/model' \
--header 'Content-Type: application/json' \
--data-raw '{
    "chatAddress": "http://127.0.0.1:8000/chat",
    "name": "ChatGLM",
    "checkParametersAddress": "http://127.0.0.1:8000/checkParams",
    "remark": "简介",
    "inputMaxToken": 1500
}'
```

## 3.测试

请求接口 GET:/scene/model/type 查看是否已成功添加模型。

## 4.使用

在创建 scene 时选择所需的模型。