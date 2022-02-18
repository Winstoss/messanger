import { useEffect, useState } from "react";
import { NavLink } from "react-router-dom";

async function fetchChats(token) {
  const response = await fetch(`http://localhost:8080/chats`, {
    headers: { authorization: "Bearer " + token },
  });
  return response.json();
}

export function ChatList() {
  const [chats, setChats] = useState(null);

  useEffect(() => {
    fetchChats(localStorage.getItem("token")).then((chats) => {
      setChats(chats);
    });
  }, []);

  if (!chats) {
    return "Loading...";
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
