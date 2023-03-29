export interface ChatDTO {
  contextId: string;
  context: string;
  historySize?: number;
}

export interface ChatResult {
  contextId: string;
  contents: string[];
  jsonData: any[];
}

export interface ChatMessage {
  userMessage: string;
  aiMessage: string[];
}

export interface ChatConversation {
  contextId: string;
  messages: ChatMessage[];
  jsonData: any[];
}
