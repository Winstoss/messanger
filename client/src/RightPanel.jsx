import { Routes, Route, useParams } from "react-router";
import { useEffect, useState } from "react";
import { MessagePanel, messageTypeDefiner } from "./Components/MessagePanel";
import * as ContextMenu from "@radix-ui/react-context-menu";

export function RightPanel() {
  return (
    <div style={{ background: "salmon" }}>
      <Routes>
        <Route path="/:type/:chatId" element={<ChatWindow />} />
        <Route path="*" element="No chat selected" />
      </Routes>
    </div>
  );
}

function MessageContext({ children, handleEdit, handleDelete }) {
  return (
    <ContextMenu.Root>
      <ContextMenu.Trigger>{children}</ContextMenu.Trigger>
      <ContextMenu.Content>
        <ContextMenu.Item onSelect={handleEdit}>Edit</ContextMenu.Item>
        <ContextMenu.Item onSelect={handleDelete}>Delete</ContextMenu.Item>
      </ContextMenu.Content>
    </ContextMenu.Root>
  );
}

function transformSseStream() {
  return new TransformStream({
    start() {
      this.buffer = "";
    },
    transform(chunk, controller) {
      this.buffer += chunk;
      while (true) {
        const endIndex = this.buffer.indexOf("\n\n");
        if (endIndex == -1) break;
        const eventStr = this.buffer.substring(0, endIndex);
        controller.enqueue(JSON.parse(eventStr.substring(5)));
        this.buffer = this.buffer.substring(endIndex + 2);
      }
    },
  });
}

async function getStream({ signal, type, chatId, token }) {
  const response = await fetch(`http://localhost:8080/chats/${type}/${chatId}/sse-stream`, {
    signal: signal,
    method: "POST",
    headers: { authorization: "Bearer " + token },
  });

  return response.body
    .pipeThrough(new TextDecoderStream("utf-8"))
    .pipeThrough(transformSseStream())
    .getReader();
}

function ChatWindow() {
  const [loading, setLoading] = useState(false);
  const [messages, setMessages] = useState([]);
  const { type, chatId } = useParams();
  const [isEditor, editing] = useState(false);
  const [selectedMessage, setSelectedMessage] = useState(null);
  const token = localStorage.getItem("token");
  const [chat, chatExists] = useState(false);
  const [eventSource, setSource] = useState(null);
  const controller = new AbortController();

  async function getMessages() {
    const promise = await fetch(`http://localhost:8080/chats/${type}/${chatId}/messages/`, {
      method: "GET",
      headers: { authorization: "Bearer " + token },
    });
    return await promise.json();
  }

  async function getChat() {
    const promise = await fetch(`http://localhost:8080/chats/${type}/${chatId}`, {
      method: "GET",
      headers: { authorization: "Bearer " + token, Accept: "application/json" },
    });
    return await promise.json();
  }

  async function handleDelete(messageId, text, file) {
    const jwt = localStorage.getItem("token");

    const response = await fetch(
      `http://localhost:8080/chats/${type}/${chatId}/messages/${messageId}`,
      {
        method: "DELETE",
        headers: { authorization: "Bearer " + jwt, type: messageTypeDefiner(text, file) },
      }
    );

    const res = await response.json();
    if (!res.nickname) {
      return res;
    } else return alert(res);
  }

  function handleEdit(messageId, text, file) {
    editing(true);
    setSelectedMessage({ messageId, text, file });
  }

  async function handleStream({controller, type, chatId, token}){

    const reader = await getStream({ signal: controller.signal, type, chatId, token });

    while (true) {
      const { value, done } = await reader.read();
      if (done) break;
      console.log(value);
      value.deleted
        ? setMessages(messages => messages.filter((it) => it.messageId != value.messageId))
        : setMessages(messages => [...messages, value]);
    }
  }

  useEffect(() => {
    setLoading(true);
    getChat().then((err) => {
      if (!err.message) {
        chatExists(true);
        handleStream({controller, type, chatId, token});
      }
      setLoading(false);
    });
    return () => controller.abort();
  }, [type, chatId, chat]);

  useEffect(() => {
    setLoading(true);
    getMessages().then((res) => {
      setMessages(res);
      setLoading(false);
      //console.log(messages);
    });
  }, [type, chatId]);

  return (
    <div>
      Chat of type {type} and id: {chatId};
      <div>
        {messages.map(({ messageId, nickname, file, text }) => (
          <p>
            <MessageContext
              key={`${messageId}`}
              handleDelete={() => handleDelete(messageId, text, file)}
              handleEdit={() => handleEdit(messageId, text, file)}
            >
              {nickname} : {file} {text}
            </MessageContext>
          </p>
        ))}
      </div>
      <div>
        <MessagePanel
          chat={chat}
          chatExists={() => chatExists(true)}
          isEditor={isEditor}
          message={selectedMessage}
          editing={editing}
          getStream={getStream}
        />
      </div>
    </div>
  );
}
