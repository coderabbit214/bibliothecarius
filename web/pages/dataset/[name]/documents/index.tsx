import {Table, Button, Space, Upload, message, Popconfirm} from 'antd';
import {DeleteOutlined, EditOutlined, PlusOutlined, SyncOutlined, UploadOutlined} from '@ant-design/icons';
import React, {useEffect, useState} from 'react';
import {useRouter} from 'next/router';
import Document from '@/models/document';
import {getDocuments, uploadDocument, deleteDocument, reprocessDocument} from '@/services/document-service';

const {Column} = Table;

const DocumentPage = () => {
  const router = useRouter();
  const {name: datasetName} = router.query;
  const [documents, setDocuments] = useState<Document[]>([]);

  useEffect(() => {
    if (datasetName) {
      fetchDocuments();
    }
  }, [datasetName]);

  const fetchDocuments = async () => {
    const docs = await getDocuments(datasetName as string);
    setDocuments(docs);
  };

  const handleUpload = async (info: any) => {
    if (info.file.status === 'done') {
      message.success(`${info.file.name} file uploaded successfully`);
      fetchDocuments();
    } else if (info.file.status === 'error') {
      info.file.error && message.error(info.file.error.message);
    }
  };

  const handleDelete = async (id: number) => {
    deleteDocument(datasetName as string, id).then(() =>
      fetchDocuments()
    ).catch((err) => {
      if (err.message) {
        message.error(err.message);
      } else {
        message.error('Failed to delete');
      }
    });
  };

  const handleReprocess = async (id: number) => {
    await reprocessDocument(datasetName as string, id);
    fetchDocuments();
  };

  return (
    <div className="container mx-auto p-4">
      <h1 className="text-2xl mb-4">Document Management - {datasetName}</h1>
      <div className="w-full">
        <Upload
          customRequest={({file, onSuccess, onError}) => {
            uploadDocument(datasetName as string, file as File)
              .then(() => {
                onSuccess?.(null);
              })
              .catch((err) => {
                onError?.(err);
              });
          }}
          onChange={handleUpload}
          showUploadList={false}
        >
          <Button type="primary" icon={<UploadOutlined />} className="mb-4">Upload Document</Button>
        </Upload>
        <Table dataSource={documents} rowKey="id">
          <Column title="ID" dataIndex="id" />
          <Column title="Dataset ID" dataIndex="datasetId" />
          <Column title="Name" dataIndex="name" />
          <Column title="Hash Value" dataIndex="hashCode" />
          <Column title="Status" dataIndex="state" />
          <Column title="File Key" dataIndex="fileKey" />
          <Column title="File Size" dataIndex="size" />
          <Column title="File Type" dataIndex="type" />
          <Column title="Creation Time" dataIndex="createTime" />
          <Column title="Update Time" dataIndex="updateTime" />
          <Column
            title="Action"
            dataIndex="action"
            render={(_: any, record: Document) => (
              <Space size="middle">
                <Button
                  type="primary"
                  icon={<SyncOutlined />}
                  onClick={() => handleReprocess(record.id)}
                />
                <Popconfirm
                  title="Confirm deletion?"
                  onConfirm={() => handleDelete(record.id)}
                  okText="Yes"
                  cancelText="No"
                >
                  <Button type="primary" icon={<DeleteOutlined />} danger />
                </Popconfirm>
              </Space>
            )}
          />
        </Table>
      </div>
    </div>
  );
};

export default DocumentPage;
