import React from "react";

import SceneTable from "@/components/scene/scene-table";


const ScenePage: React.FC = () => {
  return (
    <div className="container mx-auto p-4">
      <h1 className="text-2xl mb-4">Scene Management</h1>
      <SceneTable />
    </div>
  );
};

export default ScenePage;
