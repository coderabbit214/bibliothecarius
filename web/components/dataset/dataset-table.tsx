import React, {useEffect, useState} from "react";
import {
  Table,
  Button,
  Popconfirm,
  message,
} from "antd";
import {
  PlusOutlined,
  EditOutlined,
  DeleteOutlined, FileSearchOutlined,
} from "@ant-design/icons";
import Dataset from "@/models/dataset";
import {
  getDatasets,
  deleteDataset,
} from "@/services/dataset-service";
import DatasetDrawer from "./dataset-drawer";
import {useRouter} from "next/router";

const DatasetTable: React.FC = () => {
  const [datasets, setDatasets] = useState<Dataset[]>([]);
  const [selectedDataset, setSelectedDataset] = useState<Dataset>();
  const [drawerVisible, setDrawerVisible] = useState<boolean>(false);

  const router = useRouter();

  useEffect(() => {
    fetchDatasets();
  }, []);

  const fetchDatasets = async () => {
    const result = await getDatasets();
    setDatasets(result);
  };

  const handleDocumentManagement = (dataset: Dataset) => {
    router.push(`/dataset/${dataset.name}/documents`);
  }

  const handleDelete = async (id: number) => {
    try {
      await deleteDataset(id);
      message.success("Deleted successfully");
      fetchDatasets();
    } catch (error) {
      message.error("Failed to delete");
    }
  };

  const handleEdit = (dataset: Dataset) => {
    setSelectedDataset(dataset);
    setDrawerVisible(true);
  };

  const handleAdd = () => {
    setSelectedDataset(undefined);
    setDrawerVisible(true);
  };

  const handleCloseDrawer = () => {
    setDrawerVisible(false);
  };

  const handleDatasetUpdated = () => {
    fetchDatasets();
  };

  const columns = [
    {
      title: "Name",
      dataIndex: "name",
      key: "name",
    },
    {
      title: "Remark",
      dataIndex: "remark",
      key: "remark",
    },
    {
      title: "Parsing Type",
      dataIndex: "vectorType",
      key: "vectorType",
    },
    {
      title: "Creation Time",
      dataIndex: "createTime",
      key: "createTime",
    },
    {
      title: "Modification Time",
      dataIndex: "updateTime",
      key: "updateTime",
    },
    {
      title: "Operation",
      key: "action",
      render: (_: any, record: Dataset) => (
        <div className="flex space-x-2">
          <Button
            type="primary"
            icon={<FileSearchOutlined />}
            onClick={() => handleDocumentManagement(record)}
          />
          <Button
            type="primary"
            icon={<EditOutlined />}
            onClick={() => handleEdit(record)}
          />
          <Popconfirm
            title="Confirm deletion?"
            onConfirm={() => handleDelete(record.id)}
            okText="Yes"
            cancelText="No"
          >
            <Button type="primary" icon={<DeleteOutlined />} danger />
          </Popconfirm>
        </div>
      ),
    },
  ];

  return (
    <div className="w-full">
      <Button
        type="primary"
        icon={<PlusOutlined />}
        className="mb-4"
        onClick={handleAdd}
      >
        Add
      </Button>
      <Table columns={columns} dataSource={datasets} rowKey="id" />
      <DatasetDrawer
        visible={drawerVisible}
        onClose={handleCloseDrawer}
        onDatasetUpdated={handleDatasetUpdated}
        dataset={selectedDataset}
      />
    </div>
  );
};

export default DatasetTable;
