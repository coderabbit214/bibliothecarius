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
  DeleteOutlined,
} from "@ant-design/icons";
import Scene from "@/models/scene";
import {
  getScenes,
  deleteScene, getSceneById,
} from "@/services/scene-service";
import SceneDrawer from "./scene-drawer";
import Dataset from "@/models/dataset";
import {getDatasets} from "@/services/dataset-service";

const SceneTable: React.FC = () => {
  const [scenes, setScenes] = useState<Scene[]>([]);
  const [selectedScene, setSelectedScene] = useState<Scene>();
  const [datasets, setDatasets] = useState<Dataset[]>([]);
  const [drawerVisible, setDrawerVisible] = useState<boolean>(false);

  useEffect(() => {
    fetchScenes();
    fetchDatasets();
  }, []);

  const fetchScenes = async () => {
    const result = await getScenes();
    setScenes(result);
  };

  const fetchDatasets = async () => {
    const result = await getDatasets();
    setDatasets(result);
  }

  const handleDelete = async (id: number) => {
    try {
      await deleteScene(id);
      message.success("Deleted successfully");
      fetchScenes();
    } catch (error) {
      message.error("Deletion failed");
    }
  };

  const handleEdit = (scene: Scene) => {
    getSceneById(scene.id).then((result) => {
      scene = result;
    });
    scene.params = JSON.stringify(scene.params);
    setSelectedScene(scene);
    setDrawerVisible(true);
  };

  const handleAdd = () => {
    setSelectedScene(undefined);
    setDrawerVisible(true);
  };

  const handleCloseDrawer = () => {
    setDrawerVisible(false);
  };

  const handleSceneUpdated = () => {
    fetchScenes();
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
      title: "Message Input Template",
      dataIndex: "template",
      key: "template",
    },
    {
      title: "Dataset",
      key: "datasetId",
      render: (_: any, record: Scene) => (
        <>
          {
            datasets.map((dataset) => {
              if (dataset.id === record.datasetId) {
                return dataset.name;
              }
            })
          }
        </>
      )
    },
    {
      title: "Model Type",
      dataIndex: "modelType",
      key: "modelType",
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
      title: "Action",
      key: "action",
      render: (_: any, record: Scene) => (
        <div className="flex space-x-2">
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
      <Table columns={columns} dataSource={scenes} rowKey="id" />
      <SceneDrawer
        visible={drawerVisible}
        onClose={handleCloseDrawer}
        onSceneUpdated={handleSceneUpdated}
        scene={selectedScene}
      />
    </div>
  );
};

export default SceneTable;
