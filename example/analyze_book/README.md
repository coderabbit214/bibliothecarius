# Analyzing a Book

Warning: Although the example "Animal Farm" provided by this project has entered the public domain in most countries and can be used and distributed freely, you still need to follow local laws to ensure that downloading this document does not infringe on copyright issues.

## 0.Prerequisite

Follow the instructions at https://github.com/coderabbit214/bibliothecarius/blob/main/README.mdto launch the project.

## 1.Prepare Data

1. Click "Add"

![截屏2023-03-26 15.59.01](https://images-jsh.oss-cn-beijing.aliyuncs.com/coderabbit/2023/03/26/20230326-161932.png?)

2. Fill in the parameters

> Parameter Explanation：
>
> name：Name (unique, in English)
>
> remark： Remark
>
> parsing type：Vectorization parameter, currently only supports`openaiVector`

![0365f05774103bc4518506b8e1790a30](https://images-jsh.oss-cn-beijing.aliyuncs.com/coderabbit/2023/03/26/20230326-210431.jpg?)

3. Enter`dataset`

![6e6ac7c23fdd08da756a70dc9362c276](https://images-jsh.oss-cn-beijing.aliyuncs.com/coderabbit/2023/03/26/20230326-210440.jpg?)

4. Upload file`./animal-farm.txt`

![截屏2023-03-26 15.59.39](https://images-jsh.oss-cn-beijing.aliyuncs.com/coderabbit/2023/03/26/20230326-162545.png?)

5. Wait for processing to succeed

![fa13e671936413396d0eb9d49cec9fc7](https://images-jsh.oss-cn-beijing.aliyuncs.com/coderabbit/2023/03/26/20230326-210447.jpg?)

## 2.CreateScene

1. Click "Add"

![截屏2023-03-26 16.26.21](https://images-jsh.oss-cn-beijing.aliyuncs.com/coderabbit/2023/03/26/20230326-162723.png?)

2. Fill the form

> Parameter description：
>
> name: Unique identifier
>
> remark：Remark
>
> template：${message} replaces each question asked, ${data} replaces related data
>
> dataset：Associated dataset, here we choose the one created above
>
> modelType：Dialogue model, currently supports openai
>
> parameters：OpenAI parameters, related concepts can be found on the OpenAI website

![1d5ca119001fa1b7e845becc7be5f1c2](https://images-jsh.oss-cn-beijing.aliyuncs.com/coderabbit/2023/03/26/20230326-210412.jpg?)

## 3. Test

Open the backend service and navigate to the root path:

http://127.0.0.1:8080/

Ask a question:

![截屏2023-03-26 16.31.55](https://images-jsh.oss-cn-beijing.aliyuncs.com/coderabbit/2023/03/26/20230326-163202.png?)
