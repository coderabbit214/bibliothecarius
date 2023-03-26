import React, {useEffect} from "react";
import {Drawer, Form, Input, Button, DatePicker, message, Select} from "antd";
import Scene from "@/models/scene";
import {addScene, updateScene} from "@/services/scene-service";
import DatasetSelect from "@/components/dataset/dataset-select";
import {HttpError} from "@/libs/fetch";

interface SceneDrawerProps {
  visible: boolean;
  onClose: () => void;
  onSceneUpdated: () => void;
  scene?: Scene;
}

const SceneDrawer: React.FC<SceneDrawerProps> = ({
                                                   visible,
                                                   onClose,
                                                   onSceneUpdated,
                                                   scene,
                                                 }) => {
  const [form] = Form.useForm();

  useEffect(() => {
    if (scene) {
      form.setFieldsValue(scene);
    } else {
      form.resetFields();
    }
  }, [scene, form]);

  const onFinish = async (values: Scene) => {
    try {
      if (scene) {
        values.id = scene.id;
        await updateScene(values);
        message.success("Successfully modified");
      } else {
        await addScene(values);
        message.success("Successfully added");
      }
      onSceneUpdated();
      onClose();
    } catch (error) {
      if (error instanceof HttpError) {
        message.error(error.message);
      } else {
        message.error("Operation failed");
      }
    }
  };

  return (
    <Drawer
      title={scene ? "Modify Scene" : "Create Scene"}
      width={720}
      onClose={onClose}
      visible={visible}
      bodyStyle={{paddingBottom: 80}}
    >
      <Form form={form} layout="vertical" onFinish={onFinish}>

        <Form.Item
          name="name"
          label="Name"
          rules={[
            {
              required: true,
              message: 'Please input the name of the scene',
            },
          ]}
        >
          <Input />
        </Form.Item>

        <Form.Item name="remark" label="Remark">
          <Input />
        </Form.Item>

        <Form.Item
          name="template"
          label="Message Template"
          rules={[
            {
              required: true,
              message: 'Please input the message template',
            },
          ]}
        >
          <Input.TextArea rows={4} />
        </Form.Item>

        <Form.Item name="datasetId" label="Dataset">
          <DatasetSelect />
        </Form.Item>

        <Form.Item
          name="modelType"
          label="Model Type"
          rules={[
            {
              required: true,
              message: 'Please select a model type',
            },
          ]}
        >
          <Select>
            <Select.Option value="openaiChat">OpenAI</Select.Option>
          </Select>
        </Form.Item>

        <Form.Item
          name="params"
          label="Parameters"
          rules={[
            {
              required: true,
              message: 'Please input the parameters',
            }
          ]}
        >
          <Input.TextArea rows={4} />
        </Form.Item>

        <Form.Item>
          <Button type="primary" htmlType="submit">
            Submit
          </Button>
        </Form.Item>
      </Form>
    </Drawer>
  );
};

export default SceneDrawer;
