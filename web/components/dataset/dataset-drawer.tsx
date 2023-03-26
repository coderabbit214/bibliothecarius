import React, {useEffect} from "react";
import {Drawer, Form, Input, Button, DatePicker, message} from "antd";
import Dataset from "@/models/dataset";
import {addDataset, updateDataset} from "@/services/dataset-service";
import {HttpError} from "@/libs/fetch";

// Define DatasetDrawerProps interface
interface DatasetDrawerProps {
  visible: boolean;
  onClose: () => void;
  onDatasetUpdated: () => void;
  dataset?: Dataset;
}

// DatasetDrawer component
const DatasetDrawer: React.FC<DatasetDrawerProps> = ({
                                                       visible,
                                                       onClose,
                                                       onDatasetUpdated,
                                                       dataset,
                                                     }) => {
  const [form] = Form.useForm();

  // Set form fields based on the dataset
  useEffect(() => {
    if (dataset) {
      form.setFieldsValue(dataset);
    } else {
      form.resetFields();
    }
  }, [dataset, form]);

  // Handle form submission
  const handleSubmit = async (values: Dataset) => {
    try {
      if (dataset) {
        values.id = dataset.id;
        delete values.name;
        await updateDataset(values);
        message.success("Successfully modified");
      } else {
        await addDataset(values);
        message.success("Successfully added");
      }
      onDatasetUpdated();
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
      title={dataset ? "Modify dataset" : "Create dataset"}
      width={720}
      onClose={onClose}
      visible={visible}
      bodyStyle={{paddingBottom: 80}}
    >
      <Form
        form={form}
        layout="vertical"
        onFinish={handleSubmit}
        initialValues={{createTime: null, updateTime: null, vectorType: "openaiVector"}}
      >
        <Form.Item
          name="name"
          label="Name"
          rules={[{required: true, message: "Please enter a name"}]}
        >
          <Input placeholder="Please enter a name" disabled={!!dataset} />
        </Form.Item>
        <Form.Item
          name="remark"
          label="Remark"
          rules={[{required: true, message: "Please enter a remark"}]}
        >
          <Input.TextArea rows={4} placeholder="Please enter a remark" />
        </Form.Item>
        <Form.Item
          name="vectorType"
          label="Parsing type"
          rules={[{required: true, message: "Please enter a parsing type"}]}
        >
          <Input placeholder="Please enter a parsing type" />
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

export default DatasetDrawer;
