# 本地化向量计算接入text2vec-chinese

## 1.启动项目

请前往 https://github.com/coderabbit214/text2vec-bibliothecarius 下载并启动项目。

## 2.添加到Bibliothecarius中

请求接口 POST:/external/vector 将接口地址添加到 Bibliothecarius 中。

```bash
curl --location --request POST 'http://127.0.0.1:8080/external/vector' \
--header 'Content-Type: application/json' \
--data-raw '{
    "address": "your address",
    "name": "vector",
    "remark": "本地化中文向量计算"
}'
```

## 3.测试

请求接口 GET:/dataset/vector/type 查看是否已成功添加向量计算方式。

## 4.使用

在创建 dataset 时选择所需的向量计算方式，进入 dataset 后上传文件或通过接口上传数据。
