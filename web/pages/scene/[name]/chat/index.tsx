import React, {useState} from "react";
import {useRouter} from "next/router";
import {Button, Input, Spin} from "antd";
import {ChatConversation, ChatDTO, ChatMessage, ChatResult,} from "@/models/chat";
import {chat} from "@/services/chat-service";

const Chat = () => {
  const router = useRouter();
  const {name: sceneName} = router.query;
  const [inputMessage, setInputMessage] = useState("");
  const [conversations, setConversations] = useState<ChatConversation[]>([]);
  const [historySize, setHistorySize] = useState<number | undefined>(undefined);
  const [loading, setLoading] = useState(false);


  const addMessage = async (message: string) => {
    const lastConversation = conversations[conversations.length - 1];
    const contextId = lastConversation ? lastConversation.contextId : "";

    const chatData: ChatDTO = {
      contextId: contextId,
      context: message,
    };

    // Display user message immediately
    addMessageToConversations(message, true);

    let chatResult: ChatResult = {contents: [], contextId: "", jsonData: []};

    chat(sceneName as string, chatData)
      .then((res) => {
        chatResult = res;
        // Display AI message after receiving response
        addMessageToConversations(chatResult.contents.join(" "), false);
      })
      .catch((err) => {
        console.log(err);
        chatResult.contents = ["Error, please try again"];
      });
  };

  const addMessageToConversations = (
    message: string,
    isUserMessage: boolean
  ) => {
    const newMessage: ChatMessage = {
      userMessage: isUserMessage ? message : "",
      aiMessage: isUserMessage ? [] : [message],
    };

    if (conversations.length > 0) {
      setConversations((prevConversations) => {
        const updatedConversations = [...prevConversations];
        updatedConversations[updatedConversations.length - 1].messages.push(
          newMessage
        );
        return updatedConversations;
      });
    } else {
      const newConversation: ChatConversation = {
        contextId: "",
        messages: [newMessage],
        jsonData: [],
      };
      setConversations((prevConversations) => [
        ...prevConversations,
        newConversation,
      ]);
    }
  };

  const handleSendMessage = () => {
    if (inputMessage.trim()) {
      addMessage(inputMessage);
      setInputMessage("");
      setLoading(true);
    }
  };

  const handleHistorySizeChange = (value: string) => {
    setHistorySize(value ? parseInt(value) : undefined);
  };

  return (
    <div className="flex flex-col h-full">
      <div className="flex-1 overflow-y-auto">
        {conversations.flatMap((conv) => conv.messages).map((message: ChatMessage, index: number) => (
          <div key={index} className="mb-4">
            <div
              className="bg-blue-500 text-white rounded-tl-lg rounded-tr-lg rounded-br-lg px-3 py-1 inline-block mb-1">
              <strong>User:</strong> {message.userMessage}
            </div>
            <br />
            {message.aiMessage.map((aiMsg, idx) => (
              <div key={idx}
                   className="bg-gray-300 text-black rounded-tl-lg rounded-tr-lg rounded-br-lg px-3 py-1 inline-block mt-1">
                <strong>AI:</strong> {aiMsg}
              </div>
            ))}
          </div>
        ))}
        {loading && (
          <div className="p-4">
            <Spin />
          </div>
        )}
      </div>
      <div className="bg-white shadow-lg rounded-xl p-6 mt-4">
        <Input
          value={inputMessage}
          onChange={(e) => setInputMessage(e.target.value)}
          onPressEnter={handleSendMessage}
          placeholder="Type your message here"
          className="mb-4"
        />
        <div className="flex justify-between items-center">
          <Button onClick={handleSendMessage} className="mr-4">
            Send
          </Button>
          <div>
            <label htmlFor="historySize" className="mr-2">
              History Size:
            </label>
            <Input
              type="number"
              id="historySize"
              defaultValue={3}
              value={historySize}
              onChange={e => handleHistorySizeChange(e.target.value)}
              style={{width: "100px"}}
            />
          </div>
        </div>
      </div>
    </div>
  );
};

export default Chat;
