# Analyzing a Book

## 0.Prerequisite

Follow the instructions at https://github.com/coderabbit214/bibliothecarius/blob/main/README.mdto launch the project.

## 1.Prepare Data

1. Click "Add"

![截屏2023-03-26 15.59.01](https://images-jsh.oss-cn-beijing.aliyuncs.com/coderabbit/2023/03/26/20230326-161932.png)

2. Fill in the parameters

> Parameter Explanation：
>
> name：Name (unique, in English)
>
> remark： Remark
>
> parsing type：Vectorization parameter, currently only supports`openaiVector`

![截屏2023-03-26 15.59.12](https://images-jsh.oss-cn-beijing.aliyuncs.com/coderabbit/2023/03/26/20230326-162014.png)

3. Enter`dataset`

![截屏2023-03-26 16.22.56](https://images-jsh.oss-cn-beijing.aliyuncs.com/coderabbit/2023/03/26/20230326-162345.png)

4. Upload data`./动物庄园.txt`

![截屏2023-03-26 15.59.39](https://images-jsh.oss-cn-beijing.aliyuncs.com/coderabbit/2023/03/26/20230326-162545.png)

5. Wait for processing to succeed

![截屏2023-03-26 15.59.39的副本](https://images-jsh.oss-cn-beijing.aliyuncs.com/coderabbit/2023/03/26/20230326-162605.png)

## 2.CreateScene

1. Click "Add"

![截屏2023-03-26 16.26.21](https://images-jsh.oss-cn-beijing.aliyuncs.com/coderabbit/2023/03/26/20230326-162723.png)

2. 填写数据

> 参数说明：
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

![截屏2023-03-26 16.26.32](https://images-jsh.oss-cn-beijing.aliyuncs.com/coderabbit/2023/03/26/20230326-162751.png)

## 3. Test

Open the backend service and navigate to the root path:

http://127.0.0.1:8080/

Ask a question:

![截屏2023-03-26 16.31.55](https://images-jsh.oss-cn-beijing.aliyuncs.com/coderabbit/2023/03/26/20230326-163202.png)
