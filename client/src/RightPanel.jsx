import { Routes, Route, useParams } from "react-router";
import { useEffect, useState } from "react";

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

function ChatWindow() {
  const [loading, setLoading] = useState(false);
  const [messages, setMessages] = useState(null);
  const { type, chatId } = useParams();
  const token = localStorage.getItem("token");
  console.log(type);
  console.log(chatId);

  async function getMessages() {
    setLoading(true);
    const promise = await fetch(
      `http://localhost:8080/chats/${encodeURIComponent(type)}/${encodeURIComponent(
        chatId
      )}/messages`,
      {
        method: "GET",
        headers: { authorization: "Bearer " + token, Accept: "application/json" },
      }
    );
    const res = await promise.json();
    setMessages(res);
    setLoading(false);
  }
  /*{messages.value.map(({ messageId, nickname, file, text }) => (
    <p key={`${messageId}`}>
      ${nickname} : ${file}, ${text}
    </p>
  ))}*/
  useEffect(() => getMessages(), []);
  console.log(messages);

  return (
    <div>
      Chat of type ${type} and id: ${chatId};
      <ul>
        {messages.map(({ messageId, nickname, file, text }) => (
          <p key={`${messageId}`}>
            {nickname} : {file} {text}
          </p>
        ))}
      </ul>
    </div>
  );
}
