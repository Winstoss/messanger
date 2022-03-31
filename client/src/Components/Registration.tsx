import React, { useState } from "react";
import { useNavigate } from "react-router";
import { registerUser } from "../api";

type Props = {
  onTransitionToLogin?: () => void;
};

export function Registration({ onTransitionToLogin }: Props) {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [nickname, setNickname] = useState("");
  const [phoneNumber, setPhoneNumber] = useState("");
  const [bio, setBio] = useState("");
  const navigate = useNavigate();

  async function handleSubmit(event: React.FormEvent) {
    event.preventDefault();

    const res = await registerUser({ username, nickname, password, phoneNumber, bio });
    if (res === true) {
      navigate("/");
    } else alert(res);
  }

  return (
    <form onSubmit={handleSubmit}>
      <label>
        Username
        <input
          type="text"
          placeholder="Username"
          value={username}
          onChange={(e) => setUsername(e.target.value)}
        />
      </label>
      <label>
        Nickname
        <input
          type="text"
          placeholder="Nickname"
          value={nickname}
          onChange={(e) => setNickname(e.target.value)}
        />
      </label>
      <label>
        Password
        <input
          type="password"
          placeholder="Password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
        />
      </label>
      <label>
        <input
          type="tel"
          placeholder="Phone number"
          value={phoneNumber}
          onChange={(e) => setPhoneNumber(e.target.value)}
        />
      </label>
      <label>
        <input type="text" placeholder="Bio" value={bio} onChange={(e) => setBio(e.target.value)} />
      </label>
      <input type="submit" value="Registrate" />
      <button onClick={onTransitionToLogin}> To login</button>
    </form>
  );
}
