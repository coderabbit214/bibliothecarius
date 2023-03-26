import Document from '@/models/document';
import fetch from '@/libs/fetch';

export async function getDocuments(datasetName: string): Promise<Document[]> {
  return await fetch(`/api/dataset/${datasetName}/document/list`);
}

export async function uploadDocument(datasetName: string, file: File): Promise<void> {
  const formData = new FormData();
  formData.append('file', file);

  await fetch(`/api/dataset/${datasetName}/document`, {
    method: 'POST',
    body: formData,
  });
}

export async function deleteDocument(datasetName: string, id: number): Promise<void> {
  await fetch(`/api/dataset/${datasetName}/document/${id}`, {
    method: 'DELETE',
  });
}

export async function reprocessDocument(datasetName: string, id: number): Promise<void> {
  await fetch(`/api/dataset/${datasetName}/reprocess/document/${id}`, {
    method: 'POST',
  });
}
