# Bibliothecarius

[中文文档](./README.zh.md)


## About

### What is Bibliothecarius

Bibliothecarius is a local data analysis tool that can engage in conversation based on custom prompts. The tool supports multiple models, allowing for horizontal comparison, and supports data isolation and multiple data types.

### What can Bibliothecarius do

- Personal or enterprise knowledge base question-and-answer assistant
- Analyze books or materials
- AI assistant exclusively for a certain group
- Can even organize AI to play murder mystery games with you
- .....

## Plans

- [x] Multi-round dialogue
- [ ] Better UI
- [ ] Support for more file types
  - [x] txt
  - [x] pdf
  - [x] md
  - [ ] docx
- [x] Support for more models
  - [x] gpt3.5
    - [x] [Localized model](./doc/en/Local Model Integration Guide.md)
        - [x] [ChatGLM](./doc/en/ChatGLM-6B Integration.md)
    - [x] [Localized vector computation](./doc/en/Localized Vector Calculation Integration Guide.md)
        - [x]  [text2vec](./doc/en/Localized Vector Calculation Integration with text2vec-chinese.md)


## Quick Start

### Environment Setup

#### Use the provided `docker-compose.yaml`

Before starting the stack, copy the template config file and then update it with your own settings:

```bash
cp ./bibliothecarius/config/application.example.yaml ./bibliothecarius/config/application.yaml
```

At minimum, review and update these fields in `./bibliothecarius/config/application.yaml`:

- MySQL configuration
- OpenAI key
- Qdrant service address
- Storage configuration

After the config file is ready, start the stack from the repository root:

```bash
docker compose up -d
```

> `docker-compose.yaml` now expects `./bibliothecarius/config/application.yaml` to exist. If you skip the `cp` step above, Compose will fail fast instead of silently creating an empty path.

#### Build the environment on your own
- MySQL
  - Use an existing database: initialization SQL is available in `./mysql/init/init.sql`, simply execute it.
- Qdrant
  - Refer to [official installation](https://qdrant.tech/documentation/quick_start/)


### Run the Service

1. If you have not created the config file yet, copy it first:

   ```bash
   cp ./bibliothecarius/config/application.example.yaml ./bibliothecarius/config/application.yaml
   ```

2. Edit `./bibliothecarius/config/application.yaml`.

3. Start the full stack:

   ```bash
   docker compose up -d
   ```

   If you only want the backend-related services, you can run:

   ```bash
   docker compose up -d mysql qdrant bibliothecarius
   ```

4. Verify the backend service by visiting http://127.0.0.1:8080/

5. If you want to embed Bibliothecarius into your own service, you can leave the front-end service disabled.

   Front-end project: `./web`

[Interface document](https://www.apifox.cn/apidoc/shared-0dfab7c9-3d3f-498a-b4c2-88b5e6b99a01)

The Bibliothecarius interface has two main parts:

1. dataset: Your local data-related interface, including the operation of the dataset and the interface to add and delete data to the dataset.
2. scene: Your model requests parameters and prompt and data set association interfaces.



## Example

You can use the following examples to familiarize yourself with Bibliothecarius.

-  [Analyze books ormaterials](example/analyze_book/README.md)
- Personal or enterprise knowledge base question-and-answer assistant



