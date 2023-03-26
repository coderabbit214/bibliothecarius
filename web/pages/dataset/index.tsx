import React from "react";

import DatasetTable from "@/components/dataset/dataset-table";


const DatasetPage: React.FC = () => {
  return (
    <div className="container mx-auto p-4">
      <h1 className="text-2xl mb-4">Dataset Management</h1>
      <DatasetTable />
    </div>
  );
};

export default DatasetPage;
