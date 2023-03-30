import {SelectProps} from "antd/es/select";
import React from "react";
import {getModelTypes} from "@/services/scene-service";
import {Select} from "antd";

interface ModelValue {
  label: string;
  value: string;
}

export interface ModelSelectProps<ValueType = any>
  extends Omit<SelectProps<ValueType | ValueType[]>, "options" | "children"> {
}

const ModelSelect: React.FC<ModelSelectProps> = ({...props}: ModelSelectProps) => {

  const [options, setOptions] = React.useState<ModelValue[]>([]);

  React.useEffect(() => {
    fetchModelList().then((newOptions) => {
      setOptions(newOptions);
    });
  }, []);

  async function fetchModelList(): Promise<ModelValue[]> {
    return getModelTypes().then((models) =>
      models
        .map((model) => ({
          label: model,
          value: model,
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

export default ModelSelect;
