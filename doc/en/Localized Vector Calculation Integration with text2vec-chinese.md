# Localized Vector Calculation Integration with text2vec-chinese

## 1.Start the Project

Please download and start the project from https://github.com/coderabbit214/text2vec-bibliothecarius.

## 2.Add to Bibliothecarius

Use the API endpoint POST:/external/vector to add the API address to Bibliothecarius.

```bash
curl --location --request POST 'http://127.0.0.1:8080/external/vector' \
--header 'Content-Type: application/json' \
--data-raw '{
    "address": "your address",
    "name": "vector",
    "size": 768,
    "remark": "Localized Chinese Vector Calculation"
}'
```

## 3.Test

Use the API endpoint GET:/dataset/vector/type to check if the vector calculation method has been successfully added.

## 4.Usage

When creating a dataset, select the desired vector calculation method and upload files or data via API after entering the dataset.
