import { useEffect, useState } from "react";

export function UserSettings({ onClose }) {
  const [jwt, setJwt] = useState(localStorage.getItem("token"));
  const [exception, setException] = useState(null);
  const [userInfo, setUserInfo] = useState(JSON.parse(localStorage.getItem("user data")));

  const [nickname, setNickname] = useState(userInfo?.nickname);
  const [password, setPassword] = useState("");
  const [bio, setBio] = useState(userInfo.bio);
  const [phoneNumber, setPhoneNumber] = useState(userInfo.phoneNumber);
  const [image, setImage] = useState(userInfo.image);
  const [loading, setLoading] = useState(false);

  async function handleSubmit(event) {
    event.preventDefault();

    const response = await fetch(`http://localhost:8080/user/me`, {
      method: "PATCH",
      headers: { authorization: "Bearer " + jwt },
      body: new FormData(event.target),
    });
    const res = await response.json();

    if (!res.nickname) {
      setJwt(null);
      setException(res);
    } else {
      setUserInfo(res);
      setException(null);
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
    if (userInfo) {
      localStorage.setItem("user data", JSON.stringify(userInfo));
    } else {
      localStorage.removeItem("user data");
    }
  }, [userInfo]);

  return (
    <div>
      {userInfo.avatarPath && (
        <img src={`http://localhost:8080/files/${userInfo.avatarPath}`} width="50" height="50" />
      )}
      <form onSubmit={handleSubmit}>
        <input
          type="text"
          name="nickname"
          value={nickname}
          onChange={(e) => setNickname(e.target.value)}
        />
        <input
          type="password"
          name="password"
          value={password}
          placeholder="type new password..."
          onChange={(e) => setPassword(e.target.value)}
        />
        <input type="text" name="bio" value={bio} onChange={(e) => setBio(e.target.value)} />
        <input
          type="text"
          name="phoneNumber"
          value={phoneNumber}
          onChange={(e) => setPhoneNumber(e.target.value)}
        />
        <input type="file" name="image" onChange={(e) => setImage(e.target.files.item[0])} />
        <input type="submit" value="Change" />
        <button onClick={onClose} />
      </form>
    </div>
  );
}
