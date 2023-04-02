# ChatGLM-6B Integration

## 1. Start the Project

Please go to https://github.com/coderabbit214/ChatGLM-6B-bibliothecarius to download and start the project.

## 2. Add to Bibliothecarius

Request interface POST:/external/model to add the interface address to Bibliothecarius.

```bash
bashCopy code
curl --location --request POST 'http://127.0.0.1:8080/external/model' \
--header 'Content-Type: application/json' \
--data-raw '{
    "chatAddress": "http://127.0.0.1:8000/chat",
    "name": "ChatGLM",
    "checkParametersAddress": "http://127.0.0.1:8000/checkParams",
    "remark": "description",
    "inputMaxToken": 1500
}'
```

## 3. Test

Request interface GET:/scene/model/type to check if the model has been successfully added.

## 4. Usage

Select the desired model when creating a scene.