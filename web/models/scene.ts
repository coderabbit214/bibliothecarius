export default interface Scene {
  id: number;
  name?: string;
  remark: string;
  template: string;
  datasetId: number;
  modelType: string;
  params: string | any;
  createTime: string;
  updateTime: string;
}
