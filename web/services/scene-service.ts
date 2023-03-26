// service.ts
import Scene from "@/models/scene";
import fetch from "@/libs/fetch";


export async function getScenes(): Promise<Scene[]> {
  return await fetch(`/api/scene/list`);
}

export async function getSceneById(id: number): Promise<Scene> {
  return await fetch(`/api/scene/${id}`);
}

export async function addScene(scene: Scene): Promise<void> {
  scene = updateSceneParams(scene);
  await fetch(`/api/scene`, {
    method: "POST",
    headers: {"Content-Type": "application/json"},
    body: JSON.stringify(scene),
  });
}

export async function updateScene(scene: Scene): Promise<void> {
  //check if scene.params is a string, if so, parse it to object
  scene = updateSceneParams(scene);
  await fetch(`/api/scene`, {
    method: "PUT",
    headers: {"Content-Type": "application/json"},
    body: JSON.stringify(scene),
  });
}

export async function deleteScene(id: number): Promise<void> {
  await fetch(`/api/scene/${id}`, {method: "DELETE"});
}

function updateSceneParams(scene: Scene): Scene {
  scene.params = (typeof scene.params === 'string' || scene.params instanceof String) ? JSON.parse(scene.params as string) as any : scene.params;
  return scene;
}
