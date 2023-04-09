import {SelectProps} from "antd/es/select";
import React from "react";
import {Select} from "antd";
import {getVectorTypes} from "@/services/dataset-service";

interface VectorValue {
  label: string;
  value: string;
}

export interface VectorSelectProps<ValueType = any>
  extends Omit<SelectProps<ValueType | ValueType[]>, "options" | "children"> {
}

const VectorSelect: React.FC<VectorSelectProps> = ({...props}: VectorSelectProps) => {

  const [options, setOptions] = React.useState<VectorValue[]>([]);

  React.useEffect(() => {
    fetchVectorList().then((newOptions) => {
      setOptions(newOptions);
    });
  }, []);

  async function fetchVectorList(): Promise<VectorValue[]> {
    return getVectorTypes().then((vectors) =>
      vectors
        .map((vector) => ({
          label: vector,
          value: vector,
        }))
    );
  };

  return (
    <Select
      showSearch={true}
      labelInValue={false}
      style={{width: "100%"}}
      options={options}
      {...props}
    />
  );
};

export default VectorSelect;
