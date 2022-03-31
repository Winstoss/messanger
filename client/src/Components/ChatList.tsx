import { useEffect, useState } from "react";
import { NavLink } from "react-router-dom";
import { Chat, fetchChats } from "../api";

export function ChatList() {
  const [chats, setChats] = useState<Chat[] | null>(null);

  useEffect(() => {
    fetchChats(localStorage.getItem("token")).then((chats) => {
      setChats(chats);
    });
  }, []);

  if (!chats) {
    return <>Loading...</>;
  }

  return (
    <ul>
      {chats.map(({ type, chatId, chatName }) => (
        <li key={`${type}/${chatId}`}>
          <NavLink to={`/${type}/${chatId}`}>{chatName}</NavLink>
        </li>
      ))}
    </ul>
  );
}
