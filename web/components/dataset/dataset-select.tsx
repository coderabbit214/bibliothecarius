import Dataset from "@/models/dataset";
import {SelectProps} from "antd/es/select";
import DebounceSelect from "../debounce-select";
import React from "react";
import {getDatasets} from "@/services/dataset-service";

interface DatasetValue {
  label: string;
  value: number;
}

export interface DatasetSelectProps<ValueType = any>
  extends Omit<SelectProps<ValueType | ValueType[]>, "options" | "children"> {
  debounceTimeout?: number;
}

const DatasetSelect: React.FC<DatasetSelectProps> = ({
                                                       debounceTimeout = 800,
                                                       ...props
                                                     }: DatasetSelectProps) => {

  async function fetchDatasetList(datasetName: string): Promise<DatasetValue[]> {
    return getDatasets().then((datasets) =>
      datasets
        .filter((dataset) => dataset.name?.includes(datasetName))
        .map((dataset: Dataset) => ({
          label: dataset.name!,
          value: dataset.id,
        }))
    );
  }

  return (
    <DebounceSelect
      showSearch={true}
      labelInValue={false}
      fetchOptions={fetchDatasetList}
      style={{width: "100%"}}
      debounceTimeout={debounceTimeout}
      eager
      {...props}
    />
  );
};

export default DatasetSelect;
