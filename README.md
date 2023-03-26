# Bibliothecarius

[中文文档](https://github.com/coderabbit214/bibliothecarius/blob/main/README.zh.md)


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
  - [ ] pdf
- [ ] Support for more models
  - [x] gpt3.5
  - [ ] ChatGLM

## Quick Start

### Environment Setup

#### Use our prepared`docker-compose.yaml`

After downloading the code, initialize the environment with docker-compose:

```bash
docker-compose up -d
```
#### Build the environment on your own
- MySQL
  - Use an existing database: initialization SQL is available in ./mysql/init/init.sql, simply execute it.
- Qdrant
  - Refer to [official installation](https://qdrant.tech/documentation/quick_start/)


### Run the Service

1. Modify the configuration file `./bibliothecarius/config/application.yaml`, including:

   - MySQL configuration
   - opanai key
   - Qdrant service address
   - Storage configuration

2. Start the backend service with docker-compose

   ```bash
   cd bibliothecarius
   docker-compose up -d
   ```

3. Verify the backend service by visiting http://127.0.0.1:8080/

4. If you want to embed in your service, you can leave the front-end service unenabled

   Front-end project`./web`

[Interface document](https://www.apifox.cn/apidoc/shared-0dfab7c9-3d3f-498a-b4c2-88b5e6b99a01)

The Bibliothecarius interface has two main parts:

1. dataset: Your local data-related interface, including the operation of the dataset and the interface to add and delete data to the dataset.
2. scene: Your model requests parameters and prompt and data set association interfaces.



## Example

You can use the following examples to familiarize yourself with Bibliothecarius.

-  [Analyze books ormaterials](https://github.com/coderabbit214/bibliothecarius/blob/main/example/analyze_book/README.md)
- Personal or enterprise knowledge base question-and-answer assistant



