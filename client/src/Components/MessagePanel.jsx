import { useParams } from "react-router";
import { useEffect, useState } from "react";


export function messageTypeDefiner(text, file){
  if(text, !file) return "text"
  if(!text && file) return "file"
  if(text && file) return "described"
}



export function MessagePanel({ chat, chatExists, isEditor, messageId, text: messageText, file: messageFile, editing, getStream}) {
  const { type, chatId } = useParams();
  const jwt = localStorage.getItem("token");
  const [text, setText] = useState("");
  const [file, setFile] = useState("");
  var httpResponse = "";

  useEffect(()=>
  {if(isEditor){
    setText(messageText)
    setFile(messageFile)}
  }, [])
  

  async function handleSubmit(event) {

    event.preventDefault()
    console.log(text)
    console.log(file)
    console.log(event.target)
    console.log(messageTypeDefiner(text, file))

    if(isEditor){
      const response = await fetch(`http://localhost:8080/chats/${type}/${chatId}/messages/${messageId}`, {
        method: "PATCH",
        headers: { authorization: "Bearer " + jwt,  type:  messageTypeDefiner(text, file),},
        body: new FormData(event.target),
      });
    } else{
      if(!chat){
        const response = await fetch(`http://localhost:8080/chats/${type}/${chatId}`, {
          method: "POST",
          headers: {authorization: "Bearer " + jwt,}
        })
        const chatex = await response.json()
        if(!chatex.message){
          chatExists()
        } else (
          alert(chatex)
        )
      }
      const response = await fetch(`http://localhost:8080/chats/${type}/${chatId}/messages/`, {
        method: "POST",
        headers: { authorization: "Bearer " + jwt,  type:  messageTypeDefiner(text, file),},
        body: new FormData(event.target),
      });
    }

    const res = await response.json();
    console.log(res)
    httpResponse = res;
    if(httpResponse.messageId){
        alert("message sent")
    }

  }

  return (
    <div>
      <form onSubmit={handleSubmit}>
        <input
          type="text"
          placeholder="input here..."
          name="text"
          value={text}
          onChange={(e) => setText(e.target.value)}
        />
        <input type="file" name="file" onChange={(e) => {setFile(true)}} />
        <input type="submit" value="Send" />
        <button onClick={getStream}>test sse</button>
        {isEditor && <button value="cancel editing" onClick={()=> editing(false)}/>}
      </form>
    </div>
  );
}
