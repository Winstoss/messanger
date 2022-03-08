import { useNavigate } from "react-router";
import { useState, useEffect } from "react";

export default function Login({setRegistration}) {
  const navigate = useNavigate();
  const [jwt, setJwt] = useState(localStorage.getItem("token"));
  const [loading, setLoading] = useState(false);
  const [exception, setException] = useState(null);
  const [userMeta, setUserMeta] = useState(null);

  const [login, setLogin] = useState("");
  const [password, setPassword] = useState("");

  /** @param event {React.FormEvent} */
  async function handleLogin(event) {
    event.preventDefault();
    setLoading(true);
    // const formData = new FormData(event.target)

    const requestOptions = {
      method: "POST",
      headers: { "content-type": "application/json" },
      body: JSON.stringify({
        username: login,
        password: password,
      }),
    };

    const response = await fetch(`http://localhost:8080/login`, requestOptions);
    var res = await response.json();
    if (!res.token) {
      setJwt(null);
      setException(res);
    } else {
      setJwt(res.token);
      setUserMeta(
        JSON.stringify({
          userId: res.userId,
          avatarPath: res.avatarPath,
          nickname: res.nickname,
          bio: res.bio,
          phoneNumber: res.phoneNumber,
        })
      );
      setException(null);
      navigate("/");
    }
    setLoading(false);
  }

  useEffect(() => {
    if (jwt) {
      localStorage.setItem("token", jwt);
    } else {
      localStorage.removeItem("token");
    }
  }, [jwt]);

  useEffect(() => {
    if (userMeta) {
      localStorage.setItem("user data", userMeta);
    } else {
      localStorage.removeItem("user data");
    }
  }, [userMeta]);

  return (
    <div>
      <form onSubmit={handleLogin}>
        <p>
          <b>Login form:</b>
        </p>
        <input type="text" name="login" value={login} onChange={(e) => setLogin(e.target.value)} />
        <input
          type="password"
          name="password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
        />
        <input type="submit" value="Отправить" />
        {loading && "loading..."}
        {exception && <div>{JSON.stringify(exception)}</div>}
      </form>
      <button onClick={setRegistration}>Sign up</button>
    </div>
  );
}
