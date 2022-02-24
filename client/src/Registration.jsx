import { useState } from "react";

export function Registration(){

    const [username, setUsername] = useState("")
    const [password, setPassword] = useState("")
    const [nickname, setNickname] = useState("")
    const [phoneNumber, setPhoneNumber] = useState("")
    const [bio, setBio] = useState("")

    async function handleSubmit(event){
        event.preventDefault()

        const response = await fetch(`http://localhost:8080/user/registration`, {
            method: "GET",
            headers: {},
            body: JSON.stringify({
                username: event.target.username,
                nickname: event.target.nickname,
                password: event.target.password,
                phoneNumber: event.target.phoneNumber,
                bio: event.target.bio
            })
        })

        const res = await response.json()
        if(res === true){
            alert("user registered!")
        }
        else(alert(res))

    }


    return <div onSubmit={handleSubmit}>
        <input type="text" placeholder="Username" value={username} onChange={(e) => setUsername(e.target.value)} />
        <input type="text" placeholder="Nickname" value={nickname} onChange={(e) => setNickname(e.target.value)} />
        <input type="password" placeholder="Nickname" value={password} onChange={(e) => setPassword(e.target.value)} />
        <input type="tel" placeholder="Phone number" value={phoneNumber} onChange={(e) => setPhoneNumber(e.target.value)} />
        <input type="text" placeholder="Bio" value={bio} onChange={(e) => setBio(e.target.value)} />
        <input type="submit" value="Registrate" />
    </div>
}