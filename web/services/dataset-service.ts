import Dataset from "@/models/dataset";
import fetch from "@/libs/fetch";

export async function getVectorTypes(): Promise<string[]> {
  return fetch(`/api/dataset/vector/type`);
}

export async function getDatasets(): Promise<Dataset[]> {
  return await fetch("/api/dataset/list/");
}

export async function deleteDataset(id: number): Promise<void> {
  await fetch(`/api/dataset/${id}`, {method: "DELETE"});
}

export async function addDataset(dataset: Dataset): Promise<void> {
  await fetch("/api/dataset", {
    method: "POST", headers: {
      'Content-Type': 'application/json'
    }, body: JSON.stringify(dataset)
  });
}

export async function updateDataset(dataset: Dataset): Promise<void> {
  await fetch("/api/dataset", {
    method: "PUT", headers: {
      'Content-Type': 'application/json'
    }, body: JSON.stringify(dataset)
  });
}
