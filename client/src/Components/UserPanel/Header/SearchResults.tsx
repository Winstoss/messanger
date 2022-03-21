import { NavLink } from "react-router-dom";
import { UserSearchEntry } from "../../../api";

type Props = {
  chats: UserSearchEntry[];
};

export function SearchResults({ chats }: Props) {
  return (
    <ul>
      {chats.map(({ avatarPath, nickname, userId }) => (
        <li key={`/private/${userId}`}>
          <NavLink to={`/private/${userId}`}>{nickname}</NavLink>
        </li>
      ))}
    </ul>
  );
}
