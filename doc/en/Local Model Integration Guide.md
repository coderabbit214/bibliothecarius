# Local Model Integration Guide
> Bibliothecarius initialization provides the `chat` model from OpenAI for dialogue, but you can also introduce your own model. Please refer to the following instructions.

## 1.Implement Local Interface

Bibliothecarius initiates a request:

- `POST`

- `Content-Type: application/json`

- Parameters:

  ```json
  {
  	"input":"user's question after assembly",
    // Historical record
    "chatContextList":[
      {
        // Sorting
        "sort": 0,
        "user": "user's question",
        "assistant": "AI output"
      }
    ],
    // User configuration parameters
    "params":{}
  }
  ```

Your local service needs to return a result:

```
string
```

## 2. Integration

> You can create the interface using API or a visual interface.

### Creating an Interface Using API

Please refer to the [API documentation](https://apifox.com/apidoc/shared-0dfab7c9-3d3f-498a-b4c2-88b5e6b99a01/api-71770815)

Request interface `POST:/external/model`

Parameters:

```json
{
    "chatAddress": "chat interface address",
    "name": "name (unique and cannot be duplicated with system built-ins)",
    "checkParametersAddress": "parameter checking interface (if empty, parameters will not be checked)",
    "remark": "description",
  	// Maximum allowed input request
    "inputMaxToken": 53
}
```

### Creating an Interface Using a Visual Interface

\#TODO Front-end implementation is pending

## 3.Example

-  [ChatGLM-6B Integration.md](ChatGLM-6B%20Integration.md)