import { Routes, Route, useParams } from "react-router";
import { useEffect, useState } from "react";
import { MessagePanel } from "./Components/MessagePanel";
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
      <ContextMenu.Item onSelect={handleEdit}>Edit</ContextMenu.Item>
      <ContextMenu.Item onSelect={handleDelete}>Delete</ContextMenu.Item>
    </ContextMenu.Root>
  );
}

async function getMessages() {
  const promise = await fetch(`http://localhost:8080/chats/${type}/${chatId}/messages/`, {
    method: "GET",
    headers: { authorization: "Bearer " + token, Accept: "application/json" },
  });
  const res = await promise.json();
}

function ChatWindow() {
  const [loading, setLoading] = useState(false);
  const [messages, setMessages] = useState([]);
  const { type, chatId } = useParams();
  const [isEditor, editing] = useState(false);
  const[selectedMessage, setSelectedMessage] = useState(null)
  const token = localStorage.getItem("token");


  async function handleDelete(messageId, text, file) {
    const jwt = localStorage.getItem("token");

    const response = await fetch(
      `http://localhost:8080/chats/${type}/${chatId}/messages/${messageId}`,
      {
        method: "DELETE",
        headers: { authorization: "Bearer " + jwt, type: MessageTypeDefiner(text, file) },
      }
    );

    const res = await response.json();
    if (!res.nickname) {
      return res;
    } else return alert(res);
  }

  function handleEdit(messageId, text, file) {
    editing(true);
    setSelectedMessage({messageId, text, file})
  }

  useEffect(() => {
    setLoading(true);
    getMessages().then((res) => {
      setMessages(res);
      setLoading(false);
    });
  }, [type, chatId]);

  return (
    <div>
      Chat of type {type} and id: {chatId};
      <div>
        {messages.map(({ messageId, nickname, file, text }) => (
          <MessageContext
            handleDelete={() => handleDelete(messageId, text, file)}
            handleEdit={() => handleEdit(messageId, text, file)}
          >
            <p key={`${messageId}`}>
              {nickname} : {file} {text}
            </p>
          </MessageContext>
        ))}
      </div>
      <div>
        <MessagePanel isEditor={isEditor} message={selectedMessage} editing={editing} />
      </div>
    </div>
  );
}
