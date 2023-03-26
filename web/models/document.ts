export default interface Document {
  id: number;
  datasetId: number;
  name: string;
  hashCode: string;
  state: string;
  fileKey: string;
  size: number;
  type: string;
  createTime: string;
  updateTime: string;
}
