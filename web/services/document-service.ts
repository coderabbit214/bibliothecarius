import Document from '@/models/document';
import fetch from '@/libs/fetch';

export async function getDocuments(datasetName: string): Promise<Document[]> {
  return await fetch(`/api/document/dataset/${datasetName}/list`);
}

export async function uploadDocument(datasetName: string, files: File[], tags: string[] | undefined): Promise<void> {
  const formData = new FormData();
  files.forEach((file) => {
    formData.append('files', file);
  })
  if (tags != undefined && tags.length > 0) {
    tags.forEach((tag) => {
      formData.append('tags', tag);
    })
  }
  await fetch(`/api/document/dataset/${datasetName}`, {
    method: 'POST',
    body: formData,
  });
}

export async function deleteDocument(datasetName: string, id: number): Promise<void> {
  await fetch(`/api/document/dataset/${datasetName}/${id}`, {
    method: 'DELETE',
  });
}

export async function reprocessDocument(datasetName: string, id: number): Promise<void> {
  await fetch(`/api/document/dataset/${datasetName}/reprocess/${id}`, {
    method: 'POST',
  });
}
