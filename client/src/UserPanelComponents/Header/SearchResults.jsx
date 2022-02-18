import { NavLink } from "react-router-dom";

export function SearchResults(data) {

  return (
    <ul>
      {data.data.map(({ avatarPath, nickname, userId }) => (
        <li key={`/private/${userId}`}>
          <NavLink to={`/private/${userId}`}>{nickname}</NavLink>
        </li>
      ))}
    </ul>
  );
}
