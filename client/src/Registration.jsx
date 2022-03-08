import { useState } from "react";

export function Registration({setRegistration}){

    const [username, setUsername] = useState("")
    const [password, setPassword] = useState("")
    const [nickname, setNickname] = useState("")
    const [phoneNumber, setPhoneNumber] = useState("")
    const [bio, setBio] = useState("")

    async function handleSubmit(event){
        event.preventDefault()

        const response = await fetch(`http://localhost:8080/user/registration`, {
            method: "POST",
            headers: {"content-type": "application/json" },
            body: JSON.stringify({
                username: username,
                nickname: nickname,
                password: password,
                phoneNumber: phoneNumber,
                bio: bio
            })
        })

        const res = await response.json()
        if(res === true){
            alert("user registered!")
        }
        else(alert(res))

    }


    return <form onSubmit={handleSubmit}>
        <input type="text" placeholder="Username" value={username} onChange={(e) => setUsername(e.target.value)} />
        <input type="text" placeholder="Nickname" value={nickname} onChange={(e) => setNickname(e.target.value)} />
        <input type="password" placeholder="Password" value={password} onChange={(e) => setPassword(e.target.value)} />
        <input type="tel" placeholder="Phone number" value={phoneNumber} onChange={(e) => setPhoneNumber(e.target.value)} />
        <input type="text" placeholder="Bio" value={bio} onChange={(e) => setBio(e.target.value)} />
        <input type="submit" value="Registrate" />
        <button onClick={setRegistration}> To login</button>
    </form>
}