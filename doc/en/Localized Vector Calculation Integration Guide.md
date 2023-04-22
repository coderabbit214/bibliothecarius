# Localized Vector Calculation Integration Guide

> Bibliothecarius initialization provides the text-embedding-ada-002 model from openai for vector calculations, but you can also integrate your own vector calculation method. Please refer to the following instructions.

## 1.Implementing a Local Interface

Bibliothecarius initiates a request:

- `POST`

- `Content-Type: application/json`

- Parameters：

  ```json
  {
  	"input":"your text"
  }
  ```

Your local service should return an array:

```
[double]
```

## 2.Integration

> You can create an API or use a visual interface.

### Creating an API

Please refer to the [API documentation.](https://apifox.com/apidoc/shared-0dfab7c9-3d3f-498a-b4c2-88b5e6b99a01/api-72139088)

Request the POST:/external/vector interface.

Parameters:

```json
{
    "name": "name (unique and cannot be the same as built-in systems)",
    "remark": "description",
    "size": "your model size",
    "address": "request address"
}
```

### Using a Visual Interface

#TODO The front-end has not been implemented yet.


## 3.Example

- Integration with [text2vec](Localized%20Vector%20Calculation%20Integration%20with%20text2vec-chinese.md)
