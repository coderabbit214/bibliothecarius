import fetch from "@/libs/fetch";
import {ChatDTO, ChatResult} from "@/models/chat";

export async function chat(sceneName: string, chat: ChatDTO): Promise<ChatResult> {
  return await fetch(`/api/scene/${sceneName}/chat`, {
    method: "POST", headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(chat)
  });
}

