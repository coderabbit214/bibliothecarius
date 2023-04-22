import React, {useEffect, useState} from "react";
import {useRouter} from "next/router";
import {Button, Input, Spin, Tag} from "antd";
import {ChatConversation, ChatDTO, ChatMessage, ChatResult,} from "@/models/chat";
import {chat} from "@/services/chat-service";
import {getTagsByName} from "@/services/scene-service";

const Chat = () => {
    const router = useRouter();
    const {name: sceneName} = router.query;
    const [tags, setTags] = useState<Map<string, boolean>>(new Map());
    const [inputMessage, setInputMessage] = useState("");
    const [conversations, setConversations] = useState<ChatConversation[]>([]);
    const [historySize, setHistorySize] = useState<number | undefined>(undefined);
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        initTags()
    },[])


    const initTags = () => {
        getTagsByName(sceneName as string).then((result) => {
            setTags(new Map(result.map((tag) => [tag, false])));
        })
    }

    const addMessage = async (message: string) => {
        const lastConversation = conversations[conversations.length - 1];
        const contextId = lastConversation ? lastConversation.contextId : "";

        let selectTags: string[] = [];
        for (let [key, value] of tags) {
            if (value) {
                selectTags.push(key);
            }
        }

        const chatData: ChatDTO = {
            contextId: contextId,
            context: message,
            tags: selectTags
        };

        // Display user message immediately
        addMessageToConversations(message, true);

        let chatResult: ChatResult = {contents: [], contextId: "", jsonData: [], tags: []};

        chat(sceneName as string, chatData)
            .then((res) => {
                chatResult = res;

                // Update the last message with AI message after receiving response
                setConversations((prevConversations) => {
                    const updatedConversations = [...prevConversations];
                    const lastIndex = updatedConversations.length - 1;
                    const lastMessageIndex =
                        updatedConversations[lastIndex].messages.length - 1;
                    updatedConversations[lastIndex].messages[lastMessageIndex].aiMessage = [
                        ...chatResult.contents,
                    ];
                    return updatedConversations;
                });
                setLoading(false);
            })
            .catch((err) => {
                console.log(err);
                chatResult.contents = ["Error, please try again"];
                setLoading(false);
            });
    };

    const selectTag = (key: string) => {
        setTags((prevTags) => {
            const newTags = new Map(prevTags);
            newTags.set(key, !tags.get(key));
            return newTags;
        });
    }

    const addMessageToConversations = (
        message: string,
        isUserMessage: boolean
    ) => {
        const newMessage: ChatMessage = {
            userMessage: isUserMessage ? message : undefined,
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

    // @ts-ignore
    return (
        <div className="flex flex-col h-full">
            <div className="flex-1 overflow-y-auto">
                {conversations.flatMap((conv) => conv.messages).map((message: ChatMessage, index: number) => (
                    <div key={index} className="mb-4">
                        {message.userMessage &&
                            (<>
                                <div
                                    className="bg-blue-500 text-white rounded-tl-lg rounded-tr-lg rounded-br-lg px-3 py-1 inline-block mb-1">
                                    <strong>User:</strong> {message.userMessage}
                                </div>
                                <br/>
                            </>)
                        }
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
                        <Spin/>
                    </div>
                )}
            </div>
            <div className="bg-white shadow-lg rounded-xl p-6 mt-4">
                {[...tags.entries()].map(([key, value]) => (
                    <Tag key={key} className="mb-1 mr-2" color={value ? "success" : "default"}
                         onClick={() => selectTag(key)}>{key}</Tag>
                ))}
                <Input
                    value={inputMessage}
                    onChange={(e) => setInputMessage(e.target.value)}
                    onPressEnter={handleSendMessage}
                    placeholder="Type your message here"
                    className="mb-4"
                    disabled={loading}
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
