import { useNavigate } from "react-router";
import React, { useState, useEffect } from "react";
import { loginUser, ServerError } from "../api";
import { useLocalStorage } from "./useLocalStorage";

type Props = {
  onTransferToRegistration?: () => void;
};

export default function Login({ onTransferToRegistration }: Props) {
  const [loading, setLoading] = useState(false);
  const [login, setLogin] = useState("");
  const [password, setPassword] = useState("");
  const [exception, setException] = useState<ServerError | null>(null);

  const [jwt, setJwt] = useState(localStorage.getItem("token"));
  const [userMeta, setUserMeta] = useState<string | null>(null);
  useLocalStorage("token", jwt);
  useLocalStorage("user data", userMeta);

  const navigate = useNavigate();

  async function handleLogin(event: React.FormEvent) {
    event.preventDefault();
    setLoading(true);

    const res = await loginUser(login, password);

    if ("token" in res) {
      setJwt(res.token);
      setUserMeta(JSON.stringify(res));
      setException(null);
      navigate("/");
    } else {
      setJwt(null);
      setException(res);
    }
    setLoading(false);
  }

  return (
    <div>
      <form onSubmit={handleLogin}>
        <h3>Login form:</h3>
        <label>
          Login
          <input
            type="text"
            name="login"
            value={login}
            onChange={(e) => setLogin(e.target.value)}
          />
        </label>
        <label>
          Password
          <input
            type="password"
            name="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
          />
        </label>
        <input type="submit" value="Отправить" />
        {loading && "loading..."}
        {exception && <div>{JSON.stringify(exception)}</div>}
      </form>
      <button onClick={onTransferToRegistration}>Sign up</button>
    </div>
  );
}
