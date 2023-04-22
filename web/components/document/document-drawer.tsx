import React, {useEffect, useState} from "react";
import {Drawer, Form, Input, Button, message, Upload} from "antd";
import {HttpError} from "@/libs/fetch";
import {InboxOutlined} from "@ant-design/icons";
import {uploadDocument} from "@/services/document-service";
import {useRouter} from "next/router";
import Tags from "@/components/document/tags";

interface DocumentDrawerProps {
  visible: boolean;
  onClose: () => void;
}

type DocumentUpload = {
  fileList: any,
  tags: any
}

const DocumentDrawer: React.FC<DocumentDrawerProps> = ({
                                                         visible,
                                                         onClose
                                                       }) => {
  const [form] = Form.useForm();
  const [tags, setTags] = useState<string[]>();

  const getTags = (val: string[]) => {
    setTags(val);
  };

  const router = useRouter();
  const {name: datasetName} = router.query;
  const handleSubmit = async (values: DocumentUpload) => {
    try {
      var files: File[] = Array(values.fileList.length);
      for (let i = 0; i < values.fileList.length; i++) {
        files[i] = values.fileList[i].originFileObj;
      }
      await uploadDocument(datasetName as string, files, tags)
      form.resetFields();
      onClose();
    } catch (error) {
      if (error instanceof HttpError) {
        message.error(error.message);
      } else {
        message.error("Operation failed");
      }
    }
  };

  const normFile = (e: any) => {
    console.log('Upload event:', e);
    if (Array.isArray(e)) {
      return e;
    }
    return e?.fileList;
  };


  return (
    <Drawer
      title={"Create document"}
      width={720}
      onClose={onClose}
      open={visible}
      bodyStyle={{paddingBottom: 80}}
    >
      <Form
        form={form}
        layout="vertical"
        onFinish={handleSubmit}
        initialValues={{createTime: null, updateTime: null, vectorType: "openaiVector"}}
      >
        <Form.Item
          name="fileList"
          label="FileList"
          valuePropName="fileList"
          getValueFromEvent={normFile}
        >
          <Upload.Dragger name="files" action="">
            <p className="ant-upload-drag-icon">
              <InboxOutlined/>
            </p>
            <p className="ant-upload-text">Click or drag file to this area to upload</p>
            <p className="ant-upload-hint">Support for a single or bulk upload.</p>
          </Upload.Dragger>
        </Form.Item>
        <Form.Item
          name="tags"
          label="Tags"
        >
          <Tags getTags={getTags}/>
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

export default DocumentDrawer;
