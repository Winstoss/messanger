import { useEffect, useState } from "react";
import {
  ChatEvent,
  chatEventStream,
  ChatID,
  createPrivateChat,
  deleteMessage,
  editMessage,
  fetchChat,
  fetchMessages,
  isDeletedMessageEvent,
  isServerError,
  Message,
  MessageID,
  sendMessage,
} from "../api";
import { toAsyncIter } from "../Util/toAsyncIter";
import { MessageContext } from "./MessageContext";
import { MessagePanel } from "./MessagePanel";

const controller = new AbortController();

type Props = {
  chatId: ChatID;
};

function applyUpdate(messages: Message[], event: ChatEvent) {
  if (isDeletedMessageEvent(event)) {
    return messages.filter((it) => it.messageId != event.messageId);
  }
  return [...messages, event];
}

export function ChatWindow({ chatId }: Props) {
  const [loading, setLoading] = useState(true);
  const [messages, setMessages] = useState<Message[]>([]);
  const [editingMessage, setEditingMessage] = useState<MessageID | null>(null);
  const token = localStorage.getItem("token");
  const [chatExists, setChatExists] = useState<boolean>();
  const [composedMessage, setComposedMessage] = useState("");
  const [attachedFile, setAttachedFile] = useState<File>();

  async function handleDelete(message: MessageID) {
    const res = await deleteMessage(chatId, message, token);
    if (res !== true) alert(res);
  }

  function handleEdit(message: MessageID) {
    setEditingMessage(message);
  }

  useEffect(() => {
    setLoading(true);
    setMessages([]);
  }, [chatId.chatId, chatId.type, token]);

  useEffect(() => {
    fetchChat(chatId, token).then((res) => {
      setChatExists(!isServerError(res));
    });
  }, [chatId.chatId, chatId.type, token]);

  useEffect(() => {
    if (chatExists) {
      fetchMessages(chatId, token).then((messages) => {
        setMessages(messages);
        setLoading(false);
      });
    }
  }, [chatId.chatId, chatId.type, chatExists, token]);

  useEffect(() => {
    if (chatExists) {
      chatEventStream(chatId, token, controller.signal).then(async (stream) => {
        for await (const event of toAsyncIter(stream)) {
          setMessages((messages) => applyUpdate(messages, event));
        }
      });
      return () => controller.abort();
    }
  }, [chatId.chatId, chatId.type, chatExists, token]);

  async function handleSendMessage() {
    if (editingMessage) {
      return editMessage({
        chat: chatId,
        message: editingMessage,
        newContents: composedMessage,
        newAttachments: attachedFile,
        token,
      });
    }
    if (!chatExists) {
      await createPrivateChat(chatId.chatId, token);
      sendMessage({
        chat: chatId,
        contents: composedMessage,
        attachments: attachedFile,
        token,
      });
    }
  }

  return (
    <div>
      Chat of type {chatId.type} and id: {chatId.chatId};
      <div>
        {messages.map(({ messageId, nickname, file, text }) => (
          <p>
            <MessageContext
              key={`${messageId}`}
              onDelete={() => handleDelete({ messageId, text, file })}
              onEdit={() => handleEdit({ messageId, text, file })}
            >
              {nickname} : {file} {text}
            </MessageContext>
          </p>
        ))}
      </div>
      <div>
        <MessagePanel
          message={composedMessage}
          editing={!!editingMessage}
          onMessageChange={setComposedMessage}
          onAttachFile={setAttachedFile}
          attachedFile={attachedFile}
          onCancelEditing={() => {
            setEditingMessage(null);
            setComposedMessage("");
          }}
          onSend={handleSendMessage}
        />
      </div>
    </div>
  );
}
